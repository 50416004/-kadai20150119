package reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class ReservationControl {
	String reservation_userid;
	private boolean flagLogin;

	
	ReservationControl(){
		flagLogin = false;
	}


	

	
	public String getReservationOn( String facility, String ryear_str, String rmonth_str, String rday_str){
		String res = "";
		
		try {
			int ryear = Integer.parseInt( ryear_str);
			int rmonth = Integer.parseInt( rmonth_str);
			int rday = Integer.parseInt( rday_str);
		} catch(NumberFormatException e){
			res ="�N�����ɂ͐������w�肵�Ă�������";
			return res;
		}
		res = facility + " �\���\n\n";

		
		if (rmonth_str.length()==1) {
			rmonth_str = "0" + rmonth_str;
		}
		if ( rday_str.length()==1){
			rday_str = "0" + rday_str;
		}
		
		String rdate = ryear_str + "-" + rmonth_str + "-" + rday_str;

		
		MySQL mysql = new MySQL();

		
		try {
			
			ResultSet rs = mysql.getReservation(rdate, facility);
			boolean exist = false;
			while(rs.next()){
				String start = rs.getString("start_time");
				String end = rs.getString("end_time");
				res += " " + start + " -- " + end + "\n";
				exist = true;
			}

			if ( !exist){
				res = "�\��͂���܂���";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	public String loginLogout( MainFrame frame){
		String res=""; 
		if ( flagLogin){ 
			flagLogin = false;
			frame.buttonLog.setLabel("���O�C��"); 
		} else {
			
			LoginDialog ld = new LoginDialog(frame);
			ld.setVisible(true);
			ld.setModalityType(LoginDialog.ModalityType.APPLICATION_MODAL);
			
			if ( ld.canceled){
				return "";
			}
		
			
			reservation_userid = ld.tfUserID.getText();
			
			String password = ld.tfPassword.getText();
		
			
			try { 
				MySQL mysql = new MySQL();
				ResultSet rs = mysql.getLogin(reservation_userid); 
				if (rs.next()){
					rs.getString("password");
					String password_from_db = rs.getString("password");
					if ( password_from_db.equals(password)){ 
						flagLogin = true;
						frame.buttonLog.setLabel("���O�A�E�g");
						res = "";
					}else {
						
						res = "���O�C���Ăł��܂���.ID �p�X���[�h���������܂�";
					}
				} else { 
					res = "���O�C���Ăł��܂���.ID �p�X���[�h���Ⴂ�܂��B";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	private boolean checkReservationDate( int y, int m, int d){
		
		Calendar dateR = Calendar.getInstance();
		dateR.set( y, m-1, d);	

		
		Calendar date1 = Calendar.getInstance();
		date1.add(Calendar.DATE, 1);

		
		Calendar date2 = Calendar.getInstance();
		date2.add(Calendar.DATE, 90);

		if ( dateR.after(date1) && dateR.before(date2)){
			return true;
		}
		return false;
	}

	public String makeReservation(MainFrame frame){

		String res="";		

		if ( flagLogin){ 
			ReservationDialog rd = new ReservationDialog(frame);

			
			rd.tfYear.setText(frame.tfYear.getText());
			rd.tfMonth.setText(frame.tfMonth.getText());
			rd.tfDay.setText(frame.tfDay.getText());

			
			rd.setVisible(true);
			if ( rd.canceled){
				return res;
			}
			
			try {
				
				String ryear_str = rd.tfYear.getText();
				String rmonth_str = rd.tfMonth.getText();
				String rday_str = rd.tfDay.getText();

				
				int ryear = Integer.parseInt( ryear_str);
				int rmonth = Integer.parseInt( rmonth_str);
				int rday = Integer.parseInt( rday_str);

				if ( checkReservationDate( ryear, rmonth, rday)){
				
					String facility = rd.choiceFacility.getSelectedItem();
					String st = rd.startHour.getSelectedItem()+":" + rd.startMinute.getSelectedItem() +":00";
					String et = rd.endHour.getSelectedItem() + ":" + rd.endMinute.getSelectedItem() +":00";

					if( st.equals(et)){		
						res = "�J�n�����ƏI�������������ł�";
					} else {

						try {
							
							if (rmonth_str.length()==1) {
								rmonth_str = "0" + rmonth_str;
							}
							if ( rday_str.length()==1){
								rday_str = "0" + rday_str;
							}
							
							String rdate = ryear_str + "-" + rmonth_str + "-" + rday_str;
			
							MySQL mysql = new MySQL();
							ResultSet rs = mysql.selectReservation(rdate, facility);
						   
						      boolean ng = false;	
						      while(rs.next()){
							  		
							        String start = rs.getString("start_time");
							        String end = rs.getString("end_time");

							        if ( (start.compareTo(st)<0 && st.compareTo(end)<0) ||		
							        	 (st.compareTo(start)<0 && start.compareTo(et)<0)){		
							        	ng = true; break;
							        }
						      }
							 

						      if (!ng){
			
						    	  int rs_int = mysql.setReservation(rdate, st, et, reservation_userid, facility);
						    	  res ="�\�񂳂�܂���";
						      } else {
						    	  res = "���ɂ���\��ɏd�Ȃ��Ă��܂�";
						      }
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					res = "�\����������ł��D";
				}
			} catch(NumberFormatException e){
				res ="�\����ɂ͐������w�肵�Ă�������";
			}
		} else { 
			res = "���O�C�����Ă�������";
		}
		return res;
	}
public String getExplanation(MainFrame frame){
	String res = "";
	MySQL mysql = new MySQL();
	try {
		ResultSet rs = mysql.getExplanation(frame.choiceFacility.getSelectedItem());
		while(rs.next()){
			String fe =rs.getString("explanation");
			res = fe ;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return res;
}
public String confirmReservation(MainFrame frame){
	String res ="";
	MySQL mysql =new MySQL();
	if(flagLogin){
		res = reservation_userid + "�l�̗\���\n";
		try {
			ResultSet rs = mysql.confirmReservation(reservation_userid);
			boolean exist = false;
			while (rs.next()){
				String fn =rs.getString("facility_name");
				String date =rs.getString("date");
				String start =rs.getString("start_time");
				String end =rs.getString("end_time");
				res  += " " + fn + " " + date + "" +start + "--" +end +"\n";
				exist = true;
			}if ( !exist){
			res ="���q�l�̂��\��͂���܂���\n";
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}else{
			res="���O�C�����Ă�������";
		}
		return res;
	}
}
	














package reservation;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ReservationDialog extends Dialog implements ActionListener, WindowListener,ItemListener{
	
	boolean canceled;		
		
		Panel panelNorth;
		Panel panelMid;
		Panel panelSouth;
		
		ChoiceFacility choiceFacility;	
		TextField tfYear,tfMonth,tfDay;	
		ChoiceHour startHour;			
		ChoiceMinute startMinute;		
		ChoiceHour endHour;				
		ChoiceMinute endMinute;			
		
		Button buttonOK;
		Button buttonCancel;


	public ReservationDialog(Frame owner) {
		super(owner, "新規予約", true);

		
		canceled = true;

		
		choiceFacility = new ChoiceFacility();
		
		tfYear = new TextField("",4);
		tfMonth = new TextField("",2);
		tfDay = new TextField("",2);
		
		startHour = new ChoiceHour();
		startMinute = new ChoiceMinute();
		
		endHour = new ChoiceHour();
		endMinute = new ChoiceMinute();

		buttonOK = new Button("予約実行");
		buttonCancel = new Button("キャンセル");

		
		panelNorth = new Panel();
		panelMid = new Panel();
		panelSouth = new Panel();

		
		panelNorth.add( new Label("施設　"));
		panelNorth.add(choiceFacility);
		panelNorth.add( new Label("予約日 "));
		panelNorth.add(tfYear);
		panelNorth.add(new Label("年"));
		panelNorth.add(tfMonth);
		panelNorth.add(new Label("月"));
		panelNorth.add(tfDay);
		panelNorth.add(new Label("日　"));

		
		panelMid.add( new Label("予約時間　"));
		panelMid.add( startHour);
		panelMid.add( new Label("時"));
		panelMid.add( startMinute);
		panelMid.add( new Label("分　~　"));
		panelMid.add( endHour);
		panelMid.add( new Label("時"));
		panelMid.add( endMinute);
		panelMid.add( new Label("分"));

		
		panelSouth.add(buttonCancel);
		panelSouth.add( new Label("　　　　"));
		panelSouth.add(buttonOK);

		
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		add(panelMid,BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);

		
		addWindowListener(this);
		
		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		
		choiceFacility.addItemListener(this);
		startHour.addItemListener(this);
		startMinute.addItemListener(this);
		endHour.addItemListener(this);
		endMinute.addItemListener(this);

		
		resetTimeRange();

		
		this.setBounds( 100, 100, 500, 120);
		setResizable(false);

		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		setVisible(false);
		dispose();

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
			if ( e.getSource() == buttonCancel){
				setVisible(false);
				dispose();
			} else if ( e.getSource() == buttonOK){
				canceled = false;
				setVisible(false);
				dispose();
			}
	}
			private void resetTimeRange() {
				
				if ( choiceFacility.getSelectedIndex()==0){
					
					startHour.resetRange(10, 20);
					endHour.resetRange(10, 21);
				} else {
					
					startHour.resetRange(9, 19);
					endHour.resetRange(9, 20);
				}
			}

		


	

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
			if ( e.getSource()==choiceFacility){
				
				resetTimeRange();
			} else if ( e.getSource()==startHour){
				
				int start = Integer.parseInt( startHour.getSelectedItem());
				endHour.resetRange(start, Integer.parseInt( endHour.getLast()));
			} else if ( e.getSource()==endHour){
				
				if ( endHour.getSelectedIndex()==endHour.getItemCount()-1){
					endMinute.select(0);
				}
			} if( e.getSource()==endMinute){
				
				if ( endHour.getSelectedIndex()==endHour.getItemCount()-1){
					endMinute.select(0);
				}
			}
		}


	}



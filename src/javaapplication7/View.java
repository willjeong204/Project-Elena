package javaapplication7;

import java.awt.event.WindowEvent;	//for CloseListener()
import java.awt.event.WindowAdapter;	//for CloseListener()
import java.util.Observable;		//for update();
import java.awt.event.ActionListener;	//for addController()


import javax.swing.*;

//View is an Observer
import java.awt.*;



@SuppressWarnings("serial")
class View extends javax.swing.JFrame implements java.util.Observer {

	//attributes as must be visible within class
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private JPanel panelmap = new JPanel();
	private JPanel panelinput = new JPanel();
	private JLabel jLabelstart = new JLabel();
	private JLabel jLabelend = new JLabel();
	private JLabel jLabelDev = new JLabel();
	private JTextField Destination = new JTextField();
    private JTextField Source = new JTextField();
    private JTextField Deviation = new JTextField();
    private JToggleButton jToggleminele = new JToggleButton();
    private JToggleButton jTogglemaxele = new JToggleButton();
    private JButton clear = new JButton();
    private JButton go = new JButton();
    private JButton jButtonaddfav = new JButton();
    public Google_Map_UI mapView;
	
	View() {
		
        mapView = new Google_Map_UI();
   
        
		JFrame frame = new JFrame("Elevation Navigation Application");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1280, 900);
		
		//create 2 panels, this is top
        panelmap.setLayout( new BorderLayout() );
        panelmap.add(mapView);
		
		//this is bot
		GroupLayout panelinputLayout = new GroupLayout(panelinput);
		panelinput.setLayout(panelinputLayout);
		
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(700);
		splitPane.setTopComponent(panelmap);
		splitPane.setBottomComponent(panelinput);
	
		Dimension minimumSize = new Dimension(100, 100);
		panelmap.setMinimumSize(minimumSize);
		panelinput.setMinimumSize(minimumSize);
	
		frame.add(splitPane);
		frame.setVisible(true);
		
		panelinputLayout.setAutoCreateGaps(true);
		panelinputLayout.setAutoCreateContainerGaps(true);
		
		jLabelstart.setText("Starting Point: ");
		jLabelend.setText("Ending Point: ");
		jLabelDev.setText("Enter Decimal Deviation: ");
		Source.setText("");
		Destination.setText("");
		Deviation.setText("");
		jTogglemaxele.setText("Maximum Elevation");
		jTogglemaxele.setActionCommand("MAX");
		jToggleminele.setText("Minimum Elevation");
		jToggleminele.setActionCommand("MIN");
		clear.setText("Clear");
		clear.setActionCommand("CLEAR");
		go.setText("Go");
		go.setActionCommand("GO");
		jButtonaddfav.setText("Add to my Fav");
		jButtonaddfav.setActionCommand("ADDFAV");
		
		panelinputLayout.setHorizontalGroup(panelinputLayout.createSequentialGroup()
			.addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(jLabelstart)
				.addComponent(jLabelend)
				.addComponent(jLabelDev))
		    .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    		.addComponent(jToggleminele)
		    		.addComponent(Source, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
		    		.addComponent(Destination, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
		    		.addComponent(Deviation, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
		    .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    		.addComponent(jTogglemaxele)
		    		.addGroup(panelinputLayout.createSequentialGroup()
		    			.addComponent(clear)
		    			.addComponent(go))
		    		.addComponent(jButtonaddfav)
		    )
		);
					
		panelinputLayout.setVerticalGroup(panelinputLayout.createSequentialGroup()
		    .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		    		.addComponent(jTogglemaxele)
				.addComponent(jToggleminele))
		    .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    		.addComponent(jLabelstart)
		    		.addComponent(Source, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		    		.addComponent(clear)
	    			.addComponent(go))
		    .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    		.addComponent(jLabelend)
		    		.addComponent(Destination, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		    		.addComponent(jButtonaddfav))
		    .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		    		.addComponent(jLabelDev)
		    		.addComponent(Deviation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
	}

	public void addController(final ActionListener controller){
		jTogglemaxele.addActionListener(controller);
		jToggleminele.addActionListener(controller);
		clear.addActionListener(controller);
		go.addActionListener(controller);
		jButtonaddfav.addActionListener(controller);
	}
	
	public static class CloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
			System.exit(0);
		}
	}
	
	public Google_Map_UI getMapView() {
		return mapView;
	}
	
	public String getSrc() {
		return Source.getText();
	}
	public String getDest() {
		return Destination.getText();
	}

	@Override
	public void update(Observable obs, Object obj) {
		// TODO Auto-generated method stub
		if(obj.toString().equals("true") || obj.toString().equals("false")) {
			System.out.println("View      : Observable is " + obs.getClass() + ", object passed is " + obj.getClass());
			jToggleminele.setSelected((boolean) obj);
			jTogglemaxele.setSelected(!(boolean) obj);
		}
		
		if(obj.toString().equals("")) {
			Source.setText(obj.toString());
			Destination.setText(obj.toString());
		}
		
	}

} //View
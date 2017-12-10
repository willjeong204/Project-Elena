package javaapplication7;

import java.awt.event.WindowEvent;	//for CloseListener()
import java.awt.event.WindowAdapter;	//for CloseListener()
import java.util.Observable;		//for update();
import java.awt.event.ActionListener;	//for addController()

import org.json.JSONException;

import javax.swing.*;

//View is an Observer
import java.awt.*;

import com.teamdev.jxmaps.MapViewOptions;


class View extends javax.swing.JFrame implements java.util.Observer {

	//attributes as must be visible within class
	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private JPanel panelmap = new JPanel();
	private JPanel panelinput = new JPanel();
	private JLabel jLabelstart = new JLabel();
	private JLabel jLabelend = new JLabel();;
	public JTextField Destination = new JTextField();
    public JTextField Source = new JTextField();;
    private JButton jButtonminele = new JButton();
    private JButton jButtonmaxele = new JButton();
    private JButton clear = new JButton();
    private JButton go = new JButton();;
    private JButton jButtonaddfav = new JButton();
    private Google_Map_UI mapView;
	
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
		splitPane.setDividerLocation(550);
		splitPane.setTopComponent(panelmap);
		splitPane.setBottomComponent(panelinput);
	
		Dimension minimumSize = new Dimension(100, 50);
		panelmap.setMinimumSize(minimumSize);
		panelinput.setMinimumSize(minimumSize);
	
		frame.add(splitPane);
		frame.setVisible(true);
		
		panelinputLayout.setAutoCreateGaps(true);
		panelinputLayout.setAutoCreateContainerGaps(true);
		
		jLabelstart.setText("Starting Point: ");
		jLabelend.setText("Ending Point: ");
		Source.setText("Type in your Destination in here");
		Source.setActionCommand("SOURCE");
		Destination.setText("Type in your Destination in here");
		Destination.setActionCommand("DESTINATION");
		jButtonmaxele.setText("Maximum Elevation");
		jButtonmaxele.setActionCommand("MAX");
		jButtonminele.setText("Minimum Elevation");
		jButtonminele.setActionCommand("MIN");
		clear.setText("Clear");
		clear.setActionCommand("CLEAR");
		go.setText("Go");
		go.setActionCommand("GO");
		jButtonaddfav.setText("Add to my Fav");
		jButtonaddfav.setActionCommand("ADDFAV");
		
		panelinputLayout.setHorizontalGroup(
	        panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	        .addGroup(panelinputLayout.createSequentialGroup()
	            .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
	                .addGroup(panelinputLayout.createSequentialGroup()
	                    .addComponent(jButtonminele, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jButtonmaxele, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
	                    .addGroup(panelinputLayout.createSequentialGroup()
	                        .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
	                            .addComponent(jLabelend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(jLabelstart))
	                        .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
	                            .addComponent(Destination)
	                            .addComponent(Source, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
	                    .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
	                        .addComponent(jButtonaddfav, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .addGroup(panelinputLayout.createSequentialGroup()
	                            .addComponent(clear)
	                            .addComponent(go))
	                     )
	                 )
	            )
	        )
		);
        panelinputLayout.setVerticalGroup(
            panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(panelinputLayout.createSequentialGroup()
                .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(panelinputLayout.createSequentialGroup()
                        .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonminele)
                            .addComponent(jButtonmaxele))
                        .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelstart)
                            .addComponent(Source, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelend)
                            .addComponent(Destination, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelinputLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(go)
                            .addComponent(clear))
                        .addComponent(jButtonaddfav)
                    )
               )
            )
        );
		
	}

	public void addController(ActionListener controller){
		
		Source.addActionListener(controller);
		Destination.addActionListener(controller);
		jButtonmaxele.addActionListener(controller);
		jButtonminele.addActionListener(controller);
		clear.addActionListener(controller);
		go.addActionListener(controller);
		jButtonaddfav.addActionListener(controller);
	}
	
	public static class CloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			e.getWindow().setVisible(false);
			System.exit(0);
		} //windowClosing()
	} //CloseListener

	@Override
	public void update(Observable obs, Object obj) {

    	}

} //View
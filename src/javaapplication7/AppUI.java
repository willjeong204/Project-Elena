package javaapplication7;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.*;
import java.awt.*;

import com.teamdev.jxmaps.MapViewOptions;

public class AppUI extends javax.swing.JFrame{
    private JSplitPane splitPane;
    private JPanel panelmap;
    private JPanel panelinput;
    private JLabel jLabelstart = new JLabel();
    private JLabel jLabelend = new JLabel();;
    private JTextField Destination = new JTextField();
    private JTextField Source = new JTextField();;
    private JToggleButton jToggleButtonminele = new JToggleButton();
    private JToggleButton jToggleButtonmaxele = new JToggleButton();
    private JButton clear = new JButton();
    private JButton go = new JButton();;
    private JButton jButtonaddfav = new JButton();;
    
    public AppUI() {
        initComponents();
    }
    
    private void initComponents() {
            setTitle("Elevation Navegating Application");
            setSize(1000, 750);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            
            //create 2 panels, 1 top 1 bottom
            createPanelMap();
            createpanelinput();
            
            splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setOneTouchExpandable(true);
            splitPane.setDividerLocation(550);
            splitPane.setTopComponent(panelmap);
            splitPane.setBottomComponent(panelinput);
        
            Dimension minimumSize = new Dimension(100, 50);
            panelmap.setMinimumSize(minimumSize);
            panelinput.setMinimumSize(minimumSize);
        
            setContentPane(splitPane);
    }
    
    public void createPanelMap() {
        panelmap = new JPanel();
        panelmap.setLayout( new BorderLayout() );
        MapViewOptions options = new MapViewOptions();
        options.importPlaces();
        final Google_Map_UI mapView = new Google_Map_UI();
        panelmap.add(mapView);
        
    }
    public void createpanelinput() {
        panelinput = new JPanel();
        GroupLayout panelinputLayout = new javax.swing.GroupLayout(panelinput);
        panelinput.setLayout(panelinputLayout);
        
        panelinputLayout.setAutoCreateGaps(true);
        panelinputLayout.setAutoCreateContainerGaps(true);
        
        jLabelstart.setText("Starting Point: ");
        Source.setText("Type in your Destination in here");
        jLabelend.setText("Ending Point: ");
        Destination.setText("Type in your Destination in here");
        jToggleButtonmaxele.setText("Maximum Elevation");
        jToggleButtonminele.setText("Minimum Elevation");
        clear.setText("Clear");
        go.setText("Go");
        jButtonaddfav.setText("Add to my Fav");
        
        panelinputLayout.setHorizontalGroup(
            panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelinputLayout.createSequentialGroup()
                .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelinputLayout.createSequentialGroup()
                        .addComponent(jToggleButtonminele, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jToggleButtonmaxele, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelinputLayout.createSequentialGroup()
                            .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabelend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelstart))
                            .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(Destination)
                                .addComponent(Source, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonaddfav, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelinputLayout.createSequentialGroup()
                                .addComponent(clear)
                                .addComponent(go))
                         )
                     )
                )
            )
        );
        panelinputLayout.setVerticalGroup(
            panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelinputLayout.createSequentialGroup()
                .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelinputLayout.createSequentialGroup()
                        .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jToggleButtonminele)
                            .addComponent(jToggleButtonmaxele))
                        .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelstart)
                            .addComponent(Source, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelend)
                            .addComponent(Destination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelinputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(go)
                            .addComponent(clear))
                        .addComponent(jButtonaddfav)
                    )
               )
            )
        );
    }
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppUI().setVisible(true);
            }
        });
    }
}

package com.philips.lighting.gui;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *  LightColoursFrame.java                
 *  An example showing how to change Bulb Colours using a JColorChooser.
 *
 */
public class LightsFrame extends JFrame  {

  private static final long serialVersionUID = -3830092035262367974L;
  private PHHueSDK phHueSDK;

  private JList <String> lightIdentifiersList;
  private List<PHLight> allLights;

  public LightsFrame() {
    super("Bulb Colour Changer");
    
    // The the HueSDK singleton.
    phHueSDK = PHHueSDK.getInstance();
    
    Container content = getContentPane();
   
    // Get the selected bridge.
    PHBridge bridge = phHueSDK.getSelectedBridge(); 
    
    // To get lights use the Resource Cache.  
    allLights = bridge.getResourceCache().getAllLights();
   
    DefaultListModel <String> sampleModel = new DefaultListModel<String>();
    
    for (PHLight light : allLights) {
        sampleModel.addElement(light.getIdentifier() + "  " + light.getName() );
    }

    lightIdentifiersList = new JList<String>(sampleModel);
    lightIdentifiersList.setVisibleRowCount(4);
    lightIdentifiersList.setSelectedIndex(0);

    JScrollPane listPane = new JScrollPane(lightIdentifiersList);
    listPane.setPreferredSize(new Dimension(300,100));
    
    JPanel listPanel = new JPanel();
    listPanel.setBackground(Color.white);
    
    Border listPanelBorder = BorderFactory.createTitledBorder("My Lights");
    listPanel.setBorder(listPanelBorder);
    listPanel.add(listPane);
    content.add(listPanel, BorderLayout.CENTER);
    
    JButton onButton = new JButton("On");
    onButton.addActionListener(new BulbOn());

    JButton offButton = new JButton("Off");
    offButton.addActionListener(new BulbOff());

      Border buttonPanelBorder = BorderFactory.createTitledBorder("Change Selected");
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.white);
    buttonPanel.setBorder(buttonPanelBorder);
    buttonPanel.add(onButton);
    buttonPanel.add(offButton);
    
    content.add(buttonPanel, BorderLayout.SOUTH);
    setPreferredSize(new Dimension(400,250));
    pack();
    setVisible(true);
  }

  private class BulbOn implements ActionListener {
    public void actionPerformed(ActionEvent event) {
        

            int selectedBulb = lightIdentifiersList.getSelectedIndex();
            if (selectedBulb !=-1) {
                int selectedIndex= lightIdentifiersList.getSelectedIndex();
                String lightIdentifer = allLights.get(selectedIndex).getIdentifier();
                
                PHLightState lightState = new PHLightState();
                lightState.setOn(true);
                phHueSDK.getSelectedBridge().updateLightState(lightIdentifer, lightState, null);  // null is passed here as we are not interested in the response from the Bridge. 
                
            }

    }
  } // class

    private class BulbOff implements ActionListener {
        public void actionPerformed(ActionEvent event) {


            int selectedBulb = lightIdentifiersList.getSelectedIndex();
            if (selectedBulb !=-1) {
                int selectedIndex= lightIdentifiersList.getSelectedIndex();
                String lightIdentifer = allLights.get(selectedIndex).getIdentifier();

                PHLightState lightState = new PHLightState();
                lightState.setOn(false);
                phHueSDK.getSelectedBridge().updateLightState(lightIdentifer, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.

            }

        }
    } // class

}
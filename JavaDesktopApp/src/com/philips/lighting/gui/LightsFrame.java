package com.philips.lighting.gui;

import com.philips.lighting.hue.sdk.PHHueSDK;
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


  private List<PHLight> allLights;

  public LightsFrame() {
    super("Bulb On/Off");
    
    // The the HueSDK singleton.
    phHueSDK = PHHueSDK.getInstance();
      drawFrame();

  }

 public void drawFrame() {
    Container content = getContentPane();
   
    // Get the selected bridge.
    PHBridge bridge = phHueSDK.getSelectedBridge(); 
    
    // To get lights use the Resource Cache.  
    allLights = bridge.getResourceCache().getAllLights();

      JPanel allLightsPanel = new JPanel();
      allLightsPanel.setBackground(Color.white);

    for (PHLight light : allLights) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.white);
        Border buttonPanelBorder = BorderFactory.createTitledBorder(light.getName() + " dim=" + light.getLastKnownLightState().getBrightness() );
        buttonPanel.setBorder(buttonPanelBorder);

        JButton onButton = new JButton("On " + light.getName());
        onButton.addActionListener(new BulbOn(light.getIdentifier()));
        onButton.setEnabled( !light.getLastKnownLightState().isOn() );

        JButton offButton = new JButton("Off " + light.getName());
        offButton.addActionListener(new BulbOff(light.getIdentifier()));
        offButton.setEnabled( light.getLastKnownLightState().isOn() );

        buttonPanel.add(onButton);
        buttonPanel.add(offButton);
        allLightsPanel.add(buttonPanel);
    }

    content.add(allLightsPanel);
    setPreferredSize(new Dimension(400, 250));
    pack();
    setVisible(true);
  }

  private class BulbOn implements ActionListener {
      String lightIdentifier;
      public BulbOn(String identifier) {
          this.lightIdentifier = identifier;
      }
    public void actionPerformed(ActionEvent event) {
        PHLightState lightState = new PHLightState();
        lightState.setOn(true);
        phHueSDK.getSelectedBridge().updateLightState(this.lightIdentifier, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.
    }
  } // class

    private class BulbOff implements ActionListener {
        String lightIdentifier;
        public BulbOff(String identifier) {
            this.lightIdentifier = identifier;
        }

        public void actionPerformed(ActionEvent event) {
            PHLightState lightState = new PHLightState();
            lightState.setOn(false);
            phHueSDK.getSelectedBridge().updateLightState(this.lightIdentifier, lightState, null);  // null is passed here as we are not interested in the response from the Bridge.

        }
    } // class

}
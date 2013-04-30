package accessframework;

import accessframework.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MojoSkin extends AccessFrontend {
  JTextArea myText, myDebug;
  JScrollPane myPane, myPane2;

  public MojoSkin() {
    super();
    Font f = new Font ("serif", Font.BOLD, 18);
    setTitle ("ACCESS Framework - Java Prototype");
    setSize (350, 300);
    setupButtons();
    myText = new JTextArea (10, 10);
    myText.setLineWrap (true);
    myText.setWrapStyleWord (true);
    myPane = new JScrollPane (myText);
    myText.setEditable (false);
    myText.setFont (f);
    myText.setForeground (Color.BLACK);

    myText.setText ("Welcome to ACCESS");
    
    myDebug = new JTextArea (10, 10);
    myDebug.setLineWrap (true);
    myDebug.setWrapStyleWord (true);
    myPane2 = new JScrollPane (myText);
    myDebug.setEnabled (false);
    myDebug.setFont (f);

    add (myText, BorderLayout.CENTER);
//    add (myDebug, BorderLayout.SOUTH);
    getEngine().startEngine();
    setName ("Mojo");
    setAlwaysOnTop (true);
  }

  public static void main (String args[]) {
    MojoSkin me = new MojoSkin();
    me.setVisible (true);
  }  
  public void sendMessage (String str) {
        myText.setText (str);
        System.out.println ("What up: " + str);
  }

    public void sendDebugMessage (String str) {
        myDebug.setText (str);
  }

  public void setupButtons() {
    ArrayList<String> plugs;
    JPanel myPanel;
    JButton tmp;
    ArrayList<JButton> myButtons = new ArrayList<JButton>();
    plugs = getEngine().getPluginButtons();
    Font f = new Font ("serif", Font.BOLD, 28);

    for (String p : plugs) {
      tmp = new JButton (p);
      tmp.setFont (f);
      System.out.println ("Addng button for " + p);
      tmp.addActionListener (new InvokeListener (getEngine(), p));
      myButtons.add (tmp);
    }

    myPanel = new JPanel();
    myPanel.setLayout (new GridLayout (myButtons.size(), 1));

    for (JButton b : myButtons) {
      myPanel.add (b);
    }

    add (myPanel, BorderLayout.SOUTH);
  }


}

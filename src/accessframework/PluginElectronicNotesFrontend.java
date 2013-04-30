package accessframework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PluginElectronicNotesFrontend extends JFrame implements WindowListener {
  AccessPlugin plugin;
  JTextArea myText;
  JScrollPane myPane;

  public void windowClosing (WindowEvent e) {}
  public void windowClosed (WindowEvent e) {}

  public void windowOpening (WindowEvent e) {
  }

  public void windowOpened (WindowEvent e) {
    InvokeablePlugin ip = (InvokeablePlugin)plugin;

    myText.setText ("");
    ip.invokePlugin();
  }
  
  public void windowActivated (WindowEvent e) {}
  public void windowDeactivated (WindowEvent e) {}
  public void windowIconified (WindowEvent e) {}
  public void windowDeiconified (WindowEvent e) {}


  public PluginElectronicNotesFrontend(AccessPlugin p) {
    plugin = p;
    myText = new JTextArea (10, 10);
    myPane = new JScrollPane (myText);
    add (myPane, BorderLayout.CENTER);
    setSize (300, 300);
    setName ("ElectronicNotes");
    setTitle ("Electronic Notes");
    addWindowListener (this);
    setAlwaysOnTop (true);
    setVisible (true);
  }

  public String getData() {
    return myText.getText();
  }

  public void setData (String str) {
    myText.setText (str);
  }
}
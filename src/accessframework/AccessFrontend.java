package accessframework;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class AccessFrontend extends JFrame implements ActionListener, WindowListener {
  AccessEngine myEngine;
//  SystemTray myTray;
//  TrayIcon myIcon;
  JButton praise, scold;

  public AccessFrontend() {
    JPanel myPanel;

    myEngine = new AccessEngine(this);
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    addWindowListener (this);

    praise = new JButton ("I like this change");
    praise.addActionListener (this);
    scold = new JButton ("I don't like this change");
    scold.addActionListener (this);

    praise.setEnabled (false);
    scold.setEnabled (false);

    myPanel = new JPanel ();
    myPanel.setLayout (new GridLayout (1, 2));

    myPanel.add (praise);
    myPanel.add (scold);

    add (myPanel, BorderLayout.NORTH);

//    setupSystemTray();

    this.setState (Frame.ICONIFIED);

  }

  public void windowDeactivated (WindowEvent e) {}
  public void windowActivated (WindowEvent e) {}
  public void windowDeiconified (WindowEvent e) {}
  public void windowIconified (WindowEvent e) {}
  public void windowClosed (WindowEvent e) {}
  public void windowClosing (WindowEvent e) {
    myEngine.shutdown();
  }
  public void windowOpened (WindowEvent e) {}

  public void sendMessage (String str) {

      System.out.println ("SENDING MESSAGE: " + str);
      
    JOptionPane.showMessageDialog (this, str, "Hello", JOptionPane.INFORMATION_MESSAGE);
  }

    public void sendDebugMessage (String str) {

      System.out.println ("SENDING MESSAGE: " + str);

    JOptionPane.showMessageDialog (this, str, "Hello", JOptionPane.INFORMATION_MESSAGE);
  }

  /*
  void setupSystemTray() {
    Image img;
    ArrayList<String> plugs;
    PopupMenu myPopup;
    MenuItem tmp;

    if (SystemTray.isSupported()) {
      plugs = myEngine.getInvokeablePlugins();
      img = Toolkit.getDefaultToolkit().getImage ("logo.gif");
      System.out.println ("Image is " + img);
      myTray = SystemTray.getSystemTray();
      myIcon = new TrayIcon (img);
      myPopup = new PopupMenu();

      tmp = new MenuItem ("Exit");
      tmp.addActionListener (this);
      myPopup.add (tmp);

      for (String p : plugs) {
        tmp = new MenuItem (p);
        tmp.addActionListener (new InvokeListener (myEngine, p));
        myPopup.add (tmp);
      }

      myIcon.setImageAutoSize (true);
      myIcon.addActionListener (this);
      myIcon.setPopupMenu (myPopup);
      try {
        myTray.add (myIcon);
      }
      catch (Exception ex) {
        System.out.println ("Tray could not be added");
      }
    }
    else {
      System.out.println ("System tray not supported");
    }
  }
*/

  public void permitReinforcement() {
    this.setState (Frame.NORMAL);
    praise.setEnabled (true);
    scold.setEnabled (true);
  }

  public void prohibitReinforcement() {
    praise.setEnabled (false);
    scold.setEnabled (false);
  }

  public void actionPerformed (ActionEvent e) {
//    if (e.getSource() == myIcon) {
//      System.exit(0);
//    }
      if (e.getSource() == praise) {
          sendMessage("Okay, I will be more willing to make similar corrections in the future.");
          getEngine().reinforcePlugin (true);
      }
      else {
          sendMessage("Okay, I will be less willing to make similar corrections in the future.");
          getEngine().reinforcePlugin (false);
      }

      praise.setEnabled (false);
      scold.setEnabled (false);
      this.setState (Frame.ICONIFIED);
  }

  protected AccessEngine getEngine() {
    return myEngine;
  }

}
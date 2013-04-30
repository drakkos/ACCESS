package accessframework;

import java.awt.event.*;
import javax.swing.*;

public class InvokeListener implements ActionListener {
  private AccessEngine myEngine;
  private String plugin;

  public InvokeListener (AccessEngine e, String p) {
    myEngine = e;
    plugin = p;
  }

  public void actionPerformed (ActionEvent e) {
    myEngine.invokePlugin (plugin);
  }
}
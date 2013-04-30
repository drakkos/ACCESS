package accessframework;

import accessframework.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class PluginElectronicNotes extends AccessPlugin implements InvokeablePlugin {
    HashMap <String,String> notes;
    PluginElectronicNotesFrontend myFrontend;
    String lastApp;
    int count;


    public PluginElectronicNotes (AccessEngine e) {
      super (e);
      notes = new HashMap <String,String>();
      lastApp = "Unknown";
      count = 0;
    }

    public boolean displayButton() {
        return false;
    }
    public void positiveReinforcement() {
    }
    public void negativeReinforcement() {
    }

    public void mouseMoved(long time, int x, int y) {
    }

    public void mouseClicked (long time, String button) {
    }

    public void keyTyped (long time, String key) {
    }

    public void create() {
    }

    public void performCorrection() {
    }

    public String getName() {
      return "Electronic Notes";
    }

    public void tick() {
      String activeApp;

      count += 1;

      if (count == 5) {
          count = 0;
          System.out.println ("Update");
          activeApp = getEngine().getContext().getActiveWindowTitle();
    //      System.out.println ("Last app "+ lastApp + " vs " + activeApp);

          if (activeApp == null) {
            return;
          }

          if (activeApp.equalsIgnoreCase ("Untitled")) {
            return;
          }

          if (myFrontend != null) {
            if (lastApp != null && lastApp.equalsIgnoreCase (activeApp)) {
              notes.put (lastApp, myFrontend.getData());
            }
            else {
              myFrontend.setData (notes.get (activeApp));

            }
            lastApp = activeApp;
          }
          else {
              lastApp = activeApp;
          }
      }



    }

    public void heartbeat() {


    }

    public void reset() {
    }

    public synchronized void invokePlugin() {
      String activeApp;

      
      if (myFrontend == null) {
        myFrontend = new PluginElectronicNotesFrontend (this);
      }

      myFrontend.setVisible (true);
      System.out.println ("Active App Is: " + lastApp);
      myFrontend.setData (notes.get (lastApp));
      
    }

    public Object getState() {
      return notes;
    }

    public void setState(Object ob) {
      HashMap<String,String> newS = (HashMap<String,String>)ob;

      notes = newS;

    }

    public boolean wantsToMakeCorrection() {
      return false;
    }

    public void saveMe() {
        FileWriter fw;
        PrintWriter pw;
        BufferedWriter bw;
        File f;
        Set<String> keys;
        ArrayList<String> bits = new ArrayList<String>();
        StringTokenizer myToken;
        String key;

        String dir = getEngine().getContext().getUserHomeDir();

        f = new File (dir + "notes/");

        if (f.exists() == false) {
            f.mkdir();
        }

        keys = notes.keySet();

        for (String k : keys) {

            myToken = new StringTokenizer (k, "/\\");

            while (myToken.hasMoreTokens()) {
                bits.add (myToken.nextToken());
            }

            key = bits.get (bits.size()-1);

            System.out.println("Saving: " + key + ":" + notes.get(k));
            try {
                fw = new FileWriter (dir + "notes/" + key);
                bw = new BufferedWriter (fw);
                pw = new PrintWriter (bw);

                pw.println (notes.get (k));
                pw.close();
            }
            catch (Exception ex) {
                System.out.println ("Exception: " + ex);
            }

        }
    }

    public void loadMe() {
        FileReader fr;
        BufferedReader br;
        File[] files;
        File f;
        String dir;
        String tmp, txt = "";
        ArrayList<String> bits = new ArrayList<String>();
        StringTokenizer myToken;
        String key;

        dir = getEngine().getContext().getUserHomeDir();

        f = new File (dir + "notes/");

        files = f.listFiles();

        if (files == null || files.length == 0) {
            System.out.println ("No files");
            return;
        }
        
        for (File fi : files) {
            try {
                fr = new FileReader (fi);
                br = new BufferedReader (fr);
                txt = "";
                bits.clear();
                tmp = br.readLine();

                while (tmp != null) {
                    txt = txt + tmp;
                    txt += System.getProperty ("line.separator");
                    tmp = br.readLine();
                }

                myToken = new StringTokenizer (fi.getAbsolutePath(), "/\\");

                while (myToken.hasMoreTokens()) {
                    bits.add (myToken.nextToken());
                }

                key = bits.get (bits.size()-1);
                System.out.println (key + ":" + txt);
                notes.put (key, txt);
            }
            catch (Exception ex) {
                System.out.println ("Exception: " + ex);
            }

        }

        System.out.println ("Loaded: " + notes);

    }

}
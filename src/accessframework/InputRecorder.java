package accessframework;

import accessframework.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

public class InputRecorder extends AccessPlugin implements InvokeablePlugin  {
    public static final int MOUSE_CLICK_LEFT = 0;
    public static final int MOUSE_CLICK_RIGHT = 1;
    public static final int MOUSE_MOVE = 2;
    public static final int KEY_RELEASE = 3;
    public static final int KEY_PRESS = 4;

    ArrayList<PointTime> myPoints;
    ArrayList<KeyTime> myKeys;
    InputRecorderFrontend myFrontend;
    boolean recording, saved;
    long counter, firstTime;
    int lastIndex;
    InputStreamReader ir;
    BufferedReader br;
    OutputStreamWriter ow;
    BufferedWriter bw;

    public boolean displayButton() {
        return false;
    }

    public InputRecorder (AccessEngine e) {
        super (e);

        myPoints = new ArrayList<PointTime>();
        myKeys  = new ArrayList<KeyTime>();

        myFrontend = new InputRecorderFrontend (this);
    }

    void positiveReinforcement() {
    }

    void negativeReinforcement() {
    }

    void mouseMoved(long time, int x, int y) {
        if (recording) {
            PointTime p = new PointTime (x, y, time);
            p.setExtra (MOUSE_MOVE);
            myPoints.add (p);
        }
    }
    
    void mouseClicked (long time, String button) {
        System.out.println ("Button is " + button);

        if (recording) {
            PointTime p = new PointTime (-1, -1, time);
            p.setExtra (MOUSE_CLICK_LEFT);
            myPoints.add (p);
        }
    }
    
    void keyTyped (long time, String key) {

    }

    void keyDown (long time, String key) {
    }

    void keyReleased (long time, String key) {
    }

    void notSelected() {
    }

    void create() {
    }

    boolean wantsToMakeCorrection() {
        return false;
    }

    void performCorrection() {
    }

    String getName() {
        return "Input Recorder";
    }

    void heartbeat() {
    }

    void reset() {
    }

    void tick() {
    }

    void setState (Object ob) {
    }

    Object getState () {
        return null;
    }

    void saveMe() {
    }

    void loadMe() {
    }

    public String getMessage() {
        return "How did you see this?";
    }

    public void loadData (String filename) {
        if (filename == null) {
            return;
        }
        
        myPoints.clear();
        
        FileReader readBase;
        BufferedReader in;
        String temp;

        try {
           readBase = new FileReader (filename);
           in = new BufferedReader (readBase);
           temp = in.readLine();

           while (temp != null) {
               myPoints.add (new PointTime (temp));
               temp = in.readLine();
           }
           in.close();
           
        }

        catch (IOException except) {
            return;
        }
    }

    public void saveData (String filename) {
            FileWriter baseWriter;

        if (filename == null) {
            return;
        }

        try {
            if (saved) {
                baseWriter = new FileWriter (filename, true);
            }
            else {
                baseWriter = new FileWriter (filename);
                saved = true;
            }
            BufferedWriter buffer =new BufferedWriter (baseWriter);
            PrintWriter out = new PrintWriter (buffer);

            for (int i = 0; i < myPoints.size(); i++) {
               out.println (myPoints.get(i).toString());
            }

            myPoints.clear();
            out.close();
        }
        catch (Exception ex) {
            System.out.println ("Error saving");
        }
    }

    public void clearData() {
        recording = false;
        myPoints.clear();
    }

    public void playBack() {
        long firstTime = 0, delay;

        if (myPoints.size() > 0) {
            firstTime = myPoints.get(0).getTime();
        }

        for (int i = 1; i < myPoints.size(); i++) {
            delay = myPoints.get(i).getTime() - firstTime;
            getEngine().robotSetDelay ((int)delay);

            switch (myPoints.get(i).getExtra()) {
                case MOUSE_MOVE:
                    getEngine().robotMouseMove (myPoints.get(i).getX(), myPoints.get(i).getY());
                break;
                case MOUSE_CLICK_LEFT:
                    getEngine().robotMouseClick (InputEvent.BUTTON1_MASK);
                    getEngine().robotMouseRelease (InputEvent.BUTTON1_MASK);
                break;    
            }

            firstTime = myPoints.get(i).getTime();
        }

    }
    public synchronized void invokePlugin() {
        if (myFrontend.isVisible() == false) {
            myFrontend.setVisible (true);
        }

        myFrontend.toFront();
    }

    public void setRecording (boolean b) {
        recording = b;
    }

    public boolean getRecording() {
        return recording;
    }

    public  int createSocketPort () {
        return 10001;
    }

    public void parseEntry (String str) {
        System.out.println ("Server: " + str);
        String[] bits;

        if (str.equals ("start")) {
            setRecording (true);
        }
        else if (str.equals ("stop")) {
            setRecording (false);
        }
        else {
             bits = str.split (" ");
             saveData (bits[1]);
        }
    }

}
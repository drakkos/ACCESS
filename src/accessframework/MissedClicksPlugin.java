package accessframework;

import java.awt.geom.Point2D;
import java.util.*;

public class MissedClicksPlugin extends AccessPlugin {

    private int counter;
    private int threshold = 10;
    
    public MissedClicksPlugin (AccessEngine e) {
        super(e);

    }

    public String queryLogDetails() {
        return "counter (" + counter  + "), Threshold (" + threshold  + ")";
    }

    public void positiveReinforcement() {
    }

    public void negativeReinforcement() {
        getEngine().getContext().setPointerPrecision (false);
    }


    public void mouseMoved(long time, int x, int y) {
//        System.out.println ("" + x + ", " + y);
    }

    public void mouseClicked(long time, String button) {
//        System.out.println ("Mouse clicked");
    }

    public void keyTyped(long time, String key) {
    }

    public void create() {
    }

    public boolean wantsToMakeCorrection() {
        if (counter >= threshold && getEngine().getContext().getPointerPrecision() == false) {
            return true;
        }
        return false;
    }

    public void performCorrection() {
        getEngine().getContext().setPointerPrecision (true);
        counter = 0;
    }

    public String getName() {
        return "Missed Clicks Detector";
    }

    public void heartbeat() {
    }

    public void reset() {
        counter = 0;
    }

    public void invokePlugin() {
    }

    public Object getState() {
        return null;
    }

    public void setState(Object ob) {
    }

    public void tick() {
    }

    public void saveMe() {
    }

    public void loadMe() {
    }

    public String getMessage() {
        return "The precision of the mouse cursor has been increased.";
    }

    public int createSocketPort () {
        return 10000;
    }

    public void parseEntry (String str) {
        System.out.println ("Server blah: \"" + str + "\"");
        
        if (str.equalsIgnoreCase ("miss")) {
            counter += 1; 
            System.out.println ("Counter is " + counter);
        }
    }
}

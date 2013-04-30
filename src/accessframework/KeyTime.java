/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package accessframework;

import java.util.*;

/**
 *
 * @author Drakkos
 */
public class KeyTime {
    private int key;
    private long time;
    private int extra;

    void setExtra (int e) {
        extra = e;
    }

    int getExtra() {
        return extra;
    }


    public KeyTime (int k, long ntime) {
        key = k;
        time = ntime;
    }

    public KeyTime (String str) {
        constructData (str);
    }

    private void setKey (int k) {
        key = k;
    }

    private void setTime (long nt) {
        time = nt;
    }

    public int getKey() {
        return key;
    }

    public long getTime() {
        return time;
    }

    public String toString() {
        return getTime() + "," + getKey() + "," + getExtra();
    }

    public void constructData (String str) {
        String[] bits = str.split (",");

        setTime (Long.parseLong (bits[0]));
        setKey (Integer.parseInt (bits[1]));
        setExtra (Integer.parseInt (bits[3]));

    }
}

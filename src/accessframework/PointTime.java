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
public class PointTime {
    private int x;
    private int y;
    private long time;
    private int extra, extra2;

    void setExtra (int e) {
        extra = e;
    }

    int getExtra() {
        return extra;
    }


    public PointTime (int nx, int ny, long ntime) {
        x = nx;
        y = ny;
        time = ntime;
    }

    public PointTime (String str) {
        constructData (str);
    }

    private void setX (int nx) {
        x = nx;
    }

    private void setY (int ny) {
        y = ny;
    }

    private void setTime (long nt) {
        time = nt;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getTime() {
        return time;
    }

    public String toString() {
        return getTime() + "," + getX() + "," + getY() + "," + getExtra();
    }

    public void constructData (String str) {
        String[] bits = str.split (",");

        setTime (Long.parseLong (bits[0]));
        setX (Integer.parseInt (bits[1]));
        setY (Integer.parseInt (bits[2]));
        setExtra (Integer.parseInt (bits[3]));

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package accessframework;

import java.util.*;
import java.awt.*;


/**
 *
 * @author Drakkos
 */
public class MouseLocatorBase extends AccessPluginMouse {

    ArrayList<PointTime> points;
    private boolean beenScolded;
    private long now, last;
    private int tremorThreshold;
    private int shakeThreshold;
    private int shakeCounter;
    private int cornerLength = 20;
    private int cornerHeight = 20;
    private int cornerCounter;
    Dimension screen;


    public MouseLocatorBase(AccessEngine e) {
        super(e);

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        points = new ArrayList<PointTime>();

        shakeCounter = 0;
        shakeThreshold = 2;
        tremorThreshold = 80;

		// Get the current screen size
	screen = toolkit.getScreenSize();

        System.out.println ("Screen is " + screen.getHeight() + ", " + screen.getWidth());
        System.out.println ("X is " + (screen.getHeight()-cornerHeight));
        System.out.println ("Y is " + (screen.getWidth()-cornerLength));

    }

    public boolean getIsInRectangle (int x, int y, int len, int ht, int mx, int my) {
        if (mx < x || my < y) {
            return false;
        }
        
        if (mx > x + len || my > y + ht) {
            return false;
        }
        
        return true;
    }
    public boolean getIsInCorner (int x, int y) {
        if (getIsInRectangle (0, 0, cornerLength, cornerHeight, x, y)) {
            return true;
        }

/*        if (getIsInRectangle (
                (int)screen.getWidth()-cornerLength,
                0,
                cornerLength, cornerHeight,
                x, y)) {
                return true;
        }*/

        return false;
    }
    public void registerPoint (int x, int y) {
        now = System.currentTimeMillis();
        points.add(new PointTime(x, y, now));
    }

    public void positiveReinforcement() {
        tremorThreshold += 1;
        shakeThreshold -= 1;
    }

    public void negativeReinforcement() {
        tremorThreshold -= 1;
        shakeThreshold += 1;
    }

    public String queryLogDetails() {
        return "Tremor (" + tremorThreshold + "), Shake (" + shakeThreshold + ")";
    }

    public int getDistance(Direction d1, Direction d2) {
        int counter = 0;
        int current = 0;
        boolean inc = true;
        boolean dir;

        current = d1.getDir();

        if (d1 == Direction.UNKNOWN || d2 == Direction.UNKNOWN) {
            return 0;
        }

        while (current != d2.getDir()) {
            current += 1;

            if (inc) {
                counter += 1;
            } else {
                counter -= 1;
            }

            if (counter >= 4) {
                inc = false;
            }

            if (current > Direction.NORTHWEST.getDir()) {
                current = Direction.NORTH.getDir();
            }
        }

        if (inc) {
            return counter;
        }
        else {
            return counter * -1;
        }
    }

    public Direction getDirection(PointTime p1, PointTime p2) {
        int xDir, yDir;

        xDir = p1.getX() - p2.getX();
        yDir = p1.getY() - p2.getY();

        if (xDir < 0 && yDir == 0) {
            return Direction.WEST;
        }

        if (xDir > 0 && yDir == 0) {
            return Direction.EAST;
        }

        if (xDir == 0 && yDir < 0) {
            return Direction.NORTH;
        }

        if (xDir == 0 && yDir > 0) {
            return Direction.SOUTH;
        }

        if (xDir < 0 && yDir < 0) {
            return Direction.NORTHWEST;
        }

        if (xDir < 0 && yDir > 0) {
            return Direction.SOUTHWEST;
        }

        if (xDir > 0 && yDir < 0) {
            return Direction.NORTHEAST;
        }

        if (xDir > 0 && yDir > 0) {
            return Direction.SOUTHEAST;
        }

        return Direction.UNKNOWN;
    }

    public int getAbruptness(Direction a, Direction b) {
        Direction high, low;
        int val;

        if (a.getDir() > b.getDir()) {
            high = a;
            low = b;
        } else if (b.getDir() > a.getDir()) {
            high = b;
            low = a;
        } else {
            high = a;
            low = b;
        }

        val = Math.abs (getDistance(high, low));

        if (val == 4) {
            return 2;
        }
        if (val > 2) {
            return 1;
        }

        return 0;

    }

    public ArrayList<Direction> flattenDirections (ArrayList<Direction> dirs) {
        ArrayList<Direction> flattened = new ArrayList<Direction>();
        Direction last, current;

        for (int i = 1; i < dirs.size(); i++) {
            last = dirs.get(i-1);
            current = dirs.get(i);

            if (current != last) {
                flattened.add (current);
            }
        }

        return flattened;
    }

    public boolean detectSpiral (ArrayList<PointTime> points) {
        ArrayList<Direction> normalisedDirs = new ArrayList<Direction>();
        Direction nowD;
        PointTime tmp1, tmp2;
        int count, val, dist;
        boolean dir, lastDir = false;

        if (points.size() < 10) {
           return false;
        }

        try {
//            System.out.println ("Detecting spiral");
            for (int i = 0; i < points.size() - 1; i++) {
                tmp1 = points.get(i);
                tmp2 = points.get(i + 1);

                nowD = getDirection(tmp1, tmp2);

                normalisedDirs.add (nowD);
            }

            normalisedDirs = flattenDirections (normalisedDirs);

//            getEngine().sendMessage ("Sizeof dirs is " + normalisedDirs);

            count = 0;
            for (int i = 1; i < normalisedDirs.size(); i++) {
                if (normalisedDirs.get(i) == normalisedDirs.get(i-1)) {
                    continue;
                }

                val = getDistance (normalisedDirs.get(i), normalisedDirs.get(i-1));
                dist = Math.abs (val);

                if (val < 0) {
                    dir = false;
                }
                else if (val > 0) {
                    dir = true;
                }
                else {
                    continue;
                }


                if (dist > 2) {
                    continue;
                }

                if (dir == lastDir) {
                    count += 1;
                }
                else {
                       count = 0;
                 }

                lastDir = dir;

                if (count >= 4) {
                    return true;
                }
            }
        }
        catch (Exception ex) {
            System.out.println (ex.getMessage());
        }
        return false;
    }

    public boolean detectDirectionChange(ArrayList<PointTime> points) {
        PointTime tmp1, tmp2;
        Direction lastd = Direction.UNKNOWN, nowd;
        int numDirChanges = 0;
        int abruptnessCounter = 0;
        double percent;
        double avgAbrupt, overall;

//        System.out.println ("Num of points is " + points.size());

        try {
            // Don't bother if we have less than 50 data points
            if (points.size() < 50) {
                return false;
            }

            // Discard the tails of the movement.
            for (int i = 0; i < points.size() - 2; i++) {
                tmp1 = points.get(i);
                tmp2 = points.get(i + 1);

                nowd = getDirection(tmp1, tmp2);

                if (lastd != Direction.UNKNOWN && nowd != lastd) {
                    if (Math.abs (getDistance(nowd, lastd)) > 1) {
                        numDirChanges += 1;
                        abruptnessCounter += getAbruptness(nowd, lastd);
                    }
                }

                lastd = nowd;
            }

            // Get the percentage of movements that were changes.

    //        System.out.println ("Vals :" + numDirChanges + ", " + points.size() + ", " + abruptnessCounter);

            percent = ((double) numDirChanges / (double) points.size()) * 100.0;
            avgAbrupt = ((double) abruptnessCounter / (double) points.size()) * 100.0;
            overall = avgAbrupt * percent;


            if (overall > 5) {
                System.out.println("Shake detected");
                return true;
            }
        }
        catch (Exception ex) {
            System.out.println (ex.getMessage());
        }
        return false;
    }

    public int getCursorSize() {
        return getEngine().getContext().getCursorSize();
    }




    public void keyTyped(long time, String key) {
    }

    public void create() {
    }

    public void clearPoints() {
        points.clear();
    }

    public ArrayList<PointTime> getPoints() {
        return points;
    }

    public void mouseMoved(long time, int x, int y) {
        now = System.currentTimeMillis();


//        System.out.println (time + ": " + x + "," + y);
        
        if (now - last > 100) {
            if (getIsInCorner (x, y)) {
                getEngine().sendDebugMessage ("Corner detected: " + shakeCounter);
                shakeCounter += 1;
            }
            else if (detectDirectionChange(points) == true) {
                shakeCounter += 1;
                getEngine().sendDebugMessage ("Shake Detected: " + shakeCounter);
            }
            else if (detectSpiral(points) == true) {
                shakeCounter += 1;
                getEngine().sendDebugMessage ("Spiral Detected: " + shakeCounter);
            }

            clearPoints();
        }

        registerPoint (x, y);
        last = now;
    }

    public void mouseClicked(long time, String button) {
        clearPoints();
    }

    public boolean wantsToMakeCorrection() {
        if (shakeCounter >= shakeThreshold) {
            return true;
        }
        return false;
    }

    public void performCorrection() {
            shakeCounter = 0;
            clearPoints();
    }

    public String getName() {
        return "Mouse Locator Base";
    }

    public void heartbeat() {
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

    public void notSelected() {
        shakeCounter = 0;
    }
}

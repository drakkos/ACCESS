package accessframework;

import java.awt.geom.Point2D;
import java.util.*;

public class DirectionChanges extends AccessPluginMouse {

    ArrayList<PointTime> points;
    long now, last;
    int threshold;
    int counter, times;

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

    public DirectionChanges(AccessEngine e) {
        super(e);

        threshold = 500;
        times = 5;

        points = new ArrayList<PointTime>();

    }
    public String queryLogDetails() {
        return "Threshold (" + threshold   + "), Times (" + times  + ")";
    }

    public void positiveReinforcement() {
          threshold -= 100;
          times -= 1;
    }

    public void negativeReinforcement() {
        threshold += 100;
        times += 1;
    }

    public Direction getAverageDirection(ArrayList<PointTime> points) {
        int total = 0;
        double avg;
        PointTime lastone;
        PointTime first;
        Direction startDir;

        if (points.size() < 5) {
//            System.out.println("Not enough points");
            return Direction.UNKNOWN;
        }

        first = points.get(0);
        lastone = points.get(0);

        startDir = getDirection(first, points.get(1));

        System.out.println("Start dir is " + startDir);

        for (int i = 2; i < points.size(); i++) {
            total += getDistance(startDir, getDirection(lastone, points.get(i)));
            lastone = points.get(i);
        }

        total /= points.size();

        avg = startDir.getDir() + total;

        if (avg > 8) {
            avg -= 8;
        }

//        System.out.println("Average direction is " + (int) avg);
        for (Direction d : Direction.values()) {
            if (d.getDir() == (int) avg) {
                return d;
            }
        }

        return Direction.UNKNOWN;

    }

    public boolean detectDoubleback(ArrayList<PointTime> points) {
        ArrayList<PointTime> start, end;
        long rightNow, cutoff;
        Direction d1, d2;

        rightNow = System.currentTimeMillis();

        cutoff = rightNow - threshold;

        start = new ArrayList<PointTime>();
        end = new ArrayList<PointTime>();

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getTime() < cutoff) {
                start.add(points.get(i));
            } else {
                end.add(points.get(i));
            }
        }

        if (start.size() < 10) {
            return false;
        }

        if (end.size() < 10) {
            return false;
        }

        d1 = getAverageDirection(start);
        d2 = getAverageDirection(end);
        
        if (Math.abs (getDistance(d1, d2)) >= 1) {
            return true;
        }

        return false;
    }


    public void mouseMoved(long time, int x, int y) {
        now = System.currentTimeMillis();

        points.add(new PointTime(x, y, now));

    }

    public void mouseClicked(long time, String button) {
        System.out.println("Mouse clicked");

        if (detectDoubleback(points) == true) {
            counter += 1;
            System.out.println("Doubleback is " + counter);
        }
        points.clear();
    }

    public void keyTyped(long time, String key) {
    }

    public void create() {
    }

    public boolean wantsToMakeCorrection() {
        int val;

        val = getEngine().getContext().getMouseSpeed();
        
        if (val == 1) {
            return false;
        }

        if (counter >= times) {
            counter = 0;
            return true;
        }


        return false;
    }

    public void performCorrection() {
        int val;

        val = getEngine().getContext().getMouseSpeed();
        
        if (val == 1) {
            return;
        }
        
        val -=1;
                
        getEngine().getContext().setMouseSpeed (val);
    }

    public String getName() {
        return "Doubleback Detector";
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
        return "The mouse cursor has been made a little slower which should make " +
                "it easier to click on targets.";

    }
}

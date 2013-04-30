package accessframework;


public class DoubleClickPlugin extends AccessPlugin {

    private int current;
    private long lastClick;
    private int threshold;
    private int counter;
    private int offset = 100;
    private int motion = 0;

    public DoubleClickPlugin (AccessEngine e) {
      super (e);
      current = getEngine().getContext().getDoubleClickDelay();
      threshold = 2;
    }

    public String queryLogDetails() {
        return "Threshold (" + threshold   + "), Current (" + current  + ")";
    }

    public void positiveReinforcement() {
        threshold -= 1;
    }
    public void negativeReinforcement() {
        current -= offset;

        getEngine().getContext().setDoubleClickDelay (current);
        threshold += 1;

        System.out.println ("Delay is now " + getEngine().getContext().getDoubleClickDelay());
    }

    public void mouseMoved(long time, int x, int y) {
        // If the mouse moves too much, reset the last click - this wasn't
        // an attempt to double click at all.
        if (lastClick == 0) {
            return;
        }
        
        if (motion > 10) {
            System.out.println ("Reseting click");
            lastClick = 0;
            motion = 0;
        }
        motion += 1;
    }

    public void mouseClicked (long time, String button) {
        long currentClick;
        long delay = 0;
        currentClick = System.currentTimeMillis();

        delay = currentClick - lastClick;

        if (delay >= 1000) {
            lastClick = currentClick;
   //         System.out.println ("No double click - " + delay);
            return;
        }


//        System.out.println ("Current is " + current);
        // Let's use one second as the double click limit.


        System.out.println ("Delay is " + delay + ", current is " + current);

        // The delay between these clicks was greater than the
        // double click threshold;
        if (delay > current) {
            counter += 1;
            System.out.println ("Counter increased to " + counter);
        }

        lastClick = 0;
    }

    public void keyTyped (long time, String key) {
    }

    public void create() {
    }

    public boolean wantsToMakeCorrection() {
      if (counter >= threshold && offset > 0) {
          return true;
      }
      return true;
    }

    public void performCorrection() {
        current += offset;

        getEngine().getContext().setDoubleClickDelay (current);
        System.out.println ("Delay is now " + getEngine().getContext().getDoubleClickDelay());
        counter = 0;
    }

    public String getName() {
      return "Double Click Plugin";
    }

    public void heartbeat() {
    }

    public void reset() {
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
        return "Double-clicking has been made a little easier to do.";
    }
}
package accessframework;

import accessframework.*;

public class DynamicKeyboard extends AccessPlugin {
  private int currentKeyDelay;
  private long keyDownTime;
  private int counter;
  private boolean keyDown;
  private int threshold;
  long keyUpTime;
  private double mult;

  public DynamicKeyboard(AccessEngine e) {
    super (e);
    currentKeyDelay = getEngine().getContext().getKeyRepeatDelay();
    threshold = 2;
    mult = 1000;
    
  }

  public boolean wantsToMakeCorrection() {
    if (counter >= threshold) {
      return true;
    }
    return false;
  }

  public void performCorrection() {
    if (counter >= threshold) {
      getEngine().getContext().setKeyRepeatDelay (currentKeyDelay + 1);
      currentKeyDelay = getEngine().getContext().getKeyRepeatDelay();
      counter = 0;
    }
  }

    public void positiveReinforcement() {
        threshold -= 1;

    }
    public void negativeReinforcement() {
        threshold += 1;
        currentKeyDelay -= 1;

        getEngine().getContext().setKeyRepeatDelay (currentKeyDelay);
    }

    public void mouseMoved(long time, int x, int y) {

    }

    public void mouseClicked (long time, String button) {

    }

    public void keyReleased (long time, String key) {
        long diff;

//        System.out.println ("Key Released");
        keyUpTime = System.currentTimeMillis();

        diff = keyUpTime - keyDownTime;

  //      System.out.println ("Diff is " + diff + ", delay is " + currentKeyDelay);
        
        if (diff > currentKeyDelay && diff > (mult * currentKeyDelay)) {
            System.out.println ("Counter Increased");
            counter += 1;
        }

    }

    public void keyTyped (long time, String key) {
    }
    
    public void keyDown (long time, String key) {
 //     System.out.println ("Typing key " + key + ", " + currentKeyDelay + ", " + keyDown);
      keyDownTime = System.currentTimeMillis();
      
    }

    public void create() {}
    public String getName() {
      return "The Dynamic Keyboard";
    }

    public void heartbeat() {}
    public void reset() {
        counter = 0;
    }
    
    public void tick() {}
    public void setState (Object ob) {}
    public Object getState () {
      return null;
    }

    public void saveMe() {
    }

    public void loadMe() {
    }

    public String getMessage() {
        return "This plugin has increased the delay between you pressing a " +
            "a key and the key repeating.";
    }
}
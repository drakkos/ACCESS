package accessframework;


public class Clacker extends AccessPlugin implements InvokeablePlugin{

    public Clacker (AccessEngine e) {
      super (e);
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
      System.out.println ("HELLO: " + key);
      getEngine().sendMessage (time + "" + key);
    }

    public void create() { 
    }

    public boolean wantsToMakeCorrection() {
      return false;
    }
    
    public void performCorrection() {
    }

    public String getName() {
      return "Clacker";
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
}
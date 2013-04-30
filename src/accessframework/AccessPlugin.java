package accessframework;

abstract class AccessPlugin {
    private AccessEngine myEngine;

    public AccessPlugin (AccessEngine e) {
      myEngine = e;
    }

    public AccessEngine getEngine() {
      return myEngine;
    }

    public String queryLogDetails() {
        return "";
    }
    // Handles positive reinforcement for a plugin.
    abstract void positiveReinforcement();
    // Handles negative reinforcement for a plugin
    abstract void negativeReinforcement();
    // Is called when the framework registers a mouse move event
    abstract void mouseMoved(long time, int x, int y);
    // Is called when the framework registers a mouse click event
    abstract void mouseClicked (long time, String button);
    // Is called when the framework registers a mouse click event
    abstract void keyTyped (long time, String key);
    // Is called when the plugin is first added - it's essentially
    // a constructor for the plugin.
    void keyDown (long time, String key) {
    }
    
    void keyReleased (long time, String key) {
    }

    void notSelected() {
    }
    abstract void create();
    // Is called to determine if the plugin wants to make some
    // kind of correction.
    abstract boolean wantsToMakeCorrection();
    // Is called when the plugin is selected by the framework to
    // perform some kind of corrective action.
    abstract void performCorrection();
    // Defines the name of the plugin (this should be a unique identifier)
    abstract String getName();
    // Is called every second by the framework.
    abstract void heartbeat();
    // Is called every hour by the framework.
    abstract void reset();
    // Is called every single tick.
    abstract void tick();

    abstract void setState (Object ob);

    abstract Object getState ();
    abstract void saveMe();
    abstract void loadMe();

    public String getMessage() {
        return "Sorry, I have not been set up to provide you with useful information";
    }

    public int createSocketPort () {
        return 0;
    }

    public void parseEntry (String str) {
    }

    public void mouseScrollUp() {
    }

    public void mouseScrollDown() {
    }

}

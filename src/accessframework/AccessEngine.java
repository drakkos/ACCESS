package accessframework;

/**
 *  Access Framework engine
 * @author Michael Heron
 * @started 6th of May, 2009
 */
import accessframework.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class AccessEngine implements ActionListener {

    public static final int TICK_INTERVAL = 100;
    public static final int SECOND = (1000 / TICK_INTERVAL);
    public static final int CORRECTION_INTERVAL = 10 * (SECOND);
    public static final int AUTO_ACCEPT_THRESHOLD = 2;
    ArrayList<AccessPlugin> myPlugins;
    AccessPlugin selected, lastSelected;
    HashMap<AccessPlugin, Integer> reinforcement;
    HashMap<String, String> metrics;
    OperatingSystemContext os;
    AccessFrontend myFrontend;
    ArrayList<InputEntry> inputToProcess;
    ArrayList<PluginServer> pluginServers;
    AccessInputParser myParser;
    long lastRead;
    int oneSec, oneHour;
    int hb_count, inputCount;
    javax.swing.Timer t;
    Robot robby;
    boolean keyCurrentlyPressed;
    int awaitingCorrection;
    AccessServer as;
    String logFile;

    public void setMetric(String key, String value) {
        metrics.put(key, value);
    }

    public String getMetric(String key) {
        return metrics.get(key);
    }

    public AccessEngine(AccessFrontend f) {
        myPlugins = new ArrayList<AccessPlugin>();
        reinforcement = new HashMap<AccessPlugin, Integer>();
        metrics = new HashMap<String, String>();
        t = new javax.swing.Timer(TICK_INTERVAL, this);
        hb_count = 0;
        inputToProcess = new ArrayList<InputEntry>();
        myParser = new AccessInputParser();
        myFrontend = f;
        lastRead = System.currentTimeMillis();
        oneSec = (1000 / TICK_INTERVAL);
        oneHour = oneSec * 60 * 60;
        pluginServers = new ArrayList<PluginServer>();
        as = new AccessServer(this);
        new Thread(as).start();

        setLogFile ("c:/log.txt");

        os = AccessContextFactory.getOperatingContext(System.getProperty("os.name"));

        try {
            robby = new Robot();
        } catch (Exception ex) {
            System.out.println ("Error loading robby " + ex.getLocalizedMessage());
        }

        setupPlugins();

        log ("Engine started");
    }

    void setupPlugins() {
        addPlugin (new DoubleClickPlugin (this));
        addPlugin(new InputRecorder(this));
        //     addPlugin (new MouseTrailsPlugin (this));
        //    addPlugin (new PointerSizePlugin (this));
        //   addPlugin (new MissedClicksPlugin (this));
        //   addPlugin (new DirectionChanges (this));
        //        addPlugin (new PluginElectronicNotes (this));
    }

    void robotSetDelay(int delay) {
        robby.delay(delay);
    }

    void robotKeyPress(int keycode) {
        robby.keyPress(keycode);
    }

    void robotKeyRelease(int keycode) {
        robby.keyRelease (keycode);
    }

    void robotMouseMove(int x, int y) {
        robby.mouseMove(x, y);
    }

    void robotMouseClick(int button) {
        robby.mousePress (button);
    }

    void robotMouseRelease(int button) {
        robby.mouseRelease (button);
    }

    OperatingSystemContext getContext() {
        return os;
    }

    public void startEngine() {
        os.setupContext();
        t.start();
    }

    public String outputWheel (HashMap <AccessPlugin,Integer> map) {
        String str = "";

        if (map.size() == 0) {
            return "empty";
        }
        for (Map.Entry entry : map.entrySet()) {
            str += ((AccessPlugin) entry.getKey()).getName() + ": " + entry.getValue();
            str += " [" + ((AccessPlugin) entry.getKey()).queryLogDetails() + "]";
        }

        return str;
    }

    public String outputWheel() {
        return outputWheel (reinforcement);
    }

    public void sendMessage(String str) {
        myFrontend.sendMessage(str);
    }

    public void sendDebugMessage(String str) {
        myFrontend.sendDebugMessage(str);
    }

    public boolean addPlugin(AccessPlugin p) {
        if (findPlugin(p.getName()) == null) {
            myPlugins.add(p);
            p.create();
            p.loadMe();

            if (p.createSocketPort() != 0) {
                PluginServer pl = new PluginServer(p, p.createSocketPort());
                pluginServers.add(pl);
                new Thread(pl).start();
            }

            reinforcement.put(p, 100);

            return true;
        }
        return false;
    }

    AccessPlugin findPlugin(String name) {
        for (int i = 0; i < myPlugins.size(); i++) {
            if (myPlugins.get(i).getName().equalsIgnoreCase(name)) {
                return myPlugins.get(i);
            }
        }
        return null;
    }

    public boolean reinforcePlugin(boolean positive) {
        int oldVal;

        if (lastSelected == null) {
            return false;
        }

        oldVal = reinforcement.get(lastSelected);

        if (!positive) {
            oldVal /= 2;
            lastSelected.negativeReinforcement();
            log (lastSelected.getName() + " negatively reinforced (" + oldVal + ")");
        } else {
            oldVal *= 2;
            lastSelected.positiveReinforcement();
            log (lastSelected.getName() + " positively reinforced (" + oldVal + ")");
        }

        reinforcement.put(lastSelected, oldVal);

        awaitingCorrection = 0;

        // Switch off the chance to reinforce.
        myFrontend.prohibitReinforcement();
        outputWheel();
        return true;
    }

    public void heartbeat() {
        for (AccessPlugin p : myPlugins) {
            p.heartbeat();
        }
    }

    public void tick() {
        for (AccessPlugin p : myPlugins) {
            p.tick();
        }
    }

    public void reset() {
        for (AccessPlugin p : myPlugins) {
            p.reset();
        }
    }

    public void parseFiles() {
    }

    public void processInput(InputEntry tmp) {
        int x = tmp.getX();
        int y = tmp.getY();
        String misc = tmp.getMisc();

        for (AccessPlugin p : myPlugins) {
            if (tmp.getType().equalsIgnoreCase("Key")) {
                if (misc.equalsIgnoreCase("down")) {
                    if (!keyCurrentlyPressed) {
                        p.keyDown(tmp.getTime(), tmp.getKey());
                    }

                    p.keyTyped(tmp.getTime(), tmp.getKey());
                    keyCurrentlyPressed = true;
                } else {
                    p.keyReleased(tmp.getTime(), tmp.getKey());
                    keyCurrentlyPressed = false;
                }
            }
            else if (misc.equalsIgnoreCase ("scrolldown")) {
                p.mouseScrollDown();
            }
            else if (misc.equalsIgnoreCase ("scrollup")) {
                p.mouseScrollUp();
            }
            else if (misc.indexOf("down") != -1) {
                p.mouseClicked(tmp.getTime(), misc);
            } else {
                p.mouseMoved(tmp.getTime(), x, y);
            }
        }
    }

    public int totalWheel(HashMap<AccessPlugin, Integer> table) {
        int total = 0;
        for (AccessPlugin a : table.keySet()) {
            total += table.get(a).intValue();
        }

        return total;
    }

    public AccessPlugin spinRouletteWheel(HashMap<AccessPlugin, Integer> table) {
        int total = 0;
        int num = 0;
        int count = 0;

        total = totalWheel(table);

        num = (int) (Math.random() * total) + 1;

        for (AccessPlugin a : table.keySet()) {
            count += table.get(a).intValue();

            if (count >= num) {
                return a;
            }
        }
        return null;
    }

    public void actionPerformed(ActionEvent e) {
        long ticktime;
        long currentTime;
        int one_sec;
        HashMap<AccessPlugin, Integer> table = new HashMap<AccessPlugin, Integer>();
        // Every tick, work out which events needs to be sent into
        // which plugins.

        tick();
        
        // Every two full seconds, do a heartbeat.
        if (hb_count % oneSec * 1 == 0) {
            heartbeat();
        }

        hb_count += 1;

        // Every full hour, do a reset.
        if (hb_count >= oneHour) {
            reset();
            hb_count = 0;
        }


        // Okay, let's handle our corrections.
        if (hb_count % CORRECTION_INTERVAL == 0) {
            // Only suggest another correction if our previous correction
            // has been rated.
            if (awaitingCorrection == 0) {
                log ("Full Wheel is " + this.outputWheel());
                for (AccessPlugin p : myPlugins) {
                    if (p.wantsToMakeCorrection()) {
                        table.put(p, reinforcement.get(p));
                    }
                }
            } else {
                awaitingCorrection += 1;

                // If we pass the auto accept threshold, we simply allow
                // the correction to persist without readjusting weightings.
                if (awaitingCorrection >= AUTO_ACCEPT_THRESHOLD) {
                    log ("Last plugin auto accepted");
                    awaitingCorrection = 0;
                }
            }


            if (table.size() > 0) {
                log ("Spinning wheel is :" + outputWheel (table));
            }
            selected = spinRouletteWheel(table);


            if (selected != null) {
                log ("Selected plugin is " + selected.getName());
                selected.performCorrection();
                myFrontend.permitReinforcement();
                awaitingCorrection = 1;
                sendMessage(selected.getMessage() + "\n\n" +
                        "Please try it out to see if it makes your tasks " +
                        "simpler to perform.");
                lastSelected = selected;
            }
            else {
                log ("No plugin selected");
            }

            for (AccessPlugin p : myPlugins) {
                if (p != selected) {
                    p.notSelected();
                }
            }
        }
    }

    ArrayList<String> getInvokeablePlugins() {
        ArrayList<String> plugs = new ArrayList<String>();
        for (AccessPlugin p : myPlugins) {
            if (p instanceof InvokeablePlugin) {
                plugs.add(p.getName());
            }
        }
        return plugs;
    }

    ArrayList<String> getPluginButtons() {
        ArrayList<String> plugs = new ArrayList<String>();
        InvokeablePlugin i;
        for (AccessPlugin p : myPlugins) {
            if (p instanceof InvokeablePlugin) {
                i = (InvokeablePlugin)p;
                if (i.displayButton()) {
                    plugs.add (p.getName());
                }
            }
        }
        return plugs;
    }

    public void invokePlugin(String name) {
        AccessPlugin p = findPlugin(name);
        InvokeablePlugin i;

        if (p instanceof InvokeablePlugin) {
            i = (InvokeablePlugin) p;
            i.invokePlugin();
        }
    }

    public void shutdown() {
        os.closeContext();

        for (AccessPlugin p : myPlugins) {
            p.saveMe();
        }
    }

    public void parseEntry(String str) {
        InputEntry ie;
        String[] bits;
        String[] bits2;

        bits = str.split(" ");

        ie = new InputEntry();

        ie.setTime(System.currentTimeMillis());
        ie.setType(bits[0]);
        ie.setMisc(bits[1]);

        if (bits[0].equalsIgnoreCase("mouse")) {
            bits2 = bits[2].split(",");

            ie.setX(Integer.parseInt(bits2[0]));
            ie.setY(Integer.parseInt(bits2[1]));
        } else {
            ie.setKey(bits[2]);
        }

        processInput(ie);

    }

    void setLogFile (String f) {
        logFile = f;
    }
    
    String getLogFile() {
        return logFile;
    }
    
    public void log (String text) {
        FileWriter baseWriter;
        String filename = getLogFile();

        try {
            baseWriter = new FileWriter (filename, true);
            BufferedWriter buffer =new BufferedWriter (baseWriter);
            PrintWriter out = new PrintWriter (buffer);

            out.println (System.currentTimeMillis() + ": " + text);
            out.close();
        }
        catch (Exception ex) {
            System.out.println ("Error saving");
        }
    }
}

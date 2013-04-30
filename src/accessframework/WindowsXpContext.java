package accessframework;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.nio.channels.*;

public class WindowsXpContext extends OperatingSystemContext {

    String lastApp;
    private static int SPI_SENDCHANGE = 0x02;
    private static int SPI_SETCURSORS = 87;
    private static int SPI_SETMOUSETRAILS = 93;
    private static int SPI_GETMOUSETRAILS = 0x005e;
    private static int SPI_GETKEYBOARDDELAY = 0x0016;
    private static int SPI_SETKEYBOARDDELAY = 0x0017;
    private static int SPI_GETFILTERKEYS = 0x0032;
    private static int SPI_SETFILTERKEYS = 0x0033;
    private static int FKF_FILTERKEYSON = 0x0001;
    private static int SPI_SETDOUBLECLICKTIME = 0x0020;
    private static int SPI_SETMOUSESPEED = 0x0071;

    static {
        try {
            System.load("c:/libAccessDll.dll");
            System.out.println("DLL Loaded");
        } catch (UnsatisfiedLinkError ex) {
            System.out.println("Broken DLL" + ex);
        }

    }

    private static native String getApplication();
    private static native String test();
    private static native String getSystemKey(String bing, String bong, String bang);
    private static native String getUserKey(String bing, String bong, String bang);
    private static native void setSystemKey(String bing, String bong, String bang, String blah);
    private static native void setUserKey(String bing, String bong, String bang, String blah);
    private static native void refreshRegistry(int key, int event, int val);
    private static native void refreshRegistryAlt(int key, int event, int val);
    private static native void refreshFilterKeys(int code, int iwait, int idelay, int irepeat, int ibounce);
    private static native void refreshMouse(int speed, int thresh1, int thresh2);
    private static native int queryMouseThreshold1();
    private static native int queryMouseThreshold2();
    private static native int queryMouseAcceleration();

    public void setMouseVals(int s, int t1, int t2) {
        if (s != -1) {
            setUserKey("Control Panel\\Mouse", "MouseSpeed",
                    "REG_SZ", "" + s);
        }
        if (t1 != -1) {
            setUserKey("Control Panel\\Mouse", "MouseThreshold1",
                    "REG_SZ", "" + t1);
        }
        if (t2 != -1) {
            setUserKey("Control Panel\\Mouse", "MouseThreshold2",
                    "REG_SZ", "" + t2);
        }
        
        refreshMouse (s, t1, t2);
    }

    public void setPointerPrecision (boolean b) {
        if (b) {
            setMouseVals (1, 6, 10);
        }
        else {
            setMouseVals (0, 0, 0);
        }
    }
    public int getMouseAcceleration()  {
        return queryMouseAcceleration();
    }

    public int getMouseSpeed() {
        return Integer.parseInt(getUserKey("Control Panel\\Mouse", "MouseSensitivity",
                "REG_SZ"));
    }

    public void setMouseSpeed (int val) {
        System.out.println ("Setting mouse speed to " + val);
        setUserKey("Control Panel\\Mouse", "MouseSensitivity", "REG_SZ", "" + val);

        refreshRegistryAlt(SPI_SETMOUSESPEED, SPI_SENDCHANGE, val);
    }
    
    public void setKeyboardAcceptDelay(int rep) {
        setUserKey("Control Panel\\Accessibility\\Keyboard Response", "DelayBeforeAcceptance",
                "REG_SZ", "" + rep);

        refreshFilterKeys(FKF_FILTERKEYSON, -1, rep, -1, -1);
    }

    public int getKeyboardAcceptDelay() {
        return Integer.parseInt(getUserKey("Control Panel\\Accessibility\\Keyboard Response", "DelayBeforeAcceptance",
                "REG_SZ"));
    }

    public int getDoubleClickDelay() {
        return Integer.parseInt(getUserKey("Control Panel\\Mouse\\", "DoubleClickSpeed",
                "REG_SZ"));
    }

    public void setDoubleClickDelay(int rep) {
        setUserKey("Control Panel\\Mouse", "DoubleClickSpeed", "REG_SZ", "" + rep);

        refreshRegistry(SPI_SETDOUBLECLICKTIME, SPI_SENDCHANGE, rep);
    }

    public int getBounceTime() {
        return Integer.parseInt(getUserKey("Control Panel\\Accessibility\\Keyboard Response", "BounceTime",
                "REG_SZ"));
    }

    public int getAutoRepeatDelay() {
        return Integer.parseInt(getUserKey("Control Panel\\Accessibility\\Keyboard Response", "AutoRepeatDelay",
                "REG_SZ"));
    }

    public void setKeyRepeatDelay(int rep) {
        setUserKey("Control Panel\\Keyboard", "KeyboardDelay", "REG_SZ", "" + rep);

        refreshRegistry(SPI_SETKEYBOARDDELAY, SPI_SENDCHANGE, rep);
    }

    public int getKeyRepeatDelay() {
        String str = getUserKey("Control Panel\\Keyboard", "KeyboardDelay", "REG_SZ");
        int val;

        System.out.println("Key Repeat Delay is: " + str);
        val = Integer.parseInt(str);

        return val;
    }

    public void runWindowsProgram(String dir) {
        Process p;
        InputStream stdout = null;

        System.out.println ("Loading " + dir);
        try {
            p = Runtime.getRuntime().exec(dir);
        } catch (Exception ex) {
            System.err.println("Exception: " + ex);
        }
    }

    public void setupContext() {
        // Start the daemons, preciousss
        runWindowsProgram ("d://listener.exe");
        setMouseSpeed (20);
        setDoubleClickDelay (200);
        makeCursorNormal();
        switchOffMouseTrails();
        setPointerPrecision (false);
    }

    public WindowsXpContext() {
        super();
    }

    public String getUserLogin() {
        String str = "Blah";

//    str = getUserKey ("Console", "ScreenColors", "REG_DWORD");

//    System.out.println ("Str is " + str);
//    System.out.println ("Num is " + this.convertStringToHex(str));

//    test();
//    setUserKey ("Identities", "Last Username", "REG_SZ", "Main Identity");



        return str;
    }

//  private String getForegroundWindow() {
//      return "Blah";
//  }
    public int getCursorSize() {
        String str = getUserKey("Control Panel\\Cursors", "Arrow", "REG_EXPAND_SZ");

        if (str.indexOf("arrow_m.cur") != -1) {
            return 2;
        }

        if (str.indexOf("arrow_l.cur") != -1) {
            return 3;
        }

        return 1;
    }

    public void makeCursorNormal() {
        setUserKey("Control Panel\\Cursors", "Arrow", "REG_EXPAND_SZ",
                "");
        setUserKey("Control Panel\\Cursors", "AppStarting", "REG_EXPAND_SZ",
                "");
        setUserKey("Control Panel\\Cursors", "Wait", "REG_EXPAND_SZ",
                "");
        setUserKey("Control Panel\\Cursors", "CrossHair", "REG_EXPAND_SZ",
                "");
        setUserKey("Control Panel\\Cursors", "IBeam", "REG_EXPAND_SZ",
                "");

        refreshRegistry(SPI_SETCURSORS, SPI_SENDCHANGE, 0);
    }

    public void makeCursorBigger() {
        setUserKey("Control Panel\\Cursors", "Arrow", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\arrow_m.cur");
        setUserKey("Control Panel\\Cursors", "AppStarting", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\wait_m.cur");
        setUserKey("Control Panel\\Cursors", "Wait", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\busy_m.cur");
        setUserKey("Control Panel\\Cursors", "CrossHair", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\cross_m.cur");
        setUserKey("Control Panel\\Cursors", "IBeam", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\beam_m.cur");

        refreshRegistry(SPI_SETCURSORS, SPI_SENDCHANGE, 0);

        System.out.println("Making cursor Bigger");
    }

    public void makeCursorBiggest() {
        setUserKey("Control Panel\\Cursors", "Arrow", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\arrow_l.cur");
        setUserKey("Control Panel\\Cursors", "AppStarting", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\wait_l.cur");
        setUserKey("Control Panel\\Cursors", "Wait", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\busy_l.cur");
        setUserKey("Control Panel\\Cursors", "CrossHair", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\cross_l.cur");
        setUserKey("Control Panel\\Cursors", "IBeam", "REG_EXPAND_SZ",
                "%SystemRoot%\\cursors\\beam_ls.cur");

        refreshRegistry(SPI_SETCURSORS, SPI_SENDCHANGE, 0);

        System.out.println("Making cursor Bigger");
    }

    public void switchOnMouseTrails(int num) {
        setUserKey("Control Panel\\Mouse", "MouseTrails", "REG_SZ",
                "" + num);
        refreshRegistry(SPI_SETMOUSETRAILS, SPI_SENDCHANGE, num);

        System.out.println("Mouse Trails to " + num);
}

    public void switchOffMouseTrails() {
        setUserKey("Control Panel\\Mouse", "MouseTrails", "REG_SZ",
                "0");
        refreshRegistry(SPI_SETMOUSETRAILS, SPI_SENDCHANGE, 0);
    }

    public boolean queryMouseTrails() {
        String str = getUserKey("Control Panel\\Mouse", "MouseTrails", "REG_SZ");

        if (!str.equals("0")) {
            return true;
        }

        return false;
    }

    public String getActiveWindowTitle() {
        String appName;
        ArrayList<String> bits = new ArrayList<String>();
        StringTokenizer myToken;

        appName = getApplication();

        myToken = new StringTokenizer(appName, "/\\");

        while (myToken.hasMoreTokens()) {
            bits.add(myToken.nextToken());
        }

        if (bits.size() == 0) {
            return lastApp;
        }


        if (appName.endsWith("jdk1")) {
            return lastApp;
        }

//      System.out.println ("Last app is " + lastApp);

        appName = bits.get(bits.size() - 1);

        lastApp = appName;
//      return new WindowsXpContext().getWindowTitle();
        return appName;
    }

    public String getActiveWindow() {
        String appName;

        appName = getApplication();


        if (appName.endsWith("jdk1")) {
            return lastApp;
        }

//      System.out.println ("Last app is " + lastApp);

        lastApp = appName;
//      return new WindowsXpContext().getWindowTitle();
        return appName;
    }

    ArrayList<String> getAllProcesses() {
        ArrayList<String> str = new ArrayList<String>();
        String login = getUserLogin();

        return str;
    }

    public String getName() {
        return "Windows XP";
    }

    public String getCurrentProcess() {
        return "Blah!";
    }

    public String getUserHomeDir() {
        return System.getProperty("user.home") + "/";
    }

    ArrayList<String> readFile(String file) {
        String type;
        String txt;
        ArrayList<String> tmp = new ArrayList<String>();
        FileReader fr;
        BufferedReader br;
        File f;

        System.out.println("Reading file: " + file);

        if (file.equals("mouse_events.access")) {
            f = new File(System.getProperty("user.dir") + "\\mouse_log");
        } else {
            f = new File(System.getProperty("user.dir") + "\\key_log");
        }

        if (f.exists() == false) {
            System.out.println("No file:" + f);
            return tmp;
        }
        System.out.println("File exists:" + f);

        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);

            txt = br.readLine();

            while (txt != null) {
                tmp.add(txt);
                txt = br.readLine();
            }
        } catch (Exception ex) {
        }

        return tmp;
    }

    boolean deleteFile(String file) {

        return true;
    }

    public void closeContext() {
        // Shut down the daemons
 //       runWindowsProgram ("taskkill -F /IM listener.exe");
    }
}


package accessframework;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.nio.channels.*;
import java.util.prefs.*;

public class WindowsSevenContext extends OperatingSystemContext {
  String lastApp;
  WindowsRegistry reg;
  private static final String LOCAL_MACHINE = "HKEY_LOCAL_MACHINE";
  private static final String CURRENT_USER = "HKEY_CURRENT_USER";

  private static final String IE_VERSION = "SOFTWARE\\Microsoft\\Internet Explorer";
  private static final String CURSOR_SIZE = "Console";

  static {
      try {
        System.load("C:\\libJNIDemoCdl-1.dll");
      }
      catch (UnsatisfiedLinkError ex) {
          System.out.println ("" + ex);
      }

      System.out.println ("DLL Loaded");
  }

  private  String getWindowTitle() {
    return "Blah";
  }

  public int getVersion() {

    System.out.println ("IE Version: " + reg.getKey(LOCAL_MACHINE, IE_VERSION, "Version"));
    System.out.println ("Cursor Size: " + reg.getKey(CURRENT_USER, CURSOR_SIZE, "CursorSize"));
    return 5;
  }

  public void setKeyRepeatDelay (int rep) {
  }

  public int getKeyRepeatDelay() {
    return 100;
  }

  public void setMousePointerSize(int val) {
    
  }

  public int getMousePointerSize() {
    return 5;
  }
  public void runWindowsProgram (String dir) {
    try {
        Runtime.getRuntime().exec (dir);
    }
    catch (Exception ex) {
        System.err.println ("Exception: " + ex);
    }
  }

  public void setupContext() {
    // Start the daemons, preciousss
////    runWindowsProgram ("c:/windowsmouselistener");
  }

  public WindowsSevenContext () {
    super ();
    this.getWindowTitle();
    reg = new WindowsRegistry();
    getVersion();
  }


  public String getUserLogin() {
    return "Unknown";
  }

  private String getForegroundWindow() {
      return "Blah";
  }

  public String getActiveWindowTitle() {
      String appName;
      ArrayList<String> bits = new ArrayList<String>();
      StringTokenizer myToken;

      appName = getWindowTitle();


      myToken = new StringTokenizer (appName, "/\\");

      while (myToken.hasMoreTokens()) {
         bits.add (myToken.nextToken());
      }

      appName = bits.get (bits.size()-1);

      if (appName.endsWith ("java.exe")) {
         return lastApp;
      }

//      System.out.println ("Last app is " + lastApp);

      lastApp = appName;
//      return new WindowsXpContext().getWindowTitle();
      return appName;
  }

  public String getActiveWindow() {
      String appName;

      appName = getWindowTitle();


      if (appName.endsWith ("java.exe")) {
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
    return System.getProperty ("user.home") + "/";
  }

  ArrayList<String> readFile(String file) {
    String type;
    String txt;
    ArrayList<String> tmp = new ArrayList<String>();
    FileReader fr;
    BufferedReader br;
    File f;

    System.out.println ("Reading file: " + file);

    if (file.equals ("mouse_events.access")) {
        f = new File (System.getProperty ("user.dir") + "\\mouse_log");
    }
    else {
        f = new File (System.getProperty ("user.dir") + "\\key_log");
    }

    if (f.exists() == false) {
        System.out.println ("No file:" + f);
        return tmp;
    }
        System.out.println ("File exists:" + f);

    try {
        fr = new FileReader (f);
        br = new BufferedReader (fr);

        txt = br.readLine();

        while (txt != null) {
            tmp.add (txt);
            txt = br.readLine();
        }
    }
    catch (Exception ex) {
    }

    return tmp;
  }

  boolean deleteFile (String file) {
    File f = null;
    String filename;
    FileLock fl;
    FileChannel fc;
    boolean success = false;




    if (file.equals ("mouse_events.access")) {
        filename = System.getProperty ("user.dir") + "\\mouse_log";
    }
    else {
        filename = System.getProperty ("user.dir") + "\\key_log";
    }

    f = new File (filename);

    try {
        fc = new RandomAccessFile (f, "rw").getChannel();

        fl = fc.lock();

        System.out.println ("" + fl);
        fl.release();
        System.out.println ("" + fl);
    }
    catch (Exception ex) {
        System.out.println ("" + ex);
    }

    if (f.exists()) {
        if (f.canWrite()) {
            System.out.println ("Deleting file: " + file);
            success = f.delete();
        }

        if (!success) {
            System.out.println ("Can't delete file");
        }
    }
    return true;
  }

  public void closeContext() {
    // Shut down the daemons.
//    runWindowsProgram ("taskkill /IM windowskeylistener.exe");
//    runWindowsProgram ("taskkill /IM windowsmouselistener.exe");
  }

}


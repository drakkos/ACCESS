package accessframework;

import java.util.*;

abstract class OperatingSystemContext {
    // Read in a file from the file system.

    abstract ArrayList<String> readFile(String file);
    // Delete a file from the file system.

    abstract boolean deleteFile(String file);
    // Get the descriptive name of the context.

    abstract String getName();
    // Get the current active process.

    abstract String getCurrentProcess();
    // Get the current user login name, if any.

    abstract String getUserLogin();
    // Get which window reference is active.

    abstract String getActiveWindow();
    // Get the title of the active window.

    abstract String getActiveWindowTitle();
    // Set the key repeat delay for the OS.

    abstract void setKeyRepeatDelay(int val);
    // Get the key repeat delay for the OS

    abstract int getKeyRepeatDelay();
    // Initialise the context so it starts up
    // the key reader and the mouse reader and such.

    abstract void setupContext();

    abstract void closeContext();

    abstract String getUserHomeDir();

    public OperatingSystemContext() {
    }

    public void setKeyboardAcceptDelay(int rep) {
        return;
    }

    public int getKeyboardAcceptDelay() {
        return -1;
    }

    public int getBounceTime() {
        return -1;
    }

    public int getAutoRepeatDelay() {
        return -1;
    }

    public int getCursorSize() {
        return -1;
    }

    public boolean queryMouseTrails() {
        return false;
    }

    public void makeCursorNormal() {
    }

    public void makeCursorBigger() {
    }

    public void makeCursorBiggest() {
    }

    public void switchOnMouseTrails(int i) {
    }

    public void switchOffMouseTrails() {
    }

    public int getDoubleClickDelay() {
        return -1;
    }

    public void setDoubleClickDelay(int rep) {
    }

    public int getMouseAcceleration() {
        return -1;
    }

    public int getMouseThreshold1() {
        return -1;
    }

    public int getMouseThreshold2() {
        return -1;
    }

    public boolean getPointerPrecision() {
        if (getMouseAcceleration() == 0) {
            return false;
        }
        return true;
    }

    public void setPointerPrecision (boolean b) {
    }

    public void setMouseSpeed (int i) {
    }

    public int getMouseSpeed() {
        return 15;
    }


}

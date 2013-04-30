/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package accessframework;

/**
 *
 * @author Drakkos
 */
public class AccessPluginMouse extends AccessPlugin {

    public enum Direction {

        NORTH(1),
        NORTHEAST(2),
        EAST(3),
        SOUTHEAST(4),
        SOUTH(5),
        SOUTHWEST(6),
        WEST(7),
        NORTHWEST(8),
        UNKNOWN(0);
        private final int dir;

        Direction(int ndir) {
            dir = ndir;
        }

        public int getDir() {
            return dir;
        }
    };

    public AccessPluginMouse(AccessEngine e) {
        super(e);
    }


    public void positiveReinforcement() {
    }

    public void negativeReinforcement() {
    }

    public void mouseMoved(long time, int x, int y) {
    }

    public void mouseClicked(long time, String button) {
    }

    public void keyTyped(long time, String key) {
    }

    public void create() {
    }

    public boolean wantsToMakeCorrection() {
        return false;
    }

    public void adjustCursorSize (int intended) {
    }

    public void performCorrection() {
    }

    public String getName() {
        return "";
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
        return "This plugin has not been correctly configured";
    }
}

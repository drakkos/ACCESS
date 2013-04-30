package accessframework;

import java.awt.geom.Point2D;
import java.util.*;

public class MouseTrailsPlugin extends MouseLocatorBase {

    private int previousSize, lastCorrection;
    private boolean switchedOn;

    public MouseTrailsPlugin (AccessEngine e) {
        super(e);
        previousSize = 0;
    }

    public void negativeReinforcement() {
        super.negativeReinforcement();

        if (switchedOn) {
            getEngine().getContext().switchOffMouseTrails();
        }
        else {
            previousSize -= 1;
            getEngine().getContext().switchOnMouseTrails(previousSize);
        }
    }



    public boolean wantsToMakeCorrection() {
        if (previousSize > 7) {
            return false;
        }
        
        return super.wantsToMakeCorrection();
    }

    public void performCorrection() {
        if (getEngine().getContext().queryMouseTrails() == false) {
            previousSize = 3;
            getEngine().getContext().switchOnMouseTrails (previousSize);
            switchedOn = true;
        }
        else {
            previousSize += 1;
            getEngine().getContext().switchOnMouseTrails (previousSize);
            switchedOn = false;

        }
        super.performCorrection();
    }

    public String getName() {
        return "Mouse Trails Plugin";
    }

    public String getMessage() {
        if (switchedOn) {
            return "You should find that your cursor leaves a trail when you move it now.";
        }
        else {
            return "The length of your mouse trails has been increased.";
        }
    }
}

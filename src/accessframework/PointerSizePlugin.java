package accessframework;

import java.awt.geom.Point2D;
import java.util.*;

public class PointerSizePlugin extends MouseLocatorBase {

    private int previousSize, lastCorrection;
    private long now, last;

    public PointerSizePlugin(AccessEngine e) {
        super(e);
        previousSize = getCursorSize();
    }

    public void negativeReinforcement() {
        super.negativeReinforcement();
        previousSize -= 1;
        adjustCursorSize(previousSize);
    }

    public void adjustCursorSize(int intended) {
        switch (intended) {
            case 2:
                getEngine().getContext().makeCursorBigger();
                break;
            case 3:
                getEngine().getContext().makeCursorBiggest();
                break;
            default:
                getEngine().getContext().makeCursorNormal();
                break;
        }
    }

    public boolean wantsToMakeCorrection() {
        if (getCursorSize() >= 2) {
            return false;
        }

        return super.wantsToMakeCorrection();
    }

    public void performCorrection() {
        System.out.println("Performing Correction");

        if (previousSize < 3) {
            previousSize += 1;
            adjustCursorSize(previousSize);
        }

        super.performCorrection();
    }

    public String getMessage() {
        return "The size of the mouse cursor has been increased.";
    }
}

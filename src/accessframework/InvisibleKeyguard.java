package accessframework;

import accessframework.*;
import java.util.*;

public class InvisibleKeyguard extends AccessPlugin {

    HashMap<Character, char[]> adjacent;
    HashMap<String, Float> bigrams;
    private long lock;
    private long lastKeyPress;
    boolean keyDown;
    char lastKey;
    int overlap;

    public void addAdjacentKeys(char key, char[] adj) {
        adjacent.put(key, adj);
    }

    public boolean isAdjacent(char first, char last) {
        char ch[] = adjacent.get(first);

        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == last) {
                return true;
            }
        }
        return false;
    }

    public InvisibleKeyguard(AccessEngine e) {
        super(e);

        adjacent = new HashMap<Character, char[]>();
        bigrams = new HashMap<String, Float>();

        lock = 100;

        // We have to do this by hand.   EeeeeEeee
        char[] adja = {'q', 'w', 's', 'x', 'z'};
        char[] adjb = {'v', 'f', 'g', 'h', 'j', 'm'};
        char[] adjc = {'x', 's', 'd', 'f', 'g', 'b'};
        char[] adjd = {'x', 's', 'w', 'e', 'r', 'f', 'c'};
        char[] adje = {'s', 'w', '3', '4', 'r', 'f', 'd'};
        char[] adjf = {'c', 'd', 'r', 't', 'g', 'b', 'v'};
        char[] adjg = {'v', 'f', 't', 'y', 'h', 'b'};
        char[] adjh = {'b', 'g', 'y', 'j', 'n', 'u'};
        char[] adji = {'j', 'u', '8', '9', 'o', 'k'};
        char[] adjj = {'n', 'h', 'u', 'i', 'k', 'm'};
        char[] adjk = {'m', 'j', 'i', 'o', 'l', ','};
        char[] adjl = {',', 'k', 'o', 'p', ';', '.'};
        char[] adjm = {'n', 'j', 'k', ',', ' '};
        char[] adjn = {'b', 'h', 'j', 'm', ' '};
        char[] adjo = {'k', 'i', '9', '0', 'p', 'l'};
        char[] adjp = {'l', 'o', '0', '-', '[', ';'};
        char[] adjq = {'a', 's', 'w', '2', '1'};
        char[] adjr = {'d', 'e', '4', '5', 't', 'g', 'f'};
        char[] adjs = {'z', 'a', 'w', 'e', 'd', 'c', 'x'};
        char[] adjt = {'f', 'r', '5', '6', 'y', 'h', 'g'};
        char[] adju = {'h', 'y', '7', '8', 'i', 'k', 'j'};
        char[] adjv = {' ', 'c', 'f', 'g', 'b'};
        char[] adjw = {'a', 'q', '2', '3', 'e', 'd', 's'};
        char[] adjx = {'z', 's', 'd', 'c'};
        char[] adjy = {'g', 't', '6', '7', 'u', 'h'};
        char[] adjz = {'\\', 'a', 's', 'x'};


        addAdjacentKeys('a', adja);
        addAdjacentKeys('b', adjb);
        addAdjacentKeys('c', adjc);
        addAdjacentKeys('d', adjd);
        addAdjacentKeys('e', adje);
        addAdjacentKeys('f', adjf);
        addAdjacentKeys('g', adjg);
        addAdjacentKeys('h', adjh);
        addAdjacentKeys('i', adji);
        addAdjacentKeys('j', adjj);
        addAdjacentKeys('k', adjk);
        addAdjacentKeys('l', adjl);
        addAdjacentKeys('m', adjm);
        addAdjacentKeys('n', adjn);
        addAdjacentKeys('o', adjo);
        addAdjacentKeys('p', adjp);
        addAdjacentKeys('q', adjq);
        addAdjacentKeys('r', adjr);
        addAdjacentKeys('s', adjs);
        addAdjacentKeys('t', adjt);
        addAdjacentKeys('u', adju);
        addAdjacentKeys('v', adjv);
        addAdjacentKeys('w', adjw);
        addAdjacentKeys('x', adjx);
        addAdjacentKeys('y', adjy);
        addAdjacentKeys('z', adjz);

        // Here for completeness and ease of swapping.
        bigrams.put("th", new Float(1.52));
        bigrams.put("he", new Float(1.28));
        bigrams.put("in", new Float(0.94));
        bigrams.put("er", new Float(0.94));
        bigrams.put("an", new Float(0.82));
        bigrams.put("re", new Float(0.68));
        bigrams.put("nd", new Float(0.63));
        bigrams.put("at", new Float(0.59));
        bigrams.put("on", new Float(0.57));
        bigrams.put("nt", new Float(0.56));
        bigrams.put("ha", new Float(0.56));
        bigrams.put("es", new Float(0.56));
        bigrams.put("st", new Float(0.55));
        bigrams.put("en", new Float(0.55));
        bigrams.put("ed", new Float(0.53));
        bigrams.put("to", new Float(0.52));
        bigrams.put("it", new Float(0.50));
        bigrams.put("ou", new Float(0.50));
        bigrams.put("ea", new Float(0.47));
        bigrams.put("hi", new Float(0.46));
        bigrams.put("is", new Float(0.46));
        bigrams.put("or", new Float(0.43));
        bigrams.put("ti", new Float(0.34));
        bigrams.put("as", new Float(0.33));
        bigrams.put("te", new Float(0.27));
        bigrams.put("et", new Float(0.19));
        bigrams.put("ng", new Float(0.18));
        bigrams.put("of", new Float(0.16));
        bigrams.put("al", new Float(0.09));
        bigrams.put("de", new Float(0.09));
        bigrams.put("se", new Float(0.08));
        bigrams.put("le", new Float(0.08));
        bigrams.put("sa", new Float(0.06));
        bigrams.put("si", new Float(0.05));
        bigrams.put("ar", new Float(0.04));
        bigrams.put("ve", new Float(0.04));
        bigrams.put("ra", new Float(0.04));
        bigrams.put("ld", new Float(0.02));
        bigrams.put("ur", new Float(0.02));

    }

    public boolean wantsToMakeCorrection() {
        if (overlap > 10) {
            return true;
        }

        overlap = 0;
        return false;
    }

    public void performCorrection() {
        getEngine().getContext().setKeyboardAcceptDelay((int)lock);
        overlap = 0;
    }

    public void positiveReinforcement() {
        lock += 50;
    }

    public void negativeReinforcement() {
        lock -= 10;
    }

    public void mouseMoved(long time, int x, int y) {
    }

    public void mouseClicked(long time, String button) {
    }

    public void keyTyped(long time, String key) {
        long now;
        char thisKey;

        thisKey = key.charAt(0);

        keyDown = true;

        now = System.currentTimeMillis();

        if (now - lastKeyPress <= lock) {
            if (isAdjacent(thisKey, lastKey)) {
                System.out.println("Overlap detected");
                overlap += 1;
            }
        }

        lastKey = thisKey;
        lastKeyPress = now;
    }

    public void keyReleased(long time, String key) {
        keyDown = false;
    }

    public void create() {
    }

    public String getName() {
        return "The Invisible Keyguard";
    }

    public void heartbeat() {
    }

    public void reset() {
    }

    public void tick() {
    }

    public void setState(Object ob) {
    }

    public Object getState() {
        return null;
    }

    public void saveMe() {
    }

    public void loadMe() {
    }
}

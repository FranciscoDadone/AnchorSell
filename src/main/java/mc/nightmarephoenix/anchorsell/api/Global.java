package mc.nightmarephoenix.anchorsell.api;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.models.Anchor;

import java.util.ArrayList;

/**
 * Storage in RAM
 */
public class Global {

    /**
     * Adds an anchor to the cache
     * @param newAnchor
     */
    public static void addAnchor(Anchor newAnchor) {
        anchors.add(newAnchor);
    }

    /**
     * Removes an anchor from cache.
     * When it is mined or exploded.
     * @param anchor
     */
    public static void removeAnchor(Anchor anchor) {
        anchors.remove(anchor);
    }

    /**
     * Returns all the cached anchors.
     * @return
     */
    public static ArrayList<Anchor> getAllAnchors() {
        return anchors;
    }

    private static ArrayList<Anchor> anchors = new ArrayList<>();
    public static String particlesStatus;
    public static AnchorSell plugin;

}

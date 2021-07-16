package mc.nightmarephoenix.anchorsell.storage;

import org.bukkit.Location;
import java.util.ArrayList;

/**
 * Storage in ram
 */
public class Cache {

    /**
     * Adds an anchor to the cache
     * @param newAnchor
     */
    public static void addAnchor(Location newAnchor) {
        anchors.add(newAnchor);
    }

    /**
     * Removes an anchor from cache.
     * When it is mined or exploded.
     * @param anchor
     */
    public static void removeAnchor(Location anchor) {
        anchors.remove(anchor);
    }

    /**
     * Returns all the cached anchors.
     * @return
     */
    public static ArrayList<Location> getAllAnchors() {
        return anchors;
    }

    private static ArrayList<Location> anchors = new ArrayList<>();
    public static String particlesStatus;
}

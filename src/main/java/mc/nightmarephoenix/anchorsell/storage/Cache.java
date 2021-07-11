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

    public static Location getNextAnchor() {
        Location result;

        if (getAllAnchors().isEmpty()) return null;

        float calc;

        try {
            calc = currentAnchorIndex % (getAllAnchors().size());
        } catch (ArithmeticException e) {
            return null;
        }

        if (calc == 0f) {
            currentAnchorIndex = 0;
        }

        result = getAllAnchors().get(currentAnchorIndex);

        currentAnchorIndex += 1;

        return result;
    }

    private static ArrayList<Location> anchors = new ArrayList<>();

    private static Location currentAnchor;
    private static int currentAnchorIndex = 0;
}

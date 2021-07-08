package mc.nightmarephoenix.anchorsell.storage;

import org.bukkit.Location;

import java.util.ArrayList;

public class Cache {

    public static void addAnchor(Location newAnchor) {
        anchors.add(newAnchor);
    }

    public static void removeAnchor(Location anchor) {
        anchors.remove(anchor);
    }

    public static ArrayList<Location> getAllAnchors() {
        return anchors;
    }

    private static ArrayList<Location> anchors = new ArrayList<>();

}

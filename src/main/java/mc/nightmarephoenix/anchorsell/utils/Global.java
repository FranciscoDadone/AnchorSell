package mc.nightmarephoenix.anchorsell.utils;

enum WorldGuard {
    ACTIVE,
    NOT_ACTIVE;
}

public class Global {

    private static WorldGuard wg;

    public static void setWorldGuard(boolean value) {
        if(value)
            wg = wg.ACTIVE;
        else
            wg = wg.NOT_ACTIVE;
    }

    public static WorldGuard getWorldGuard() {
        return wg;
    }
}
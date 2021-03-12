package mc.nightmarephoenix.anchorsell.hooks;

public class Global {

    private static WG wg;

    public static void setWorldGuard(boolean value) {
        if(value)
            wg = wg.ACTIVE;
        else
            wg = wg.NOT_ACTIVE;
    }

    public static WG getWorldGuard() {
        return wg;
    }
}
package mc.nightmarephoenix.anchorsell.hooks;

public class Hooks {

    private static WG wg;

    /**
     * Sets the world guard hook.
     * @param value WG active or not
     */
    public static void setWorldGuard(boolean value) {
        if(value) wg = WG.ACTIVE;
        else      wg = WG.NOT_ACTIVE;
    }

    /**
     * Getter
     * @return WG class
     */
    public static WG getWorldGuard() {
        return wg;
    }

}
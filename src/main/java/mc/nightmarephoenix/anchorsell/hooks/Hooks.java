package mc.nightmarephoenix.anchorsell.hooks;

public class Hooks {

    private static WG wg;

    /**
     * Sets the world guard hook.
     * @param value
     */
    public static void setWorldGuard(boolean value) {
        if(value) wg = wg.ACTIVE;
        else      wg = wg.NOT_ACTIVE;
    }

    /**
     * Getter
     * @return
     */
    public static WG getWorldGuard() {
        return wg;
    }

}
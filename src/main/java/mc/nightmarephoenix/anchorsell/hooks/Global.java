package mc.nightmarephoenix.anchorsell.hooks;

public class Global {

    private static WG wg;
    private static FactionsX fX;

    /**
     * Sets the world guard hook.
     * @param value
     */
    public static void setWorldGuard(boolean value) {
        if(value) wg = wg.ACTIVE;
        else      wg = wg.NOT_ACTIVE;
    }

    /**
     * Sets the FactionsX hook.
     * @param value
     */
    public static void setFactionsX(boolean value) {
        if(value) fX = fX.ACTIVE;
        else      fX = fX.NOT_ACTIVE;
    }

    /**
     * Getter
     * @return
     */
    public static WG getWorldGuard() {
        return wg;
    }

    /**
     * Getter
     * @return
     */
    public static FactionsX getFactionsX() {
        return fX;
    }
}
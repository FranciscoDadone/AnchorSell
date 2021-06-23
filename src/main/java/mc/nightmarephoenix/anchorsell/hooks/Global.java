package mc.nightmarephoenix.anchorsell.hooks;

public class Global {

    private static WG wg;
    private static FactionsX fX;

    public static void setWorldGuard(boolean value) {
        if(value)
            wg = wg.ACTIVE;
        else
            wg = wg.NOT_ACTIVE;
    }

    public static void setFactionsX(boolean value) {
        if(value) fX = fX.ACTIVE;
        else      fX = fX.NOT_ACTIVE;
    }

    public static WG getWorldGuard() {
        return wg;
    }
    public static FactionsX getFactionsX() {
        return fX;
    }
}
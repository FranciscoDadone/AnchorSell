package com.franciscodadone.anchorsell.thirdparty.essentials;

import com.earth2me.essentials.Essentials;

public class EssentialsManager {

    public static void setupEssentials() {
        essentials = (Essentials) Essentials.getProvidingPlugin(Essentials.class);
    }

    public static Essentials getEssentials() {
        return essentials;
    }

    private static Essentials essentials;
}

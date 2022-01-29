package com.franciscodadone.anchorsell.thirdparty.bstats;

import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.api.StorageManager;

public class setupBstats {

    public static void init() {

        Metrics metrics = new Metrics(Global.plugin, 13580);

        // ------------- Custom charts ------------- //

        // Anchor price
        metrics.addCustomChart(new Metrics.SimplePie("anchor_price", () -> String.valueOf(Global.plugin.getConfig().getDouble("anchor-value"))));

        // Total anchors users can have
        metrics.addCustomChart(new Metrics.SimplePie("total_anchors_users_can_have", () -> String.valueOf(Global.plugin.getConfig().getDouble("total-anchors-user-can-have"))));

        // Safe anchor area
        metrics.addCustomChart(new Metrics.SimplePie("safe_anchor_area", () -> String.valueOf(Global.plugin.getConfig().getDouble("safe-anchor-area"))));

        // Pay timer in minutes
        metrics.addCustomChart(new Metrics.SimplePie("pay_timer_in_minutes", () -> String.valueOf(Global.plugin.getConfig().getDouble("pay-timer-in-minutes"))));

        // Upgrade Multiplier
        metrics.addCustomChart(new Metrics.SimplePie("upgrade_multiplier", () -> String.valueOf(Global.plugin.getConfig().getDouble("anchor.upgrade-multiplier"))));

        // Pay modifier
        metrics.addCustomChart(new Metrics.SimplePie("pay_modifier", () -> String.valueOf(Global.plugin.getConfig().getDouble("anchor.pay-modifier"))));

        // Particles
        metrics.addCustomChart(new Metrics.SimplePie("particles", () -> String.valueOf(Global.plugin.getConfig().getDouble("particles"))));

        // Hologram
        metrics.addCustomChart(new Metrics.SimplePie("has_hologram", () -> (StorageManager.retrieveHologramLocation() != null) ? "true" : "false"));

        // ----------------------------------------- //

    }

}

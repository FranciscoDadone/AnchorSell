package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Upgrades extends SubCommands {
    @Override
    public String getDescription() {
        return "Shows the upgrade path of the anchors.";
    }

    @Override
    public String getName() {
        return "upgrades";
    }

    @Override
    public String syntax() {
        return "/anchor upgrades";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.upgrades";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender.hasPermission("anchorsell.admin.upgrades")) {
            sender.sendMessage(Utils.Color("&7&m----------&r &5&lAnchor &7&m----------"));
            for (int i = 1; i <= 64; i++) {
                sender.sendMessage(Utils.Color(
                        "Level: " + i
                                + " | MToUpgrade: " + Utils.getMoneyToUpgrade(i, Global.plugin)
                                + " | MPerMinute: " + Utils.getMoneyPerMinute(i)
                ));
            }
            sender.sendMessage(Utils.Color("&7&m----------------------------"));
        } else Utils.noPermission(getPermission(), sender);
    }
}

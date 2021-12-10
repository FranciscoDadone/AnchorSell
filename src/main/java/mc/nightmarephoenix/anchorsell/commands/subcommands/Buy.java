package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.inventories.BuyScreen;
import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class Buy extends SubCommands {
    @Override
    public String getDescription() {
        return "Buy an anchor";
    }

    @Override
    public String getName() {
        return "buy";
    }

    @Override
    public String syntax() {
        return "/anchor buy";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "anchorsell.player.buy";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender.hasPermission("anchorsell.player.buy")) {
            Bukkit.getPlayer(sender.getName()).openInventory(
                    new BuyScreen(Bukkit.getPlayer(sender.getName()), Global.plugin).getInventory()
            );
        } else Utils.noPermission(getPermission(), sender);
    }
}

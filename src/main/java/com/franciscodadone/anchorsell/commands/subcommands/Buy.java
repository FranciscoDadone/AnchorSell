package com.franciscodadone.anchorsell.commands.subcommands;

import com.franciscodadone.anchorsell.inventories.BuyScreen;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            Objects.requireNonNull(Bukkit.getPlayer(sender.getName())).openInventory(
                    new BuyScreen().getInventory()
            );
        } else Utils.noPermission(getPermission(), sender);
    }
}

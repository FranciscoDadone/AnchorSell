package com.franciscodadone.anchorsell.commands.subcommands;

import com.franciscodadone.anchorsell.api.StorageManager;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Revalidate extends SubCommands {
    @Override
    public String getDescription() {
        return "Revalidates a user file.";
    }

    @Override
    public String getName() {
        return "revalidate";
    }

    @Override
    public String syntax() {
        return "/anchor revalidate [username]";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        ArrayList<String> subcommands = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            subcommands.add(p.getName());
        }
        return subcommands;
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.revalidate";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.hasPermission("anchorsell.admin.revalidate")) {
            if(args.length == 2) {
                StorageManager.revalidateUser(Bukkit.getOfflinePlayer(args[1]));
                sender.sendMessage(Utils.Color("&aRevalidated &c" + args[1] + " &afiles."));
            } else {
                sender.sendMessage(Utils.Color("Usage: &e/anchor revalidate [username]"));
            }
        } else Utils.noPermission(getPermission(), sender);
    }
}

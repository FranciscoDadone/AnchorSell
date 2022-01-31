package com.franciscodadone.anchorsell.commands.subcommands;

import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetUserFileName extends SubCommands {
    @Override
    public String getDescription() {
        return "Gets the user file name (UUID)";
    }

    @Override
    public String getName() {
        return "getUserFileName";
    }

    @Override
    public String syntax() {
        return "/anchor getUserFileName [username]";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        ArrayList<String> subcommand = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            subcommand.add(p.getName());
        }
        return subcommand;
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.getUserFileName";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.hasPermission("anchorsell.admin.getUserFileName")) {
            if(args.length == 2) {
                String uuid;
                try {
                    uuid = Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId().toString();
                } catch (Exception ignored) {
                    uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId().toString();
                }
                sender.sendMessage(Utils.Color("&aUser file name: &c" + uuid + ".yml"));
            } else {
                sender.sendMessage(Utils.Color("Usage: &e/anchor getuserfilename [username]"));
            }
        } else Utils.noPermission(getPermission(), sender);
    }
}
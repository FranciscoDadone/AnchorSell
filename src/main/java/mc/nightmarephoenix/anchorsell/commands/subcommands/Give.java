package mc.nightmarephoenix.anchorsell.commands.subcommands;

import jdk.jshell.execution.Util;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Give extends SubCommands {
    @Override
    public String getDescription() {
        return "Gives an anchor to the player.";
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String syntax() {
        return "/anchor give [playerName] [quantity] [level]";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        ArrayList<String> subcommands = new ArrayList<>();
        if(args.length == 2) {
            for(Player p : Bukkit.getOnlinePlayers()) subcommands.add(p.getName());
        } else if(args.length == 3 || args.length == 4) {
            for(int i = 1; i <= 64; i++) subcommands.add(String.valueOf(i));
        }
        return subcommands;
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.give";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.hasPermission("anchorsell.admin.give")) {
            if(args.length == 1) {
                Utils.sendConfigMessage("cant-give-anchor-message", sender);
            } else if (args.length == 2) {
                Player p = sender.getServer().getPlayer(args[1]);
                if(!p.getInventory().addItem(Utils.getAnchor(1, 1)).isEmpty()) {
                    sender.sendMessage(Utils.Color("&c" + p.getName() + " has their inventory full."));
                } else {
                    sender.sendMessage(Utils.Color("&aGave &c1 &aAnchor to &c" + p.getName()));
                }
            }
            // Give Anchor with quantity argument
            else if (args.length == 3) {
                Player p = sender.getServer().getPlayer(args[1]);
                if(!p.getInventory().addItem(Utils.getAnchor(1, Integer.parseInt(args[2]))).isEmpty()) {
                    sender.sendMessage(Utils.Color("&c" + p.getName() + " has their inventory full."));
                } else {
                    sender.sendMessage(Utils.Color("&aGave &c" + args[2] + " &aAnchor to &c" + p.getName()));
                }
            }
            // Give Anchor with quantity and level argument
            else if (args.length == 4) {
                Player p = sender.getServer().getPlayer(args[1]);
                if(!p.getInventory().addItem(Utils.getAnchor(Integer.parseInt(args[3]), Integer.parseInt(args[2]))).isEmpty()) {
                    sender.sendMessage(Utils.Color("&c" + p.getName() + " has their inventory full."));
                } else {
                    sender.sendMessage(Utils.Color("&aGave &c" + args[2] + " &aAnchor to &c" + p.getName() + " &a(level " + Integer.parseInt(args[3]) + ")"));
                }
            }
        } else Utils.noPermission(getPermission(), sender);
    }
}

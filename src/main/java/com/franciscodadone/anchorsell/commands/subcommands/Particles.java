package com.franciscodadone.anchorsell.commands.subcommands;

import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Particles extends SubCommands {
    @Override
    public String getDescription() {
        return "Changes the particles of the anchors.";
    }

    @Override
    public String getName() {
        return "particles";
    }

    @Override
    public String syntax() {
        return "/anchor particles [all/low/off]";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        ArrayList<String> subcommand = new ArrayList<>();
        if(args.length == 2) {
            subcommand.add("all");
            subcommand.add("low");
            subcommand.add("off");
            return subcommand;
        }
        return subcommand;
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.particles";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.hasPermission("anchorsell.admin.particles")) {
            if(args.length == 2 && (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("low") || args[1].equalsIgnoreCase("off"))) {
                Utils.setConfigValue("particles", args[1]);
                Global.particlesStatus = args[1];

                sender.sendMessage(Utils.Color("&aParticles changed to " + args[1] + "."));
            } else {
                sender.sendMessage(Utils.Color("Usage: &e" + syntax()));
            }
        } else Utils.noPermission(getPermission(), sender);
    }
}

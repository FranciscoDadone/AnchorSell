package com.franciscodadone.anchorsell.commands.subcommands;

import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Version extends SubCommands {
    @Override
    public String getDescription() {
        return "Gives the plugin version.";
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String syntax() {
        return "/anchor version";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(Utils.Color("&7&m-----------------&r &5&lAnchorSell &7&m-----------------"));
        sender.sendMessage(Utils.Color("&eVersion: &f" + Global.plugin.getDescription().getVersion()));
        sender.sendMessage(Utils.Color("&9https://www.spigotmc.org/resources/anchorsell.90038/"));
        sender.sendMessage(Utils.Color("&7&m----------------------------------------------"));
    }
}

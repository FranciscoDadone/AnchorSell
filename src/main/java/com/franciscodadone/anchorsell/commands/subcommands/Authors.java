package com.franciscodadone.anchorsell.commands.subcommands;

import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Authors extends SubCommands {
    @Override
    public String getDescription() {
        return "Gives the AnchorSell authors";
    }

    @Override
    public String getName() {
        return "authors";
    }

    @Override
    public String syntax() {
        return "/anchor authors";
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

        sender.sendMessage(Utils.Color("&7&m-------------&r &5&lAnchorSell &7&m-------------"));
        sender.sendMessage(Utils.Color("&ePlugin made by: &fMatiasME and DadoGamer13"));
        sender.sendMessage(Utils.Color("&eGithub:&f https://github.com/FranciscoDadone/AnchorSell.git"));
        sender.sendMessage(Utils.Color("&7&m--------------------------------------"));

    }
}

package com.franciscodadone.anchorsell.commands.subcommands;

import com.franciscodadone.anchorsell.api.StorageManager;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ChangePrice extends SubCommands {
    @Override
    public String getDescription() {
        return "Changes the price of the anchors.";
    }

    @Override
    public String getName() {
        return "changePrice";
    }

    @Override
    public String syntax() {
        return "/anchor changePrice";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.changePrice";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.hasPermission("anchorsell.admin.changePrice")) {
            StorageManager.changePrice(Integer.parseInt(args[1]));
            sender.sendMessage(Utils.Color("&aPrice for anchors changed to &c$" + Integer.parseInt(args[1])));
        } else Utils.noPermission(getPermission(), sender);
    }
}

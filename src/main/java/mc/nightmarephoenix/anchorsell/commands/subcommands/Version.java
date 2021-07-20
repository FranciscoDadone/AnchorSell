package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.utils.Utils;
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
        sender.sendMessage(Utils.Color("&5&lAnchorSell version: &f" + Global.plugin.getDescription().getVersion()));
        sender.sendMessage(Utils.Color("&eSpigot page: &fhttps://www.spigotmc.org/resources/anchorsell-earn-money-automatically-1-16-x-1-17-x.90038/"));
    }
}

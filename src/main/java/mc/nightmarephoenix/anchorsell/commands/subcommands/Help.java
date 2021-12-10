package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class Help extends SubCommands {
    @Override
    public String getDescription() {
        return "Shows the help";
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String syntax() {
        return "/anchor";
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
        if(sender.hasPermission("anchorsell.admin.help")) {
            Utils.sendConfigMultilineMessage("help-message-admin", sender);
        } else {
            Utils.sendConfigMultilineMessage("help-message", sender);
        }
    }
}

package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class ChangeTotalAnchorsUserCanHave extends SubCommands {
    @Override
    public String getDescription() {
        return "Changes the total anchors users can have.";
    }

    @Override
    public String getName() {
        return "changeTotalAnchorsUserCanHave";
    }

    @Override
    public String syntax() {
        return "/anchor changeTotalAnchorsUserCanHave [number]";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.changeTotalAnchorsUserCanHave";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.hasPermission("anchorsell.admin.changeTotalAnchorsUserCanHave")) {
            if(args.length == 2 && Utils.isNumeric(args[1])) {
                StorageManager.changeTotalAnchorsUserCanHave(Integer.parseInt(args[1]));
                sender.sendMessage(Utils.Color("&aTotal anchors per user changed to &c" + args[1]));
            } else {
                sender.sendMessage(Utils.Color("Usage: &e/anchor changeTotalAnchorsUserCanHave [number]"));
            }
        } else Utils.noPermission(getPermission(), sender);
    }
}

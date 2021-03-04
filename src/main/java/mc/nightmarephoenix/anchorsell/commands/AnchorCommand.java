package mc.nightmarephoenix.anchorsell.commands;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AnchorCommand extends JavaPlugin implements CommandExecutor {

    public AnchorCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isAnchorCommand = label.equalsIgnoreCase("anchor");

        if (isAnchorCommand && sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("anchor11!!");
            if (sender.hasPermission("anchorsell.player")) {
                player.sendMessage("anchor!!");
            }
        }

        return super.onCommand(sender, command, label, args);
    }
}
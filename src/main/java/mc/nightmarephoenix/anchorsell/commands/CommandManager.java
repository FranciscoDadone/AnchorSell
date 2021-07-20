package mc.nightmarephoenix.anchorsell.commands;

import mc.nightmarephoenix.anchorsell.commands.subcommands.*;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandManager implements TabExecutor {

    ArrayList<SubCommands> subCommands = new ArrayList<>();

    public CommandManager() {

        // Users
        subCommands.add(new Help());
        subCommands.add(new Authors());
        subCommands.add(new Version());
        subCommands.add(new Buy());
        subCommands.add(new mc.nightmarephoenix.anchorsell.commands.subcommands.List());
        subCommands.add(new Top());
        subCommands.add(new Upgrades());

        // Admin
        subCommands.add(new Reload());
        subCommands.add(new ChangePrice());
        subCommands.add(new ChangeSafeZone());
        subCommands.add(new ChangeTotalAnchorsUserCanHave());
        subCommands.add(new ChangeUpgradeMultiplier());
        subCommands.add(new GetUserFileName());
        subCommands.add(new Give());
        subCommands.add(new Particles());
        subCommands.add(new Revalidate());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        AtomicBoolean found = new AtomicBoolean(false);
        if (args.length >= 1) {
            subCommands.forEach((cmd) -> {
                if (args[0].equalsIgnoreCase(cmd.getName())) {
                    cmd.perform(sender, args);
                    found.set(true);
                }
            });
            if (!found.get())
                Utils.sendConfigMessage("unknown-command", sender);
        } else {
            new Help().perform(sender, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            ArrayList<String> arguments = new ArrayList<>();

            subCommands.forEach((cmd) -> {
                if(cmd.getPermission().equals("") || sender.hasPermission(cmd.getPermission()))
                    arguments.add(cmd.getName());
            });

            return arguments;

        } else if (args.length == 2) {
            ArrayList<String> subcommands = new ArrayList<>();

            subCommands.forEach((cmd) -> {
                for (String subcommand : cmd.getSubCommandsArgs(sender, args)) {
                    if (args[0].equalsIgnoreCase(cmd.getName())) {
                        subcommands.add(subcommand);
                    }
                }
            });

            return subcommands;
        }
        return new ArrayList<>();
    }
}
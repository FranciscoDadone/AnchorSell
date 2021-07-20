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

    ArrayList<SubCommands> adminSubCommands = new ArrayList<>();
    ArrayList<SubCommands> usersSubCommands = new ArrayList<>();

    public CommandManager() {

        // Users
        usersSubCommands.add(new Help());
        usersSubCommands.add(new Authors());
        usersSubCommands.add(new Version());
        usersSubCommands.add(new Buy());
        usersSubCommands.add(new mc.nightmarephoenix.anchorsell.commands.subcommands.List());
        usersSubCommands.add(new Top());
        usersSubCommands.add(new Upgrades());

        // Admin
        adminSubCommands.add(new Reload());
        adminSubCommands.add(new ChangePrice());
        adminSubCommands.add(new ChangeSafeZone());
        adminSubCommands.add(new ChangeTotalAnchorsUserCanHave());
        adminSubCommands.add(new ChangeUpgradeMultiplier());
        adminSubCommands.add(new GetUserFileName());
        adminSubCommands.add(new Give());
        adminSubCommands.add(new Particles());
        adminSubCommands.add(new Revalidate());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        AtomicBoolean found = new AtomicBoolean(false);
        if(args.length >= 1) {
            adminSubCommands.forEach((cmd) -> {
                if(args[0].equalsIgnoreCase(cmd.getName())) {
                    cmd.perform(sender, args);
                    found.set(true);
                }
            });
//            if(sender.hasPermission("anchorsell.admin")) {
//            }
            usersSubCommands.forEach((cmd) -> {
                if(args[0].equalsIgnoreCase(cmd.getName())) {
                    cmd.perform(sender, args);
                    found.set(true);
                }
            });
//            if(sender.hasPermission("anchorsell.player") || sender.hasPermission("anchorsell.admin")) {
//            }
            if(!found.get())
                Utils.sendConfigMessage("unknown-command", sender);
        } else {
            new Help().perform(sender, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(args.length == 1) {
            ArrayList<String> arguments = new ArrayList<>();
            adminSubCommands.forEach((cmd) -> {
                if(cmd.getPermission().equals("") || sender.hasPermission(cmd.getPermission()))
                    arguments.add(cmd.getName());
            });
//            if(sender.hasPermission("anchorsell.admin")) {
//            }
            usersSubCommands.forEach((cmd) -> {
                if(cmd.getPermission().equals("") || sender.hasPermission(cmd.getPermission()))
                    arguments.add(cmd.getName());
            });
//            if(sender.hasPermission("anchorsell.player") || sender.hasPermission("anchorsell.admin")) {
//            }
            return arguments;
        } else if(args.length == 2) {
            ArrayList<String> subcommands = new ArrayList<>();
            if(sender.hasPermission("anchorsell.admin")) {
                adminSubCommands.forEach((cmd) -> {
                    for(String subcommand: cmd.getSubCommandsArgs(sender, args)) {
                        if(args[0].equalsIgnoreCase(cmd.getName())) {
                            subcommands.add(subcommand);
                        }
                    }
                });
            }
            if(sender.hasPermission("anchorsell.player") || sender.hasPermission("anchorsell.admin")) {
                usersSubCommands.forEach((cmd) -> {
                    for(String subcommand: cmd.getSubCommandsArgs(sender, args)) {
                        if(args[0].equalsIgnoreCase(cmd.getName()))
                            subcommands.add(subcommand);
                    }
                });
            }
            return subcommands;
        }
        return new ArrayList<>();
    }
}
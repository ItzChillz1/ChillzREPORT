package chillz.Report;

import java.util.List;

import chillz.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager {

    public static void process(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            notPlayer(sender);
            return;
        }
        Player player = (Player) sender;
        if (args.length > 0) {
            switch (args[0]) {
                case "check":
                    if (!player.hasPermission("report.admin")) {
                        usage(sender);
                        return;
                    }
                    if (args.length > 1) {
                        OfflinePlayer target = getPlayer(args[1]);
                        if (target != null) {
                            List<String> reports = Main.reportManager.getReports(target.getName());
                            for (String report : reports) {
                                sender.sendMessage(report);
                            }
                        } else notFound(sender);
                    } else {
                        List<String> reports = Main.reportManager.getReports();
                        for (String report : reports) {
                            sender.sendMessage(report);
                        }
                    }
                    break;
                case "notify":
                    if (args.length > 1) {
                        if (!player.hasPermission("report.admin")) {
                            usage(sender);
                            return;
                        }
                        if (args[1].equalsIgnoreCase("on")) {
                            Main.reportManager.add(player);
                        } else if (args[1].equalsIgnoreCase("off")) {
                            Main.reportManager.remove(player);
                        } else usage(sender);
                    } else usage(sender);
                    break;
                case "clear":
                    if (args.length > 1) {
                        if (!player.hasPermission("report.admin")) {
                            usage(sender);
                            return;
                        }
                        OfflinePlayer target = getPlayer(args[1]);
                        if (target != null) {
                            Main.reportManager.removeReports(target.getName());
                        } else notFound(sender);
                    } else usage(sender);
                    break;
                default:
                    OfflinePlayer target = getPlayer(args[0]);
                    if (target != null) {
                        new ReportInventory(player.getName(), target.getName());
                    } else notFound(sender);
                    break;
            }
        } else {
            usage(sender);
        }
    }

    public static void notFound(CommandSender sender) {
        sender.sendMessage("§cError: Unable to find that player");
    }

    public static void notPlayer(CommandSender sender) {
        sender.sendMessage("Error: only players can use this command");
    }

    public static void usage(CommandSender sender) {
        sender.sendMessage("§b§lChillz§f§lREPORT §fUsage:");
        sender.sendMessage(" ⋙ /report <player> to open a GUI to report a player.");
        if (sender.hasPermission("report.admin")) {
            sender.sendMessage(" ⋙ /report check to see the most recent reports.");
            sender.sendMessage(" ⋙ /report check <player> to view the reports of a player.");
            sender.sendMessage(" ⋙ /report notify on/off to toggle report notifications.");
            sender.sendMessage(" ⋙ /report clear <player> to clear all player reports for assigned user.");
        } else {
            sender.sendMessage("§b§lChillz§f§lREPORT §fYou do not have permission to use that command.");
        }
    }

    public static OfflinePlayer getPlayer(String target) {
        return Bukkit.getPlayer(target);
    }
}

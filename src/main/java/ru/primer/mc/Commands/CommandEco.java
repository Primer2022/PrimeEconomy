package ru.primer.mc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.primer.mc.Main;

public class CommandEco implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!(s instanceof Player)) {
            System.out.println(ChatColor.translateAlternateColorCodes('&', cfg.getString("not-player")));
            return true;
        }

        Player p = (Player) s;

        if (!p.hasPermission("primeeconomy.eco")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("no-permission")));
            return true;
        }


        if (args.length != 3) {
            return true;
        }

        int number;
        Player target = Bukkit.getPlayerExact(args[1]);

        try {
            number = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("not-int")));
            return true;
        }

        if (number < 0) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("positive-number")));
            return true;
        }

        if (target == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("invalid-player")));
            return true;
        }

        String money = ChatColor.GREEN + String.valueOf((int) number) + "$";
        int currentBalance = (int) Main.getEcon().getBalance(target);

        switch (args[0]) {
            case ("give"): {

                Main.getEcon().depositPlayer(target, number);
                String newBalance = ChatColor.GREEN + String.valueOf((int) Main.getEcon().getBalance(target)) + "$";

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("give-sender").replace("%name%", target.getName()).replace("%amount%", money)));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("give-target").replace("%amount%", money)));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("new-balance").replace("%balance", newBalance)));

                break;
            }
            case ("set"): {

                double difference = currentBalance - number;

                if (difference > 0) {
                    Main.getEcon().withdrawPlayer(target, difference);
                } else {
                    Main.getEcon().depositPlayer(target, -difference);
                }

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("set-sender").replace("%name%", target.getName()).replace("%amount%", money)));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("set-target").replace("%amount%", money)));

                break;
            }
            case ("take"): {

                if (currentBalance - number >= 0) {
                    Main.getEcon().withdrawPlayer(target, number);
                    String newBalance = String.valueOf(Main.getEcon().getBalance(target));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("take-sender").replace("%name%", target.getName()).replace("%amount%", money)));
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("take-target").replace("%amount%", money)));
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("new-balance").replace("%balance%", newBalance)));
                    return true;
                }

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("above-balance")));
                break;
            }
        }
        return true;
    }
}

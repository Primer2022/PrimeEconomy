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

public class CommandPay implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!(s instanceof Player)) {
            System.out.println(ChatColor.translateAlternateColorCodes('&', cfg.getString("not-player")));
            return true;
        }

        Player p = (Player) s;

        if (!p.hasPermission("primeeconomy.pay")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("no-permission")));
            return true;
        }

        if (args.length < 2) {
            return true;
        }

        int number;

        try {
            number = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("not-int")));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        int sender = (int) Main.getEcon().getBalance(p);
        String moneyPay = ChatColor.GREEN + String.valueOf((int) number) + "$";

        if ((sender - number) >= 0) {
            Main.getEcon().withdrawPlayer(p, number);
            Main.getEcon().depositPlayer(target, number);
            String newBalancePlayer = ChatColor.GREEN + String.valueOf((int) Main.getEcon().getBalance(p)) + "$";
            String newBalanceTarget = ChatColor.GREEN + String.valueOf((int) Main.getEcon().getBalance(target)) + "$";
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("send-money").replace("%name%", p.getName()).replace("%amount%", moneyPay)));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("new-balance").replace("%balance%", newBalancePlayer)));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("accept-money").replace("%name%", p.getName()).replace("%amount%", moneyPay)));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("new-balance").replace("%balance%", newBalanceTarget)));
            return true;
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("above-your-balance")));
        return true;
    }
}

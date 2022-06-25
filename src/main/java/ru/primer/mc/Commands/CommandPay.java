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

import static ru.primer.mc.Utils.message;
import static ru.primer.mc.Utils.messageNotPlayer;

public class CommandPay implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!(s instanceof Player)) {
            messageNotPlayer(cfg);
            return true;
        }

        Player p = (Player) s;

        if (!p.hasPermission("primeeconomy.pay")) {
            message(cfg.getString("no-permission"), p);
            return true;
        }

        if (args.length < 2) {
            return true;
        }

        int number;

        try {
            number = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            message(cfg.getString("not-int"), p);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        int sender = (int) Main.getEcon().getBalance(p);
        String money = ChatColor.GREEN + String.valueOf((int) number) + "$";

        if ((sender - number) >= 0) {
            Main.getEcon().withdrawPlayer(p, number);
            Main.getEcon().depositPlayer(target, number);;

            String newBalancePlayer = ChatColor.GREEN + String.valueOf((int) Main.getEcon().getBalance(p)) + "$";
            String newBalanceTarget = ChatColor.GREEN + String.valueOf((int) Main.getEcon().getBalance(target)) + "$";

            message(cfg.getString("send-money").replace("%name%", target.getName()).replace("%amount%", money), p);
            message(cfg.getString("new-balance").replace("%balance%", newBalancePlayer), p);
            message(cfg.getString("accept-money").replace("%name%", p.getName()).replace("%amount%", money), target);
            message(cfg.getString("new-balance").replace("%balance%", newBalanceTarget), target);
            return true;
        }

        p.sendMessage(cfg.getString("above-your-balance").replace('&', '§'));
        return true;
    }
}

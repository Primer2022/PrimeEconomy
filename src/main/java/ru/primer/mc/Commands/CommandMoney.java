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

public class CommandMoney implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!(s instanceof Player)) {
            messageNotPlayer(cfg);
            return true;
        }

        Player p = (Player) s;
        String money = ChatColor.GREEN + String.valueOf((int) Main.getEcon().getBalance(p)) + "$";

        if (!p.hasPermission("primeeconomy.money")) {
            message(cfg.getString("no-permission"), p);
            return true;
        }

        if (args.length < 1) {
            message(cfg.getString("your-balance").replace("%amount%", money), p);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        String moneyTarget = ChatColor.GREEN + String.valueOf((int) Main.getEcon().getBalance(target)) + "$";

        if (target != null) {
            message(cfg.getString("player-balance").replace("%amount%", moneyTarget), p);
            return true;
        }

        message(cfg.getString("invalid-player"), p);
        return true;
    }
}

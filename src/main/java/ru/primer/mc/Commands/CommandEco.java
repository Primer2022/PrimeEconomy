package ru.primer.mc.Commands;

import com.google.common.xml.XmlEscapers;
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

public class CommandEco implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!(s instanceof Player)) {
            messageNotPlayer(cfg);
            return true;
        }

        Player p = (Player) s;

        if (!p.hasPermission("primeeconomy.eco")) {
            message(cfg.getString("no-permission"), p);
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
            message(cfg.getString("not-int"), p);
            return true;
        }

        if (number < 0) {
            message(cfg.getString("positive-number"), p);
            return true;
        }

        if (target == null) {
            message(cfg.getString("invalid-player"), p);
            return true;
        }

        String money = ChatColor.GREEN + String.valueOf((int) number) + "$";
        int currentBalance = (int) Main.getEcon().getBalance(target);

        switch (args[0]) {
            case ("give"): {

                Main.getEcon().depositPlayer(target, number);
                String newBalance = ChatColor.GREEN + String.valueOf((int) Main.getEcon().getBalance(target)) + "$";

                message(cfg.getString("give-sender").replace("%name%", target.getName()).replace("%amount%", money), p);
                message(cfg.getString("give-target").replace("%amount%", money), target);
                message(cfg.getString("new-balance").replace("%balance%", newBalance), target);

                break;
            }
            case ("set"): {

                double difference = currentBalance - number;

                if (difference > 0) {
                    Main.getEcon().withdrawPlayer(target, difference);
                } else {
                    Main.getEcon().depositPlayer(target, -difference);
                }

                message(cfg.getString("set-sender").replace("%name%", target.getName()).replace("%amount%", money), p);
                message(cfg.getString("set-target").replace("%amount%", money), target);

                break;
            }
            case ("take"): {

                if (currentBalance - number >= 0) {
                    Main.getEcon().withdrawPlayer(target, number);
                    String newBalance = String.valueOf(Main.getEcon().getBalance(target));

                    message(cfg.getString("take-sender").replace("%name%", target.getName()).replace("%amount%", money), p);
                    message(cfg.getString("take-target").replace("%amount%", money), target);
                    message(cfg.getString("new-balance").replace("%balance%", newBalance), target);
                    return true;
                }

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("above-balance")));
                break;
            }
        }
        return true;
    }
}

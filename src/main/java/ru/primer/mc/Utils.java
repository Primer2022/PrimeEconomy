package ru.primer.mc;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Utils {
    public static void message(String message, Player p) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void messageNotPlayer(FileConfiguration cfg) {
        System.out.println(cfg.getString("not-player").replace('&', '§'));
    }

}

package ru.primer.mc;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import ru.primer.mc.Commands.CommandEco;
import ru.primer.mc.Commands.CommandMoney;
import ru.primer.mc.Commands.CommandPay;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;

    private static Main inst;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("money").setExecutor(new CommandMoney());
        getCommand("economy").setExecutor(new CommandEco());
        getCommand("pay").setExecutor(new CommandPay());

    }

    @Override
    public void onDisable() {
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        @Nullable RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEcon() {
        return econ;
    }

    public  static  Main getInstance() {
        return inst;
    }

}

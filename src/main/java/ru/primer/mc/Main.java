package ru.primer.mc;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ru.primer.mc.Commands.CommandEco;
import ru.primer.mc.Commands.CommandMoney;
import ru.primer.mc.Commands.CommandPay;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static Economy economy;

    private static Main instance;


    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();

        if (!setupEconomy() ) {
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

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }
    public static Economy getEcon() {
        return economy;
    }

    public  static  Main getInstance() {
        return instance;
    }

}

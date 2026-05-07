package net.essentialsx.bank;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.essentialsx.bank.commands.BankCommand;

import java.util.logging.Level;

public class EssentialsBank extends JavaPlugin {

    private static EssentialsBank instance;
    private BankI18n i18n;
    private Economy vaultEconomy;
    private String bankAccountName;

    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        
        bankAccountName = config.getString("bank-account-name", "ServerBank");
        
        // Detect locale from EssentialsX natively
        String locale = "en"; // Fallback default
        if (getServer().getPluginManager().getPlugin("Essentials") != null) {
            com.earth2me.essentials.IEssentials ess = (com.earth2me.essentials.IEssentials) getServer().getPluginManager().getPlugin("Essentials");
            String essLocale = ess.getSettings().getLocale();
            if (essLocale != null && !essLocale.isEmpty()) {
                locale = essLocale;
                getLogger().info("Detected EssentialsX locale: " + locale);
            }
        }
        
        i18n = new BankI18n(this, locale);

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize bank NPC account if it doesn't exist
        ensureBankAccountExists();

        // Register command
        getCommand("bank").setExecutor(new BankCommand(this));

        getLogger().info("EssentialsBank has been enabled successfully.");
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialsBank has been disabled.");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        vaultEconomy = rsp.getProvider();
        return vaultEconomy != null;
    }

    private void ensureBankAccountExists() {
        try {
            if (!com.earth2me.essentials.api.Economy.isNPC(bankAccountName)) {
                boolean created = com.earth2me.essentials.api.Economy.createNPC(bankAccountName);
                if (created) {
                    getLogger().info("Created new NPC Bank account: " + bankAccountName);
                }
            }
        } catch (Exception e) {
            // UserDoesNotExistException in Essentials implies we should create it
            boolean created = com.earth2me.essentials.api.Economy.createNPC(bankAccountName);
            if (created) {
                getLogger().info("Created new NPC Bank account: " + bankAccountName);
            }
        }
    }

    public static EssentialsBank getInstance() {
        return instance;
    }

    public BankI18n getI18n() {
        return i18n;
    }

    public Economy getVaultEconomy() {
        return vaultEconomy;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }
}

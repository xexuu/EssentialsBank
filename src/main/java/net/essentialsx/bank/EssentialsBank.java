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
    private boolean isSetupMode = false;

    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        
        boolean enabled = config.getBoolean("enabled", false);
        if (!enabled) {
            isSetupMode = true;
            getLogger().warning("EssentialsBank is disabled in config.yml! Please configure 'bank-account-name' and set 'enabled: true'.");
        }

        bankAccountName = config.getString("bank-account-name", "*ServerBank*");
        
        if (bankAccountName.matches("^[a-zA-Z0-9_]{1,16}$")) {
            isSetupMode = true;
            getLogger().severe("==========================================");
            getLogger().severe("SECURITY ALERT: VULNERABLE BANK NAME!");
            getLogger().severe("The name '" + bankAccountName + "' can be used by a real player.");
            getLogger().severe("To prevent spoofing, the bank name MUST contain special characters.");
            getLogger().severe("Example: *ServerBank*, Bank-Account, or $ServerBank$");
            getLogger().severe("EssentialsBank is now in SETUP MODE to protect your server.");
            getLogger().severe("==========================================");
        }

        // Detect locale from EssentialsX natively
        String locale = "en"; // Fallback default
        if (getServer().getPluginManager().getPlugin("Essentials") != null) {
            com.earth2me.essentials.IEssentials ess = (com.earth2me.essentials.IEssentials) getServer().getPluginManager().getPlugin("Essentials");
            String essLocale = ess.getSettings().getLocale();
            if (essLocale != null && !essLocale.isEmpty()) {
                locale = essLocale;
                if (!isSetupMode) getLogger().info("Detected EssentialsX locale: " + locale);
            }
        }
        
        i18n = new BankI18n(this, locale);

        if (!isSetupMode && !setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!isSetupMode) {
            // Initialize bank NPC account if it doesn't exist
            ensureBankAccountExists();
        }

        // Register command
        getCommand("bank").setExecutor(new BankCommand(this));

        // Register event listeners
        getServer().getPluginManager().registerEvents(new net.essentialsx.bank.listeners.PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new net.essentialsx.bank.listeners.ReloadInterceptorListener(this), this);
        getServer().getPluginManager().registerEvents(new net.essentialsx.bank.listeners.PlayerLoginListener(this), this);

        if (isSetupMode) {
            getLogger().info("EssentialsBank is running in Setup Mode.");
        } else {
            getLogger().info("EssentialsBank has been enabled successfully.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("EssentialsBank has been disabled.");
    }

    public void reloadPlugin() {
        reloadConfig();
        FileConfiguration config = getConfig();
        
        boolean enabled = config.getBoolean("enabled", false);
        this.isSetupMode = !enabled;

        bankAccountName = config.getString("bank-account-name", "*ServerBank*");

        if (bankAccountName.matches("^[a-zA-Z0-9_]{1,16}$")) {
            this.isSetupMode = true;
            getLogger().severe("==========================================");
            getLogger().severe("SECURITY ALERT: VULNERABLE BANK NAME!");
            getLogger().severe("The name '" + bankAccountName + "' can be used by a real player.");
            getLogger().severe("To prevent spoofing, the bank name MUST contain special characters.");
            getLogger().severe("Example: *ServerBank*, Bank-Account, or $ServerBank$");
            getLogger().severe("EssentialsBank is now in SETUP MODE to protect your server.");
            getLogger().severe("==========================================");
        }

        String locale = "en";
        if (getServer().getPluginManager().getPlugin("Essentials") != null) {
            com.earth2me.essentials.IEssentials ess = (com.earth2me.essentials.IEssentials) getServer().getPluginManager().getPlugin("Essentials");
            String essLocale = ess.getSettings().getLocale();
            if (essLocale != null && !essLocale.isEmpty()) {
                locale = essLocale;
            }
        }
        
        i18n = new BankI18n(this, locale);

        if (!isSetupMode) {
            ensureBankAccountExists();
            getLogger().info("EssentialsBank configuration and locale reloaded successfully.");
        } else {
            getLogger().warning("EssentialsBank is running in Setup Mode.");
        }
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
        boolean created = false;
        try {
            if (!com.earth2me.essentials.api.Economy.isNPC(bankAccountName)) {
                created = com.earth2me.essentials.api.Economy.createNPC(bankAccountName);
            }
        } catch (Exception e) {
            // UserDoesNotExistException in Essentials implies we should create it
            created = com.earth2me.essentials.api.Economy.createNPC(bankAccountName);
        }

        if (created) {
            getLogger().info("Created new NPC Bank account: " + bankAccountName);
            // Reset balance to 0 to bypass EssentialsX new player starting balance
            double startingBalance = vaultEconomy.getBalance(bankAccountName);
            if (startingBalance > 0) {
                vaultEconomy.withdrawPlayer(bankAccountName, startingBalance);
            }
            getLogger().info("Bank balance initialized to 0.");
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

    public boolean isSetupMode() {
        return isSetupMode;
    }
}
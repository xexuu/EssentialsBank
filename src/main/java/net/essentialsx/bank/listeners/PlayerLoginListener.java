package net.essentialsx.bank.listeners;

import net.essentialsx.bank.EssentialsBank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerLoginListener implements Listener {

    private final EssentialsBank plugin;

    public PlayerLoginListener(EssentialsBank plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        // Anti-Spoofing Connection Shield
        if (event.getName().equalsIgnoreCase(plugin.getBankAccountName())) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, plugin.getI18n().tl("bankNameReservedKicked"));
        }
    }
}

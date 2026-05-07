package net.essentialsx.bank.listeners;

import net.essentialsx.bank.EssentialsBank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final EssentialsBank plugin;

    public PlayerJoinListener(EssentialsBank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.isSetupMode()) {
            Player player = event.getPlayer();
            if (player.hasPermission("essentialsbank.admin") || player.isOp()) {
                // Send notification with a slight delay so it's not buried
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    if (player.isOnline()) {
                        player.sendMessage(plugin.getI18n().tl("setupModeJoinNotification"));
                    }
                }, 40L); // 2 seconds
            }
        }
    }
}

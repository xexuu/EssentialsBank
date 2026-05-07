package net.essentialsx.bank.listeners;

import net.essentialsx.bank.EssentialsBank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class ReloadInterceptorListener implements Listener {

    private final EssentialsBank plugin;

    public ReloadInterceptorListener(EssentialsBank plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase();
        if (isEssentialsReload(command)) {
            // Schedule it to run slightly after EssentialsX has reloaded
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.reloadPlugin();
            }, 5L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onServerCommand(ServerCommandEvent event) {
        String command = "/" + event.getCommand().toLowerCase();
        if (isEssentialsReload(command)) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.reloadPlugin();
            }, 5L);
        }
    }

    private boolean isEssentialsReload(String command) {
        return command.startsWith("/essentials reload") || 
               command.startsWith("/ess reload") || 
               command.startsWith("/erepos reload");
    }
}

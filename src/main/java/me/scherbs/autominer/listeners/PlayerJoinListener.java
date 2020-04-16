package me.scherbs.autominer.listeners;

import me.scherbs.autominer.AutoMinerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent event) {
    Bukkit.getScheduler().runTaskAsynchronously(AutoMinerPlugin.getInstance(), () -> {
      if (AutoMinerPlugin.getInstance().getProfileManager().getProfileByPlayer(event.getPlayer()) == null) {
        AutoMinerPlugin.getInstance().getProfileManager().loadProfile(event.getPlayer());
      }
    });
  }
}

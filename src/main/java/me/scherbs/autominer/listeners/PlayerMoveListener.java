package me.scherbs.autominer.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.scherbs.autominer.AutoMinerPlugin;
import me.scherbs.autominer.profile.Profile;
import me.scherbs.autominer.tasks.MinerTask;
import me.scherbs.autominer.tasks.MinerWaitingTask;
import me.scherbs.autominer.tier.Tier;
import me.scherbs.autominer.tier.TierType;
import me.scherbs.autominer.utils.StringUtils;
import me.scherbs.autominer.utils.TimeUtils;
import me.scherbs.autominer.utils.WGUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerMoveListener implements Listener {

  @EventHandler
  public void onEnterAutoMine(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    Profile profile = AutoMinerPlugin.getInstance().getProfileManager().getProfileByPlayer(player);

    Location to = event.getTo();
    Location from = event.getFrom();
    if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
      return;
    }
    if (!WGUtil.inAutoMiner(to) && WGUtil.inAutoMiner(from)) {
      player.sendMessage(StringUtils.color("&b&lAM &8> &7You have now left the auto miner area."));

      for (Tier tier : profile.getTiers()) {
        tier.setPaused(true);
      }

      List<BukkitTask> toRemove = new ArrayList<>();
      for (BukkitTask task : profile.getTasks()) {
        task.cancel();
        toRemove.add(task);
      }
      profile.getTasks().removeAll(toRemove);
      return;
    }
    if (WGUtil.inAutoMiner(to) && !WGUtil.inAutoMiner(from)) {
      player.sendMessage(StringUtils.color("&b&lAM &8> &7You have now entered the auto miner area."));

      if (profile.getTiers().isEmpty()) {
        player.sendMessage(StringUtils.color("&b&lAM &8> &cYou do not have any auto miner time."));
        return;
      }
      if (player.getItemInHand().getType() != Material.DIAMOND_PICKAXE) {
        player.sendMessage(StringUtils.color("&b&lAM &8> &cHold a pickaxe in your hand to enable auto mining."));
        profile.getTasks().add(new MinerWaitingTask(player)
            .runTaskTimer(AutoMinerPlugin.getInstance(), 0L, 10L));
      } else if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
        for (Tier tier : profile.getTiers()) {
          tier.setPaused(false);
        }
        player.sendMessage(" ");
        player.sendMessage(StringUtils.color(" &a&lAUTOMINER ACTIVATED"));
        player.sendMessage(StringUtils.color(" &7You have activated the auto mining feature."));
        player.sendMessage(StringUtils.color(" &7Use &a/am check &7to check your time."));
        player.sendMessage(" ");

        profile.getTasks().add(new MinerTask(player)
            .runTaskTimer(AutoMinerPlugin.getInstance(), 0L, 10L));
      }
    }
  }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent event) {
    this.onEnterAutoMine(event);
  }
}

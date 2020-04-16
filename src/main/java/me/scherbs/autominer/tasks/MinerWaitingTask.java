package me.scherbs.autominer.tasks;

import me.scherbs.autominer.AutoMinerPlugin;
import me.scherbs.autominer.profile.Profile;
import me.scherbs.autominer.tier.Tier;
import me.scherbs.autominer.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MinerWaitingTask extends BukkitRunnable {

  private Player player;
  private Profile profile;

  public MinerWaitingTask(Player player) {
    this.player = player;
    this.profile = AutoMinerPlugin.getInstance().getProfileManager().getProfileByPlayer(player);
  }

  @Override
  public void run() {
    if (profile.getTiers().isEmpty()) {
      return;
    }
    if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
      player.sendMessage(" ");
      player.sendMessage(StringUtils.color(" &a&lAUTOMINER ACTIVATED"));
      player.sendMessage(StringUtils.color(" &7You have activated the auto mining feature."));
      player.sendMessage(StringUtils.color(" &7Use &a/am check &7to check your time."));
      player.sendMessage(" ");

      for (Tier tier : profile.getTiers()) {
        tier.setPaused(false);
      }
      profile.getTasks().remove(this);
      profile.getTasks().add(new MinerTask(player)
          .runTaskTimer(AutoMinerPlugin.getInstance(), 0L, 10L));
      cancel();
    }
  }
}

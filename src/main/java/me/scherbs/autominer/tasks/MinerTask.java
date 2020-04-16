package me.scherbs.autominer.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.clip.autosell.AutoSellAPI;
import me.clip.autosell.events.SellAllEvent;
import me.scherbs.autominer.AutoMinerPlugin;
import me.scherbs.autominer.profile.Profile;
import me.scherbs.autominer.tier.Tier;
import me.scherbs.autominer.tier.TierType;
import me.scherbs.autominer.utils.StringUtils;
import net.lightshard.prisonmines.PrisonMines;
import net.lightshard.prisonmines.mine.Mine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

public class MinerTask extends BukkitRunnable {

  private Player player;
  private Profile profile;
  private int counter;

  public MinerTask(Player player) {
    this.player = player;
    this.profile = AutoMinerPlugin.getInstance().getProfileManager().getProfileByPlayer(player);
  }

  @Override
  public void run() {
    if (profile.getTiers().isEmpty()) {
      return;
    }
    if (player.getItemInHand().getType() != Material.DIAMOND_PICKAXE) {
      cancel();

      for (Tier tier : profile.getTiers()) {
        tier.setPaused(true);
      }

      player.sendMessage(" ");
      player.sendMessage(StringUtils.color(" &c&lAUTOMINER DEACTIVATED"));
      player.sendMessage(StringUtils.color(" &7You have deactivated the auto mining feature."));
      player.sendMessage(StringUtils.color(" &cHold a pickaxe in your hand to enable auto mining."));
      player.sendMessage(" ");

      profile.getTasks().remove(this);
      profile.getTasks().add(new MinerWaitingTask(player)
          .runTaskTimer(AutoMinerPlugin.getInstance(), 0L, 10L));
      return;
    }
    counter++;

    Iterator<Tier> iterator = profile.getTiers().iterator();

    while (iterator.hasNext()) {
      Tier tier = iterator.next();
      if ((tier.getTime() - System.currentTimeMillis()) <= 0) {
        player.sendMessage(StringUtils.color("&b&lAM &8> &cYou do not have any remaining auto miner for " + StringUtils.capitalize(tier.getType().name().toLowerCase()) + "."));
        iterator.remove();
        continue;
      }

      if (counter % 10 == 0 && tier.getType() == TierType.BASIC) {

      }
      if (counter % 5 == 0 && tier.getType() == TierType.LEGENDARY) {

      }
    }
  }
}

package me.scherbs.autominer.commands;

import me.scherbs.autominer.AutoMinerPlugin;
import me.scherbs.autominer.profile.Profile;
import me.scherbs.autominer.tier.Tier;
import me.scherbs.autominer.tier.TierType;
import me.scherbs.autominer.utils.IntUtils;
import me.scherbs.autominer.utils.StringUtils;
import me.scherbs.autominer.utils.Time;
import me.scherbs.autominer.utils.Time.FormatType;
import me.scherbs.autominer.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoMinerCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      return true;
    }
    Player player = (Player) sender;
    Profile profile = AutoMinerPlugin.getInstance().getProfileManager().getProfileByPlayer(player);

    if (args.length == 0) {
      player.teleport(AutoMinerPlugin.getInstance().getSpawn());
      return true;
    }
    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("check")) {
        if (profile.getTiers().isEmpty()) {
          player.sendMessage(StringUtils.color("&b&lAM &8> &cYou do not have any auto miner time."));
          return true;
        }
        for (Tier tier : profile.getTiers()) {
          player.sendMessage(StringUtils.color(
              "&b&lAM &8> &7" + StringUtils.capitalize(tier.getType().name().toLowerCase()) + ": &a"
                  + Time.format(tier.getRemaining(), FormatType.LONG_TIME)));
        }
        return true;
      }
      if (args[0].equalsIgnoreCase("setspawn") && player.hasPermission("autominer.setspawn")) {
        AutoMinerPlugin.getInstance().setSpawn(player.getLocation());
        player.sendMessage(StringUtils.color("&b&lAM &8> &7Set the spawn location."));
        return true;
      }
    }
    if (!(sender.hasPermission("autominer.give"))) {
      sender.sendMessage(StringUtils.color("&cNo permission."));
      return true;
    }

    if (args.length != 4) {
      sender.sendMessage(StringUtils.color("&cUsage: /autominer give <player> <tier> <time>"));
      return true;
    }
    Player target = Bukkit.getPlayer(args[1]);

    if (target == null) {
      sender.sendMessage(StringUtils.color("&cNo player found."));
      return true;
    }
    Profile targetProfile = AutoMinerPlugin.getInstance().getProfileManager().getProfileByPlayer(target);
    String tierName = args[2];
    TierType type = null;

    for (TierType types : TierType.values()) {
      if (tierName.equalsIgnoreCase(types.name().toLowerCase())) {
        type = types;
      }
    }

    if (!IntUtils.isInt(args[3])) {
      sender.sendMessage(StringUtils.color("&cInvalid integer: " + args[3]));
      return true;
    }
    if (type == null) {
      sender.sendMessage(StringUtils.color("&cInvalid tier type: " + args[2]));
      return true;
    }
    int amount = Integer.parseInt(args[3]);
    Tier tier = targetProfile.getTier(type);
    tier.addTime(amount * 1000L);

    player.sendMessage(StringUtils.color("&b&lAM &8> &7Added " + amount + " seconds to " + target.getName() + "'s " + StringUtils.capitalize(tier.getType().name().toLowerCase()) + " tier."));
    return true;
  }

}

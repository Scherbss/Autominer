package me.scherbs.autominer.utils;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WGUtil {

  public static boolean inAutoMiner(Player player) {
    RegionManager regionManager = WGBukkit.getRegionManager(player.getWorld());
    ApplicableRegionSet regionSet = regionManager.getApplicableRegions(player.getLocation());
    for (ProtectedRegion region : regionSet) {
      if (region.getId().toLowerCase().contains("autominer")) {
        return true;
      }
    }
    return false;
  }

  public static boolean inAutoMiner(Location location) {
    RegionManager regionManager = WGBukkit.getRegionManager(location.getWorld());
    ApplicableRegionSet regionSet = regionManager.getApplicableRegions(location);
    for (ProtectedRegion region : regionSet) {
      if (region.getId().toLowerCase().contains("autominer")) {
        return true;
      }
    }
    return false;
  }

}

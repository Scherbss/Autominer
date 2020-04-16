package me.scherbs.autominer.utils;

import org.bukkit.ChatColor;

public class StringUtils {

  public static String color(String msg) {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }

  public static String capitalize(String message) {
    return org.apache.commons.lang.StringUtils.capitalize(message);
  }

}

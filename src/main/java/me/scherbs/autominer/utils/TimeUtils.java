package me.scherbs.autominer.utils;

import java.text.DecimalFormat;

public class TimeUtils {

  private static final DecimalFormat TIME_FORMAT = new DecimalFormat("0.0");

  public static String formatTime(long time) {
    String formatted;
    if (time < 60000) {
      formatted = TIME_FORMAT.format(time / 1000.0D) + "s";
    } else {
      long second = (time / 1000) % 60;
      long minute = (time / (1000 * 60)) % 60;
      long hour = (time / (1000 * 60 * 60)) % 24;

      formatted =
          (hour > 0 ? hour < 10 ? "0" + hour + ":" : hour + ":" : "") +
              (minute > 0 ? minute < 10 ? "0" + minute : minute : "00") + ":" + (second < 10 ? "0"
              + second : second) + "m";
    }

    return formatted;
  }

  public static int parseSeconds(final String input) {
    if (input.length() <= 1) {
      return -1;
    }
    String digits = "";
    int total = 0;
    for (final char c : input.toCharArray()) {
      if (Character.isDigit(c)) {
        digits += c;
      } else if (Character.isAlphabetic(c)) {
        final int time = parseTime(digits, c);
        if (time != -1) {
          total += time;
        }
      }
    }
    return total;
  }

  public static int parseTime(final String digits, final char frame) {
    try {
      final int parsed = Integer.parseInt(digits);
      switch (frame) {
        case 's': {
          return parsed;
        }
        case 'm': {
          return parsed * 60;
        }
        case 'h': {
          return parsed * 60 * 60;
        }
        default: {
          return -1;
        }
      }
    } catch (NumberFormatException e) {
      return -1;
    }
  }
}

package me.scherbs.autominer.profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.scherbs.autominer.AutoMinerPlugin;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ProfileManager {

  private final Map<UUID, Profile> profiles;

  public ProfileManager() {
    this.profiles = new HashMap<>();
  }

  public void loadProfile(Player player) {
    File directory = new File(AutoMinerPlugin.getInstance().getDataFolder() + "/players");
    File file = new File(AutoMinerPlugin.getInstance().getDataFolder() + "/players/" + player.getUniqueId().toString() + ".json");
    if (!directory.exists()) {
      directory.mkdir();
    }
    if (!file.exists()) {
      new Profile(player.getUniqueId());
      System.out.println("created new empty profile");
      return;
    }
    JSONParser jsonParser = new JSONParser();
    FileReader fileReader;
    try {
      fileReader = new FileReader(file);
      try {
        Profile profile = new ProfileSerializer().deserialize((JSONObject) jsonParser.parse(fileReader));
        this.profiles.put(player.getUniqueId(), profile);
      } catch (IOException ex) {
        ex.printStackTrace();
      } catch (ParseException ex2) {
        ex2.printStackTrace();
      }
    } catch (FileNotFoundException ex3) {
      ex3.printStackTrace();
    }
  }

  public Profile getProfileByPlayer(Player player) {
    Profile profile = this.profiles.get(player.getUniqueId());
    if (profile == null) {
      return null;
    }
    return profile;
  }

  public void createProfile(Player player) {
    this.profiles.put(player.getUniqueId(), new Profile(player.getUniqueId()));
  }

  public void deleteProfile(Player player) {
    this.profiles.remove(player.getUniqueId());
  }

  public Map<UUID, Profile> getProfiles() {
    return profiles;
  }
}

package me.scherbs.autominer.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.rmi.CORBA.Tie;
import me.scherbs.autominer.AutoMinerPlugin;
import me.scherbs.autominer.tier.Tier;
import me.scherbs.autominer.tier.TierType;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONObject;

public class Profile {

  private UUID uuid;

  private List<Tier> tiers = new ArrayList<>();

  private List<BukkitTask> tasks = new ArrayList<>();

  public Profile(UUID uuid) {
    this.uuid = uuid;

    this.tiers.add(new Tier(TierType.BASIC, System.currentTimeMillis() + 3600 * 1000L, true));
    this.tiers.add(new Tier(TierType.LEGENDARY, System.currentTimeMillis() + 60 * 1000L, true));

    AutoMinerPlugin.getInstance().getProfileManager().getProfiles().put(this.uuid, this);
  }

  public Profile(JSONObject jsonObject) {
    this.uuid = UUID.fromString((String) jsonObject.get("uuid"));

    if (jsonObject.containsKey("tier.BASIC")) {
      this.tiers.add(new Tier(TierType.BASIC, System.currentTimeMillis() + ((Long) jsonObject.get("tier.BASIC")), true));
    }
    if (jsonObject.containsKey("tier.LEGENDARY")) {
      this.tiers.add(new Tier(TierType.LEGENDARY, System.currentTimeMillis() + ((Long) jsonObject.get("tier.LEGENDARY")), true));
    }
  }

  public void save() {
    File profileFile = new File(AutoMinerPlugin.getInstance().getDataFolder() + "/players/" + this.uuid.toString() + ".json");
    if (!profileFile.exists()) {
      try {
        profileFile.createNewFile();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    try {
      FileWriter fileWriter = new FileWriter(profileFile);
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      JSONObject jsonObject = new ProfileSerializer().serialize(this);
      fileWriter.write(gson.toJson(jsonObject));
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public Tier getTier(TierType tierType) {
    for (Tier tier : this.tiers) {
      if (tier.getType() == tierType) {
        return tier;
      }
    }
    Tier tier = new Tier(tierType);
    this.tiers.add(tier);
    return tier;
  }

  public List<Tier> getTiers() {
    return tiers;
  }

  public List<BukkitTask> getTasks() {
    return tasks;
  }

  public UUID getUuid() {
    return uuid;
  }
}

package me.scherbs.autominer;

import me.scherbs.autominer.commands.AutoMinerCommand;
import me.scherbs.autominer.listeners.PlayerJoinListener;
import me.scherbs.autominer.listeners.PlayerMoveListener;
import me.scherbs.autominer.listeners.PlayerQuitListener;
import me.scherbs.autominer.profile.Profile;
import me.scherbs.autominer.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoMinerPlugin extends JavaPlugin {

  private static AutoMinerPlugin instance;

  private ProfileManager profileManager;

  private Location spawn;

  @Override
  public void onEnable() {
    AutoMinerPlugin.instance = this;

    if (!this.getDataFolder().exists()) {
      this.getDataFolder().mkdir();
    }
    saveDefaultConfig();

    this.profileManager = new ProfileManager();
    this.loadSpawn();

    getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

    getCommand("autominer").setExecutor(new AutoMinerCommand());
  }

  @Override
  public void onDisable() {
    this.profileManager.getProfiles().values().forEach(Profile::save);
    this.saveSpawn();
  }

  private void loadSpawn() {
    if (!getConfig().contains("spawn")) {
      return;
    }
    String path = getConfig().getString("spawn");
    String[] split = path.split(",");
    World world = Bukkit.getWorld(split[0]);
    double x = Double.parseDouble(split[1]);
    double y = Double.parseDouble(split[2]);
    double z = Double.parseDouble(split[3]);
    float yaw = Float.parseFloat(split[4]);
    float pitch = Float.parseFloat(split[5]);

    this.spawn = new Location(world, x, y, z, yaw, pitch);
  }

  private void saveSpawn() {
    String location =
        this.spawn.getWorld().getName() + "," + this.spawn.getX() + "," + this.spawn.getY() + ","
            + this.spawn.getZ() + "," + this.spawn.getYaw() + "," + this.spawn.getPitch();
    FileConfiguration config = getConfig();
    config.set("spawn", location);

    this.saveConfig();
  }

  public static AutoMinerPlugin getInstance() {
    return instance;
  }

  public ProfileManager getProfileManager() {
    return profileManager;
  }

  public Location getSpawn() {
    return spawn;
  }

  public void setSpawn(Location spawn) {
    this.spawn = spawn;
  }
}

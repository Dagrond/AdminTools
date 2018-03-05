package com.gmail.ZiomuuSs.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Team;

public class Data {
  protected Main plugin;
  protected ConfigAccessor msgAccessor;
  protected ConfigAccessor warpAccessor;
  protected ConfigAccessor teamAccessor;
  protected HashMap<String, Location> warps = new HashMap<>(); //saved warps
  protected HashMap<String, Team> savedTeams = new HashMap<>(); //all saved teams
  protected HashMap<UUID, ConfigAccessor> savedPlayers = new HashMap<>(); //all saved players, for performance
  protected Team openTeam; //team that is open to players to join into
  
  public Data(Main plugin) {
    this.plugin = plugin;
    load();
  }
  
  public Team getOpen() {
    return openTeam;
  }
  
  public void setOpen(Team team) {
    openTeam = team;
  }
  
  public boolean isSaved(UUID uuid) {
    return savedPlayers.containsKey(uuid);
  }
  
  public void addSavedPlayer(Player player) {
    savedPlayers.put(player.getUniqueId(), new ConfigAccessor(plugin, player.getUniqueId()+".yml", "Players"));
    savePlayer(player.getUniqueId());
  }
  
  private void savePlayer(UUID uuid) {
    //todo
  }
  
  public void removeSavedPlayer(UUID uuid) {
    savedPlayers.remove(uuid);
  }
  
  public Team getTeamByPlayer(Player player) {
    for (Team t : savedTeams.values()) {
      if (t.isSaved(player.getUniqueId()))
        return t;
    }
    return null;
  }
  
  public SavedPlayer getSavedAnywhere(Player player) {
    for (Team t : savedTeams.values()) {
      if (t.isSaved(player.getUniqueId()))
        return t.getPlayer(player);
    }
    return null;
  }
  
  @SuppressWarnings("unchecked")
  private void load() {
    msgAccessor = new ConfigAccessor(plugin, "Messages.yml");
    warpAccessor = new ConfigAccessor(plugin, "Warps.yml");
    msgAccessor.saveDefaultConfig();
    warpAccessor.saveDefaultConfig();
    Msg.set(msgAccessor.getConfig());
    //loading warps
    ConfigurationSection w = warpAccessor.getConfig();
    if (w.isConfigurationSection("warp")) {
      for (String warp : w.getConfigurationSection("warp").getKeys(false)) {
        warps.put(warp, new Location(Bukkit.getWorld(w.getString("warp."+warp+".world")), w.getDouble("warp."+warp+".x"), w.getDouble("warp."+warp+".y"), w.getDouble("warp."+warp+".z"), (float) w.getDouble("warp."+warp+".yaw"), (float) w.getDouble("warp."+warp+".pitch")));
      }
    }
    //loading teams
    if (new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Teams").exists()) {
      for (File file : new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Teams").listFiles()) {
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        String team = file.getName();
        team = team.substring(0, team.length() - 4); //remove the .yml
        Team st = new Team(team, this);
        if (fc.isList(team+".inventory"))
          st.setInventory(((List<ItemStack>) fc.getList(team+".inventory")).toArray(new ItemStack[0]));
        if (fc.isConfigurationSection(team+".location") && Bukkit.getServer().getWorld(fc.getString(team+".location.world")) != null)
          st.setLocation(new Location(Bukkit.getServer().getWorld(fc.getString(team+".location.world")), fc.getDouble(team+".location.x"), fc.getDouble(team+".location.y"), fc.getDouble(team+".location.z"), (float) fc.getDouble(team+".location.yaw"), (float) fc.getDouble(team+".location.pitch")));
        if (fc.isBoolean(team+".friendlyfire") && !fc.getBoolean(team+".friendlyfire"))
          st.switchFriendlyFire();
        savedTeams.put(team, st);
      }
    }
  }
  
  public boolean isTeam(String name) {
    if (savedTeams.containsKey(name))
      return true;
    else
      return false;
  }
  
  public int getEventNumber() {
    return savedTeams.size();
  }
  
  public void addTeam(String name) {
    savedTeams.put(name, new Team(name, this));
  }
  
  public Team getTeam(String name) {
    return savedTeams.get(name);
  }
  
  private void saveWarps() {
    ConfigurationSection w = warpAccessor.getConfig();
    for (String warp : warps.keySet()) {
      w.set("warp."+warp+".x", warps.get(warp).getX());
      w.set("warp."+warp+".y", warps.get(warp).getY());
      w.set("warp."+warp+".z", warps.get(warp).getZ());
      w.set("warp."+warp+".yaw", warps.get(warp).getYaw());
      w.set("warp."+warp+".pitch", warps.get(warp).getPitch());
      w.set("warp."+warp+".world", warps.get(warp).getWorld().getName());
    }
    warpAccessor.saveConfig();
  }
  
  public void saveTeams() {
    for (String team : savedTeams.keySet()) {
      ConfigAccessor ca = new ConfigAccessor(plugin, team+".yml", "Teams");
      ConfigurationSection cs = ca.getConfig();
      if (!savedTeams.get(team).getFriendFire())
        cs.set(team+".friendlyfire", false);
      if (savedTeams.get(team).getLocation() != null) {
        cs.set(team+".location.x", savedTeams.get(team).getLocation().getX());
        cs.set(team+".location.y", savedTeams.get(team).getLocation().getY());
        cs.set(team+".location.z", savedTeams.get(team).getLocation().getZ());
        cs.set(team+".location.yaw", savedTeams.get(team).getLocation().getYaw());
        cs.set(team+".location.pitch", savedTeams.get(team).getLocation().getPitch());
        cs.set(team+".location.world", savedTeams.get(team).getLocation().getWorld().getName());
      }
      if (savedTeams.get(team).getInventory() != null)
          cs.set(team+".inventory", Arrays.asList(savedTeams.get(team).getInventory()));
      ca.saveConfig();
    }
  }
  
  //return true if warp was added, return false if warp was edited
  public boolean setWarp(String name, Location loc) {
    if (warps.containsKey(name)) {
      warps.put(name, loc);
      saveWarps();
      return false;
    } else {
      warps.put(name, loc);
      saveWarps();
      return true;
    }
  }
  
  public void delWarp(String warp) {
    warps.remove(warp);
    warpAccessor.getConfig().set("warp."+warp, null);
    saveWarps();
  }
  public String getPrettyWarpList() {
    String list = "";
    for (String s : warps.keySet()) {
      list += s+", ";
    }
    if (!list.equalsIgnoreCase(""))
      return list.substring(0, list.length() - 2);
    else
      return Msg.get("none", false);
  }

  public String getPrettyTeamList() {
    String list = "";
    for (String s : savedTeams.keySet()) {
      list += s+", ";
    }
    if (!list.equalsIgnoreCase(""))
      return list.substring(0, list.length() - 2);
    else
      return Msg.get("none", false);
  }
  
  public Location getWarp(String warp) {
    if (warps.containsKey(warp))
      return warps.get(warp);
    else
      return null;
  }
  
}

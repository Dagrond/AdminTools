package com.gmail.ZiomuuSs.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Team;

public class Data {
  protected Main plugin;
  protected ConfigAccessor msgAccessor;
  protected ConfigAccessor warpAccessor;
  protected ConfigAccessor teamAccessor;
  protected HashMap<String, Location> warps = new HashMap<>();
  protected HashMap<String, Team> savedTeams = new HashMap<>();
  
  public Data(Main plugin) {
    this.plugin = plugin;
    load();
  }
  
  public boolean isSaved(UUID uuid) {
    for (Team t : savedTeams.values()) {
      if (t.isSaved(uuid))
        return true;
    }
    return false;
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
  protected void load() {
    msgAccessor = new ConfigAccessor(plugin, "Messages.yml");
    warpAccessor = new ConfigAccessor(plugin, "Warps.yml");
    teamAccessor = new ConfigAccessor(plugin, "Teams.yml");
    msgAccessor.saveDefaultConfig();
    warpAccessor.saveDefaultConfig();
    teamAccessor.saveDefaultConfig();
    Msg.set(msgAccessor.getConfig());
    //loading warps
    ConfigurationSection w = warpAccessor.getConfig();
    if (w.isConfigurationSection("warp")) {
      for (String warp : w.getConfigurationSection("warp").getKeys(false)) {
        warps.put(warp, new Location(Bukkit.getWorld(w.getString("warp."+warp+".world")), w.getDouble("warp."+warp+".x"), w.getDouble("warp."+warp+".y"), w.getDouble("warp."+warp+".z"), (float) w.getDouble("warp."+warp+".yaw"), (float) w.getDouble("warp."+warp+".pitch")));
      }
    }
    //loading teams
    w = teamAccessor.getConfig();
    if (w.isConfigurationSection("teams")) {
      for (String team : w.getConfigurationSection("teams").getKeys(false)) {
        savedTeams.put(team, new Team(team, ((List<ItemStack>) w.getList("teams."+team+".inventory")).toArray(new ItemStack[0]), new Location(Bukkit.getWorld(w.getString("teams."+team+".location.world")), w.getDouble("teams."+team+".location.x"), w.getDouble("teams."+team+".location.y"), w.getDouble("teams."+team+".location.z"), (float) w.getDouble("teams."+team+".location.yaw"), (float) w.getDouble("teams."+team+".location.pitch"))));
      }
    }
  }
  
  public boolean isTeam(String name) {
    if (savedTeams.containsKey(name))
      return true;
    else
      return false;
  }
  
  public void addTeam(String name) {
    savedTeams.put(name, new Team(name));
  }
  
  public Team getTeam(String name) {
    return savedTeams.get(name);
  }
  
  public void saveWarps() {
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
    ConfigurationSection w = teamAccessor.getConfig();
    for (String team : savedTeams.keySet()) {
      w.createSection("teams."+team);
      if (savedTeams.get(team).getLocation() != null) {
        w.set("teams."+team+".location.x", savedTeams.get(team).getLocation().getX());
        w.set("teams."+team+".location.y", savedTeams.get(team).getLocation().getY());
        w.set("teams."+team+".location.z", savedTeams.get(team).getLocation().getZ());
        w.set("teams."+team+".location.yaw", savedTeams.get(team).getLocation().getYaw());
        w.set("teams."+team+".location.pitch", savedTeams.get(team).getLocation().getPitch());
        w.set("teams."+team+".location.world", savedTeams.get(team).getLocation().getWorld().getName());
      }
      if (savedTeams.get(team).getInventory() != null)
        w.set("teams."+team+".inventory", Arrays.asList(savedTeams.get(team).getInventory()));
    }
    teamAccessor.saveConfig();
  }
  
  public void save() {
    saveWarps();
    saveTeams();
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

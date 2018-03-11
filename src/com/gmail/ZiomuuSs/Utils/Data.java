package com.gmail.ZiomuuSs.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
import com.gmail.ZiomuuSs.EventTeam;
import com.gmail.ZiomuuSs.EventTeam.TeamStatus;

public class Data {
  private Main plugin;
  private ConfigAccessor msgAccessor;
  private ConfigAccessor warpAccessor;
  private HashSet<EventTeam> currentTeams = new HashSet<>(); //teams that are currently starting in event
  private HashSet<UUID> waitingPlayers = new HashSet<>(); //players queued to join event
  private HashMap<UUID, ItemStack[]> keepInventory = new HashMap<>();
  private HashMap<String, Location> warps = new HashMap<>(); //saved warps
  private HashMap<String, EventTeam> savedTeams = new HashMap<>(); //all saved teams
  private HashMap<UUID, ConfigAccessor> savedPlayers = new HashMap<>(); //all saved players, for performance or smth I am not even sure anymore
  private HashMap<UUID, SavedPlayer> toRestore = new HashMap<>(); //players that are out of event, but waiting for respawn.
  
  public Data(Main plugin) {
    this.plugin = plugin;
    load();
  }
  
  public Main getPlugin() {
    return plugin;
  }
  
  public void broadcastToPlayers(String msg) {
    for (UUID uuid : savedPlayers.keySet()) {
      Bukkit.getPlayer(uuid).sendMessage(msg);
    }
  }
  
  public HashMap<UUID, ItemStack[]> getKeepInventory() {
    return keepInventory;
  }
  
  public int getSavedPlayersCount() {
    return savedPlayers.size();
  }
  public boolean isToRestore(UUID uuid) {
    return toRestore.containsKey(uuid);
  }
  
  public void addPlayerToRestore(UUID uuid, SavedPlayer player) {
    toRestore.put(uuid, player);
  }
  
  public void restorePlayer(UUID uuid) {
    toRestore.get(uuid).restore();
    toRestore.remove(uuid);
  }
  
  //checks maxplayers of current teams
  //and decides, if another player can join
  public boolean CanJoin() {
    for (EventTeam team : currentTeams) {
      if (team.getMaxPlayers() > 0 && !(team.getMaxPlayers() > team.getPlayerNumber()))
        return false;
    }
    return true;
  }
  
  public boolean isStarting() {
    return !currentTeams.isEmpty();
  }
  
  public void setStarting(int delay, String displayName, EventTeam...toStart) {
    for (EventTeam team : toStart) {
      currentTeams.add(team);
      team.setTeamStatus(TeamStatus.LOBBY);
    }
    
    CountdownTimer timer = new CountdownTimer(plugin, delay, () -> Bukkit.broadcastMessage(Msg.get("event_start", true, displayName, 
        Integer.toString(delay))),
         () -> {
          Bukkit.broadcastMessage(Msg.get("event_nojoin", true, displayName));
          for (EventTeam team : currentTeams) {
            team.start(displayName);
          }
          currentTeams.clear();
          for (UUID uuid : waitingPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null)
              player.sendMessage(Msg.get("event_error_kicked_queue", true));
          }
          waitingPlayers.clear();
          },
        (t) -> {
          //there will be check if counting was stopped or smth
        });
    timer.scheduleTimer();
  }
  
  public boolean isSaved(UUID uuid) {
    return savedPlayers.containsKey(uuid);
  }
  
  public boolean isQueued(UUID uuid) {
    return waitingPlayers.contains(uuid);
  }
  
  //return true if player was added to event
  //or false if player was queued to event
  public boolean addPlayer(Player player) {
    if (currentTeams.size() == 1) {
      //for 1 team
      for (EventTeam team : currentTeams)
        team.addPlayer(player);
      savedPlayers.put(player.getUniqueId(), new ConfigAccessor(plugin, player.getUniqueId()+".yml", "Players"));
      return true;
    } else {
      //for more teams
      for (UUID uuid : waitingPlayers) {
        if (Bukkit.getPlayer(uuid) == null)
          waitingPlayers.remove(uuid);
      }
      waitingPlayers.add(player.getUniqueId());
      if (currentTeams.size() == waitingPlayers.size()) {
        for (EventTeam team : currentTeams) {
          Player pl = Bukkit.getPlayer(waitingPlayers.toArray(new UUID[waitingPlayers.size()])[0]);
          team.addPlayer(pl);
          savedPlayers.put(pl.getUniqueId(), new ConfigAccessor(plugin, pl.getUniqueId()+".yml", "Players"));
          waitingPlayers.remove(pl.getUniqueId());
        }
      }
      return false;
    }
  }
  
  public void removePlayer(Player player) {
    getTeamByPlayer(player).delPlayer(player);
    savedPlayers.remove(player.getUniqueId());
  }
  
  public EventTeam getTeamByPlayer(Player player) {
    for (EventTeam t : savedTeams.values()) {
      if (t.isSaved(player.getUniqueId()))
        return t;
    }
    return null;
  }
  
  public boolean anyInProgress() {
    return !savedPlayers.isEmpty() && !currentTeams.isEmpty();
  }
  public SavedPlayer getSavedAnywhere(Player player) {
    for (EventTeam t : savedTeams.values()) {
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
        EventTeam st = new EventTeam(team, this);
        if (fc.isConfigurationSection("inventories")) {
          for (String name : fc.getConfigurationSection("inventories").getKeys(false)) {
            st.setInventory(name, ((List<ItemStack>) fc.getList("inventories."+name+".contents")).toArray(new ItemStack[0]));
            if (fc.isConfigurationSection("inventories."+name+"icon"))
              st.setInventoryIcon(name, fc.getItemStack("inventories."+name+".icon"));
          }
        }
        if (fc.isConfigurationSection("lobby"))
          st.setLobby(new Location(Bukkit.getWorld(fc.getString("lobby.world")), fc.getDouble("lobby.x"), fc.getDouble("lobby.y"), fc.getDouble("lobby.z"), (float) fc.getDouble("lobby.yaw"), (float) fc.getDouble("lobby.pitch")));
        if (fc.isBoolean("friendlyfire") && !fc.getBoolean("friendlyfire"))
          st.switchFriendlyFire();
        if (fc.isBoolean("NametagVisibility") && fc.getBoolean("NametagVisibility"))
          st.switchNametagVisibility();
        st.setMaxPlayers(fc.getInt("maxplayers"));
        if (fc.isConfigurationSection("startpoints")) {
          for (String index : fc.getConfigurationSection("startpoints").getKeys(false)) {
            st.setStartPoints(new Location(Bukkit.getWorld(fc.getString("startpoints."+index+".world")), fc.getDouble("startpoints."+index+".x"), fc.getDouble("startpoints."+index+".y"), fc.getDouble("startpoints."+index+".z"), (float) fc.getDouble("startpoints."+index+".yaw"), (float) fc.getDouble("startpoints."+index+".pitch")), Integer.valueOf(index)+1);
          }
        }
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
    savedTeams.put(name, new EventTeam(name, this));
    saveTeam(name);
  }
  
  public EventTeam getTeam(String name) {
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
  
  public void saveTeam(String team) {
    if (savedTeams.get(team) == null)
      return;
    //delete file. This is just easier to work on clear config
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Teams", team+".yml").delete();
    EventTeam tm = savedTeams.get(team);
    ConfigAccessor ca = new ConfigAccessor(plugin, team+".yml", "Teams");
    ConfigurationSection cs = ca.getConfig();
    if (!savedTeams.get(team).getFriendlyFire())
      cs.set("friendlyfire", false);
    if (savedTeams.get(team).getNametagVisibility())
      cs.set("NametagVisibility", true);
    if (savedTeams.get(team).getLobby() != null) {
      cs.set("lobby.x", tm.getLobby().getX());
      cs.set("lobby.y", tm.getLobby().getY());
      cs.set("lobby.z", tm.getLobby().getZ());
      cs.set("lobby.yaw", tm.getLobby().getYaw());
      cs.set("lobby.pitch", tm.getLobby().getPitch());
      cs.set("lobby.world", tm.getLobby().getWorld().getName());
    }
    cs.set("maxplayers", tm.getMaxPlayers());
    if (!tm.getStartPoints().isEmpty()) {
      int count = 0;
      for (Location loc : tm.getStartPoints()) {
        cs.set("startpoints."+Integer.toString(count)+".x", loc.getX());
        cs.set("startpoints."+Integer.toString(count)+".y", loc.getY());
        cs.set("startpoints."+Integer.toString(count)+".z", loc.getZ());
        cs.set("startpoints."+Integer.toString(count)+".yaw", loc.getYaw());
        cs.set("startpoints."+Integer.toString(count)+".pitch", loc.getPitch());
        cs.set("startpoints."+Integer.toString(count)+".world", loc.getWorld().getName());
        ++count;
      }
    }
    if (!tm.getInventories().isEmpty()) {
      for (String name : tm.getInventories()) {
        cs.set("inventories."+name+".contents", Arrays.asList(tm.getInventory(name)));
        if (tm.getInventoryIcon(name) != null) {
          cs.set("inventories."+name+".icon", tm.getInventoryIcon(name));
        }
      }
    }
    ca.saveConfig();
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

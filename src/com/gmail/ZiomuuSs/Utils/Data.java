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
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Events.OnCommandEvent;
import com.gmail.ZiomuuSs.Events.OnDamageEvent;
import com.gmail.ZiomuuSs.Events.OnLeaveEvent;
import com.gmail.ZiomuuSs.EventGroup;
import com.gmail.ZiomuuSs.EventTeam;

public class Data {
  private Main plugin;
  private ConfigAccessor msgAccessor;
  private ConfigAccessor warpAccessor;
  private EventGroup current; //Event that is in progress
  private HashMap<UUID, ItemStack[]> keepInventory = new HashMap<>();
  private HashMap<String, Location> warps = new HashMap<>(); //saved warps
  private HashMap<String, EventTeam> savedTeams = new HashMap<>(); //all saved teams
  private HashMap<String, EventGroup> savedGroups = new HashMap<>(); //all saved groups
  private HashMap<UUID, SavedPlayer> toRestore = new HashMap<>(); //players that are out of event, but waiting for respawn.
  private OnLeaveEvent leaveListener = new OnLeaveEvent(this);
  private OnCommandEvent commandListener = new OnCommandEvent(plugin);
  private OnDamageEvent damageListener = new OnDamageEvent(this);
  
  public Data(Main plugin) {
    this.plugin = plugin;
    load();
  }
  
  //start code for event
  public void start(EventGroup group) {
    //todo
    current = group;
    current.start();
    //registering events
    plugin.getServer().getPluginManager().registerEvents(leaveListener, plugin);
    plugin.getServer().getPluginManager().registerEvents(commandListener, plugin);
    plugin.getServer().getPluginManager().registerEvents(damageListener, plugin);
  }
  
  //stop code for event
  public void stop() {
    //todo
    current.stop();
    current = null;
    //unregistering events
    HandlerList.unregisterAll(leaveListener);
    HandlerList.unregisterAll(commandListener);
    HandlerList.unregisterAll(damageListener);
  }
  
  
  public Main getPlugin() {
    return plugin;
  }
  
  public boolean isEventGroup(String group) {
    return (savedGroups.containsKey(group));
  }
  
  public int getEventGroupNumber() {
    return savedGroups.size();
  }
  
  public EventGroup getEventGroupByName(String name) {
    return savedGroups.get(name);
  }
  
  public void addEventGroup(String name, String displayName) {
    savedGroups.put(name, new EventGroup(this, name, displayName));
    saveGroup(name);
  }
  
  public void removeEventGroup(String name) {
    savedGroups.remove(name);
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Events", name+".yml").delete();
  }
  
  public HashMap<UUID, ItemStack[]> getKeepInventory() {
    return keepInventory;
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
  
  public EventGroup getCurrentEvent() {
    return current;
  }
  
  public boolean isTeam(String name) {
    return savedTeams.containsKey(name);
  }
  
  public int getEventNumber() {
    return savedTeams.size();
  }
  
  public void addTeam(String name) {
    savedTeams.put(name, new EventTeam(name, this));
    saveTeam(name);
  }
  
  public void removeTeam(String name) {
    savedTeams.remove(name);
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Teams", name+".yml").delete();
    for (EventGroup group : savedGroups.values()) {
      if (group.getTeams().containsKey(name))
        group.getTeams().remove(name);
    }
  }
  
  public EventTeam getTeam(String name) {
    return savedTeams.get(name);
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
  
  public void saveGroup(String name) {
    if (savedGroups.get(name) == null)
      return;
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Events", name+".yml").delete();
    EventGroup group = savedGroups.get(name);
    ConfigAccessor ca = new ConfigAccessor(plugin, name+".yml", "Events");
    ConfigurationSection cs = ca.getConfig();
    //todo
    cs.set("delay", group.getDelay());
    cs.set("minplayers", group.getMinPlayers());
    cs.set("displayname", group.getDisplayName());
    if (group.getSpecLocation() != null) {
      Location l = group.getSpecLocation();
      cs.set("spec.x", l.getX());
      cs.set("spec.y", l.getY());
      cs.set("spec.z", l.getZ());
      cs.set("spec.yaw", l.getYaw());
      cs.set("spec.pitch", l.getPitch());
      cs.set("spec.world", l.getWorld().getName());
    }
    if (!group.getTeams().isEmpty()) {
      cs.set("teams", group.getTeams().keySet());
    }
    ca.saveConfig();
  }
  
  public void saveTeam(String team) {
    if (savedTeams.get(team) == null)
      return;
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Teams", team+".yml").delete();
    EventTeam tm = savedTeams.get(team);
    ConfigAccessor ca = new ConfigAccessor(plugin, team+".yml", "Teams");
    ConfigurationSection cs = ca.getConfig();
    if (!tm.getFriendlyFire())
      cs.set("friendlyfire", false);
    if (tm.getNametagVisibility())
      cs.set("NametagVisibility", true);
    if (tm.getLobby() != null) {
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
  
  @SuppressWarnings("unchecked")
  private void load() {
    msgAccessor = new ConfigAccessor(plugin, "Messages.yml");
    warpAccessor = new ConfigAccessor(plugin, "Warps.yml");
    msgAccessor.saveDefaultConfig();
    warpAccessor.saveDefaultConfig();
    Msg.set(msgAccessor.getConfig());
    //loading warps
    int warpsCount = 0;
    ConfigurationSection w = warpAccessor.getConfig();
    if (w.isConfigurationSection("warp")) {
      for (String warp : w.getConfigurationSection("warp").getKeys(false)) {
        warps.put(warp, new Location(Bukkit.getWorld(w.getString("warp."+warp+".world")), w.getDouble("warp."+warp+".x"), w.getDouble("warp."+warp+".y"), w.getDouble("warp."+warp+".z"), (float) w.getDouble("warp."+warp+".yaw"), (float) w.getDouble("warp."+warp+".pitch")));
        ++warpsCount;
      }
    }
    //loading teams
    int teamsCount = 0;
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
        ++teamsCount;
      }
    }
    //loading groups
    int groupsCount = 0;
    if (new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Groups").exists()) {
      for (File file : new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Groups").listFiles()) {
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        String gn = file.getName();
        gn = gn.substring(0, gn.length() - 4); //remove the .yml
        EventGroup group = new EventGroup(this, gn, fc.getString("displayname"));
        group.setDelay(fc.getInt("delay"));
        group.setMinPlayers(fc.getInt("minplayers"));
        if (fc.isConfigurationSection("spec"))
          group.setSpectatorsLocation(new Location(Bukkit.getWorld(fc.getString("spec.world")), fc.getDouble("spec.x"), fc.getDouble("spec.y"), fc.getDouble("spec.z"), (float) fc.getDouble("spec.yaw"), (float) fc.getDouble("spec.pitch")));
        if (fc.isList("teams")) {
          for (String team : fc.getList("teams").toArray(new String[0])) {
            group.addTeam(getTeam(team));
          }
        }
        savedGroups.put(gn, group);
        ++groupsCount;
      }
    }
    Bukkit.getLogger().info(Msg.get("console_loaded", true, Integer.toString(warpsCount), Integer.toString(teamsCount), Integer.toString(groupsCount)));
  }
}

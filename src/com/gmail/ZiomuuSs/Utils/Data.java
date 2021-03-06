package com.gmail.ZiomuuSs.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.EventGroup.StopCondition;
import com.gmail.ZiomuuSs.Events.OnCommandEvent;
import com.gmail.ZiomuuSs.Events.OnDamageEvent;

import net.milkbowl.vault.economy.Economy;

import com.gmail.ZiomuuSs.EventGroup;
import com.gmail.ZiomuuSs.EventTeam;

public class Data {
  private Main plugin;
  private ConfigAccessor msgAccessor;
  private ConfigAccessor dataAccessor;
  private EventGroup current; //Event that is in progress
  private HashMap<String, EventTeam> savedTeams = new HashMap<>(); //all saved teams
  private HashMap<String, EventGroup> savedGroups = new HashMap<>(); //all saved groups
  private HashMap<UUID, SavedPlayer> toRestore = new HashMap<>(); //players that are out of event, but waiting for respawn.
  private ItemStack[] guildItems; //items needed to create guild
  private OnCommandEvent commandListener = new OnCommandEvent(this);
  private Economy econ;
  private OnDamageEvent damageListener = new OnDamageEvent(this);
  
  public Data(Main plugin, Economy econ) {
    this.plugin = plugin;
    load();
  }
  
  //start code for event
  public void start(EventGroup group) {
    //todo
    current = group;
    current.start();
    //registering events
    plugin.getServer().getPluginManager().registerEvents(commandListener, plugin);
    plugin.getServer().getPluginManager().registerEvents(damageListener, plugin);
  }
  
  //stop code for event
  public void stop() {
    //todo
    current.stop();
    current = null;
    //unregistering events
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
  
  public ItemStack[] getItemsForGuild() {
    return guildItems;
  }
  
  public EventGroup getEventGroupByName(String name) {
    return savedGroups.get(name);
  }
  
  public void addEventGroup(String name, String displayName) {
    savedGroups.put(name, new EventGroup(this, name, ChatColor.translateAlternateColorCodes('&', displayName.replaceAll("_", " "))));
    saveGroup(name);
  }
  
  public void removeEventGroup(String name) {
    savedGroups.remove(name);
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Events", name+".yml").delete();
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
  
  public Economy getEconomy() {
    return econ;
  }
  
  public EventTeam getTeam(String name) {
    return savedTeams.get(name);
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
  
  public void setGuildItems(ItemStack[] it) {
    guildItems = it;
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Data.yml").delete();
    ConfigAccessor ca = new ConfigAccessor(plugin, "Data.yml");
    ConfigurationSection cs = ca.getConfig();
    cs.set("RequiredItemsForGuild", Arrays.asList(it));
    ca.saveConfig();
  }
  
  public void saveGroup(String name) {
    if (savedGroups.get(name) == null)
      return;
    new File(plugin.getDataFolder()+String.valueOf(File.separatorChar)+"Events", name+".yml").delete();
    EventGroup group = savedGroups.get(name);
    ConfigAccessor ca = new ConfigAccessor(plugin, name+".yml", "Events");
    ConfigurationSection cs = ca.getConfig();
    //todo
    cs.set("stopcondition", group.getStopCondition().toString());
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
      Set<String> teams = group.getTeams().keySet();
      cs.set("teams", teams.toArray(new String[teams.size()]));
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
    if (!tm.getNametagVisibility())
      cs.set("NametagVisibility", false);
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
    dataAccessor = new ConfigAccessor(plugin, "Data.yml");
    msgAccessor.saveDefaultConfig();
    dataAccessor.saveDefaultConfig();
    Msg.set(msgAccessor.getConfig());
    //loading GuildItems
    if (dataAccessor.getConfig().isList("RequiredItemsForGuild"))
      guildItems = ((List<ItemStack>) dataAccessor.getConfig().getList("RequiredItemsForGuild")).toArray(new ItemStack[0]);
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
            if (fc.isItemStack("inventories."+name+".icon"))
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
    if (new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Events").exists()) {
      for (File file : new File(plugin.getDataFolder().getAbsolutePath() + File.separatorChar + "Events").listFiles()) {
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        String gn = file.getName();
        gn = gn.substring(0, gn.length() - 4); //remove the .yml
        EventGroup group = new EventGroup(this, gn, fc.getString("displayname"));
        group.setDelay(fc.getInt("delay"));
        group.setStopCondition(StopCondition.valueOf(fc.getString("stopcondition")));
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
    int warpsCount = Warp.loadWarps(plugin);
    Bukkit.getLogger().info(Msg.get("console_loaded", true, Integer.toString(warpsCount), Integer.toString(teamsCount), Integer.toString(groupsCount)));
  }
}

package com.gmail.ZiomuuSs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import com.gmail.ZiomuuSs.Utils.CountdownTimer;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.SavedPlayer;

public class EventTeam {
  public static enum TeamStatus {
    LOBBY, DISABLED, IN_PROGRESS; //lobby - players are in lobby and waiting to teleport to startpoints, disabled - no one is in team, in_progress - players were teleported to ther startpoints
  }
  private Data data;
  private String name;
  private ItemStack[] inv;
  private TeamStatus status; //status of team
  private HashMap<String, ItemStack[]> inventories; //can pick inventory from an event
  private HashMap<String, ItemStack> inventoryIcons; //Icon of /\ when player can pick it.
  private Team team; //scoreboard team of EventTeam... this is getting ridiculous
  private Location lobby; //lobby of an event
  private ArrayList<Location> startPoints = new ArrayList<>(); //list of start point of event
  private int maxPlayers = 0; //0 - unlimited
  private int delay;
  private HashMap<UUID, SavedPlayer> savedPlayers = new HashMap<>();
  
  public EventTeam (String name, Data data) {
    team = Bukkit.getScoreboardManager().getNewScoreboard().registerNewTeam(name);
    this.name = name;
    this.data = data;
  }
  
  public void start(String displayName) {
    CountdownTimer timer = new CountdownTimer(data.getPlugin(), delay,
        () -> broadcastToMembers(Msg.get("event_start_broadcast", true, displayName, Integer.toString(delay))), () -> {
          broadcastToMembers(Msg.get("event_started", true));
          int index = 0;
          for (UUID uuid : savedPlayers.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
              player.teleport(startPoints.get(index));
              if (index == startPoints.size()-1)
                index = 0;
              else
                ++index;
            }
          }
        },
        (t) -> {
          if (t.getSecondsLeft() <= 5)
            broadcastToMembers(Msg.get("event_before_teleport", true, Integer.toString(t.getSecondsLeft())));
        }
        );
    timer.scheduleTimer();
  }
  
  public void broadcastToMembers(String string) {
    for (UUID uuid : savedPlayers.keySet()) {
      Bukkit.getPlayer(uuid).sendMessage(string);
    }
  }
  
  //returns false if ff was switched to false
  //return true if ff was switched to true
  public boolean switchFriendlyFire() {
    if (getFriendlyFire()) {
      team.setAllowFriendlyFire(false);
      return false;
    } else {
      team.setAllowFriendlyFire(true);
      return true;
    }
  }
  
  public TeamStatus getTeamStatus() {
    return status;
  }
  
  //same as ff
  public boolean switchNametagVisibility() {
    if (getNametagVisibility()) {
      team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
      return false;
    } else {
      team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
      return true;
    }
  }
  
  public void addPlayer(Player player) {
    savedPlayers.put(player.getUniqueId(), new SavedPlayer(player, lobby, data, team));
  }
  
  public void delPlayer(Player player) {
    savedPlayers.get(player.getUniqueId()).restore();
    savedPlayers.remove(player.getUniqueId());
  }
  
  //getters
  @Override
  public String toString() {
    return name;
  }
  
  public String getPrettyPlayerList() {
    String list = "";
    for (UUID uuid : savedPlayers.keySet()) {
      list += Bukkit.getOfflinePlayer(uuid).getName()+", ";
    }
    if (!list.equalsIgnoreCase(""))
      return list.substring(0, list.length() - 2);
    else
      return Msg.get("none", false);
  }
  
  public String getPrettyInventoryList() {
    String list = "";
    for (String string : inventories.keySet()) {
      list += string+", ";
    }
    if (!list.equalsIgnoreCase(""))
      return list.substring(0, list.length() - 2);
    else
      return Msg.get("none", false);
  }
 
  public int getPlayerNumber() {
    return savedPlayers.size();
  }
  
  public boolean getFriendlyFire() {
    return team.allowFriendlyFire();
  }
  
  public boolean getNametagVisibility() {
    return team.getOption(Team.Option.NAME_TAG_VISIBILITY).equals(Team.OptionStatus.ALWAYS);
  }
  
  public Set<String> getInventories() {
    return inventories.keySet();
  }
  
  public ItemStack[] getInventory(String invname) {
    return inventories.get(invname);
  }
  
  public ItemStack getInventoryIcon(String invname) {
    return inventoryIcons.get(invname);
  }
  
  public Location getLobby() {
    return lobby;
  }
  
  public int getMaxPlayers() {
    return maxPlayers;
  }
  
  public ArrayList<Location> getStartPoints() {
    return startPoints;
  }
  
  public SavedPlayer getPlayer(Player player) {
    return savedPlayers.get(player.getUniqueId());
  }
  
  //checkers
  public boolean isReady() {
    return (lobby != null && !startPoints.isEmpty() && inv != null);
  }
  
  public boolean isSaved(UUID uuid) {
    if (savedPlayers.containsKey(uuid))
      return true;
    else
      return false;
  }
  
  public boolean isInventory(String inv) {
    return inventories.containsKey(inv);
  }
  
  //setters
  //returns true if value was set
  //or false if value was changed
  public boolean setMaxPlayers(int mp) {
    if (maxPlayers>0) {
      maxPlayers = mp;
      return false;
    } else {
      maxPlayers = mp;
      return true;
    }
  }
  
  public boolean setInventory(String name, ItemStack[] inv) {
    if (inventories.containsKey(name)) {
      inventories.put(name, inv);
      return true;
    } else {
      inventories.put(name, inv);
      return false;
    }
  }
  
  public boolean setInventoryIcon(String name, ItemStack it) {
    if (inventoryIcons.containsKey(name)) {
      inventoryIcons.put(name, it);
      return true;
    } else {
      inventoryIcons.put(name, it);
      return false;
    }
  }
  
  public boolean setLobby(Location lobby) {
    if (this.lobby != null) {
      this.lobby = lobby;
      return false;
    } else {
      this.lobby = lobby;
      return true;
    }
  }
  
  public boolean setStartPoints(Location loc, int index) {
    if (index > 0) {
      --index; //(most) humans counts from 1, Java from 0
      if (startPoints.size()>= index+1) {
        startPoints.set(index, loc);
        return true;
      } else {
        startPoints.add(loc);
        return false;
      }
    } else {
      startPoints.add(loc);
      return false;
    }
  }
  
  public void removeInventory(String inv) {
    inventories.remove(inv);
    inventoryIcons.remove(inv);
  }
  
  public void addInventory(String name, ItemStack[] inv) {
    inventories.put(name, inv);
  }
}

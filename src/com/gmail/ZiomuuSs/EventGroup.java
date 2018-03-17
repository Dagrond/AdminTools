package com.gmail.ZiomuuSs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Utils.CountdownTimer;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class EventGroup {
  public static enum EventStatus {
    COUNTDOWN, DISABLED, IN_PROGRESS; //countdown - event is open for new players to join, disabled - no one is in event, in_progress - has already started(so, its closed for new players to join)
  }
  public static enum StopCondition { //condition to stop group.
    LAST_PLAYER, LAST_TEAM; //there will be more... soon
  }
  private Data data;
  private StopCondition stop = StopCondition.LAST_PLAYER; //last player by default
  private EventStatus status; //status of this event
  private HashSet<UUID> waitingPlayers = new HashSet<>(); //players queued to join event
  private HashMap<String, EventTeam> teams = new HashMap<>(); //teams that this event contains
  private HashSet<UUID> spectators = new HashSet<>(); //players that are spectating this event
  private HashSet<UUID> savedPlayers = new HashSet<>(); //all saved players, for performance or smth I am not even sure anymore
  private String name; //name of this event for operators
  private String displayName; //display name for players
  private int min = 2; //min players needed to start event
  private int delay = 60;
  private boolean cancelled = false;
  private Location spec;
  
  public EventGroup(Data data, String name, String displayName) {
    this.data = data;
    this.name = name;
    this.displayName = displayName;
  }
  
  public void start() {
    CountdownTimer timer = new CountdownTimer(data.getPlugin(), delay,
        () -> {
          Bukkit.broadcastMessage(Msg.get("event_start", true, displayName, Integer.toString(delay)));
          status = EventStatus.COUNTDOWN;
          for (EventTeam team : teams.values()) {
            team.createGui();
          }
        },
         () -> {
          Bukkit.broadcastMessage(Msg.get("event_nojoin", true, displayName));
          for (EventTeam team : teams.values()) {
            team.start(displayName);
          }
          status = EventStatus.IN_PROGRESS;
          if (savedPlayers.size() < min) {
            Bukkit.broadcastMessage(Msg.get("event_error_not_enough_players", true, displayName));
            data.stop();
          }
          },
        (t) -> {
          if (cancelled) {
            t.cancel();
            data.stop();
          }
          if (t.getSecondsLeft() == delay/2)
            Bukkit.broadcastMessage(Msg.get("event_start", true, displayName, Integer.toString(t.getSecondsLeft())));
          else if (t.getSecondsLeft() == 10)
            Bukkit.broadcastMessage(Msg.get("event_start", true, displayName, Integer.toString(t.getSecondsLeft())));
        });
    timer.scheduleTimer();
  }
  
  public void stop() {
    //todo
    status = EventStatus.DISABLED;
    for (EventTeam team : teams.values()) {
      team.stop();
    }
    savedPlayers.clear();
    cancelled = false;
  }
  
  public void setCancelled() {
    Bukkit.broadcastMessage(Msg.get("event_error_cancelled", true, displayName));
    if (status == EventStatus.COUNTDOWN)
      cancelled = true;
    else
      data.stop();
  }
  
  //getters
  
  public int getMinPlayers() {
    return min;
  }
  
  public HashSet<UUID> getWaitingPlayers() {
    return waitingPlayers;
  }
  
  public StopCondition getStopCondition() {
    return stop;
  }
  
  public EventStatus getEventStatus() {
    return status;
  }
  
  public int getSpectatorCount() {
    return spectators.size();
  }
  
  public int getParticipantCount() {
    return savedPlayers.size();
  }
  
  public HashSet<UUID> getSavedPlayers() {
    return savedPlayers;
  }
  
  public EventTeam getTeamByPlayer(Player player) {
    for (EventTeam team : teams.values()) {
      if (team.isSaved(player.getUniqueId()))
        return team;
    }
    return null;
  }
  
  public String getDisplayName() {
    return displayName;
  }
  
  public HashMap<String, EventTeam> getTeams() {
    return teams;
  }
  
  public Location getSpecLocation() {
    return spec;
  }
  
  public int getDelay() {
    return delay;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  public String getPrettyTeamList() {
    String list = "";
    for (String team : teams.keySet()) {
      list += team+", ";
    }
    if (!list.equalsIgnoreCase(""))
      return list.substring(0, list.length() - 2);
    else
      return Msg.get("none", false);
  }
  
  public String getPrettyParticipantList() {
    String list = "";
    for (UUID uuid : savedPlayers) {
      list += Bukkit.getOfflinePlayer(uuid).getName()+", ";
    }
    if (!list.equalsIgnoreCase(""))
      return list.substring(0, list.length() - 2);
    else
      return Msg.get("none", false);
  }
  
  public String getPrettySpectatorsList() {
    String list = "";
    for (UUID uuid : spectators) {
      list += Bukkit.getOfflinePlayer(uuid).getName()+", ";
    }
    if (!list.equalsIgnoreCase(""))
      return list.substring(0, list.length() - 2);
    else
      return Msg.get("none", false);
  }
  
  //checkers
  public boolean isReady() {
    if (!teams.isEmpty()) {
      for (EventTeam team : teams.values()) {
        if (!team.isReady())
          return false;
      }
      return true;
    } else
      return false;
  }
  
  public boolean isSaved(UUID uuid) {
    return savedPlayers.contains(uuid);
  }
  
  public boolean isQueued(UUID uuid) {
    return waitingPlayers.contains(uuid);
  }
  
  //checks maxplayers of current teams
  //and decides, if another player can join
  public boolean CanJoin() {
    for (EventTeam team : teams.values()) {
      if (team.getMaxPlayers() > 0 && !(team.getMaxPlayers() > team.getPlayerNumber()))
        return false;
    }
    return true;
  }
  
  //setters
  
  public void setStopCondition (StopCondition c) {
    stop = c;
  }
  
  public void setMinPlayers(int min) {
    this.min = min;
  }
  
  public void setDelay(int delay) {
    this.delay = delay;
  }
  
  public void addTeam(EventTeam team) {
    teams.put(team.toString(), team);
  }
  
  public boolean setSpectatorsLocation(Location loc) {
    if (spec == null) {
      spec = loc;
      return false;
    } else {
      spec = loc;
      return true;
    }
  }
  
  public void removePlayer(Player player) {
    getTeamByPlayer(player).delPlayer(player);
    savedPlayers.remove(player.getUniqueId());
    if (stop == StopCondition.LAST_PLAYER && savedPlayers.size() == 1) {
      Bukkit.broadcastMessage(Msg.get("event_winner", true, displayName, Bukkit.getPlayer(savedPlayers.toArray(new UUID[1])[0]).getDisplayName()));
      data.stop();
    } else if (stop == StopCondition.LAST_TEAM) {
      int activeTeams = 0;
      for (EventTeam team : teams.values()) {
        if (team.getPlayerNumber() == 0)
          ++activeTeams;
      }
      if (activeTeams == 1) {
        EventTeam team = teams.values().toArray(new EventTeam[1])[0];
        String list = "";
        for (UUID uuid : team.getOriginalPlayers()) {
          list += Bukkit.getOfflinePlayer(uuid).getName()+", ";
        }
        if (!list.equalsIgnoreCase(""))
          list = list.substring(0, list.length() - 2);
        else
          list = Msg.get("none", false);
        Bukkit.broadcastMessage(Msg.get("event_team_winner", true, displayName, team.toString(), list));
        data.stop();
      }
    }
  }
  
  public void broadcastToEvent(String msg) {
    for (UUID uuid : spectators) {
      Bukkit.getPlayer(uuid).sendMessage(msg);
    }
    for (UUID uuid : savedPlayers) {
      Bukkit.getPlayer(uuid).sendMessage(msg);
    }
  }
  
  public void addSpectator(Player player) {
    player.teleport(spec);
    player.setGameMode(GameMode.SPECTATOR);
  }
  
  public void kickSpectator (Player player) {
    spectators.remove(player.getUniqueId());
    player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    player.setGameMode(GameMode.SURVIVAL);
  }
  
  //return true if player was added to event
  //or false if player was queued to event
  public boolean addPlayer(Player player) {
    if (teams.size() == 1) {
      //for 1 team
      for (EventTeam team : teams.values())
        team.addPlayer(player);
      savedPlayers.add(player.getUniqueId());
      return true;
    } else {
      //for more teams
      //checking if all queued players are online
      for (UUID uuid : waitingPlayers) {
        if (Bukkit.getPlayer(uuid) == null)
          waitingPlayers.remove(uuid);
      }
      //checking if there's team balance
      int i = -1;
      for (EventTeam team : teams.values()) {
        if (i > -1 && team.getPlayerNumber() < i) {
          team.addPlayer(player);
          return true;
        } else
          i = team.getPlayerNumber();
      }
      waitingPlayers.add(player.getUniqueId());
      if (teams.size() == waitingPlayers.size()) {
        for (EventTeam team : teams.values()) {
          Player pl = Bukkit.getPlayer(waitingPlayers.toArray(new UUID[waitingPlayers.size()])[0]);
          team.addPlayer(pl);
          savedPlayers.add(pl.getUniqueId());
          waitingPlayers.remove(pl.getUniqueId());
        }
      }
      return false;
    }
  }
}

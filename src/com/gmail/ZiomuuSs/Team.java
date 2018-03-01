package com.gmail.ZiomuuSs;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gmail.ZiomuuSs.Utils.SavedPlayer;

public class Team {
  protected String name;
  protected Inventory inv;
  protected Location loc;
  protected HashMap<UUID, SavedPlayer> savedPlayers = new HashMap<>();
  
  public Team (String name) {
    this.name = name;
  }
  
  public void setInventory(Inventory inv) {
    this.inv = inv;
  }
  
  public void setLocation(Location loc) {
    this.loc = loc;
  }
  
  public boolean isSaved(UUID uuid) {
    if (savedPlayers.containsKey(uuid))
      return true;
    else
      return false;
  }
  
  public SavedPlayer getPlayer(Player player) {
    return savedPlayers.get(player.getUniqueId());
  }
  
  public void addPlayer(Player player) {
    savedPlayers.put(player.getUniqueId(), new SavedPlayer(player, loc, inv));
  }
  
  public void delPlayer(Player player) {
    savedPlayers.get(player.getUniqueId()).restore();
    savedPlayers.remove(player.getUniqueId());
  }
  
}

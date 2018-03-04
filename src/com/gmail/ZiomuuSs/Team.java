package com.gmail.ZiomuuSs;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.SavedPlayer;

public class Team {
  protected Data data;
  protected String name;
  protected ItemStack[] inv;
  protected Location loc;
  protected HashMap<UUID, SavedPlayer> savedPlayers = new HashMap<>();
  
  public Team (String name, Data data) {
    this.name = name;
    this.data = data;
  }
  
  public Team (String name, ItemStack[] inv, Location loc, Data data) {
    this.name = name;
    this.inv = inv;
    this.loc = loc;
    this.data = data;
  }
  
  public void setInventory(Inventory inv) {
    this.inv = inv.getContents();
  }
  
  public void setLocation(Location loc) {
    this.loc = loc;
  }
  
  public ItemStack[] getInventory() {
    return inv;
  }
  
  public Location getLocation() {
    return loc;
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
    data.addSavedPlayer(player.getUniqueId());
  }
  
  public int getPlayerNumber() {
    return savedPlayers.size();
  }
  
  public void delPlayer(Player player) {
    savedPlayers.get(player.getUniqueId()).restore();
    savedPlayers.remove(player.getUniqueId());
    data.removeSavedPlayer(player.getUniqueId());
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
  
  @Override
  public String toString() {
    return name;
  }
  
}

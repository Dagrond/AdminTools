package com.gmail.ZiomuuSs.Utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SavedPlayer {
  
  UUID uuid;
  protected int lvl;
  protected Inventory inv;
  protected Location loc;
  
  public SavedPlayer(Player player, Location eloc, Inventory einv) {
    uuid = player.getUniqueId();
    inv = player.getInventory();
    loc = player.getLocation();
    lvl = player.getLevel();
    
    player.setLevel(0);
    player.getInventory().setContents(einv.getContents());
    player.updateInventory();
    player.teleport(eloc);
  }
  
  public void restore() {
    Player player = Bukkit.getPlayer(uuid);
    player.getInventory().setContents(inv.getContents());
    player.updateInventory();
    player.setLevel(lvl);
    player.teleport(loc);
  }
  
}

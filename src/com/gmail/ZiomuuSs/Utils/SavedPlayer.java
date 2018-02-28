package com.gmail.ZiomuuSs.Utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SavedPlayer {
  
  Player player;
  protected int lvl;
  protected Inventory inv;
  protected Location loc;
  
  public SavedPlayer(Player player, Location eloc, Inventory einv) {
    this.player = player;
    inv = player.getInventory();
    loc = player.getLocation();
    lvl = player.getLevel();
    
    player.setLevel(0);
    player.getInventory().setContents(einv.getContents());
    player.teleport(eloc);
  }
  
  public void restore() {
    player.getInventory().setContents(inv.getContents());
    player.setLevel(lvl);
    player.teleport(loc);
  }
  
}

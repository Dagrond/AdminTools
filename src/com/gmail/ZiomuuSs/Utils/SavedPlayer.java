package com.gmail.ZiomuuSs.Utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SavedPlayer {
  
  UUID uuid;
  protected int lvl;
  protected float xp;
  protected ItemStack[] inv;
  protected Location loc;
  
  public SavedPlayer(Player player, Location eloc, ItemStack[] it) {
    uuid = player.getUniqueId();
    inv = player.getInventory().getContents();
    loc = player.getLocation();
    lvl = player.getLevel();
    xp = player.getExp();
    
    player.setLevel(0);
    player.setExp(0);
    player.getInventory().setContents(it);
    player.updateInventory();
    player.teleport(eloc);
  }
  
  public void restore() {
    Player player = Bukkit.getPlayer(uuid);
    player.getInventory().setContents(inv);
    player.updateInventory();
    player.setLevel(lvl);
    player.setExp(xp);
    player.teleport(loc);
  }
  
}

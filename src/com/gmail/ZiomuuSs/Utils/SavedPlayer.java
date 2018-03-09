package com.gmail.ZiomuuSs.Utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SavedPlayer {
  
  UUID uuid;
  Data data;
  protected int lvl;
  protected float xp;
  protected ItemStack[] inv;
  protected Location loc;
  
  @SuppressWarnings("deprecation")
  public SavedPlayer(Player player, Location eloc, ItemStack[] it, Data data) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams join nonametag "+player.getName());
    inv = player.getInventory().getContents();
    uuid = player.getUniqueId();
    loc = player.getLocation();
    lvl = player.getLevel();
    xp = player.getExp();
    this.data = data;
    
    player.setLevel(0);
    player.setExp(0);
    player.getActivePotionEffects().clear();
    player.setHealth(player.getMaxHealth());
    player.getInventory().setContents(it);
    player.updateInventory();
    player.teleport(eloc);
  }
  
  public void restore() {
    Player player = Bukkit.getPlayer(uuid);
    if (!player.isDead()) {
      player.getInventory().setContents(inv);
      player.updateInventory();
      player.setLevel(lvl);
      player.setExp(xp);
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams leave "+player.getName());
      player.teleport(loc);
    } else {
      if (!data.isToRestore(uuid))
        data.addPlayerToRestore(uuid, this);
    }
  }
  
}

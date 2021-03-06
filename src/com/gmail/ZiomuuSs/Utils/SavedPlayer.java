package com.gmail.ZiomuuSs.Utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

public class SavedPlayer {
  
  UUID uuid;
  Data data;
  protected int lvl;
  protected float xp;
  protected ItemStack[] inv;
  protected Team team;
  protected Location loc;
  
  public SavedPlayer(Player player, Location eloc, Data data, Team team) {
    inv = player.getInventory().getContents();
    uuid = player.getUniqueId();
    loc = player.getLocation();
    lvl = player.getLevel();
    team.addEntry(player.getName());
    xp = player.getExp();
    player.getInventory().clear();
    player.updateInventory();
    this.team = team;
    this.data = data;
    
    player.getActivePotionEffects().clear();
    player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    player.teleport(eloc);
    //anti escape because of plugins with teleport delay that works badly (they are not considering teleport as move)
    player.setVelocity(player.getLocation().getDirection().multiply(1.01));
    player.setLevel(0);
    player.setExp(0);
  }
  
  public void restore() {
    Player player = Bukkit.getPlayer(uuid);
    if (!player.isDead()) {
      player.getInventory().setContents(inv);
      team.removeEntry(player.getName());
      player.updateInventory();
      player.teleport(loc);
      player.setLevel(lvl);
      player.setExp(xp);
      player.closeInventory();
    } else {
      if (!data.isToRestore(uuid))
        data.addPlayerToRestore(uuid, this);
    }
  }
  
}

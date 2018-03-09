package com.gmail.ZiomuuSs.Events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.gmail.ZiomuuSs.Utils.Data;

public class OnDamageEvent implements Listener {
  public Data data;
  
  public OnDamageEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onDeath(EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile)) {
      Player victim = (Player) e.getEntity();
      Player attacker;
      if (e.getDamager() instanceof Player)
        attacker = (Player) e.getDamager();
      else {
        ProjectileSource ps = ((Projectile) e.getDamager()).getShooter();
        if (ps instanceof Player)
          attacker = (Player) ps;
        else
          return;
      }
      if (data.isSaved(victim.getUniqueId()) && data.isSaved(attacker.getUniqueId())) {
        if (data.getTeamByPlayer(victim).equals(data.getTeamByPlayer(attacker)) && !data.getTeamByPlayer(victim).getFriendlyFire()) {
          e.setCancelled(true);
        }
      }
    }
  }
  
}

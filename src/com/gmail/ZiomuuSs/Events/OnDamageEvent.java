package com.gmail.ZiomuuSs.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gmail.ZiomuuSs.Utils.Data;

public class OnDamageEvent implements Listener {
  public Data data;
  
  public OnDamageEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onDeath(EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
      Player victim = (Player) e.getEntity();
      Player attacker = (Player) e.getDamager();
      if (data.isSaved(victim.getUniqueId()) && data.isSaved(attacker.getUniqueId())) {
        if (data.getTeamByPlayer(victim).equals(data.getTeamByPlayer(attacker)) && !data.getTeamByPlayer(victim).getFriendlyFire()) {
          e.setCancelled(true);
        }
      }
    }
  }
  
}

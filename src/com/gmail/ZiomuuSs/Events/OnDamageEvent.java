package com.gmail.ZiomuuSs.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.gmail.ZiomuuSs.EventTeam.TeamStatus;
import com.gmail.ZiomuuSs.Utils.Data;

public class OnDamageEvent implements Listener {
  public Data data;
  
  public OnDamageEvent (Data data) {
    this.data = data;
  }
  
  //check if player is in lobby of an event. If so, cancel damage
  @EventHandler
  public void onDamage(EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof Player && data.isSaved(e.getEntity().getUniqueId()) && data.getTeamByPlayer((Player) e.getEntity()).getTeamStatus() == TeamStatus.LOBBY) {
      e.setCancelled(true);
    }
  }
  
}

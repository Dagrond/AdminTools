package com.gmail.ZiomuuSs.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.gmail.ZiomuuSs.EventGroup.EventStatus;
import com.gmail.ZiomuuSs.EventTeam.TeamStatus;
import com.gmail.ZiomuuSs.Utils.Data;

public class OnDamageEvent implements Listener {
  public Data data;
  
  public OnDamageEvent (Data data) {
    this.data = data;
  }
  
  //check if player is in lobby of an event. If so, cancel damage
  @EventHandler
  public void onDamage(EntityDamageEvent e) {
    if (e.getEntity() instanceof Player && data.getCurrentEvent().isSaved(e.getEntity().getUniqueId()) && (data.getCurrentEvent().getTeamByPlayer(((Player) e.getEntity())).getTeamStatus() == TeamStatus.LOBBY || data.getCurrentEvent().getEventStatus() == EventStatus.COUNTDOWN)) {
      e.setCancelled(true);
    }
  }
  
}

package com.gmail.ZiomuuSs.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.gmail.ZiomuuSs.EventGroup.EventStatus;
import com.gmail.ZiomuuSs.EventTeam.TeamStatus;
import com.gmail.ZiomuuSs.Utils.Data;

public class OnDropEvent implements Listener {
  public Data data;
  
  public OnDropEvent (Data data) {
    this.data = data;
  }
  
  //check if player is in lobby of an event. If so, cancel damage
  @EventHandler
  public void onDrop(PlayerDropItemEvent e) {
    if (data.getCurrentEvent().isSaved(e.getPlayer().getUniqueId()) && (data.getCurrentEvent().getTeamByPlayer(e.getPlayer()).getTeamStatus() == TeamStatus.LOBBY || data.getCurrentEvent().getEventStatus() == EventStatus.COUNTDOWN)) {
      e.setCancelled(true);
    }
  }
  
}

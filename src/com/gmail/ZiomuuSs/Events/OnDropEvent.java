package com.gmail.ZiomuuSs.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.gmail.ZiomuuSs.EventGroup.EventStatus;
import com.gmail.ZiomuuSs.EventTeam.TeamStatus;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnDropEvent implements Listener {
  public Data data;
  
  public OnDropEvent (Data data) {
    this.data = data;
  }
  
  //check if player is in lobby of an event. If so, cancel damage
  @EventHandler
  public void onDrop(PlayerDropItemEvent e) {
    //cancel drop when players is in event lobby
    if (data.getCurrentEvent() != null && data.getCurrentEvent().isSaved(e.getPlayer().getUniqueId())
        && (data.getCurrentEvent().getTeamByPlayer(e.getPlayer()).getTeamStatus() == TeamStatus.LOBBY
        || data.getCurrentEvent().getEventStatus() == EventStatus.COUNTDOWN)) {
      e.setCancelled(true);
    }
    
    if (!e.getPlayer().getWorld().getName().equals("world")) {
      Material m = e.getItemDrop().getItemStack().getType();
      if (m == Material.EMERALD || m == Material.EMERALD_BLOCK || m == Material.EMERALD_ORE) {
        e.getPlayer().sendMessage(Msg.get("error_currency_world", false));
        e.setCancelled(true);
      }
    }
  }
  
}

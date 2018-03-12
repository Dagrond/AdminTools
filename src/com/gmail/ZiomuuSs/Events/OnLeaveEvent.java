package com.gmail.ZiomuuSs.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnLeaveEvent implements Listener {
  public Data data;
  
  public OnLeaveEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onLeave(PlayerQuitEvent e) {
    if (data.getCurrentEvent().isSaved(e.getPlayer().getUniqueId())) {
      data.getCurrentEvent().removePlayer(e.getPlayer());
      Bukkit.broadcastMessage(Msg.get("event_leaved", true, e.getPlayer().getDisplayName(), Integer.toString(data.getCurrentEvent().getParticipantCount())));
    }
  }
  
}

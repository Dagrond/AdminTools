package com.gmail.ZiomuuSs.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnCommandEvent implements Listener {
  public Data data;
  
  public OnCommandEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onCommand(PlayerCommandPreprocessEvent e) {
    if (data.getCurrentEvent().isSaved(e.getPlayer().getUniqueId()) && !e.getPlayer().isOp()) {
      if (!e.getMessage().toLowerCase().startsWith("/e ") && !e.getMessage().toLowerCase().startsWith("/event ")) {
        e.getPlayer().sendMessage(Msg.get("event_error_command", false));
        e.setCancelled(true);
      }
    }
  }
  
}

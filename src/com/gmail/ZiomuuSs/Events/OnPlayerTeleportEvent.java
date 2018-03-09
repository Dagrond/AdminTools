package com.gmail.ZiomuuSs.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnPlayerTeleportEvent implements Listener {
  public Data data;
  
  public OnPlayerTeleportEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onTeleport(PlayerTeleportEvent e) {
    Player player = e.getPlayer();
    if (data.getFuckBack().containsKey(player.getUniqueId())) {
      if (e.getTo().equals(data.getFuckBack().get(player.getUniqueId()))) {
        player.sendMessage(Msg.get("event_error_fuck_back", true));
        e.setCancelled(true);
      }
    }
  }
  
}

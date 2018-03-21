package com.gmail.ZiomuuSs.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.gmail.ZiomuuSs.Utils.Data;

public class RespawnEvent implements Listener {
  public Data data;
  
  public RespawnEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onRespawn(PlayerRespawnEvent e) {
    if (data.isToRestore(e.getPlayer().getUniqueId())) {
      data.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(data.getPlugin(), new Runnable() {
        @Override
        public void run() {
          data.restorePlayer(e.getPlayer().getUniqueId());
        }
      }, 10);
    }
  }
  
}

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
    //if player has permissions and weren't in event, try to restore it's equipment
    } else if (e.getPlayer().hasPermission("AdminTools.keepinventory")) {
      if (data.getKeepInventory().containsKey(e.getPlayer().getUniqueId())) {
        data.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(data.getPlugin(), new Runnable() {
          @Override
          public void run() {
            e.getPlayer().getInventory().setContents(data.getKeepInventory().get(e.getPlayer().getUniqueId()));
            e.getPlayer().updateInventory();
            data.getKeepInventory().remove(e.getPlayer().getUniqueId());
          }
        }, 10);
      }
    }
  }
  
}

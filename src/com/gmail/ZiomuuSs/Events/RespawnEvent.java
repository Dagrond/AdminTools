package com.gmail.ZiomuuSs.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.gmail.ZiomuuSs.Main;

public class RespawnEvent implements Listener {
  public Main plugin;
  
  public RespawnEvent (Main instance) {
    plugin = instance;
  }
  
  @EventHandler
  public void onRespawn(PlayerRespawnEvent e) {
    if (plugin.getData().isSaved(e.getPlayer().getUniqueId())) {   
      plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
        @Override
        public void run() {
          plugin.getData().getTeamByPlayer(e.getPlayer()).delPlayer(e.getPlayer());
        }
      }, 10);
    }
  }
  
}

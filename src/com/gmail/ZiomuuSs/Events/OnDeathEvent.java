package com.gmail.ZiomuuSs.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnDeathEvent implements Listener {
  public Main plugin;
  
  public OnDeathEvent (Main instance) {
    plugin = instance;
  }
  
  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
    if (plugin.getData().isSaved(e.getEntity().getUniqueId())) {   
      if (e.getEntity().getKiller() instanceof Player) {
        Bukkit.broadcastMessage(Msg.get("event_killer", false, e.getEntity().getName(), e.getEntity().getKiller().getName()));
      } else if (e.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) {
        Bukkit.broadcastMessage(Msg.get("event_spleef_fall", false, e.getEntity().getName(), Integer.toString(plugin.getData().getTeamByPlayer(e.getEntity()).getPlayerNumber())));
      }
    }
  }
  
}

package com.gmail.ZiomuuSs.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gmail.ZiomuuSs.Team;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnDeathEvent implements Listener {
  public Data data;
  
  public OnDeathEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
    if (data.isSaved(e.getEntity().getUniqueId())) {   
      Team team = data.getTeamByPlayer(e.getEntity());
      data.removePlayer(e.getEntity());
      if (e.getEntity().getKiller() instanceof Player) {
        Bukkit.broadcastMessage(Msg.get("event_killer", true, e.getEntity().getName(), e.getEntity().getKiller().getName(), Integer.toString(data.getSavedPlayersCount())));
      } else if (e.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) {
        Bukkit.broadcastMessage(Msg.get("event_spleef_fall", true, e.getEntity().getName(), Integer.toString(team.getPlayerNumber())));
      }
    }
  }
  
}

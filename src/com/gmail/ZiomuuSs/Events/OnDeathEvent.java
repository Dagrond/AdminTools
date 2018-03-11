package com.gmail.ZiomuuSs.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.gmail.ZiomuuSs.EventTeam;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnDeathEvent implements Listener {
  private Data data;
  
  public OnDeathEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
    if (data.isSaved(e.getEntity().getUniqueId())) {
      EventTeam team = data.getTeamByPlayer(e.getEntity());
      data.removePlayer(e.getEntity());
      e.getDrops().clear();
      e.setDroppedExp(0);
      if (e.getEntity().getKiller() instanceof Player) {
        Bukkit.broadcastMessage(Msg.get("event_killer", true, e.getEntity().getName(), e.getEntity().getKiller().getName(), Integer.toString(data.getSavedPlayersCount())));
      } else if (e.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) {
        Bukkit.broadcastMessage(Msg.get("event_spleef_fall", true, e.getEntity().getName(), Integer.toString(team.getPlayerNumber())));
      }
    //keep inventory of players with permission if they were not in event and were not killed by player
    } else if (e.getEntity().hasPermission("AdminTools.keepinventory")) {
      if (!(e.getEntity().getKiller() instanceof Player)) {
        if (!(e.getEntity().getKiller() instanceof Projectile)) {
          e.getDrops().clear();
          data.getKeepInventory().put(e.getEntity().getUniqueId(), e.getEntity().getInventory().getContents());
        } else if (!(((Projectile) e.getEntity().getKiller()).getShooter() instanceof Player)) {
          e.getDrops().clear();
          data.getKeepInventory().put(e.getEntity().getUniqueId(), e.getEntity().getInventory().getContents());
        }
      }
    }
  }
  
}

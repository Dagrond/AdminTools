package com.gmail.ZiomuuSs.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
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
    if (data.getCurrentEvent() != null && data.getCurrentEvent().isSaved(e.getEntity().getUniqueId())) {
      EventTeam team = data.getCurrentEvent().getTeamByPlayer(e.getEntity());
      data.getCurrentEvent().removePlayer(e.getEntity());
      e.getDrops().clear();
      e.setDroppedExp(0);
      if (e.getEntity().getKiller() instanceof Player) {
        Bukkit.broadcastMessage(Msg.get("event_killer", true, e.getEntity().getName(), e.getEntity().getKiller().getName(), Integer.toString(data.getCurrentEvent().getParticipantCount())));
      } else if (e.getEntity().getLastDamageCause().getCause() == DamageCause.VOID) {
        Bukkit.broadcastMessage(Msg.get("event_spleef_fall", true, e.getEntity().getName(), Integer.toString(team.getPlayerNumber())));
      }
    } else if (e.getEntity().hasPermission("AdminTools.keepinventory")) {
      Entity killer = e.getEntity().getKiller();
      if (!(killer instanceof Player) && (!(killer instanceof Projectile) || !(((Projectile) killer).getShooter() instanceof Player))) {
        RespawnEvent.getSavedInvs().put(e.getEntity(), e.getEntity().getInventory().getContents());
        e.getDrops().clear();
      }
    }
  }
  
}

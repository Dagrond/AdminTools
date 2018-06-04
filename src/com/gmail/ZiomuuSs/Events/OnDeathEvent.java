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
    } else if (e.getEntity().hasPermission("AdminTools.keepinventory") || e.getEntity().hasPermission("AdminTools.keepexperience")) {
      Entity killer = e.getEntity().getKiller();
      if (!(killer instanceof Player) && (!(killer instanceof Projectile) || !(((Projectile) killer).getShooter() instanceof Player)) && !e.getEntity().getWorld().getName().equals("b")) {
        if (e.getEntity().hasPermission("AdminTools.keepinventory")) {
          e.setKeepInventory(true);
          e.getEntity().sendMessage(Msg.get("inventory_restored", false));
        }
        if (e.getEntity().hasPermission("AdminTools.keepexperience")) {
          e.setKeepLevel(true);
          e.setDroppedExp(0);
          e.getEntity().sendMessage(Msg.get("experience_restored", false));
        }
      } else {
        if (e.getEntity().hasPermission("AdminTools.keepinventory")) {
          e.getEntity().sendMessage(Msg.get("error_inventory_not_restored", false));
        }
        if (e.getEntity().hasPermission("AdminTools.keepexperience")) {
          e.getEntity().sendMessage(Msg.get("error_experience_not_restored", false));
        }
      }
    }
  }
  
}

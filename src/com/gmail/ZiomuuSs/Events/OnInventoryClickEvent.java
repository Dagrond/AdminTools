package com.gmail.ZiomuuSs.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.gmail.ZiomuuSs.EventTeam;
import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.Kowal;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.Warp;

public class OnInventoryClickEvent implements Listener {
  public Main plugin;
  
  public OnInventoryClickEvent (Main plugin) {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent  e) {
    if (e.getInventory().getName().equals(Msg.get("class_choose_inventory", false))) {
      Player player = (Player) e.getWhoClicked();
      EventTeam team = plugin.getData().getCurrentEvent().getTeamByPlayer(player);
      team.setPlayerInventoryByIcon(e.getCurrentItem(), player);
      e.setCancelled(true);
    } else if (e.getInventory().getName().equals(Msg.get("warp_choose_inventory", false))) {
      Warp warp = Warp.getWarps().get(e.getCurrentItem());
      if (warp != null) warp.teleport((Player) e.getWhoClicked());
      e.setCancelled(true);
    } else if (e.getInventory().getName().equals("§9§lKowal:")) {
      Kowal kowal = Kowal.currentPlayers.get(e.getWhoClicked().getUniqueId());
        Bukkit.getScheduler().runTaskLater(plugin,
          () -> kowal.UpdateInventory(), 1);
      if (e.getClickedInventory() != null && e.getClickedInventory().getName().equals("§9§lKowal:")) {
        if (e.getRawSlot() != 31) {
          if (e.getRawSlot() == 41)
            kowal.PlayerAction((Player) e.getWhoClicked(), e.getInventory().getItem(41));
          e.setCancelled(true);
        }
      }
    }
    String inv = e.getWhoClicked().getOpenInventory().getTopInventory().getName();
    if (inv.equals(Msg.get("class_choose_inventory", false)) || inv.equals(Msg.get("warp_choose_inventory", false))) {
      e.setCancelled(true);
    }
  }
  
}

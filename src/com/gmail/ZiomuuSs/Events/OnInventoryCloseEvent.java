package com.gmail.ZiomuuSs.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Kowal;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnInventoryCloseEvent implements Listener {
  public Data data;
  
  public OnInventoryCloseEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent e) {
    if (data.getCurrentEvent() != null && e.getInventory().getName().equals(Msg.get("class_choose_inventory", false))) {
      data.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(data.getPlugin(), new Runnable() {
        @Override
        public void run() {
          if (data.getCurrentEvent() != null && data.getCurrentEvent().isSaved(e.getPlayer().getUniqueId()) && !data.getCurrentEvent().getTeamByPlayer((Player) e.getPlayer()).hasChosenInventory(e.getPlayer().getUniqueId())) {
            e.getPlayer().openInventory(e.getInventory());
            e.getPlayer().sendMessage(Msg.get("event_error_choose_inventory", true));
          }
        }
      }, 1);
    } else if (Kowal.currentPlayers.containsKey(e.getPlayer().getUniqueId())) {
      Inventory inv = e.getInventory();
      if (inv.getItem(31) != null) e.getPlayer().getInventory().addItem(inv.getItem(31));
      Kowal.currentPlayers.remove(e.getPlayer().getUniqueId());
    }
  }
  
}

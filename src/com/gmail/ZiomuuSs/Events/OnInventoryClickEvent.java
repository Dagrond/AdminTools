package com.gmail.ZiomuuSs.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.gmail.ZiomuuSs.EventTeam;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnInventoryClickEvent implements Listener {
  public Data data;
  
  public OnInventoryClickEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onInventoryClose(InventoryClickEvent  e) {
    if (e.getInventory().getName().equals(Msg.get("class_choose_inventory", false))) {
      Player player = (Player) e.getWhoClicked();
      EventTeam team = data.getCurrentEvent().getTeamByPlayer(player);
      team.setPlayerInventoryByIcon(e.getCurrentItem(), player);
      e.setCancelled(true);
    }
  }
  
}

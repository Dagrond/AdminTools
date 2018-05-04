package com.gmail.ZiomuuSs.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import com.gmail.ZiomuuSs.Utils.Msg;

public class OnInventoryMoveItemEvent implements Listener {
  
  public OnInventoryMoveItemEvent () {}
  
  @EventHandler
  public void onInventoryMoveItemEvent(InventoryMoveItemEvent  e) {
    if (e.getDestination().getName().equals(Msg.get("class_choose_inventory", false))) {
      e.setCancelled(true);
    } else if (e.getDestination().getName().equals(Msg.get("warp_choose_inventory", false))) {
      e.setCancelled(true);
    }
  }
  
}

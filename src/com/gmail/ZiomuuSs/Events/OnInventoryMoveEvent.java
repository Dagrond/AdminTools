package com.gmail.ZiomuuSs.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class OnInventoryMoveEvent implements Listener {
  
  public OnInventoryMoveEvent () {
  }
  
  @EventHandler
  public void OnInventoryMove(InventoryMoveItemEvent  e) {
    Material m = e.getItem().getType();
    if (m == Material.EMERALD || m == Material.EMERALD_BLOCK || m == Material.EMERALD_ORE) {
      String name = e.getSource().getLocation().getWorld().getName();
      if (!name.equals("world") && !name.equals("b") || e.getDestination().getType() == InventoryType.SHULKER_BOX) {
        e.setItem(new ItemStack(Material.AIR));
      }
    }
  }
}

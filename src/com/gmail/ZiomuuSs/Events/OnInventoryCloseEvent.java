package com.gmail.ZiomuuSs.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class OnInventoryCloseEvent implements Listener {
  public Data data;
  
  public OnInventoryCloseEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onInventoryClose(InventoryCloseEvent e) {
    //todo
    if (e.getInventory().getName().equals(Msg.get("class_choose_inventory", false))) {
      
    }
  }
  
}

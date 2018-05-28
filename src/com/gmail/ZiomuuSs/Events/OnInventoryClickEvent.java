package com.gmail.ZiomuuSs.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import com.gmail.ZiomuuSs.EventTeam;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.Warp;

public class OnInventoryClickEvent implements Listener {
  public Data data;
  
  public OnInventoryClickEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent  e) {
    if (e.getInventory().getName().equals(Msg.get("class_choose_inventory", false))) {
      Player player = (Player) e.getWhoClicked();
      EventTeam team = data.getCurrentEvent().getTeamByPlayer(player);
      team.setPlayerInventoryByIcon(e.getCurrentItem(), player);
      e.setCancelled(true);
    } else if (e.getInventory().getName().equals(Msg.get("warp_choose_inventory", false))) {
      Warp warp = Warp.getWarps().get(e.getCurrentItem());
      if (warp != null) warp.teleport((Player) e.getWhoClicked());
      e.setCancelled(true);
    }
    String inv = e.getWhoClicked().getOpenInventory().getTopInventory().getName();
    if (inv.equals(Msg.get("class_choose_inventory", false)) || inv.equals(Msg.get("warp_choose_inventory", false))) {
      e.setCancelled(true);
    }
    
    if (e.getCurrentItem() != null) {
      Material m = e.getCurrentItem().getType();
      if (m == Material.EMERALD || m == Material.EMERALD_BLOCK || m == Material.EMERALD_ORE) {
        InventoryType type = e.getWhoClicked().getOpenInventory().getTopInventory().getType();
        if (type == InventoryType.ENDER_CHEST || type == InventoryType.SHULKER_BOX) {
          e.getWhoClicked().sendMessage(Msg.get("error_currency_hide", false));
          e.setCancelled(true);
        }
        InventoryType invt = e.getInventory().getType();
        String name = e.getWhoClicked().getWorld().getName();
        if ((!name.equals("world") && !name.equals("b")) && (invt == InventoryType.CHEST || invt == InventoryType.DROPPER || invt == InventoryType.DISPENSER || invt == InventoryType.HOPPER) && !e.getClickedInventory().getName().equals(Msg.get("warp_choose_inventory", false))) {
          e.getWhoClicked().sendMessage(Msg.get("error_currency_world", false));
          e.setCancelled(true);
        }
      }
    }
  }
  
}

package com.gmail.ZiomuuSs.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

import net.redstoneore.legacyfactions.event.EventFactionsCreate;

public class OnFactionCreateEvent implements Listener {
  
  private Data data;
  
  public OnFactionCreateEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onFactionCreate(EventFactionsCreate e) {
    if (data.getItemsForGuild() != null && !e.getFPlayer().getPlayer().hasPermission("AdminTools.GuildCreateBypass")) {
      for (ItemStack item : data.getItemsForGuild()) {
        if (item != null && !e.getFPlayer().getPlayer().getInventory().containsAtLeast(item, item.getAmount())) {
          e.getFPlayer().getPlayer().sendMessage(Msg.get("error_no_items", false));
          e.setCancelled(true);
          return;
        }
      }
      for (ItemStack item : data.getItemsForGuild()) {
        if (item != null)
          e.getFPlayer().getPlayer().getInventory().removeItem(item);
      }
      e.getFPlayer().getPlayer().updateInventory();
    } else if (e.getFPlayer().getPlayer().hasPermission("AdminTools.GuildCreateBypass") ) {
      e.getFPlayer().getPlayer().sendMessage(Msg.get("items_not_needed", false));
    }
  }
  
}

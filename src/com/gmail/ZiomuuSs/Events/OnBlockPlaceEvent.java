package com.gmail.ZiomuuSs.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.gmail.ZiomuuSs.Utils.Msg;

public class OnBlockPlaceEvent implements Listener {
  
  public OnBlockPlaceEvent () {}
  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {
    if (e.getPlayer().getWorld().getName().equals("dzialki")) {
      Material m = e.getBlock().getType();
      if (m == Material.EMERALD_ORE || m == Material.EMERALD_BLOCK) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(Msg.get("error_currency_world", false));
      }
    }
  }
  
}

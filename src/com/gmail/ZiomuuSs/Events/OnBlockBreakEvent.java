package com.gmail.ZiomuuSs.Events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.gmail.ZiomuuSs.Utils.Msg;

public class OnBlockBreakEvent implements Listener {
  
  public OnBlockBreakEvent () {}
  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    if (e.getPlayer().getWorld().getName().equals("world")) {
      Material m = e.getBlock().getType();
      if (m == Material.EMERALD_ORE || m == Material.DIAMOND_ORE || m == Material.LAPIS_ORE || m == Material.GOLD_ORE || m == Material.IRON_ORE || m == Material.COAL_ORE || m == Material.REDSTONE_ORE) {
        e.setDropItems(false);
        e.setExpToDrop(0);
        e.getPlayer().sendMessage(Msg.get("error_ore_world", false));
      }
    }
  }
  
}

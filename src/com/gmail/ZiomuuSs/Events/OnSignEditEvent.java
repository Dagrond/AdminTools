package com.gmail.ZiomuuSs.Events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.gmail.ZiomuuSs.Utils.Msg;

public class OnSignEditEvent implements Listener {
  
  public OnSignEditEvent () {}
  
  @EventHandler
  public void onSignChange(SignChangeEvent e) {
    if (!e.getBlock().getWorld().getName().equals("world")) {
      if (e.getLine(0).equalsIgnoreCase(ChatColor.BOLD+"[vault]")) {
        e.getPlayer().sendMessage(Msg.get("error_currency_world", false));
        e.setCancelled(true);
      }
    }
  }
  
}

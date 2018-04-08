package com.gmail.ZiomuuSs.Events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class RespawnEvent implements Listener {
  public Data data;
  public static HashMap<Player, ItemStack[]> savedInvs = new HashMap<>();
  
  public RespawnEvent (Data data) {
    this.data = data;
  }
  
  @EventHandler
  public void onRespawn(PlayerRespawnEvent e) {
    if (data.isToRestore(e.getPlayer().getUniqueId())) {
      data.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(data.getPlugin(), new Runnable() {
        @Override
        public void run() {
          data.restorePlayer(e.getPlayer().getUniqueId());
        }
      }, 10);
    } else if (savedInvs.containsKey(e.getPlayer())) {
      data.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(data.getPlugin(), new Runnable() {
        @Override
        public void run() {
          e.getPlayer().getInventory().setContents(savedInvs.get(e.getPlayer()));
          savedInvs.remove(e.getPlayer());
          e.getPlayer().sendMessage(Msg.get("inventory_restored", false));
        }
      }, 10);
    }
  }
  
  
  public static HashMap<Player, ItemStack[]> getSavedInvs() {
    return savedInvs;
  }
}

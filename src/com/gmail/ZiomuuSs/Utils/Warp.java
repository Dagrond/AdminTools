package com.gmail.ZiomuuSs.Utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.ZiomuuSs.Main;

public class Warp {
  private static HashMap<ItemStack, Warp> warps = new HashMap<>(); //list of all warps
  private static HashMap<Player, Location> playersInProgress = new HashMap<>(); //players that are currently teleporting
  private static Inventory WarpInv = Bukkit.createInventory(null, 54, Msg.get("warp_choose_inventory", false));; //inventory of warps
  //loading warps
  static {
    //todo
  }
  
  private Main plugin;
  private Location location;
  private String name;
  private int index;
  
  public Warp (Location loc, String name, ItemStack item, int index, Main plugin) {
    this.location = loc;
    this.name = name;
    this.index = index-1;
    this.plugin = plugin;
    warps.put(item, this);
    WarpInv.setItem(this.index, item);
  }
  
  public void teleport(Player player) {
    player.closeInventory();
    CountdownTimer timer = new CountdownTimer(plugin, 5,
        () -> {
          player.sendMessage(Msg.get("warp_delay", false, name, "5"));
          playersInProgress.put(player, player.getLocation());
        },
         () -> {
          //todo
           player.sendMessage(Msg.get("warp_teleported", false, name));
           player.teleport(location);
           playersInProgress.remove(player);
          },
        (t) -> {
          //todo
          if (player.isOnline()) {
            if (player.getLocation().distance(playersInProgress.get(player)) < 0.1) {
              player.sendMessage(Msg.get("error_warp_cancelled", false));
              playersInProgress.remove(player);
              t.cancel();
            }
          } else {
            playersInProgress.remove(player);
            t.cancel();
          }
        });
    timer.scheduleTimer();
  }
  
  public static void saveWarps() {
    //todo
  }
  
  public static boolean isInProgress(Player player) {
    return (playersInProgress.containsKey(player));
  }
  
  //getters
  @Override
  public String toString() {
    return name;
  }
  
  public int getIndex() {
    return index;
  }
  
  public Location getLoc() {
    return location;
  }
  
  public static HashMap<ItemStack, Warp> getWarps() {
    return warps;
  }
  
  public static void showWarps(Player player) {
    player.openInventory(WarpInv);
  }
  
}

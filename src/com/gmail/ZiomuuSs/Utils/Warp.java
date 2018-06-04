package com.gmail.ZiomuuSs.Utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.ZiomuuSs.Main;

import net.milkbowl.vault.economy.Economy;

public class Warp {
  private static HashMap<ItemStack, Warp> warps = new HashMap<>(); //list of all warps by item
  private static HashMap<String, Warp> Stringwarps = new HashMap<>(); //list of all warps by String
  private static ConfigAccessor warpAccessor;
  private static HashMap<Player, Location> playersInProgress = new HashMap<>(); //players that are currently teleporting
  private static Inventory WarpInv = Bukkit.createInventory(null, 54, Msg.get("warp_choose_inventory", false)); //inventory of warps
  private static ItemStack emptySlot = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
  static {
    ItemMeta meta = emptySlot.getItemMeta();
    meta.setDisplayName(" ");
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    emptySlot.setItemMeta(meta);
  }
  
  private Main plugin;
  private Location location;
  private ItemStack item;
  private String name;
  private double price = 0;
  private int index;
  
  public Warp (Location loc, String name, ItemStack item, int index, double price, Main plugin) {
    this.location = loc;
    this.name = name;
    this.index = index;
    this.plugin = plugin;
    this.item = item;
    if (price > 0) this.price = price;
    warps.put(item, this);
    Stringwarps.put(name, this);
    WarpInv.setItem(this.index, item);
    saveWarps();
  }
  
  public void setLocation(Location loc) {
    this.location = loc;
    saveWarps();
  }
  
  public static int loadWarps(Main instance) {
    WarpInv = Bukkit.createInventory(null, 54, Msg.get("warp_choose_inventory", false));
    warpAccessor = new ConfigAccessor(instance, "Warps.yml");
    warpAccessor.saveDefaultConfig();
    int loaded = 0;
    ConfigurationSection w = warpAccessor.getConfig();
    if (w.isConfigurationSection("warp")) {
      for (String warp : w.getConfigurationSection("warp").getKeys(false)) {
        ItemStack it = w.getItemStack("warp."+warp+".item");
        warps.put(it, new Warp(new Location(Bukkit.getWorld(w.getString("warp."+warp+".world")),
            w.getDouble("warp."+warp+".x"), w.getDouble("warp."+warp+".y"), w.getDouble("warp."+warp+".z"), (float) w.getDouble("warp."+warp+".yaw"),
            (float) w.getDouble("warp."+warp+".pitch")), warp, it, w.getInt("warp."+warp+".index"), w.getDouble("warp."+warp+".price"), instance));
        ++loaded;
      }
    }
    while(WarpInv.firstEmpty() != -1) {
      WarpInv.setItem(WarpInv.firstEmpty(), emptySlot);
    }
    return loaded;
  }
  
  public void teleport(Player player) {
    player.closeInventory();
    player.updateInventory();
    if (player.isOp()) {
      if (name.equals("losowy"))
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rtp "+player.getName()+" world");
      else if (name.equals("surowcowa"))
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rtp "+player.getName()+" surowcowa");
      else {
        player.teleport(location);
        player.sendMessage(Msg.get("warp_teleported", false, name));
      }
    } else {
      Economy e = plugin.getEconomy();
      if (price <= 0 || (e != null && e.getBalance(player) >= price)) {
        CountdownTimer timer = new CountdownTimer(plugin, 5,
            () -> {
              player.sendMessage(Msg.get("warp_delay", false, name, "5"));
              playersInProgress.put(player, player.getLocation());
            },
             () -> {
               if (price <= 0 || (e != null && e.getBalance(player) >= price)) {
                 if (name.equals("losowy"))
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rtp "+player.getName()+" world");
                 else if (name.equals("surowcowa"))
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rtp "+player.getName()+" surowcowa");
                 else {
                   player.teleport(location);
                   if (price > 0) {
                     e.withdrawPlayer(player, price);
                     player.sendMessage(Msg.get("warp_charged", false, Double.toString(price), name));
                   } else {
                     player.sendMessage(Msg.get("warp_teleported", false, name));
                   }
                 }
                 playersInProgress.remove(player);
               } else {
                 player.sendMessage(Msg.get("error_warp_not_enough_money", false, name, Double.toString(price)));
               }
              },
            (t) -> {
              if (player.isOnline()) {
                if (!isSimilarLocation(player.getLocation(), playersInProgress.get(player))) {
                  player.sendMessage(Msg.get("error_warp_cancelled", false));
                  playersInProgress.remove(player);
                  e.depositPlayer(player, price);
                  t.cancel();
                }
              } else {
                playersInProgress.remove(player);
                t.cancel();
              }
            });
        timer.scheduleTimer();
      } else {
        player.sendMessage(Msg.get("error_warp_not_enough_money", false, name, Double.toString(price)));
      }
    }
  }
  
  public boolean isSimilarLocation(Location loc1, Location loc2) {
    return (loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ());
  }
  
  public static void saveWarps() {
    ConfigurationSection w = warpAccessor.getConfig();
    for (String warp : Stringwarps.keySet()) {
      w.set("warp."+warp+".x", Stringwarps.get(warp).getLoc().getX());
      w.set("warp."+warp+".y", Stringwarps.get(warp).getLoc().getY());
      w.set("warp."+warp+".z", Stringwarps.get(warp).getLoc().getZ());
      w.set("warp."+warp+".yaw", Stringwarps.get(warp).getLoc().getYaw());
      w.set("warp."+warp+".pitch", Stringwarps.get(warp).getLoc().getPitch());
      w.set("warp."+warp+".world", Stringwarps.get(warp).getLoc().getWorld().getName());
      w.set("warp."+warp+".world", Stringwarps.get(warp).getLoc().getWorld().getName());
      w.set("warp."+warp+".item", Stringwarps.get(warp).getItem());
      w.set("warp."+warp+".index", Stringwarps.get(warp).getIndex());
      w.set("warp."+warp+".price", Stringwarps.get(warp).getPrice());
    }
    warpAccessor.saveConfig();
  }
  
  public static boolean isInProgress(Player player) {
    return (playersInProgress.containsKey(player));
  }
  
  public void delete() {
    warps.remove(item);
    Stringwarps.remove(name);
    saveWarps();
    warpAccessor.getConfig().set("warp."+name, null);
    warpAccessor.saveConfig();
    loadWarps(plugin);
  }
  
  //getters
  @Override
  public String toString() {
    return name;
  }
  
  public double getPrice() {
    return price;
  }
  
  public ItemStack getItem() {
    return item;
  }
  
  public int getIndex() {
    return index;
  }
  
  public static Warp getWarpByString(String name) {
    return Stringwarps.get(name);
  }
  
  public static HashMap<String, Warp> getStringWarps() {
    return Stringwarps;
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

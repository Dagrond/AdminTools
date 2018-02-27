package com.gmail.ZiomuuSs.Utils;

import java.util.HashMap;

import org.bukkit.Location;

import com.gmail.ZiomuuSs.Main;

public class Data {
  protected Main plugin;
  protected ConfigAccessor msgAccessor;
  protected ConfigAccessor warpAccessor;
  protected HashMap<String, Location> warps = new HashMap<>();
  
  public Data(Main plugin) {
    this.plugin = plugin;
    load();
  }
  
  protected void load() {
    msgAccessor = new ConfigAccessor(plugin, "Messages.yml");
    warpAccessor = new ConfigAccessor(plugin, "Warps.yml");
    msgAccessor.saveDefaultConfig();
    Msg.set(msgAccessor.getConfig());
  }
  
  //return true if warp was added, return false if warp was edited
  public boolean setWarp(String name, Location loc) {
    if (warps.containsKey(name)) {
      warps.put(name, loc);
      return false;
    } else {
      warps.put(name, loc);
      return true;
    }
  }
  
  public void save() {
    //todo
  }
}

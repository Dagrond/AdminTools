package com.gmail.ZiomuuSs;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.ZiomuuSs.Commands.AdminToolsCommand;
import com.gmail.ZiomuuSs.Commands.EventPlayerCommand;
import com.gmail.ZiomuuSs.Commands.ClearChatCommand;
import com.gmail.ZiomuuSs.Events.OnCommandEvent;
import com.gmail.ZiomuuSs.Events.OnDamageEvent;
import com.gmail.ZiomuuSs.Events.OnDeathEvent;
import com.gmail.ZiomuuSs.Events.RespawnEvent;
import com.gmail.ZiomuuSs.Utils.Data;

public final class Main extends JavaPlugin {
  protected Data data;
  
  public void onEnable() {
    data = new Data(this);
    getCommand("AdminTools").setExecutor(new AdminToolsCommand(this));
    getCommand("ClearChat").setExecutor(new ClearChatCommand(this));
    getCommand("Event").setExecutor(new EventPlayerCommand(this));
    getServer().getPluginManager().registerEvents(new RespawnEvent(this), this);
    getServer().getPluginManager().registerEvents(new OnDeathEvent(this), this);
    getServer().getPluginManager().registerEvents(new OnCommandEvent(this), this);
    getServer().getPluginManager().registerEvents(new OnDamageEvent(data), this);
  }
  
  public void onDisable() {
    //?
  }
  
  public Data getData() {
    return data;
  }
}

package com.gmail.ZiomuuSs;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.ZiomuuSs.Commands.AdminToolsCommand;
import com.gmail.ZiomuuSs.Commands.EventPlayerCommand;
import com.gmail.ZiomuuSs.Commands.ClearChatCommand;
import com.gmail.ZiomuuSs.Events.OnCommandEvent;
import com.gmail.ZiomuuSs.Events.OnDamageEvent;
import com.gmail.ZiomuuSs.Events.OnDeathEvent;
import com.gmail.ZiomuuSs.Events.OnLeaveEvent;
import com.gmail.ZiomuuSs.Events.RespawnEvent;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public final class Main extends JavaPlugin {
  protected Data data;
  
  public void onEnable() {
    data = new Data(this);
    getCommand("AdminTools").setExecutor(new AdminToolsCommand(this));
    getCommand("ClearChat").setExecutor(new ClearChatCommand(this));
    getCommand("Event").setExecutor(new EventPlayerCommand(data));
    getServer().getPluginManager().registerEvents(new RespawnEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnLeaveEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnDeathEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnCommandEvent(this), this);
    getServer().getPluginManager().registerEvents(new OnDamageEvent(data), this);
  }
  
  public void onDisable() {
    //?
  }
  
  public void reload(CommandSender sender) {
    HandlerList.unregisterAll(this);
    data = new Data(this);
    getCommand("AdminTools").setExecutor(new AdminToolsCommand(this));
    getCommand("ClearChat").setExecutor(new ClearChatCommand(this));
    getCommand("Event").setExecutor(new EventPlayerCommand(data));
    getServer().getPluginManager().registerEvents(new RespawnEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnLeaveEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnDeathEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnCommandEvent(this), this);
    getServer().getPluginManager().registerEvents(new OnDamageEvent(data), this);
    sender.sendMessage(Msg.get("reloaded", true));
  }
  
  public Data getData() {
    return data;
  }
}

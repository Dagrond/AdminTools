package com.gmail.ZiomuuSs;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.ZiomuuSs.Commands.AdminToolsCommand;
import com.gmail.ZiomuuSs.Commands.EventPlayerCommand;
import com.gmail.ZiomuuSs.Commands.ClearChatCommand;
import com.gmail.ZiomuuSs.Events.OnDeathEvent;
import com.gmail.ZiomuuSs.Events.RespawnEvent;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public final class Main extends JavaPlugin {
  protected Data data;
  
  public void onEnable() {
    reload(null);
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
    getServer().getPluginManager().registerEvents(new OnDeathEvent(data), this);
    if (sender != null)
      sender.sendMessage(Msg.get("reloaded", true));
  }
  
  public Data getData() {
    return data;
  }
}

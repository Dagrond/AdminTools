package com.gmail.ZiomuuSs;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.ZiomuuSs.Commands.AdminToolsCommand;
import com.gmail.ZiomuuSs.Commands.ChujeWypusccieMnieCommand;
import com.gmail.ZiomuuSs.Commands.ClearChatCommand;
import com.gmail.ZiomuuSs.Events.RespawnEvent;
import com.gmail.ZiomuuSs.Utils.Data;

public final class Main extends JavaPlugin {
  protected Data data;
  
  public void onEnable() {
    data = new Data(this);
    getCommand("AdminTools").setExecutor(new AdminToolsCommand(this));
    getCommand("ClearChat").setExecutor(new ClearChatCommand(this));
    getCommand("ChujeWypusccieMnie").setExecutor(new ChujeWypusccieMnieCommand(this));
    getServer().getPluginManager().registerEvents(new RespawnEvent(this), this);
  }
  
  public void onDisable() {
    //?
  }
  
  public Data getData() {
    return data;
  }
}

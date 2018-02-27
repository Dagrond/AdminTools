package com.gmail.ZiomuuSs;

import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.ZiomuuSs.Commands.AdminToolsCommand;
import com.gmail.ZiomuuSs.Commands.ClearChatCommand;
import com.gmail.ZiomuuSs.Utils.ConfigAccessor;
import com.gmail.ZiomuuSs.Utils.Data;

public final class Main extends JavaPlugin {
  ConfigAccessor warpAccessor;
  protected Data data;
  
  public void onEnable() {
    data = new Data(this);
    getCommand("AdminTools").setExecutor(new AdminToolsCommand(this));
    getCommand("ClearChat").setExecutor(new ClearChatCommand(this));
    //?
  }
  
  public void onDisable() {
    //?
  }
  
  public Data getData() {
    return data;
  }
}

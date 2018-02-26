package com.gmail.ZiomuuSs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.ZiomuuSs.Commands.AdminToolsCommand;
import com.gmail.ZiomuuSs.Commands.ClearChatCommand;
import com.gmail.ZiomuuSs.Utils.ConfigAccessor;
import com.gmail.ZiomuuSs.Utils.Msg;

public final class Main extends JavaPlugin {
  ConfigAccessor warpAccessor;
  public void onEnable() {
    ConfigAccessor msgAccessor = new ConfigAccessor(this, "Messages.yml");
    warpAccessor = new ConfigAccessor(this, "Data.yml");
    msgAccessor.saveDefaultConfig();
    Msg.set(msgAccessor.getConfig());
    getCommand("AdminTools").setExecutor(new AdminToolsCommand(this));
    getCommand("ClearChat").setExecutor(new ClearChatCommand(this));
    //?
  }
  
  public void onDisable() {
    //?
  }
  
  public ConfigurationSection getData() {
    return warpAccessor.getConfig();
  }
}

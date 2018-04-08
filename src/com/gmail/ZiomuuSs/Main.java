package com.gmail.ZiomuuSs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.ZiomuuSs.Commands.AdminToolsCommand;
import com.gmail.ZiomuuSs.Commands.EventPlayerCommand;
import com.gmail.ZiomuuSs.Commands.SimpleCommands;
import com.gmail.ZiomuuSs.Events.OnDeathEvent;
import com.gmail.ZiomuuSs.Events.OnFactionCreateEvent;
import com.gmail.ZiomuuSs.Events.RespawnEvent;
import com.gmail.ZiomuuSs.Commands.ClearChatCommand;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

import net.redstoneore.legacyfactions.Factions;

public final class Main extends JavaPlugin {
  private Data data;
  
  public void onEnable() {
    reload(null);
    if (Bukkit.getPluginManager().isPluginEnabled("LegacyFactions")) {
      Factions.get().register(new OnFactionCreateEvent(data));
   }
  }
  
  public void onDisable() {
    //?
  }
  
  public void reload(CommandSender sender) {
    HandlerList.unregisterAll(this);
    data = new Data(this);
    getServer().getPluginManager().registerEvents(new OnDeathEvent(data), this);
    getServer().getPluginManager().registerEvents(new RespawnEvent(data), this);
    getCommand("AdminTools").setExecutor(new AdminToolsCommand(this));
    getCommand("ClearChat").setExecutor(new ClearChatCommand(this));
    getCommand("Event").setExecutor(new EventPlayerCommand(data));
    SimpleCommands cmds = new SimpleCommands(this);
    getCommand("vip").setExecutor(cmds);
    getCommand("komendy").setExecutor(cmds);
    getCommand("drop").setExecutor(cmds);
    getCommand("pomoc").setExecutor(cmds);
    if (sender != null)
      sender.sendMessage(Msg.get("reloaded", true));
  }
  
  public Data getData() {
    return data;
  }
}

package com.gmail.ZiomuuSs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

import com.gmail.ZiomuuSs.Commands.AdminToolsCommand;
import com.gmail.ZiomuuSs.Commands.EventPlayerCommand;
import com.gmail.ZiomuuSs.Commands.SetWarpCommand;
import com.gmail.ZiomuuSs.Commands.SimpleCommands;
import com.gmail.ZiomuuSs.Commands.WarpCommand;
import com.gmail.ZiomuuSs.Commands.WarpListCommand;
import com.gmail.ZiomuuSs.Events.OnBlockPlaceEvent;
import com.gmail.ZiomuuSs.Events.OnDeathEvent;
import com.gmail.ZiomuuSs.Events.OnDropEvent;
import com.gmail.ZiomuuSs.Events.OnFactionCreateEvent;
import com.gmail.ZiomuuSs.Events.OnInventoryClickEvent;
import com.gmail.ZiomuuSs.Events.OnInventoryMoveEvent;
import com.gmail.ZiomuuSs.Events.RespawnEvent;
import com.gmail.ZiomuuSs.Commands.ClearChatCommand;
import com.gmail.ZiomuuSs.Commands.DelWarpCommand;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

import net.redstoneore.legacyfactions.Factions;

public final class Main extends JavaPlugin {
  private Data data;
  private Economy econ;
  
  public void onEnable() {
    reload(null);
    if (Bukkit.getPluginManager().isPluginEnabled("LegacyFactions")) {
      Factions.get().register(new OnFactionCreateEvent(data));
    }
    if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
      econ = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
      getLogger().info(Msg.get("hooked_into", true, "Vault"));
    }
  }
  
  public void onDisable() {
    //?
  }
  
  public void reload(CommandSender sender) {
    HandlerList.unregisterAll(this);
    data = new Data(this, econ);
    getServer().getPluginManager().registerEvents(new OnDeathEvent(data), this);
    getServer().getPluginManager().registerEvents(new RespawnEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnInventoryClickEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnDropEvent(data), this);
    getServer().getPluginManager().registerEvents(new OnBlockPlaceEvent(), this);
    getServer().getPluginManager().registerEvents(new OnInventoryMoveEvent(), this);
    getCommand("AdminTools").setExecutor(new AdminToolsCommand(this));
    getCommand("ClearChat").setExecutor(new ClearChatCommand(this));
    getCommand("Event").setExecutor(new EventPlayerCommand(data));
    getCommand("SetWarp").setExecutor(new SetWarpCommand(data));
    getCommand("DelWarp").setExecutor(new DelWarpCommand(data));
    getCommand("WarpList").setExecutor(new WarpListCommand(data));
    getCommand("Warp").setExecutor(new WarpCommand(data));
    SimpleCommands cmds = new SimpleCommands(this);
    getCommand("vip").setExecutor(cmds);
    getCommand("komendy").setExecutor(cmds);
    getCommand("drop").setExecutor(cmds);
    getCommand("pomoc").setExecutor(cmds);
    if (sender != null)
      sender.sendMessage(Msg.get("reloaded", true));
  }
  
  public Economy getEconomy() {
    return econ;
  }
  
  public Data getData() {
    return data;
  }
}

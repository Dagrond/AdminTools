package com.gmail.ZiomuuSs.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Main;

public class SimpleCommands implements CommandExecutor {
  Main plugin;
  
  public SimpleCommands(Main instance) {
    plugin = instance;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("vip")) {
      if (sender instanceof Player)
        ((Player) sender).performCommand("help vip");
    } else if (cmd.getName().equalsIgnoreCase("supervip") || cmd.getName().equalsIgnoreCase("svip")) {
      if (sender instanceof Player)
        ((Player) sender).performCommand("help svip");
    } else if (cmd.getName().equalsIgnoreCase("drop")) {
      ((Player) sender).sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lNa naszym serwerze drop jest z rud!"));
    } else if (cmd.getName().equalsIgnoreCase("pomoc")) {
      if (sender instanceof Player)
        ((Player) sender).performCommand("help");
    } else if (cmd.getName().equalsIgnoreCase("komendy")) {
      if (sender instanceof Player)
        ((Player) sender).performCommand("help komendy");
    }
    return true;
  }
}
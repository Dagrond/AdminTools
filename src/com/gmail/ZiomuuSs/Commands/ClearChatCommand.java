package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.Msg;

public class ClearChatCommand implements CommandExecutor {
  Main plugin;
  
  public ClearChatCommand(Main instance) {
    plugin = instance;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("ClearChat") || cmd.getName().equalsIgnoreCase("cc")) {
      if (sender.hasPermission("AdminTools.ClearChat") || sender.hasPermission("AdminTools.*")) {
        for (Player player : Bukkit.getOnlinePlayers()) {
          for (int i = 100; i >= 0; --i)
            player.sendMessage("");
        }
        Bukkit.broadcastMessage(Msg.get("chat_cleared", false, (sender instanceof Player ? sender.getName() : "Konsole")));
        return true;
      } else {
        sender.sendMessage(Msg.get("error_permission", true));
        return true;
      }
    }
    return true;
  }
}
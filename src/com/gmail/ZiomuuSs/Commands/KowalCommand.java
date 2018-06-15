package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.Kowal;
import com.gmail.ZiomuuSs.Utils.Msg;

public class KowalCommand implements CommandExecutor {
  Main plugin;
  
  public KowalCommand(Main instance) {
    plugin = instance;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("Kowal")) {
      if (sender.hasPermission("AdminTools.Kowal") || sender.hasPermission("AdminTools.*")) {
        if (args.length > 0) {
          Player player = Bukkit.getPlayer(args[0]);
          if (player != null) {
            player.closeInventory();
            new Kowal(plugin, player);
            return true;
          } else {
            sender.sendMessage(Msg.get("error_player_not_exist", true, args[0]));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_usage", true, "/kowal <player>"));
          return true;
        }
      } else {
        sender.sendMessage(Msg.get("error_permission", true));
        return true;
      }
    }
    return true;
  }
}
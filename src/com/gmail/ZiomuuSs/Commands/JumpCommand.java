package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.Msg;

public class JumpCommand implements CommandExecutor {
  Main plugin;
  
  public JumpCommand(Main instance) {
    plugin = instance;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("Jump")) {
      if (sender.hasPermission("AdminTools.Jump") || sender.hasPermission("AdminTools.*")) {
        if (args.length > 0) {
          Player player = Bukkit.getPlayer(args[0]);
          if (player != null) {
            Location loc = player.getLocation();
            loc.setPitch(0);
            Vector v = loc.getDirection().multiply(3);
            v.setY(v.getY()+0.4);
            player.setVelocity(v);
            return true;
          } else {
            sender.sendMessage(Msg.get("error_player_not_exist", true, args[0]));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_usage", true, "/jump <player>"));
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
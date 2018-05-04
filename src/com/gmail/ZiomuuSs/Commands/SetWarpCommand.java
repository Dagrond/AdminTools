package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.Warp;

public class SetWarpCommand implements CommandExecutor {
  Data data;
  
  public SetWarpCommand(Data data) {
    this.data = data;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("SetWarp")) { // /setwarp (name) (slot)
      if (sender instanceof Player) {
        if (sender.hasPermission("AdminTools.setwarp") || sender.hasPermission("AdminTools.*")) {
          if (args.length>1) {
            if (args[1].matches("-?\\d+")) {
              int index = Integer.valueOf(args[1]);
              if (index>0) {
                Player player = (Player) sender;
                if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                  new Warp(player.getLocation(), args[0], player.getInventory().getItemInMainHand(), index, data.getPlugin());
                  player.sendMessage(Msg.get("warp_created", true, args[0]));
                  return true;
                } else {
                  sender.sendMessage(Msg.get("error_warp_item_needed", true));
                  return true;
                }
              } else {
                sender.sendMessage(Msg.get("error_must_be_integer", true, "index"));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_must_be_integer", true, "index"));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_usage", true, "/setwarp (name) (index)"));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_permission", true));
          return true;
        }
      } else {
        sender.sendMessage(Msg.get("error_player_needed", true));
        return true;
      }
    }
    return true;
  }
}
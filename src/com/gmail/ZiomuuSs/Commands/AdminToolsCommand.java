package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class AdminToolsCommand implements CommandExecutor {
  Main plugin;
  Data data;
  
  public AdminToolsCommand(Main instance) {
    plugin = instance;
    data = plugin.getData();
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("AdminTools") || cmd.getName().equalsIgnoreCase("at") || cmd.getName().equalsIgnoreCase("admin")) {
      if (args.length > 0) {
        if (args[0].equalsIgnoreCase("help")) {
          if (sender.hasPermission("AdminTools.help") || sender.hasPermission("AdminTools.*")) {
            sender.sendMessage(Msg.get("help", false));
            sender.sendMessage(Msg.get("help_1", false));
            sender.sendMessage(Msg.get("help_2", false));
            sender.sendMessage(Msg.get("help_3", false));
            sender.sendMessage(Msg.get("help_4", false));
            sender.sendMessage(Msg.get("help_5", false));
            sender.sendMessage(Msg.get("help_6", false));
            sender.sendMessage(Msg.get("help_7", false));
            return true;
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("mining")) {
          if (sender.hasPermission("AdminTools.mining") || sender.hasPermission("AdminTools.*")) {
            String players = "";
            for (Player player : Bukkit.getOnlinePlayers()) {
              if (player.getLocation().getY() <= 20) {
                players += player.getDisplayName()+", ";
              }
            }
            sender.sendMessage(Msg.get("player_mining", true, !(players.equals("")) ? players.substring(0, players.length() - 2) : Msg.get("none", false)));
            return true;
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("cc") || args[0].equalsIgnoreCase("clearchat")) {
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
        } else if (args[0].equalsIgnoreCase("permissions")) {
          if (sender.hasPermission("AdminTools.permissions") || sender.hasPermission("AdminTools.*")) {
            sender.sendMessage(Msg.get("help_permissions", false));
            sender.sendMessage(Msg.get("help_permissions_1", false));
            sender.sendMessage(Msg.get("help_permissions_2", false));
            sender.sendMessage(Msg.get("help_permissions_3", false));
            sender.sendMessage(Msg.get("help_permissions_4", false));
            sender.sendMessage(Msg.get("help_permissions_5", false));
            sender.sendMessage(Msg.get("help_permissions_6", false));
            sender.sendMessage(Msg.get("help_permissions_7", false));
            sender.sendMessage(Msg.get("help_permissions_8", false));
            return true;
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("setwarp")) {
          if (sender instanceof Player) {
            if (sender.hasPermission("AdminTools.setwarp") || sender.hasPermission("AdminTools.*")) {
              if (args.length>1) {
                if (data.setWarp(args[1], ((Player) sender).getLocation()))
                  sender.sendMessage(Msg.get("warp_add", true, args[1]));
                else
                  sender.sendMessage(Msg.get("warp_set", true, args[1]));
                return true;
              } else {
                sender.sendMessage(Msg.get("error_name_needed", true));
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
        } else if (args[0].equalsIgnoreCase("warp")) {
          if (sender instanceof Player) {
            if (sender.hasPermission("AdminTools.warp") || sender.hasPermission("AdminTools.*")) {
              if (args.length>1) {
                if (data.getWarp(args[1]) != null) {
                  ((Player) sender).teleport(data.getWarp(args[1]));
                  sender.sendMessage(Msg.get("warp_tp", true, args[1]));
                  return true;
                } else {
                  sender.sendMessage(Msg.get("error_warp_not_exist", true, args[1]));
                  return true;
                }
              } else {
                sender.sendMessage(Msg.get("warp_list", true, data.getPrettyWarpList()));
              }
            } else {
              sender.sendMessage(Msg.get("error_permission", true));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_player_needed", true));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("delwarp")) {
          if (sender.hasPermission("AdminTools.delwarp") || sender.hasPermission("AdminTools.*")) {
            if (args.length>1) {
              if (data.getWarp(args[1]) != null) {
                data.delWarp(args[1]);
                sender.sendMessage(Msg.get("warp_del", true, args[1]));
                return true;
              } else {
                sender.sendMessage(Msg.get("error_warp_not_exist", true, args[1]));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_warp_needed", true));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_no_argument", true));
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
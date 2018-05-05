package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.Warp;

public class DelWarpCommand implements CommandExecutor {
  Data data;
  
  public DelWarpCommand(Data data) {
    this.data = data;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("DelWarp")) {
      if (sender.hasPermission("AdminTools.DelWarp") || sender.hasPermission("AdminTools.*")) {
        if (args.length > 0) {
          Warp warp = Warp.getWarpByString(args[0]);
          if (warp != null) {
            warp.delete();
            sender.sendMessage(Msg.get("warp_deleted", false, args[0]));
            return true;
          } else {
            sender.sendMessage(Msg.get("error_warp_not_exist", false, args[0]));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_usage", false, "/delwarp (name)"));
          return true;
        }
      } else {
        sender.sendMessage(Msg.get("error_permission", false));
        return true;
      }
    }
    return true;
  }
}
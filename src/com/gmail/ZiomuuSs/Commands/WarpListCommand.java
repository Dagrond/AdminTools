package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.Warp;

public class WarpListCommand implements CommandExecutor {
  Data data;
  
  public WarpListCommand(Data data) {
    this.data = data;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("WarpList")) {
      if (sender.hasPermission("AdminTools.WarpList") || sender.hasPermission("AdminTools.*")) {
        String list = "";
        for (String warp : Warp.getStringWarps().keySet()) {
          list += warp+"("+(Warp.getWarpByString(warp).getIndex()+1)+"), ";
        }
        sender.sendMessage(Msg.get("warp_list", true, list));
        return true;
      } else {
        sender.sendMessage(Msg.get("error_permission", false));
        return true;
      }
    }
    return true;
  }
}
package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.Warp;

public class SetWarpLocCommand implements CommandExecutor {
  Data data;
  
  public SetWarpLocCommand(Data data) {
    this.data = data;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("SetWarpLoc")) { // /setwarploc (name)
      if (sender instanceof Player) {
        Player player = (Player) sender;
        if (sender.hasPermission("AdminTools.setwarplocation") || sender.hasPermission("AdminTools.*")) {
          if (args.length==1) {
            Warp warp = Warp.getWarpByString(args[0]);
            if (warp != null) {
              warp.setLocation(player.getLocation());
              player.sendMessage(Msg.get("warp_changed_loc", true, args[0]));
            } else {
              sender.sendMessage(Msg.get("error_warp_not_exist", false, args[0]));
            }
          } else {
            sender.sendMessage(Msg.get("error_usage", true, "/setwarploc (name)"));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_permission", false));
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
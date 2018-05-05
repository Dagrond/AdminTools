package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;
import com.gmail.ZiomuuSs.Utils.Warp;

public class WarpCommand implements CommandExecutor {
  Data data;
  
  public WarpCommand(Data data) {
    this.data = data;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("Warp")) {
      if (sender instanceof Player) {
        Player player = (Player) sender;
        if (!Warp.isInProgress(player)) {
          if (args.length < 1) {
            Warp.showWarps(player);
            sender.sendMessage(Msg.get("warp_opened", false));
          } else {
            Warp warp = Warp.getWarpByString(args[0]);
            if (warp != null) {
              warp.teleport(player);
            } else {
              sender.sendMessage(Msg.get("error_warp_not_exist", false, args[0]));
            }
          }
          return true;
        } else {
          sender.sendMessage(Msg.get("error_warp_in_progress", false));
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
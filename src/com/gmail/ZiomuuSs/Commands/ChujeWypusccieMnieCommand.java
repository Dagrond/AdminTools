package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.Msg;

public class ChujeWypusccieMnieCommand implements CommandExecutor {
  protected Main plugin;
  
  public ChujeWypusccieMnieCommand(Main instance) {
    plugin = instance;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("ChujeWypusccieMnie")) {
      if (sender instanceof Player) {
        Player player = (Player) sender;
        if (plugin.getData().isSaved(player.getUniqueId())) {
          plugin.getData().getTeamByPlayer(player).delPlayer(player);
          sender.sendMessage(Msg.get("you_have_been_freed", false));
          return true;
        } else {
          sender.sendMessage(Msg.get("you_are_not_in_team", false));
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

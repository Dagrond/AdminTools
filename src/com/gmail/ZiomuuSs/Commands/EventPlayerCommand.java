package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.Msg;

public class EventPlayerCommand implements CommandExecutor {
  protected Main plugin;
  
  public EventPlayerCommand(Main instance) {
    plugin = instance;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("event") || cmd.getName().equalsIgnoreCase("e")) {
      if (args.length>0) {
        if (args[0].equalsIgnoreCase("wyjdz")) {
          if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.getData().isSaved(player.getUniqueId())) {
              plugin.getData().getTeamByPlayer(player).delPlayer(player);
              sender.sendMessage(Msg.get("event_quit", true));
              return true;
            } else {
              sender.sendMessage(Msg.get("event_error_not_in_event", true));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_player_needed", true));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("dolacz")) {
          //todo
        } else {
          sender.sendMessage(Msg.get("event_error_usage", true, "/e dolacz/wyjdz"));
          return true;
        }
      } else {
        sender.sendMessage(Msg.get("event_error_usage", true, "/e dolacz/wyjdz"));
        return true;
      }
    }
    return true;
  }
}

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
              sender.sendMessage(Msg.get("event_quit", false));
              return true;
            } else {
              sender.sendMessage(Msg.get("event_error_not_in_event", false));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_player_needed", true));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("dolacz")) {
          if (plugin.getData().getOpen() != null) {
            if (sender instanceof Player) {
              if (!plugin.getData().isSaved(((Player) sender).getUniqueId())) {
                plugin.getData().getOpen().addPlayer((Player) sender);
                sender.sendMessage(Msg.get("event_added", false, plugin.getData().getOpen().toString()));
                return true;
              } else {
                sender.sendMessage(Msg.get("event_error_already_saved", false));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_player_needed", true));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("event_error_no_open", false));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_usage", false, "/e dolacz/wyjdz"));
          return true;
        }
      } else {
        sender.sendMessage(Msg.get("error_usage", false, "/e dolacz/wyjdz"));
        return true;
      }
    }
    return true;
  }
}

package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class EventPlayerCommand implements CommandExecutor {
  protected Data data;
  
  public EventPlayerCommand( Data data) {
    this.data = data;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("event") || cmd.getName().equalsIgnoreCase("e")) {
      if (args.length>0) {
        if (args[0].equalsIgnoreCase("wyjdz")) {
          if (sender instanceof Player) {
            Player player = (Player) sender;
            if (data.isSaved(player.getUniqueId())) {
              data.removePlayer(player);
              sender.sendMessage(Msg.get("event_quit", true));
              return true;
            } else {
              sender.sendMessage(Msg.get("event_error_not_in_event", true));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("event_error_already_queued", true));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("dolacz")) {
          //todo
          if (sender instanceof Player) {
            if (data.isStarting()) {
              Player player = (Player) sender;
              if (!data.isSaved(player.getUniqueId())) {
                if (!data.isQueued(player.getUniqueId())) {
                  if (data.CanJoin()) {
                    if (data.addPlayer(player)) {
                      sender.sendMessage(Msg.get("event_added", true));
                      return true;
                    } else {
                      sender.sendMessage(Msg.get("event_queued", true));
                      return true;
                    }
                  } else {
                    sender.sendMessage(Msg.get("event_error_maxplayers", true));
                    return true;
                  }
                } else {
                  sender.sendMessage(Msg.get("event_error_already_queued", true));
                  return true;
                }
              } else {
                sender.sendMessage(Msg.get("event_error_already_saved", true));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("event_error_no_open", true));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("event_error_player_needed", true));
            return true;
          }
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

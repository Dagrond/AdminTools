package com.gmail.ZiomuuSs.Commands;

import org.bukkit.GameMode;
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
            if (data.getCurrentEvent().isSaved(player.getUniqueId())) {
              data.getCurrentEvent().removePlayer(player);
              sender.sendMessage(Msg.get("event_quit", true));
              data.getCurrentEvent().broadcastToEvent(Msg.get("event_idonteven", true, player.getDisplayName()));
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
          if (sender instanceof Player) {
            if (data.getCurrentEvent() != null) {
              Player player = (Player) sender;
              if (!data.getCurrentEvent().isSaved(player.getUniqueId())) {
                if (!data.getCurrentEvent().isQueued(player.getUniqueId())) {
                  if (data.getCurrentEvent().CanJoin()) {
                    if (player.getPassengers().isEmpty() && player.getVehicle() == null) {
                      if (player.getGameMode() == GameMode.SURVIVAL) {
                        if (data.getCurrentEvent().addPlayer(player)) {
                          sender.sendMessage(Msg.get("event_added", true));
                          return true;
                        } else {
                          sender.sendMessage(Msg.get("event_queued", true));
                          return true;
                        }
                      } else {
                        sender.sendMessage(Msg.get("event_error_gamemode", true));
                        return true;
                      }
                    } else {
                      sender.sendMessage(Msg.get("event_error_riding", true));
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
        } else if (args[0].equalsIgnoreCase("obserwuj")) {
          //todo
        } else if (args[0].equalsIgnoreCase("klasa") || args[0].equalsIgnoreCase("wybierz") || args[0].equalsIgnoreCase("klasy")) {
          if (sender instanceof Player) {
            if (data.getCurrentEvent() != null) {
              Player player = (Player) sender;
              if (data.getCurrentEvent().isSaved(player.getUniqueId())) {
                data.getCurrentEvent().getTeamByPlayer(player).changeInventory(player);
              } else {
                sender.sendMessage(Msg.get("event_error_not_in_event", true));
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

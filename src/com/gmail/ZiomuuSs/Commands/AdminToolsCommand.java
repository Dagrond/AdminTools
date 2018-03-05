package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Utils.CountdownTimer;
import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class AdminToolsCommand implements CommandExecutor {
  Main plugin;
  Data data;
  
  public AdminToolsCommand(Main instance) {
    plugin = instance;
    data = plugin.getData();
  }
  
  @Override
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
              if (player.getLocation().getY() <= 20 && !player.hasPermission("AdminTools.bypass") && player.getWorld() == Bukkit.getWorld("world")) {
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
        } else if(args[0].equalsIgnoreCase("pattern") || args[0].equalsIgnoreCase("p")) {
          if (sender.hasPermission("AdminTools.pattern") || sender.hasPermission("AdminTools.*")) {
            if (args.length>2) {
              if (args[1].equalsIgnoreCase("create")) {
                if (!data.isTeam(args[2])) {
                  data.addTeam(args[2]);
                  data.saveTeams();
                  sender.sendMessage(Msg.get("team_created", true, args[2]));
                  return true;
                } else {
                  sender.sendMessage(Msg.get("error_team_exist", true, args[2]));
                  return true;
                }
              } else if (args[1].equalsIgnoreCase("inventory")) {
                if (sender instanceof Player) {
                  if (data.isTeam(args[2])) {
                    data.getTeam(args[2]).setInventory(((Player) sender).getInventory());
                    data.saveTeams();
                    sender.sendMessage(Msg.get("team_inventory_set", true, args[2]));
                    return true;
                  } else {
                    sender.sendMessage(Msg.get("error_team_not_exist", true, args[2]));
                    return true;
                  }
                } else {
                  sender.sendMessage(Msg.get("error_player_needed", true));
                  return true;
                }
              } else if (args[1].equalsIgnoreCase("location")) {
                if (sender instanceof Player) {
                  if (data.isTeam(args[2])) {
                    data.getTeam(args[2]).setLocation(((Player) sender).getLocation());
                    data.saveTeams();
                    sender.sendMessage(Msg.get("team_location_set", true, args[2]));
                    return true;
                  } else {
                    sender.sendMessage(Msg.get("error_team_not_exist", true, args[2]));
                    return true;
                  }
                } else {
                  sender.sendMessage(Msg.get("error_player_needed", true));
                  return true;
                }
              } else {
                sender.sendMessage(Msg.get("error_usage", true, "/at pattern create/inventory/location (team)"));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_usage", true, "/at pattern create/inventory/location (team)"));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
      } else if (args[0].equalsIgnoreCase("list")) {
          if (args.length>1) {
            if (data.isTeam(args[1])) {
              sender.sendMessage(Msg.get("list_players", true, args[1], data.getTeam(args[1]).getPrettyPlayerList(), Integer.toString(data.getTeam(args[1]).getPlayerNumber())));
              return true;
            } else {
              sender.sendMessage(Msg.get("error_team_not_exist", true, args[1]));
              return true;
              }
          } else {
            sender.sendMessage(Msg.get("list_patterns", true, data.getPrettyTeamList(), Integer.toString(data.getEventNumber())));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("bc") || args[0].equalsIgnoreCase("broadcast")) {
          if (sender.hasPermission("AdminTools.broadcast") || sender.hasPermission("AdminTools.*")) {
            if (args.length>1) {
              String msg = "";
              args[0] = "";
              for (String s : args) {
                msg += s;
              }
              Bukkit.broadcastMessage(Msg.get("event_broadcast", false, msg));
              return true;
            } else {
              sender.sendMessage(Msg.get("error_usage", true, "/at bc (msg)"));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
        } else if (args[0].equalsIgnoreCase("toggle")) {
          if (sender.hasPermission("AdminTools.toggle") || sender.hasPermission("AdminTools.*")) {
            if (args.length>1) {
              if (data.isTeam(args[1])) {
                if (data.getOpen() == data.getTeam(args[1])) {
                  data.setOpen(null);
                  sender.sendMessage(Msg.get("event_off", true, args[1]));
                  return true;
                } else if (data.getOpen() == null) {
                  data.setOpen(data.getTeam(args[1]));
                  sender.sendMessage(Msg.get("event_on", true, args[1]));
                  Bukkit.broadcastMessage(Msg.get("event_opened", false, args[1]));
                  return true;
                } else {
                  sender.sendMessage(Msg.get("error_already_open", true, data.getOpen().toString()));
                  return true;
                }
              } else {
                sender.sendMessage(Msg.get("error_team_not_exist", true, args[1]));
                return true;
              }
          } else {
            sender.sendMessage(Msg.get("error_usage", true, "/at toggle (team)"));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_permission", true));
          return true;
        }
        } else if(args[0].equalsIgnoreCase("add")) {
          if (sender.hasPermission("AdminTools.add") || sender.hasPermission("AdminTools.*")) {
            if (args.length>2) {
              if (data.isTeam(args[1])) {
                for (int i = 2; i < args.length; ++i) {
                  if (Bukkit.getPlayer(args[i]) != null) {
                    if (!data.isSaved(Bukkit.getPlayer(args[i]).getUniqueId())) {
                      data.getTeam(args[1]).addPlayer(Bukkit.getPlayer(args[i]));
                      sender.sendMessage(Msg.get("team_added", true, args[1], args[i]));
                    } else {
                      sender.sendMessage(Msg.get("error_player_already_added", true, args[i], data.getTeamByPlayer(Bukkit.getPlayer(args[i])).toString()));
                    }
                  } else {
                    sender.sendMessage(Msg.get("error_player_not_exist", true, args[i]));
                  }
                }
                return true;
              } else {
                sender.sendMessage(Msg.get("error_team_not_exist", true, args[1]));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_usage", true, "/at add (team) (player)"));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
        } else if(args[0].equalsIgnoreCase("del")) {
          if (sender.hasPermission("AdminTools.delete") || sender.hasPermission("AdminTools.*")) {
            if (args.length>1) {
              if (Bukkit.getPlayer(args[1]) != null) {
                Player player = Bukkit.getPlayer(args[1]);
                if (data.isSaved(player.getUniqueId())) {
                  data.getTeamByPlayer(player).delPlayer(player);
                  sender.sendMessage(Msg.get("player_deleted", true, args[1]));
                  return true;
                } else {
                  sender.sendMessage(Msg.get("error_player_not_saved", true, args[1]));
                  return true;
                }
              } else {
                sender.sendMessage(Msg.get("error_player_not_exist", true, args[1]));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_usage", true, "/at del (player)"));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
        } else if(args[0].equalsIgnoreCase("count")) {
          if (sender.hasPermission("AdminTools.count") || sender.hasPermission("AdminTools.*")) {
            if (args.length>1) {
              if (args[1].matches("-?\\d+")) {
                CountdownTimer timer = new CountdownTimer(plugin, Integer.valueOf(args[1]), () -> Bukkit.broadcastMessage(Msg.get("count_start", false)), () -> Bukkit.broadcastMessage(Msg.get("count_done", false)), (t) -> Bukkit.broadcastMessage(Msg.get("count_left", false, ChatColor.DARK_AQUA+Integer.toString(t.getSecondsLeft()))));
                timer.scheduleTimer();
              } else {
                sender.sendMessage(Msg.get("error_must_be_integer", true, "count"));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_usage", true, "/at count (seconds)"));
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
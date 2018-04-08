package com.gmail.ZiomuuSs.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.EventGroup;
import com.gmail.ZiomuuSs.EventGroup.StopCondition;
import com.gmail.ZiomuuSs.EventTeam;
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
        } else if (args[0].equalsIgnoreCase("items")) {
          if (sender.hasPermission("AdminTools.SetItemsForGuild") || sender.hasPermission("AdminTools.*")) {
            if (sender instanceof Player) {
              data.setGuildItems(((Player) sender).getInventory().getContents());
              sender.sendMessage(Msg.get("items_setted", true));
              return true;
            } else {
              sender.sendMessage(Msg.get("error_player_needed", true));
              return true;
            }
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
        } else if (args[0].equalsIgnoreCase("event") || args[0].equalsIgnoreCase("e")) {
          if (sender.hasPermission("AdminTools.event") || sender.hasPermission("AdminTools.*")) {
            if (args.length>2) {
              if (data.isEventGroup(args[1])) {
                EventGroup group = data.getEventGroupByName(args[1]);
                if (data.getCurrentEvent() != group) {
                  if (args[2].equalsIgnoreCase("info")) {
                    sender.sendMessage(Msg.get("group_info", true, args[1]));
                    sender.sendMessage(Msg.get("group_info_displayname", true, group.getDisplayName()));
                    sender.sendMessage(Msg.get("group_info_minplayers", true, Integer.toString(group.getMinPlayers())));
                    sender.sendMessage(Msg.get("group_info_teams", true, Integer.toString(group.getTeams().size())));
                    sender.sendMessage(Msg.get("group_info_spec", true, group.getSpecLocation() != null ? Msg.get("set", false) : Msg.get("not_set", false)));
                    sender.sendMessage(Msg.get("group_info_spectators", true, Integer.toString(group.getSpectatorCount())));
                    sender.sendMessage(Msg.get("group_info_playersin", true, Integer.toString(group.getSavedPlayers().size())));
                    return true;
                  } else if (args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("del")) {
                    data.removeEventGroup(args[1]);
                    sender.sendMessage(Msg.get("group_removed", true, args[1]));
                    return true;
                  } else if (args[2].equalsIgnoreCase("spec")) {
                    if (sender instanceof Player) {
                      if (group.setSpectatorsLocation(((Player) sender).getLocation()))
                        sender.sendMessage(Msg.get("group_spec_location_edit", true, args[1]));
                      else
                        sender.sendMessage(Msg.get("group_spec_location_set", true, args[1]));
                    } else {
                      sender.sendMessage(Msg.get("error_player_needed", true));
                      return true;
                    }
                  } else if (args[2].equalsIgnoreCase("wincondition") || args[2].equalsIgnoreCase("wc")) {
                    if (args.length>3) {
                      try {
                        group.setStopCondition(StopCondition.valueOf(args[3].toUpperCase()));
                        sender.sendMessage(Msg.get("group_wincondition_set", true, args[1], args[3].toUpperCase()));
                      } catch (IllegalArgumentException e) {
                        sender.sendMessage(Msg.get("error_wincondition_not_exist", true, args[3], "LAST_PLAYER, LAST_TEAM"));
                        return true;
                      }
                    } else {
                      sender.sendMessage(Msg.get("error_usage", true, "/at e (event) wincondition (condition)"));
                      return true;
                    }
                  } else if (args[2].equalsIgnoreCase("delay")) {
                    if (args.length>3) {
                      if (args[3].matches("-?\\d+")) {
                        group.setDelay(Integer.valueOf(args[3]));
                        sender.sendMessage(Msg.get("group_set_delay", true, args[1], args[3]));
                      } else {
                        sender.sendMessage(Msg.get("error_must_be_integer", true, "delay"));
                        return true;
                      }
                    } else {
                      sender.sendMessage(Msg.get("error_usage", true, "/at e (event) delay (delay)"));
                      return true;
                    }
                  } else if (args[2].equalsIgnoreCase("min")) {
                    if (args[4].matches("-?\\d+")) {
                      group.setMinPlayers(Integer.valueOf(args[4]));
                      sender.sendMessage(Msg.get("group_set_min_players", true, args[1], args[4]));
                    } else {
                      sender.sendMessage(Msg.get("error_must_be_integer", true, "minPlayers"));
                      return true;
                    }
                  } else if (args[2].equalsIgnoreCase("team")) {
                    if (args.length>4) {
                      if (args[3].equalsIgnoreCase("add")) {
                        if (!group.getTeams().containsKey(args[4])) {
                          if (data.isTeam(args[4])) {
                            group.addTeam(data.getTeam(args[4]));
                            sender.sendMessage(Msg.get("group_added_team", true, args[4], args[1]));
                          } else {
                            sender.sendMessage(Msg.get("error_team_not_exist", true, args[4]));
                            return true;
                          }
                        } else {
                          sender.sendMessage(Msg.get("error_team_already_in_group", true, args[4], args[1]));
                          return true;
                        }
                      } else if (args[3].equalsIgnoreCase("remove") || args[3].equalsIgnoreCase("del")) {
                        if (group.getTeams().containsKey(args[4])) {
                          group.getTeams().remove(args[4]);
                          sender.sendMessage(Msg.get("group_team_removed", true, args[4], args[1]));
                        } else {
                          sender.sendMessage(Msg.get("error_team_not_listed_in_group", true, args[4], args[1]));
                          return true;
                        }
                      } else {
                        sender.sendMessage(Msg.get("error_usage", true, "/at e (event) team add/del/list"));
                        return true;
                      }
                    } else if (args.length>3 && args[3].equalsIgnoreCase("list")) {
                      sender.sendMessage(Msg.get("group_team_list", true, args[1], group.getPrettyTeamList()));
                      return true;
                    } else {
                      sender.sendMessage(Msg.get("error_usage", true, "/at e (event) team add/list/del"));
                      return true;
                    }
                  } else {
                    sender.sendMessage(Msg.get("error_usage", true, "/at e (event) create/del/info/spec/delay/team"));
                    return true;
                  }
                  data.saveGroup(args[1]);
                } else {
                  sender.sendMessage(Msg.get("error_cannot_edit_while_in_progress", true));
                  return true;
                }
              } else if (args[2].equalsIgnoreCase("create")) {
                if (args.length>3) {
                  data.addEventGroup(args[1], args[3]);
                  sender.sendMessage(Msg.get("group_added", true, args[1], args[3]));
                  return true;
                } else {
                  sender.sendMessage(Msg.get("error_usage", true, "/at e (event) create (displayname)"));
                  return true;
                }
              } else {
                sender.sendMessage(Msg.get("error_event_not_exist", true, args[1]));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_usage", true, "/at e (event) create/delete/info/spec/delay/team"));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
        } else if(args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("t") || args[0].equalsIgnoreCase("teams")) {
          if (sender.hasPermission("AdminTools.team") || sender.hasPermission("AdminTools.*")) {
            if (args.length>2) {
              if (data.getTeam(args[1]) != null) {
                EventTeam team = data.getTeam(args[1]);
                if (args[2].equalsIgnoreCase("lobby")) {
                  if (sender instanceof Player) {
                    if (team.setLobby(((Player) sender).getLocation()))
                      sender.sendMessage(Msg.get("team_lobby_set", true, args[1]));
                    else
                      sender.sendMessage(Msg.get("team_lobby_edited", true, args[1]));
                  } else {
                    sender.sendMessage(Msg.get("error_player_needed", true));
                    return true;
                  }
                } else if (args[2].equalsIgnoreCase("inventory") || args[2].equalsIgnoreCase("inv")) {
                  if (args.length>3) {
                    if (args[3].equalsIgnoreCase("info") || args[3].equalsIgnoreCase("list")) {
                      sender.sendMessage(Msg.get("team_inventory_display", true, args[1], team.getPrettyInventoryList()));
                      return true;
                    } else if (args[3].equalsIgnoreCase("icon")) {
                      if (sender instanceof Player) {
                        if (args.length>4) {
                          if (team.isInventory(args[4])) {
                            Player player = (Player) sender;
                            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                              team.removeInventoryIcon(args[4]);
                              sender.sendMessage(Msg.get("team_inventoryicon_removed", true, args[4], args[1]));
                            } else {
                              if (team.setInventoryIcon(args[4], player.getInventory().getItemInMainHand()))
                                sender.sendMessage(Msg.get("team_inventoryicon_edit", true, args[4], args[1]));
                              else
                                sender.sendMessage(Msg.get("team_inventoryicon_add", true, args[4], args[1]));
                            }
                          } else {
                            sender.sendMessage(Msg.get("error_inventory_not_exist", true, args[4], args[1]));
                            return true;
                          }
                        } else {
                          sender.sendMessage(Msg.get("error_usage", true, "/at e (team) inventory icon (name)"));
                          return true;
                        }
                      } else {
                        sender.sendMessage(Msg.get("error_player_needed", true));
                        return true;
                      }
                    } else if (args[3].equalsIgnoreCase("del") || args[3].equalsIgnoreCase("delete") || args[3].equalsIgnoreCase("remove")) {
                      if (args.length>4) {
                        if (team.isInventory(args[4])) {
                          team.removeInventory(args[4]);
                          sender.sendMessage(Msg.get("team_inventory_deleted", true, args[4], args[1]));
                        } else {
                          sender.sendMessage(Msg.get("error_inventory_not_exist", true, args[4], args[1]));
                          return true;
                        }
                      } else {
                        sender.sendMessage(Msg.get("error_usage", true, "/at e (team) inventory del (name)"));
                        return true;
                      }
                    } else if (args[3].equalsIgnoreCase("add")) {
                      if (args.length>4) {
                        if (!team.isInventory(args[4])) {
                          if (sender instanceof Player) {
                            team.addInventory(args[4], ((Player) sender).getInventory().getContents());
                            sender.sendMessage(Msg.get("team_inventory_added", true, args[4], args[1]));
                          } else {
                            sender.sendMessage(Msg.get("error_player_needed", true));
                            return true;
                          }
                        } else {
                          sender.sendMessage(Msg.get("error_inventory_already_exist", true, args[4], args[1]));
                          return true;
                        }
                      } else {
                        sender.sendMessage(Msg.get("error_usage", true, "/at e (team) inventory add (name)"));
                        return true;
                      }
                    } else if (args[3].equalsIgnoreCase("set") || args[3].equalsIgnoreCase("edit")) {
                      if (sender instanceof Player) {
                        if (args.length>3) {
                          if (team.isInventory(args[4])) {
                            team.addInventory(args[4], ((Player) sender).getInventory().getContents());
                            sender.sendMessage(Msg.get("team_inventory_edited", true, args[4], args[1]));
                          } else {
                            sender.sendMessage(Msg.get("error_inventory_not_exist", true, args[4], args[1]));
                            return true;
                          }
                        } else {
                          sender.sendMessage(Msg.get("error_usage", true, "/at e (team) inventory set (name)"));
                          return true;
                        }
                      } else {
                        sender.sendMessage(Msg.get("error_player_needed", true));
                        return true;
                      }
                    } else {
                      sender.sendMessage(Msg.get("error_usage", true, "/at e (team) inventory list/del/add/set"));
                      return true;
                    }
                  } else {
                    sender.sendMessage(Msg.get("error_usage", true, "/at e (team) inventory list/del/add/set"));
                    return true;
                  }
                } else if (args[2].equalsIgnoreCase("startpoint") || args[2].equalsIgnoreCase("startpoints") || args[2].equalsIgnoreCase("sp")) {
                  if (args.length>3) {
                    if (args[3].equalsIgnoreCase("info") || args[3].equalsIgnoreCase("list")) {
                      sender.sendMessage(Msg.get("team_startpoints_display", true, args[1]));
                      int index = 1;
                      for (Location loc : team.getStartPoints()) {
                        sender.sendMessage(Msg.get("team_startpoints_info", true, Integer.toString(index), Msg.get("loc", false, Double.toString(Math.round(loc.getX())), Double.toString(Math.round(loc.getY())), Double.toString(Math.round(loc.getZ())), loc.getWorld().getName())));
                        ++index;
                      }
                      return true;
                    } else if (args[3].equalsIgnoreCase("del") || args[3].equalsIgnoreCase("delete") || args[3].equalsIgnoreCase("remove")) {
                      if (args.length>3) {
                        if (args[4].matches("-?\\d+")) {
                          if (team.getStartPoints().size() >= Integer.valueOf(args[4]) && Integer.valueOf(args[4])>0) {
                            team.getStartPoints().remove(Integer.valueOf(args[4])-1);
                            sender.sendMessage(Msg.get("team_startpoint_deleted", true, args[4]));
                          } else {
                            sender.sendMessage(Msg.get("error_no_startpoint", true, args[4]));
                            return true;
                          }
                        } else {
                          sender.sendMessage(Msg.get("error_must_be_integer", true, "index"));
                          return true;
                        }
                      } else {
                        sender.sendMessage(Msg.get("error_usage", true, "/at e (team) startpoint det (index)"));
                        return true;
                      }
                    } else if (args[3].equalsIgnoreCase("add")) {
                      if (sender instanceof Player) {
                        team.setStartPoints(((Player) sender).getLocation(), team.getStartPoints().size()+2);
                        sender.sendMessage(Msg.get("team_startpoint_add", true, Integer.toString(team.getStartPoints().size()), args[1]));
                      } else {
                        sender.sendMessage(Msg.get("error_player_needed", true));
                        return true;
                      }
                    } else if (args[3].equalsIgnoreCase("set") || args[3].equalsIgnoreCase("edit")) {
                      if (sender instanceof Player) {
                        if (args.length>3) {
                          if (args[4].matches("-?\\d+")) {
                            if (team.getStartPoints().size() >= Integer.valueOf(args[4]) && Integer.valueOf(args[4])>0) {
                              team.setStartPoints(((Player) sender).getLocation(), Integer.valueOf(args[4]));
                              sender.sendMessage(Msg.get("team_startpoint_edited", true, args[4], args[1]));
                            } else {
                              sender.sendMessage(Msg.get("error_no_startpoint", true, args[4]));
                              return true;
                            }
                          } else {
                            sender.sendMessage(Msg.get("error_must_be_integer", true, "index"));
                            return true;
                          }
                        } else {
                          sender.sendMessage(Msg.get("error_usage", true, "/at e (team) startpoint set (index)"));
                          return true;
                        }
                      } else {
                        sender.sendMessage(Msg.get("error_player_needed", true));
                        return true;
                      }
                    } else {
                      sender.sendMessage(Msg.get("error_usage", true, "/at e (team) startpoint list/del/add/set"));
                      return true;
                    }
                  } else {
                    sender.sendMessage(Msg.get("error_usage", true, "/at e (team) startpoint list/del/add/set"));
                    return true;
                  }
                } else if (args[2].equalsIgnoreCase("friendlyfire") || args[2].equalsIgnoreCase("ff")) {
                    if (team.switchFriendlyFire())
                      sender.sendMessage(Msg.get("team_ff_on", true, args[2]));
                    else
                      sender.sendMessage(Msg.get("team_ff_off", true, args[2]));
                } else if (args[2].equalsIgnoreCase("nametag") || args[2].equalsIgnoreCase("nt")) {
                  if (team.switchNametagVisibility())
                    sender.sendMessage(Msg.get("team_nametag_on", true, args[2]));
                  else
                    sender.sendMessage(Msg.get("team_nametag_off", true, args[2]));
                } else if (args[2].equalsIgnoreCase("info")) {
                  sender.sendMessage(Msg.get("team_info", true, args[1]));
                  sender.sendMessage(Msg.get("team_info_ff", true, team.getFriendlyFire() ? Msg.get("yes_", false) : Msg.get("no_", false)));
                  sender.sendMessage(Msg.get("team_info_nametag", true, team.getNametagVisibility() ? Msg.get("yes_", false) : Msg.get("no_", false)));
                  sender.sendMessage(Msg.get("team_info_maxplayers", true, team.getMaxPlayers()==0 ? Msg.get("unlimited", false) : Integer.toString(team.getMaxPlayers())));
                  sender.sendMessage(Msg.get("team_info_startpoints", true, team.getStartPoints().isEmpty() ? Msg.get("not_set", false) : Integer.toString(team.getStartPoints().size())));
                  sender.sendMessage(Msg.get("team_info_lobby", true, team.getLobby() != null ? Msg.get("set", false) : Msg.get("not_set", false)));
                  sender.sendMessage(Msg.get("team_info_inventory", true, !team.getInventories().isEmpty() ? Msg.get("set", false) : Msg.get("not_set", false), Integer.toString(team.getInventories().size())));
                  sender.sendMessage(Msg.get("team_info_playersin", true, Integer.toString(team.getPlayerNumber())));
                  return true;
                } else if (args[2].equalsIgnoreCase("maxplayers") || args[2].equalsIgnoreCase("mp")) {
                  if (args.length>3) {
                    if (args[3].matches("-?\\d+")) {
                      if (team.setMaxPlayers(Integer.valueOf(args[3])))
                        sender.sendMessage(Msg.get("team_maxplayers_set", true, args[1], args[3]));
                      else
                        sender.sendMessage(Msg.get("team_maxplayers_edited", true, args[1], args[3]));
                    } else {
                      sender.sendMessage(Msg.get("error_must_be_integer", true, "maxPlayers"));
                      return true;
                    }
                  } else {
                    sender.sendMessage(Msg.get("error_usage", true, "/at e (event) maxplayers (integer)"));
                    return true;
                  }
                } else {
                  sender.sendMessage(Msg.get("error_usage", true, "/at e (event) create/inventory/lobby/startpoint/maxplayers/info"));
                  return true;
                }
                data.saveTeam(team.toString());
                return true;
              } else if (args[2].equalsIgnoreCase("create")) {
                data.addTeam(args[1]);
                sender.sendMessage(Msg.get("team_created", true, args[1]));
                return true;
              } else {
                sender.sendMessage(Msg.get("error_team_not_exist", true, args[1]));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_usage", true, "/at e (event) create/inventory/lobby/startpoints/maxplayers/info"));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_permission", true));
            return true;
          }
      } else if (args[0].equalsIgnoreCase("stop")) {
        if (sender.hasPermission("AdminTools.stop") || sender.hasPermission("AdminTools.*")) {
          if (data.getCurrentEvent() != null) {
            data.getCurrentEvent().setCancelled();
            sender.sendMessage(Msg.get("group_cancelled", true));
            return true;
          } else {
            sender.sendMessage(Msg.get("error_non_in_progress", true));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_permission", true));
          return true;
        }
      } else if (args[0].equalsIgnoreCase("start")) {
        if (sender.hasPermission("AdminTools.start") || sender.hasPermission("AdminTools.*")) {
          if (data.getCurrentEvent() == null) {
            if (args.length>1) {
              EventGroup group = data.getEventGroupByName(args[1]);
              if (group != null) {
                if (group.isReady()) {
                  data.start(group);
                  sender.sendMessage(Msg.get("group_started", true, args[1]));
                  return true;
                } else {
                  sender.sendMessage(Msg.get("error_group_not_ready", true, args[1]));
                  return true;
                }
              } else {
                sender.sendMessage(Msg.get("error_group_not_exist", true, args[1]));
                return true;
              }
            } else {
              sender.sendMessage(Msg.get("error_usage", true, "/e start (delay) (displayname) (team1) <team2>"));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_inprogress", true));
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
            if (data.getCurrentEvent() != null) {
              String msg = "";
              args[0] = "";
              for (String s : args) {
                msg += s+" ";
              }
              data.getCurrentEvent().broadcastToEvent(Msg.get("event_broadcast", true, msg));
              return true;
            } else {
              sender.sendMessage(Msg.get("error_event_not_in_progress", true));
              return true;
            }
          } else {
            sender.sendMessage(Msg.get("error_usage", true, "/at bc (msg)"));
            return true;
          }
        } else {
          sender.sendMessage(Msg.get("error_permission", true));
          return true;
        }
      } else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("kick")) {
        if (sender.hasPermission("AdminTools.delete") || sender.hasPermission("AdminTools.*")) {
          if (args.length>1) {
            if (Bukkit.getPlayer(args[1]) != null) {
              Player player = Bukkit.getPlayer(args[1]);
              if (data.getCurrentEvent().isSaved(player.getUniqueId())) {
                data.getCurrentEvent().broadcastToEvent((Msg.get("event_kicked", true, args[1])));
                data.getCurrentEvent().removePlayer(player);
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
      } else if(args[0].equalsIgnoreCase("reload")) {
        if (sender.hasPermission("AdminTools.reload") || sender.hasPermission("AdminTools.*")) {
          if (data.getCurrentEvent() == null) {
            plugin.reload(sender);
            return true;
          } else {
            sender.sendMessage(Msg.get("error_inprogress", true));
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
      sender.sendMessage(Msg.get("error_usage", true, "/at help"));
      return true;
    }
  }
  return true;
  }
}
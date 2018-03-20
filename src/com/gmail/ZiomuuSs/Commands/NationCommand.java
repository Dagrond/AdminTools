package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.ZiomuuSs.Utils.Data;

public class NationCommand implements CommandExecutor {
  protected Data data;
  
  public NationCommand( Data data) {
    this.data = data;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("nation") || cmd.getName().equalsIgnoreCase("n")) {
      sender.sendMessage("todo");
    }
    return true;
  }
}

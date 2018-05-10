package com.gmail.ZiomuuSs.Commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.ZiomuuSs.Utils.Data;
import com.gmail.ZiomuuSs.Utils.Msg;

public class MenuCommand implements CommandExecutor {
  Data data;
  private static Inventory menuInv = Bukkit.createInventory(null, 9, Msg.get("player_menu_inventory", false));
  
  //items in inventory
  private static ItemStack HomeUpgrades = new ItemStack(Material.BED, 1, (short) 11);
  private static ItemMeta meta = HomeUpgrades.getItemMeta();
  static {
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    meta.setDisplayName("");
    meta.setLore(Arrays.asList("", ""));
  }
  
  public MenuCommand(Data data) {
    this.data = data;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("Menu")) {
      if (sender instanceof Player) {
        Player player = (Player) sender;
        if (player.hasPermission("essentials.sethome.multiple.vip")) {
          
        } else if (player.hasPermission("essentials.sethome.multiple.three")) {
          
        } else if (player.hasPermission("essentials.sethome.two")) {
          
        } else {
          
        }
        player.openInventory(menuInv);
        return true;
      } else {
        sender.sendMessage(Msg.get("error_player_needed", true));
        return true;
      }
    }
    return true;
  }
}
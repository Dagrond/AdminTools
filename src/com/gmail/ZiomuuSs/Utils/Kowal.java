package com.gmail.ZiomuuSs.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

import com.gmail.ZiomuuSs.Main;

import api.praya.myitems.enums.LoreStatsEnum;
import api.praya.myitems.enums.OptionStatsEnum;
import api.praya.myitems.main.MyItemsAPI;
import api.praya.myitems.manager.game.LoreStatsManagerAPI;

@SuppressWarnings("deprecation")
public class Kowal {
  
  private static final double vipmultiplier = 0.7; //final price is multipled by this if player is vip
  private static final double MImultiplier = 0.8; //if item has MyItems durability system, price is: (MyItems lore durability points to repair)*(this multiplier)
  private static final double normalmultiplier = 0.08; //if item has standard minecraft durability system, price is: (durability points to repair)*(this multiplier)
  
  /*
   * if item has standard minecraft durability system, this can be used to increase repair price depending on enchantments.
   * for every enchantment level, this bonus is added to the price. So if this variable is 8, and someone want to
   * repair his pickaxe efficiency 5, unbreaking 2, fortune 1, this will add 64 to the price.
  */
  private static final double enchantbonus = 8;
  
  private Main plugin;
  private Player player;
  private Inventory kowal = Bukkit.createInventory(null, 54, "§9§lKowal:");; //inventory of blacksmith
  public static HashMap<UUID, Kowal> currentPlayers = new HashMap<>();
  private static ItemStack repairItemReady = new ItemStack(Material.WOOL, 1, (short) 5); //button for repair when everything is fine
  private static ItemStack repairItemNotEnoughCash = new ItemStack(Material.WOOL, 1, (short) 14); //button for repair when there is not enough cash to repair it
  private static ItemStack cannotRepair = new ItemStack(Material.BARRIER, 1); //button for repair when item cannot be repaired
  private static ItemStack background = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15); //item to cover all free slots
  private static ItemStack infoItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3); //item that is displaying help how to use this inventory
  private static LoreStatsManagerAPI durAPI = MyItemsAPI.getInstance().getGameManagerAPI().getLoreStatsManagerAPI(); //Accesing MyItemsAPI to check durability
  
  static {
    ItemMeta meta = repairItemReady.getItemMeta();
    meta.setDisplayName("§2§lNapraw przedmiot");
    repairItemReady.setItemMeta(meta);
    meta = repairItemNotEnoughCash.getItemMeta();
    meta.setDisplayName("§4§lNapraw przedmiot");
    repairItemNotEnoughCash.setItemMeta(meta);
    meta = cannotRepair.getItemMeta();
    meta.setDisplayName("§2§lNapraw przedmiot");
    meta.setLore(Arrays.asList("§4§lPrzedmiotu nie mozna naprawic!", "§4§lUpewnij sie, czy:", "§4§l- przedmiot wymaga naprawy", "§4§l- wlozyles przedmiot w slot", "§4§l- przedmiot nie jest zestackowany"));
    cannotRepair.setItemMeta(meta);
    meta = background.getItemMeta();
    meta.setDisplayName(" ");
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    background.setItemMeta(meta);
    SkullMeta Smeta = (SkullMeta) infoItem.getItemMeta();
    Smeta.setDisplayName("§2§lPomoc");
    Smeta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Question"));
    Smeta.setLore(Arrays.asList("§aAby naprawic przedmiot wloz", "§ago w slot pod tym", "§aprzedmiotem, a nastepnie",  "§akliknij przycisk §2§l\"Napraw przedmiot\"§a.", "§8(Wersja: v1.0)"));
    infoItem.setItemMeta(Smeta);
  }
  
  public Kowal(Main instance, Player player) {
    plugin = instance;
    kowal.setItem(22, infoItem);
    kowal.setItem(41, cannotRepair);
    for (int i=53; i>=0; --i) {
      if (i != 22 && i != 31 && i != 41)
        kowal.setItem(i, background);
    }
    currentPlayers.put(player.getUniqueId(), this);
    player.openInventory(kowal);
    this.player = player;
  }
  
  public void PlayerAction(Player player, ItemStack item) {
    if (item.isSimilar(repairItemReady)) {
      ItemStack MIitem = player.getOpenInventory().getTopInventory().getItem(31);
      double price = new BigDecimal((isVip() ? getRepairPrice(MIitem)*vipmultiplier : getRepairPrice(MIitem))).setScale(2,RoundingMode.HALF_UP).doubleValue();
      if (plugin.getEconomy().getBalance(player) >= price) {
        plugin.getEconomy().withdrawPlayer(player, price);
        if (durAPI.hasLoreStats(MIitem, LoreStatsEnum.DURABILITY))
          durAPI.itemRepair(MIitem, (int) durAPI.getLoreValue(MIitem, LoreStatsEnum.DURABILITY, OptionStatsEnum.MAX));
        else
          MIitem.setDurability((short) 0);
        player.sendMessage(Msg.get("blacksmith_repaired", false, Double.toString(price)));
        return;
      } else {
        player.closeInventory();
        player.sendMessage(Msg.get("error_blacksmith_not_enough", false, Double.toString(price), Double.toString(plugin.getEconomy().getBalance(player))));
      }
    }
  }
  
  private boolean isVip() {
    return player.hasPermission("AdminTools.vip");
  }
  
  private double getRepairPrice(ItemStack item) {
    double price = 0;
    if (durAPI.hasLoreStats(item, LoreStatsEnum.DURABILITY))
      price = (durAPI.getLoreValue(item, LoreStatsEnum.DURABILITY, OptionStatsEnum.MAX)-durAPI.getLoreValue(item, LoreStatsEnum.DURABILITY, OptionStatsEnum.CURRENT))*MImultiplier;
    else {
      price = (item.getType().getMaxDurability()-item.getDurability())*normalmultiplier;
      for (int e : item.getEnchantments().values())
        price += e*enchantbonus;
    }
    return new BigDecimal(price).setScale(2,RoundingMode.HALF_UP).doubleValue();
  }
  
  public void UpdateInventory() {
    Inventory inv = player.getOpenInventory().getTopInventory();
    ItemStack item = inv.getItem(31);
    if (item == null) {
      inv.setItem(41, cannotRepair);
      return;
    }
    if ((durAPI.hasLoreStats(item, LoreStatsEnum.DURABILITY) && durAPI.getLoreValue(item, LoreStatsEnum.DURABILITY, OptionStatsEnum.CURRENT) < durAPI.getLoreValue(item, LoreStatsEnum.DURABILITY, OptionStatsEnum.MAX)) || (item.getItemMeta() instanceof Repairable && item.getDurability() != (short) 0) && item.getAmount() == 1) {
      double price = (isVip() ? new BigDecimal(getRepairPrice(item)*vipmultiplier).setScale(2,RoundingMode.HALF_UP).doubleValue() : getRepairPrice(item));
      if (plugin.getEconomy().getBalance(player) >= price) {
        ItemMeta meta = repairItemReady.getItemMeta();
        meta.setLore(Arrays.asList("§aKliknij, aby naprawic przedmiot.", "§2§lKoszt: §4§l"+getRepairPrice(item), "§2§lKoszt dla §6§lVIPa§2§l: §4§l"+new BigDecimal(getRepairPrice(item)*vipmultiplier).setScale(2,RoundingMode.HALF_UP).doubleValue()));
        repairItemReady.setItemMeta(meta);
        inv.setItem(41, repairItemReady);
      } else {
        ItemMeta meta = repairItemNotEnoughCash.getItemMeta();
        meta.setLore(Arrays.asList("§cNie stac Cie na naprawe!", "§2§lKoszt: §4§l"+getRepairPrice(item), "§2§lKoszt dla §6§lVIPa§2§l: §4§l"+new BigDecimal(getRepairPrice(item)*vipmultiplier).setScale(2,RoundingMode.HALF_UP).doubleValue()));
        repairItemNotEnoughCash.setItemMeta(meta);
        inv.setItem(41, repairItemNotEnoughCash);
      }
    } else {
      inv.setItem(41, cannotRepair);
    }
  }
}

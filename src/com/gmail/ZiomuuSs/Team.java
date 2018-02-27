package com.gmail.ZiomuuSs;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Team {
  protected String name;
  protected Inventory inv;
  protected Location loc;
  protected HashSet<Player> players = new HashSet<>();
}

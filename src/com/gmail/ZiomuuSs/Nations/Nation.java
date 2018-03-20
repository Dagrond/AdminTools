package com.gmail.ZiomuuSs.Nations;

import java.util.HashSet;
import java.util.UUID;

public class Nation {
  private HashSet<UUID> members = new HashSet<>(); //members of current nation
  private String name; //name of nation
  
  public Nation(String name) {
    this.name = name;
  }
  
  //todo
  
  
  //getters
  @Override
  public String toString() {
    return name;
  }
  
}

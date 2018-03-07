package com.gmail.ZiomuuSs;

import java.util.UUID;

import com.gmail.ZiomuuSs.Utils.Data;

public class EventHandler {
  private Team[] starting; //if any event is starting?
  private Data data;
  private int delay = 10;
  private UUID waitingPlayer; //player that is waiting to add (must waiting because of team
  
  public EventHandler(Data data, int delay) {
    this.data = data;
    this.delay = delay;
  }
  
  public boolean isStarting() {
    return starting != null;
  }
  
  public Team[] getStarting() {
    return starting;
  }
  
  public UUID getWaiting() {
    return waitingPlayer;
  }
  
  public void setWaiting(UUID uuid) {
    waitingPlayer = uuid;
  }
}

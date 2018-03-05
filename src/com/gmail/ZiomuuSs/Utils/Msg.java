package com.gmail.ZiomuuSs.Utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class Msg {
  
  private static ConfigurationSection msg;
  
  public static void set(ConfigurationSection msgConfig) {
    msg = msgConfig;
  }
  
  public static String get (String path, boolean prefix, String...opt) {
    if (!msg.isString(path))
      return ChatColor.RED+"Error occured during loading message. Perhaps Messages.yml is configured badly. Please, contact server operator.";
    String string = "";
    if (path.startsWith("event_")) {
      if (prefix)
        string = msg.getString("prefix_event");
      if (path.startsWith("event_error_")) {
        string += msg.getString("error_color_event")+msg.getString(path);
        string = string.replaceAll("%s", msg.getString("error_variable_color_event"));
        string = string.replaceAll("%n", msg.getString("error_color_event"));
        if (opt.length > 0) {
          for (int i = 0; i<opt.length; i++) {
            string = string.replaceAll("%"+(i+1), msg.getString("error_variable_color_event")+opt[i]+msg.getString("error_color_event"));
          }
        }
      } else {
        string += msg.getString("message_color_event")+msg.getString(path);
        string = string.replaceAll("%s", msg.getString("message_variable_color_event"));
        string = string.replaceAll("%n", msg.getString("message_color_event"));
        if (opt.length > 0) {
          for (int i = 0; i<opt.length; i++) {
            string = string.replaceAll("%"+(i+1), msg.getString("message_variable_color_event")+opt[i]+msg.getString("message_color_event"));
          }
        }
      }
    } else {
      if (prefix)
        string = msg.getString("prefix");
      if (path.startsWith("error_")) {
        string += msg.getString("error_color")+msg.getString(path);
        string = string.replaceAll("%s", msg.getString("error_variable_color"));
        string = string.replaceAll("%n", msg.getString("error_color"));
        if (opt.length > 0) {
          for (int i = 0; i<opt.length; i++) {
            string = string.replaceAll("%"+(i+1), msg.getString("error_variable_color")+opt[i]+msg.getString("error_color"));
          }
        }
      } else {
        string += msg.getString("message_color")+msg.getString(path);
        string = string.replaceAll("%s", msg.getString("message_variable_color"));
        string = string.replaceAll("%n", msg.getString("message_color"));
        if (opt.length > 0) {
          for (int i = 0; i<opt.length; i++) {
            string = string.replaceAll("%"+(i+1), msg.getString("message_variable_color")+opt[i]+msg.getString("message_color"));
          }
        }
      }
  }
    return ChatColor.translateAlternateColorCodes('&', string);
  }
}

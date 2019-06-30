package me.wuxie.sv.svequipment.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class Message {
    public static void send(Player player,String msg, Object... args) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(msg, args)));
    }
}

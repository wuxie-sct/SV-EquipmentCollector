package me.wuxie.sv.svequipment.listener;

import me.wuxie.sv.svequipment.SVEquipment;
import me.wuxie.sv.svequipment.suit.SuitData;
import me.wuxie.sv.svequipment.util.ConfigData;
import me.wuxie.sv.svequipment.util.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnListener implements Listener {
    @EventHandler
    public void onJoinListener(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.isOnline()) {
            SVEquipment.getPlayerdata().put(p.getUniqueId(), new PlayerData(p));
            SVEquipment.getPlayerdata().get(p.getUniqueId()).getPlayerData();
            if(ConfigData.getConfig().getBoolean("EnableSuit")){
                SVEquipment.getSuitData().put(p.getUniqueId(),new SuitData(p));
                SVEquipment.getSuitData().get(p.getUniqueId()).getSuitList();
            }
        }
    }
}

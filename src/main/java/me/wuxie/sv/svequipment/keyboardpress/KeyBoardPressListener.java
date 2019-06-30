package me.wuxie.sv.svequipment.keyboardpress;

import lk.vexview.event.KeyBoardPressEvent;
import me.wuxie.sv.svequipment.SVEquipment;
import me.wuxie.sv.svequipment.gui.ShowEquipmentGui;
import me.wuxie.sv.svequipment.util.ConfigData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KeyBoardPressListener implements Listener {
    @EventHandler
    public void onPress(KeyBoardPressEvent e){
        if(e.getType().equals(KeyBoardPressEvent.KeyPressType.valueOf("NOGUI"))){
            if(e.getKey() == ConfigData.getConfig().getInt("OpenEquipmentGUI_KEY")){
                new ShowEquipmentGui(SVEquipment.getPlayerdata().get(e.getPlayer().getUniqueId())).show();
            }
        }
    }
}

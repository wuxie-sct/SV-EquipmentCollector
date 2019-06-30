package me.wuxie.sv.svequipment.listener;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import github.saukiya.sxattribute.event.SXLoadItemDataEvent;
import me.wuxie.sv.svequipment.SVEquipment;
import me.wuxie.sv.svequipment.util.ConfigData;
import me.wuxie.sv.svequipment.util.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

/**
 *  加载属性的类
 */
public class SXOnLoadItemDataListener implements Listener {
    @EventHandler
    public void onLoad(SXLoadItemDataEvent e){
        if (e.getEntity() instanceof Player) {
            if(e.getType().equals(SXConditionType.EQUIPMENT)){
                PlayerData data = SVEquipment.getPlayerdata().get(e.getEntity().getUniqueId());
                SXAttributeData Data = SXAttribute.getApi().getItemData(e.getEntity(), null,data.getOtherEquips());
                if(ConfigData.getConfig().getBoolean("EnableSuit")) {
                    SVEquipment.getSuitData().get(e.getEntity().getUniqueId()).getSuitList();
                    SXAttributeData suitData = SXAttribute.getApi().getLoreData(e.getEntity(), null, SVEquipment.getSuitData().get(e.getEntity().getUniqueId()).getSuitAttribute()==null?new ArrayList<>():SVEquipment.getSuitData().get(e.getEntity().getUniqueId()).getSuitAttribute());
                    if (suitData != null) {
                        Data = Data != null ? Data.add(suitData) : suitData;
                    }
                }
                /*if(SVEquipment.sxGemIsEnable){
                    SXAttributeData gemData = SXAttribute.getApi().getLoreData(e.getEntity(),null, GemData.getItemData(data.getOtherEquips()));
                if(gemData!=null){
                    Data = Data!=null?Data.add(gemData):gemData;
                }
                }*/
                if (Data != null) {
                    e.setAttributeData(e.getAttributeData()!= null ? e.getAttributeData().add(Data):Data);
                }
            }
        }
    }
}

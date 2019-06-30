package me.wuxie.sv.svequipment.suit;

import lombok.Getter;
import me.wuxie.sv.svequipment.SVEquipment;
import me.wuxie.sv.svequipment.util.ConfigData;
import me.wuxie.sv.svequipment.util.PlayerData;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SuitData {
    private Player player;
    public SuitData(Player player){
        this.player =player;
    }
    @Getter
    private Map<String, Integer> suitEquipAmount = new HashMap<>();
    @Getter
    private List<ItemStack> equips =new ArrayList<>();
    @Getter
    private Map<String, List<String>> suitAttributeMap = new HashMap<>();

    @Getter
    private List<String> suitAttribute = new ArrayList<>();

    private YamlConfiguration yml = ConfigData.getSuitDataConfig();

    public Map<String, Integer> getSuitAmount(){
        Map<String, Integer> amount =new HashMap<>();
        PlayerData data = SVEquipment.getPlayerdata().get(player.getUniqueId());
        ItemStack[] armor = player.getInventory().getArmorContents();
        Set<String> keySet =yml.getKeys(false);
        for(ItemStack i:armor){
            if(i!=null&&!i.getType().equals(Material.AIR)&&i.getItemMeta().hasDisplayName()){
                String iname = i.getItemMeta().getDisplayName();
                a:
                for(String key:keySet){
                    for(String n:yml.getStringList(key+".NameList")){
                        if(iname.replace("§","&").equalsIgnoreCase(n.replace("§","&"))){
                            amount.put(yml.getString(key+".suitName"),(amount.get(yml.getString(key+".suitName"))==null?0:amount.get(yml.getString(key+".suitName")))+1);
                            break a;
                        }
                    }
                }
            }
        }
        if(player.getInventory().getItemInOffHand()!=null&&!player.getInventory().getItemInOffHand().getType().equals(Material.AIR)){
            ItemStack item = player.getInventory().getItemInOffHand();
            if(item.getItemMeta().hasDisplayName()){
                String iname = item.getItemMeta().getDisplayName();
                a:
                for(String key:keySet){
                    for(String n:yml.getStringList(key+".NameList")){
                        if(iname.replace("§","&").equalsIgnoreCase(n.replace("§","&"))){
                            amount.put(yml.getString(key+".suitName"),(amount.get(yml.getString(key+".suitName"))==null?0:amount.get(yml.getString(key+".suitName")))+1);
                            break a;
                        }
                    }
                }
            }
        }
        for(Map.Entry map:data.getEquip().entrySet()){
            ItemStack item =(ItemStack)map.getValue();
            if(item!=null&&item.hasItemMeta()&&item.getItemMeta().hasDisplayName()){
                String itemname = ((ItemStack)map.getValue()).getItemMeta().getDisplayName();
                a:
                for(String key:keySet){
                    for(String n:yml.getStringList(key+".NameList")){
                        if(itemname.replace("§","&").equalsIgnoreCase(n.replace("§","&"))){
                            amount.put(yml.getString(key+".suitName"),(amount.get(yml.getString(key+".suitName"))==null?0:amount.get(yml.getString(key+".suitName")))+1);
                            break a;
                        }
                    }
                }
            }
        }
        return amount;
    }
    public void getSuitList(){
        Map<String, Integer> suitamount =getSuitAmount();
        if(!suitamount.equals(suitEquipAmount)){
            suitEquipAmount = new HashMap<>();
            suitAttributeMap = new HashMap<>();
            suitAttribute = new ArrayList<>();
            suitEquipAmount = suitamount;
            for(Map.Entry map :suitamount.entrySet()){
                int amount = (int) map.getValue();
                a:
                for(String key:yml.getKeys(false)){
                    if(map.getKey().equals(yml.getString(key+".suitName"))){
                        for(String k:yml.getConfigurationSection(key+".Amount").getKeys(false)){
                            int min = Integer.parseInt(k.split("-")[0]);
                            int max = Integer.parseInt(k.split("-")[1]);
                            if(amount>=min&&amount<=max){
                                suitAttribute.addAll(yml.getStringList(key+".Amount."+k));
                                suitAttributeMap.put((String) map.getKey(),yml.getStringList(key+".Amount."+k));
                                break a;
                            }
                        }
                    }
                }
            }
        }
    }
}

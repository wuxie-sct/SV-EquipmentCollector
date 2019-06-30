package me.wuxie.sv.svequipment.util;

import github.saukiya.sxattribute.SXAttribute;
import lk.vexview.gui.VexGui;
import lombok.Getter;
import lombok.Setter;
import me.wuxie.myplugin.datasharing.PluginYaml;
import me.wuxie.sv.svequipment.SVEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerData {
    public PlayerData(Player p){
        this.player = p;
        if(SVEquipment.isDataSharing()){
            this.file = new PluginYaml(SVEquipment.getPlugin()).getFile("PlayerData"+File.separator+player.getUniqueId()+ ".yml",false);
        }else {
            this.file = new File("plugins/SV-Equipment/PlayerData/" + player.getUniqueId() + ".yml");
        }
    }
    private Player player;
    private  File file;
    @Getter
    private Map<String, ItemStack> equip = new HashMap<>();
    @Getter
    @Setter
    private VexGui lastopendGui = null;
    @Getter
    YamlConfiguration data = new YamlConfiguration();
    @Getter
    private ItemStack[] otherEquips;
    @Getter
    private List<ItemStack> allEquipment;

    /**
     * @return 返回当前玩家
     */
    public Player getPlayer(){
        return this.player;
    }

    /**
     * 获取玩家总信息
     */
    public void getPlayerData(){
        if(file.exists()){
            data = YamlConfiguration.loadConfiguration(file);
            boolean needSave=false;
            for (String key: ConfigData.getShowEquipmentConfig().getConfigurationSection("otherequipment").getKeys(false)){
                if(data.get("otherequipment."+key)==null){
                    needSave=true;
                    data.set("otherequipment."+key,new ItemStack(Material.AIR));
                }
                if(!data.getItemStack("otherequipment."+key).getType().equals(Material.AIR)){
                    this.equip.put("otherequipment#"+key,data.getItemStack("otherequipment."+key));
                }
            }
            if(needSave){
                try {
                    data.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            updateOtherEquipList();
        }else{
            for (String key: ConfigData.getShowEquipmentConfig().getConfigurationSection("otherequipment").getKeys(false)){
                data.set("otherequipment."+key,new ItemStack(Material.AIR));
            }
            try {
                data.save(file);
                Bukkit.getConsoleSender().sendMessage("§a玩家 "+player.getName()+" 配置创建成功！");
                updateOtherEquipList();
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("§4玩家 "+player.getName()+" 配置创建失败！！请重启服务器！");
            }
        }
    }
    /**
     * 判断某个槽位是否有装备
     * @param mappath
     * @return
     */
    public boolean hasEquip(String mappath) {
        if (mappath.equalsIgnoreCase("equipment#chestplate")) {
            if(player.getInventory().getChestplate()!=null&&!player.getInventory().getChestplate().getType().equals(Material.AIR)){
                return true;
            }else {return false;}
        }else if (mappath.equalsIgnoreCase("equipment#helmet")) {
            if(player.getInventory().getHelmet()!=null&&!player.getInventory().getHelmet().getType().equals(Material.AIR)){
                return true;
        }else {return false;}
        }else if (mappath.equalsIgnoreCase("equipment#leggings")) {
            if(player.getInventory().getLeggings()!=null&&!player.getInventory().getLeggings().getType().equals(Material.AIR)){
                return true;
            }else {return false;}
        }else if (mappath.equalsIgnoreCase("equipment#boots")) {
            if(player.getInventory().getBoots()!=null&&!player.getInventory().getBoots().getType().equals(Material.AIR)){
                return true;
            }else {return false;}
        }else if (mappath.equalsIgnoreCase("equipment#offhand")) {
            if(player.getInventory().getItemInOffHand()!=null&&!player.getInventory().getItemInOffHand().getType().equals(Material.AIR)){
                return true;
            }else {return false;}
        }else {
            if (equip.get(mappath) != null) {
                return true;
            }
            return false;
        }
    }
    /**
     * 穿上装备
     * @param mappath map路径
     * @param item 穿上的装备
     */
    public boolean setEquip(String mappath,ItemStack item){
        boolean isequip = false;
        String itemname;
        if(item.getItemMeta().hasDisplayName()){
            itemname = item.getItemMeta().getDisplayName();
        }else{
            itemname = item.getType().name();
        }
        if(mappath.equalsIgnoreCase("equipment#chestplate")){
            if(item.getType().name().contains("CHESTPLATE")){
                if(SXAttribute.getApi().getItemLevel(item)>player.getLevel()){
                    Message.send(player,ConfigData.getConfig().getString("Message.no_level"),itemname);
                    return false;
                }
                player.getInventory().setChestplate(item);
                return true;
            }
        }else if(mappath.equalsIgnoreCase("equipment#helmet")){
            if(item.getType().name().contains("HELMET")){
                if(SXAttribute.getApi().getItemLevel(item)>player.getLevel()){
                    Message.send(player,ConfigData.getConfig().getString("Message.no_level"),itemname);
                    return false;
                }
                player.getInventory().setHelmet(item);
                return true;
            }
        }
        else if(mappath.equalsIgnoreCase("equipment#boots")){
            if(item.getType().name().contains("BOOTS")){
                if(SXAttribute.getApi().getItemLevel(item)>player.getLevel()){
                    Message.send(player,ConfigData.getConfig().getString("Message.no_level"),itemname);
                    return false;
                }
                player.getInventory().setBoots(item);
                return true;
            }
        }
        else if(mappath.equalsIgnoreCase("equipment#leggings")){
            if(item.getType().name().contains("LEGGINGS")){
                if(SXAttribute.getApi().getItemLevel(item)>player.getLevel()){
                    Message.send(player,ConfigData.getConfig().getString("Message.no_level"),itemname);
                    return false;
                }
                player.getInventory().setLeggings(item);
                return true;
            }
        }
        else if(mappath.equalsIgnoreCase("equipment#offhand")){
            if(SXAttribute.getApi().getItemLevel(item)>player.getLevel()){
                Message.send(player,ConfigData.getConfig().getString("Message.no_level"),itemname);
                return false;
            }
            player.getInventory().setItemInOffHand(item);
            return true;
        }
        else {
            ItemMeta meta =item.getItemMeta();
            if(meta.hasLore()){
                for(String lore:meta.getLore()){
                    if(lore.contains(ConfigData.getShowEquipmentConfig().getString(mappath.replace("#",".")+".getlore"))){
                        isequip = true;
                        if(SXAttribute.getApi().getItemLevel(item)>player.getLevel()){
                            Message.send(player,ConfigData.getConfig().getString("Message.no_level"),itemname);
                            return false;
                        }
                        break;
                    }
                }
            }
        }
        if(isequip){
            equip.put(mappath,item);
            data.set(mappath.replace("#","."),item);
            try {
                data.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message.send(player,ConfigData.getConfig().getString("Message.add_Equip"),itemname);
            updateOtherEquipList();
            SXAttribute.getApi().updateEquipmentData(player);
            return true;
        }
        String type =SVEquipment.getPlayerisequip().get(player.getUniqueId()).replace("#",".");
        Message.send(player,ConfigData.getConfig().getString("Message.no_type"),ConfigData.getShowEquipmentConfig().getString(type+".equipTypeName"));
        return false;
    }
    /**
     * 卸下装备 先要判断 hasEquip(mappath)
     * @param mappaths map路径
     */
    public boolean delEquip(String mappaths){
        boolean hasSize=false;
        for(ItemStack item:player.getInventory().getContents()){
            if(item==null||item.getType().equals(Material.AIR)){
                hasSize=true;
                break;
            }
        }
        if(hasSize){
            String itemname;
            ItemStack itemStack;
            if(mappaths.equalsIgnoreCase("equipment#chestplate")){
                itemStack =player.getInventory().getChestplate();
                player.getInventory().setChestplate(null);
            }
            else if(mappaths.equalsIgnoreCase("equipment#helmet")){
                itemStack =player.getInventory().getHelmet();
                player.getInventory().setHelmet(null);
            }
            else if(mappaths.equalsIgnoreCase("equipment#boots")){
                itemStack =player.getInventory().getBoots();
                player.getInventory().setBoots(null);
            }
            else if(mappaths.equalsIgnoreCase("equipment#leggings")){
                itemStack =player.getInventory().getLeggings();
                player.getInventory().setLeggings(null);
            }
            else if(mappaths.equalsIgnoreCase("equipment#offhand")){
                itemStack =player.getInventory().getItemInOffHand();
                player.getInventory().setItemInOffHand(null);
            }else {
                itemStack = equip.get(mappaths);
                equip.put(mappaths, null);
                data.set(mappaths.replace("#", "."), new ItemStack(Material.AIR));
                try {
                    data.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (itemStack.getItemMeta().hasDisplayName()) {
                itemname = itemStack.getItemMeta().getDisplayName();
            } else {
                itemname = itemStack.getType().name();
            }
            player.getInventory().addItem(itemStack);
            Message.send(player,ConfigData.getConfig().getString("Message.del_Equip"),itemname);
            updateOtherEquipList();
            return true;
        }else{
            Message.send(player,ConfigData.getConfig().getString("Message.no_inventory_size"));
            return false;
        }
    }

    /**
     * 获取所有正在装备的物品列表
     * @return
     */
    public List<ItemStack> getAllequip(){
        allEquipment = new ArrayList<>();
        for(ItemStack item:player.getInventory().getArmorContents()){
            if(item!=null&&!item.getType().equals(Material.AIR)){
                allEquipment.add(item);
            }
        }
        allEquipment.addAll(Arrays.asList(otherEquips));
        return allEquipment;
    }
    /**
     * 更新装备库里正在装备的物品信息
     */
    private void updateOtherEquipList(){
        List<ItemStack> update =new ArrayList<>();
        for(String path:ConfigData.getShowEquipmentConfig().getConfigurationSection("otherequipment").getKeys(false)){
            if(equip.get("otherequipment#"+path)!=null){
                update.add(equip.get("otherequipment#"+path));
            }
        }
        otherEquips = update.toArray(new ItemStack[update.size()]);
    }
}

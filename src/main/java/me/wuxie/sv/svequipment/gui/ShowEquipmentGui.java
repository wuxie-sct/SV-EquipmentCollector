package me.wuxie.sv.svequipment.gui;

import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.*;
import me.wuxie.sv.svequipment.SVEquipment;
import me.wuxie.sv.svequipment.suit.SuitData;
import me.wuxie.sv.svequipment.util.ConfigData;
import me.wuxie.sv.svequipment.util.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ShowEquipmentGui {
    private PlayerData data;
    private int slotId =0;
    private YamlConfiguration yml = ConfigData.getShowEquipmentConfig();
    public ShowEquipmentGui(PlayerData data) {
        this.data = data;
    }
    /**
     * 向玩家展示这个GUI界面
     * */
    public void show(){
        new BukkitRunnable() {
            @Override
            public void run() {
                VexGui gui = new VexGui(yml.getString("url"), yml.getInt("x"), yml.getInt("y"), yml.getInt("w"), yml.getInt("h"), yml.getInt("w"), yml.getInt("h"));
                if(yml.getBoolean("playerDraw.enable")){
                    VexPlayerDraw draw =new VexPlayerDraw(
                            yml.getInt("playerDraw.x"),
                            yml.getInt("playerDraw.y"),
                            yml.getInt("playerDraw.scale"),
                            data.getPlayer());
                    gui.addComponent(draw);
                }
                if(yml.getBoolean("showInventoryEquipment")){
                    equipMethod(gui,data,"equipment.chestplate");
                    equipMethod(gui,data,"equipment.helmet");
                    equipMethod(gui,data,"equipment.offhand");
                    equipMethod(gui,data,"equipment.leggings");
                    equipMethod(gui,data,"equipment.boots");
                }
                for(String key:yml.getConfigurationSection("otherequipment").getKeys(false)){
                    equipMethod(gui,data,"otherequipment."+key);
                }
                if(ConfigData.getConfig().getBoolean("EnableSuit")){
                    SuitData suitData = SVEquipment.getSuitData().get(data.getPlayer().getUniqueId());
                    int vbid=yml.getInt("Suit.Button.id");
                    int vbx=yml.getInt("Suit.Button.x");
                    int vby=yml.getInt("Suit.Button.y");
                    int vbw=yml.getInt("Suit.Button.w");
                    int vbh=yml.getInt("Suit.Button.h");
                    String name=yml.getString("Suit.Button.name");
                    String url1=yml.getString("Suit.Button.url1");
                    String url2=yml.getString("Suit.Button.url2");
                    List<String> attribute =new ArrayList<>();
                    String format=yml.getString("Suit.Button.format");
                    if(suitData.getSuitAmount().size()>0){
                        for(Map.Entry map:suitData.getSuitAmount().entrySet()){
                            attribute.add(format.replace("<suit>", (String) map.getKey()).replace("<amount>", ((int)map.getValue())+""));
                            if(suitData.getSuitAttributeMap().containsKey(map.getKey())){
                                attribute.addAll(suitData.getSuitAttributeMap().get(map.getKey()));
                            }else{
                                attribute.add("§7没有激活套装属性！~");
                            }
                        }
                    }else{
                        attribute.add("§7没有套装！~");
                    }
                    try{
                        VexHoverText text =new VexHoverText(attribute,vbx,vby,vbw,vbh);
                        VexButton vb = new VexButton(vbid,name,url1,url2,vbx,vby,vbw,vbh,text);
                        gui.addComponent(vb);
                    }catch (Exception ignored){}
                }
                try {
                    VexViewAPI.openGui(data.getPlayer(),gui);
                }catch (Exception e){
                    System.err.println("错误:无法打开该GUI界面");
                    System.err.println("原因:"+e.getMessage());
                    System.err.println("以下是具体报错内容:");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(SVEquipment.getPlugin());
    }
    private boolean hasName(ItemStack item){
        if (item ==null)return false;
        return item.getItemMeta().hasDisplayName();
    }
    private boolean hasLore(ItemStack item){
        if (item ==null)return false;
        return item.getItemMeta().hasLore();
    }

    /**
     *  运行配置路径下对应装备的处理方法
     * @param gui 对应gui
     * @param data PlayerData
     * @param type 装备类型路径
     */
    private void equipMethod(VexGui gui,PlayerData data,String type){
        Set<String> namecontain = ConfigData.getConfig().getConfigurationSection("displayNameUrl").getKeys(false);
        if(data.hasEquip(type.replace(".","#"))){
            ItemStack item;
            if(type.split("\\.")[1].equalsIgnoreCase("chestplate")){
                item = data.getPlayer().getInventory().getChestplate();
            }else if(type.split("\\.")[1].equalsIgnoreCase("helmet")){
                item = data.getPlayer().getInventory().getHelmet();
            }
            else if(type.split("\\.")[1].equalsIgnoreCase("boots")){
                item = data.getPlayer().getInventory().getBoots();
            }
            else if(type.split("\\.")[1].equalsIgnoreCase("leggings")){
                item = data.getPlayer().getInventory().getLeggings();
            }
            else if(type.split("\\.")[1].equalsIgnoreCase("offhand")){
                item = data.getPlayer().getInventory().getItemInOffHand();
            }else{
                item =data.getEquip().get(type.replace(".","#"));
            }
            int x =yml.getInt(type+".slot.x");
            int y =yml.getInt(type+".slot.y");
            int ix =yml.getInt(type+".itemHasImage.x");
            int iy =yml.getInt(type+".itemHasImage.y");
            int w =yml.getInt(type+".itemHasImage.w");
            int h =yml.getInt(type+".itemHasImage.h");
            List<String> buttonlist =new ArrayList<>();
            if(yml.get(type+".Button.delButton.text")!=null){
                buttonlist.addAll(yml.getStringList(type+".Button.delButton.text"));
            }
            VexHoverText buttontext =new VexHoverText(buttonlist,yml.getInt(type+".Button.x"),
                    yml.getInt(type+".Button.y"),
                    yml.getInt(type+".Button.w"),
                    yml.getInt(type+".Button.h"));
            VexButton button =new VexButton(
                    yml.getInt(type+".Button.delButton.id"),
                    yml.getString(type+".Button.delButton.name"),
                    yml.getString(type+".Button.url1"),
                    yml.getString(type+".Button.url2"),
                    yml.getInt(type+".Button.x"),
                    yml.getInt(type+".Button.y"),
                    yml.getInt(type+".Button.w"),
                    yml.getInt(type+".Button.h"),(player)-> {
                if (data.delEquip(type.replace(".", "#"))) {
                    player.closeInventory();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            new ShowEquipmentGui(data).show();
                        }
                    }.runTaskAsynchronously(SVEquipment.getPlugin());
                }
            },buttontext);
            gui.addComponent(button);
            if(hasName(item)){
                String name =item.getItemMeta().getDisplayName();
                if(namecontain.contains(ChatColor.stripColor(name))){
                    List<String> list =new ArrayList<>();
                    list.add(name);
                    if(hasLore(item)){
                        list.add(" ");
                        list.addAll(item.getItemMeta().getLore());
                    }
                    VexHoverText text =new VexHoverText(list,x,y,w,h);
                    VexImage equipImage =new VexImage(
                            ConfigData.getConfig().getString("displayNameUrl."+ChatColor.stripColor(name)),
                            ix, iy, w, h, w, h, text);
                    gui.addComponent(equipImage);
                }else{
                    VexSlot slot =new VexSlot(slotId,x,y,item);
                    gui.addComponent(slot);
                    //slotType.put(slotId,type);
                    slotId=slotId+1;
                }
            }else{
                VexSlot slot =new VexSlot(slotId,x,y,item);
                gui.addComponent(slot);
                //slotType.put(slotId,type);
                slotId=slotId+1;
            }
        }else{
            List<String> buttonlist =new ArrayList<>();
            if(yml.get(type+".Button.addButton.text")!=null){
                buttonlist.addAll(yml.getStringList(type+".Button.addButton.text"));
            }
            VexHoverText buttontext =new VexHoverText(buttonlist,yml.getInt(type+".Button.x"),
                    yml.getInt(type+".Button.y"),
                    yml.getInt(type+".Button.w"),
                    yml.getInt(type+".Button.h"));
            VexButton button =new VexButton(
                    yml.getInt(type+".Button.addButton.id"),
                    yml.getString(type+".Button.addButton.name"),
                    yml.getString(type+".Button.url1"),
                    yml.getString(type+".Button.url2"),
                    yml.getInt(type+".Button.x"),
                    yml.getInt(type+".Button.y"),
                    yml.getInt(type+".Button.w"),
                    yml.getInt(type+".Button.h"),(player)->{
                SVEquipment.getPlayerisequip().put(player.getUniqueId(),type.replace(".","#"));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.closeInventory();
                        data.setLastopendGui(gui);
                        if(ConfigData.getAddItemEquipConfig().getBoolean("EnableChest")){
                            AdditemChestGui.show(player);
                        }else{
                            AddItemGui.show(player);
                        }
                    }
                }.runTaskAsynchronously(SVEquipment.getPlugin());
            },buttontext);
            gui.addComponent(button);
        }
    }
}

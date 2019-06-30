package me.wuxie.sv.svequipment.gui;

import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexInventoryGui;

import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexHoverText;
import lk.vexview.gui.components.VexSlot;
import me.wuxie.sv.svequipment.SVEquipment;
import me.wuxie.sv.svequipment.util.ConfigData;

import me.wuxie.sv.svequipment.util.Message;
import me.wuxie.sv.svequipment.util.PlayerData;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * 代表一个InventoryGUI界面
 * */
public class AddItemGui{
    private static YamlConfiguration yml =ConfigData.getAddItemEquipConfig();
    public AddItemGui(){
    }
    /**
     * 向玩家展示这个GUI界面
     * */
    public static void show(Player player){
        YamlConfiguration yml =ConfigData.getAddItemEquipConfig();
           VexInventoryGui gui = new VexInventoryGui(
              yml.getString("url"),
              yml.getInt("x"),
              yml.getInt("y"),
              yml.getInt("w"),
              yml.getInt("h"),
              yml.getInt("w"),
              yml.getInt("h"),
              yml.getInt("slotleft"),
              yml.getInt("slottop"));
           VexSlot slot = getAddItemSlot();
           gui.addComponent(slot);
           List<String> list1 = yml.getStringList("yesButton.text");
            List<String> list2 = yml.getStringList("noButton.text");
            VexHoverText text1 =new VexHoverText(list1==null?new ArrayList<>():list1,
                    yml.getInt("yesButton.x"),
                    yml.getInt("yesButton.y"),
                    yml.getInt("yesButton.w"),
                    yml.getInt("yesButton.h"));
            VexHoverText text2 =new VexHoverText(list2==null?new ArrayList<>():list2,
                    yml.getInt("noButton.x"),
                    yml.getInt("noButton.y"),
                    yml.getInt("noButton.w"),
                    yml.getInt("noButton.h"));
           VexButton button = new VexButton(
                   yml.getInt("yesButton.id"),
                   yml.getString("yesButton.name"),
                   yml.getString("yesButton.url1"),
                   yml.getString("yesButton.url2"),
                   yml.getInt("yesButton.x"),
                   yml.getInt("yesButton.y"),
                   yml.getInt("yesButton.w"),
                   yml.getInt("yesButton.h"),
                   p -> {
               if(!slot.getItem().getType().equals(Material.AIR)){
                   if(slot.getItem().getAmount()==1){
                       PlayerData data = SVEquipment.getPlayerdata().get(p.getUniqueId());
                       if(data.setEquip(SVEquipment.getPlayerisequip().get(p.getUniqueId()),slot.getItem())){
                           removeItem(p.getInventory(),slot.getItem());
                           p.closeInventory();
                           new BukkitRunnable(){
                               @Override
                               public void run() {
                                   new ShowEquipmentGui(data).show();
                               }
                           }.runTaskLaterAsynchronously(SVEquipment.getPlugin(),5);
                       }
                   }else{
                       Message.send(p,ConfigData.getConfig().getString("Message.amount_big"));
                   }
                }else{
                   Message.send(p,ConfigData.getConfig().getString("Message.amount_zero"));
               }
            },text1);
            gui.addComponent(button);
            VexButton button2 = new VexButton(
                    yml.getInt("noButton.id"),
                    yml.getString("noButton.name"),
                    yml.getString("noButton.url1"),
                    yml.getString("noButton.url2"),
                    yml.getInt("noButton.x"),
                    yml.getInt("noButton.y"),
                    yml.getInt("noButton.w"),
                    yml.getInt("noButton.h"),
                    (p)->{
                        PlayerData data =SVEquipment.getPlayerdata().get(p.getUniqueId());
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                gui.setClosable(true);
                                new ShowEquipmentGui(data).show();
                            }
                        }.runTaskLaterAsynchronously(SVEquipment.getPlugin(),5);
                    });
            gui.addComponent(button2);
            VexViewAPI.openGui(player,gui);
    }

    /**
     *
     * @return 添加物品的孔
     */
    private static VexSlot getAddItemSlot(){
        int id=0;
        int x=yml.getInt("Slot.x");
        int y=yml.getInt("Slot.y");
        ItemStack item= new ItemStack(Material.AIR);
        return new VexSlot(id,x,y,item);
    }

    /**
     *
     * @param inv 玩家背包
     * @param itemStack 要删除的物品(数量 -1)
     */
    private static void removeItem(Inventory inv, ItemStack itemStack){
        ItemStack itemClone = itemStack.clone();
        itemClone.setAmount(1);
        for(int i = 0; i< 1; i++){
            inv.removeItem(itemClone);
        }
    }
}
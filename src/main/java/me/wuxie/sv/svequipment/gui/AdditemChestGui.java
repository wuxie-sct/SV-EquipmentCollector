package me.wuxie.sv.svequipment.gui;

import me.wuxie.sv.svequipment.SVEquipment;
import me.wuxie.sv.svequipment.util.ConfigData;
import me.wuxie.sv.svequipment.util.ItemStackUtil;
import me.wuxie.sv.svequipment.util.Message;
import me.wuxie.sv.svequipment.util.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class AdditemChestGui implements Listener {
    public static void show(Player player){
        player.openInventory(additemGui());
    }
    public static Inventory additemGui(){
        Inventory inv = Bukkit.createInventory(null,36,"§7§8§9§7§8§9");
        ItemStack zs = ItemStackUtil.createItem(" ",160,7,1,new ArrayList<>(),"false","false");
        ItemStack button = ItemStackUtil.createItem("§a[§b点击穿上§a]",388,0,1,new ArrayList<>(),"false","false");
        for(int a=0;a<36;a++){
            inv.setItem(a,zs);
        }
        inv.setItem(13,null);
        inv.setItem(22,button);
        return inv;
    }
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getInventory()==null||e.getClickedInventory()==null){ return; }
        if(e.getClickedInventory() instanceof PlayerInventory){ return; }
        if(!e.getClickedInventory().getTitle().equalsIgnoreCase("§7§8§9§7§8§9")){ return; }
        ItemStack zs = ItemStackUtil.createItem(" ",160,7,1,new ArrayList<>(),"false","false");
        ItemStack button = ItemStackUtil.createItem("§a[§b点击穿上§a]",388,0,1,new ArrayList<>(),"false","false");
        if(e.getCurrentItem().equals(zs)||e.getCurrentItem().equals(button)){ e.setCancelled(true); }
        if(e.getSlot()==22){
            if(e.getInventory().getItem(13)!=null&&!e.getInventory().getItem(13).getType().equals(Material.AIR)){
                if(e.getInventory().getItem(13).getAmount()==1){
                    PlayerData data = SVEquipment.getPlayerdata().get(e.getWhoClicked().getUniqueId());
                    if(data.setEquip(SVEquipment.getPlayerisequip().get(e.getWhoClicked().getUniqueId()),e.getInventory().getItem(13))){
                        e.getClickedInventory().setItem(13,null);
                        e.getWhoClicked().closeInventory();
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                new ShowEquipmentGui(data).show();
                            }
                        }.runTaskAsynchronously(SVEquipment.getPlugin());
                    }
                }else{
                    Message.send((Player) e.getWhoClicked(), ConfigData.getConfig().getString("Message.amount_big"));
                }
            }else{
                Message.send((Player) e.getWhoClicked(),ConfigData.getConfig().getString("Message.amount_zero"));
            }
        }
    }
}

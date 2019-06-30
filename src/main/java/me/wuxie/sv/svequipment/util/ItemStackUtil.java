package me.wuxie.sv.svequipment.util;

import me.wuxie.sv.svequipment.SVEquipment;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackUtil {

    public static ItemStack createItem(String name, int id, int data, List<String> lore, List<String> enchantment, List<String> flags, String unBreak){
        ItemStack item =new ItemStack(Material.getMaterial(id),1,(short)data);
        ItemMeta meta =item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        for(String flag:flags){
            meta.addItemFlags(ItemFlag.valueOf(flag));
        }
        if(unBreak.equalsIgnoreCase("true")){
            if(SVEquipment.getServerVersion()<11){
                meta.spigot().setUnbreakable(true);
            }else{
                meta.setUnbreakable(true);
            }
        }

        item.setItemMeta(meta);
        Map<Enchantment,Integer> EnchantmentMap=new HashMap<>();
        for(String ench:enchantment){
            EnchantmentMap.put(Enchantment.getByName(ench.split(" ")[0]),Integer.parseInt(ench.split(" ")[1]));
        }
        item.addUnsafeEnchantments(EnchantmentMap);
        return item;
    }

    public static ItemStack createItem(String name, int id, int data,int amount, List<String> lore, String enchantment, String unBreak){
        ItemStack item =new ItemStack(Material.getMaterial(id),1,(short)data);
        ItemMeta meta =item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if(unBreak.equalsIgnoreCase("true")){
            if(SVEquipment.getServerVersion()<11){
                meta.spigot().setUnbreakable(true);
            }else{
                meta.setUnbreakable(true);
            }
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        if(enchantment.equalsIgnoreCase("true")){
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,1);
        }
        item.setAmount(amount);
        return item;
    }
}

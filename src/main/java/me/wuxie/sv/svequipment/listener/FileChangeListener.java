package me.wuxie.sv.svequipment.listener;

import me.wuxie.myplugin.datasharing.FileChangeEvent;
import me.wuxie.sv.svequipment.SVEquipment;
import me.wuxie.sv.svequipment.util.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FileChangeListener implements Listener {
    @EventHandler
    public void onFileChange(FileChangeEvent event){
        if(event.getPlugin().equals(SVEquipment.getPlugin())){
            Bukkit.getConsoleSender().sendMessage("[SV-Equipment] 检测到配置发生文件变化，正在重载插件配置！");
            ConfigData.getAddItemGuiPluginYaml().fileUpdateRunCancel();
            ConfigData.getConfigPluginYaml().fileUpdateRunCancel();
            ConfigData.getEquipShowGuiPluginYaml().fileUpdateRunCancel();
            ConfigData.getSuitDataPluginYaml().fileUpdateRunCancel();
            ConfigData.loadConfig();
            Bukkit.getConsoleSender().sendMessage("§athe plugin is reload !");
        }
    }
}

package me.wuxie.sv.svequipment.util;

import lombok.Getter;
import me.wuxie.myplugin.datasharing.PluginYaml;
import me.wuxie.sv.svequipment.SVEquipment;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;

public class ConfigData {
    @Getter
    private static YamlConfiguration addItemEquipConfig;
    @Getter
    private static YamlConfiguration showEquipmentConfig;
    @Getter
    private static YamlConfiguration config;
    @Getter
    private static YamlConfiguration SuitDataConfig;
    @Getter
    static File filePath;
    @Getter
    static File configFile;
    @Getter
    static File addItemGuiFile;
    @Getter
    static File equipShowGuiFile;
    @Getter
    static File SuitDataFile;
    @Getter
    static PluginYaml configPluginYaml;
    @Getter
    static PluginYaml addItemGuiPluginYaml;
    @Getter
    static PluginYaml equipShowGuiPluginYaml;
    @Getter
    static PluginYaml SuitDataPluginYaml;
    public static void loadConfig(){
        if(SVEquipment.isDataSharing()){
            filePath=new PluginYaml(SVEquipment.getPlugin()).getFile(null,false);
            if(filePath.exists()){
                configPluginYaml =new PluginYaml(SVEquipment.getPlugin());
                addItemGuiPluginYaml =new PluginYaml(SVEquipment.getPlugin());
                equipShowGuiPluginYaml =new PluginYaml(SVEquipment.getPlugin());
                SuitDataPluginYaml =new PluginYaml(SVEquipment.getPlugin());

                configFile =configPluginYaml.getFile("config.yml",true);
                if(!configFile.exists()){
                    defaultConfig();
                    configFile =configPluginYaml.getFile("config.yml",true);
                    try {
                        config.save(configFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                addItemGuiFile =addItemGuiPluginYaml.getFile("AddItemGui.yml",true);
                if(!addItemGuiFile.exists()){
                    defaultAddItemEquipConfig();
                    addItemGuiFile =addItemGuiPluginYaml.getFile("AddItemGui.yml",true);
                    try {
                        addItemEquipConfig.save(addItemGuiFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                equipShowGuiFile =equipShowGuiPluginYaml.getFile("EquipShowGui.yml",true);
                if(!equipShowGuiFile.exists()){
                    defaultShowEquipmentConfig();
                    equipShowGuiFile =equipShowGuiPluginYaml.getFile("EquipShowGui.yml",true);
                    try {
                        showEquipmentConfig.save(equipShowGuiFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                SuitDataFile =SuitDataPluginYaml.getFile("SuitData.yml",true);
                if(!SuitDataFile.exists()){
                    defaultSuitDataConfig();
                    SuitDataFile =SuitDataPluginYaml.getFile("SuitData.yml",true);
                    try {
                        SuitDataConfig.save(SuitDataFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                config = YamlConfiguration.loadConfiguration(configFile);
                addItemEquipConfig = YamlConfiguration.loadConfiguration(addItemGuiFile);
                showEquipmentConfig = YamlConfiguration.loadConfiguration(equipShowGuiFile);
                SuitDataConfig =YamlConfiguration.loadConfiguration(SuitDataFile);
            }else{
                load();
                configPluginYaml =new PluginYaml(SVEquipment.getPlugin());
                addItemGuiPluginYaml =new PluginYaml(SVEquipment.getPlugin());
                equipShowGuiPluginYaml =new PluginYaml(SVEquipment.getPlugin());
                SuitDataPluginYaml =new PluginYaml(SVEquipment.getPlugin());

                configFile =configPluginYaml.getFile("config.yml",true);
                addItemGuiFile =addItemGuiPluginYaml.getFile("AddItemGui.yml",true);
                equipShowGuiFile =equipShowGuiPluginYaml.getFile("EquipShowGui.yml",true);
                SuitDataFile =SuitDataPluginYaml.getFile("SuitData.yml",true);
                try {
                    config.save(configFile);
                    addItemEquipConfig.save(addItemGuiFile);
                    showEquipmentConfig.save(equipShowGuiFile);
                    SuitDataConfig.save(SuitDataFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            load();
        }
    }
    private static void defaultConfig(){
        configFile = new File("plugins/SV-Equipment/config.yml");
        if (!SVEquipment.getPlugin().getDataFolder().exists()) {
            SVEquipment.getPlugin().getDataFolder().mkdir();
        }
        if (!configFile.exists()) {
            SVEquipment.getPlugin().saveDefaultConfig();
        }
        config =YamlConfiguration.loadConfiguration(configFile);
    }
    private static void defaultAddItemEquipConfig(){
        addItemGuiFile = new File("plugins/SV-Equipment/AddItemGui.yml");
        if (!SVEquipment.getPlugin().getDataFolder().exists()) {
            SVEquipment.getPlugin().getDataFolder().mkdir();
        }
        if (!addItemGuiFile.exists()) {
            SVEquipment.getPlugin().saveResource("AddItemGui.yml", true);
        }
        addItemEquipConfig =YamlConfiguration.loadConfiguration(addItemGuiFile);
    }
    private static void defaultShowEquipmentConfig(){
        equipShowGuiFile = new File("plugins/SV-Equipment/EquipShowGui.yml");
        if (!SVEquipment.getPlugin().getDataFolder().exists()) {
            SVEquipment.getPlugin().getDataFolder().mkdir();
        }
        if (!equipShowGuiFile.exists()) {
            SVEquipment.getPlugin().saveResource("EquipShowGui.yml", true);
        }
        showEquipmentConfig =YamlConfiguration.loadConfiguration(equipShowGuiFile);
    }
    private static void defaultSuitDataConfig(){
        SuitDataFile = new File("plugins/SV-Equipment/SuitData.yml");
        if (!SVEquipment.getPlugin().getDataFolder().exists()) {
            SVEquipment.getPlugin().getDataFolder().mkdir();
        }
        if (!SuitDataFile.exists()) {
            SVEquipment.getPlugin().saveResource("SuitData.yml", true);
        }
        SuitDataConfig =YamlConfiguration.loadConfiguration(SuitDataFile);
    }
    public static void load(){
        configFile = new File("plugins/SV-Equipment/config.yml");
        addItemGuiFile = new File("plugins/SV-Equipment/AddItemGui.yml");
        equipShowGuiFile = new File("plugins/SV-Equipment/EquipShowGui.yml");
        SuitDataFile = new File("plugins/SV-Equipment/SuitData.yml");
        if (!SVEquipment.getPlugin().getDataFolder().exists()) {
            SVEquipment.getPlugin().getDataFolder().mkdir();
        }
        if (!configFile.exists()) {
            SVEquipment.getPlugin().saveDefaultConfig();
        }
        if (!addItemGuiFile.exists()) {
            SVEquipment.getPlugin().saveResource("AddItemGui.yml", true);
        }
        if (!equipShowGuiFile.exists()) {
            SVEquipment.getPlugin().saveResource("EquipShowGui.yml", true);
        }
        if (!SuitDataFile.exists()) {
            SVEquipment.getPlugin().saveResource("SuitData.yml", true);
        }
        config =YamlConfiguration.loadConfiguration(configFile);
        addItemEquipConfig =YamlConfiguration.loadConfiguration(addItemGuiFile);
        showEquipmentConfig =YamlConfiguration.loadConfiguration(equipShowGuiFile);
        SuitDataConfig =YamlConfiguration.loadConfiguration(SuitDataFile);
    }
}

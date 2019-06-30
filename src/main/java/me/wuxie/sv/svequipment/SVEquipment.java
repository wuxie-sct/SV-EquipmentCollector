package me.wuxie.sv.svequipment;

import lombok.Getter;
import me.wuxie.sv.svequipment.gui.AdditemChestGui;
import me.wuxie.sv.svequipment.gui.ShowEquipmentGui;
import me.wuxie.sv.svequipment.keyboardpress.KeyBoardPressListener;
import me.wuxie.sv.svequipment.listener.FileChangeListener;
import me.wuxie.sv.svequipment.listener.OnListener;
import me.wuxie.sv.svequipment.listener.SXOnLoadItemDataListener;
import me.wuxie.sv.svequipment.suit.SuitData;
import me.wuxie.sv.svequipment.util.ConfigData;
import me.wuxie.sv.svequipment.util.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SVEquipment extends JavaPlugin {
    @Getter
    private static JavaPlugin plugin;
    public static boolean sxGemIsEnable =false;
    @Getter
    private static int serverVersion;
    @Getter
    private static Map<UUID, PlayerData> playerdata =new HashMap<>();
    @Getter
    private static Map<UUID, SuitData> suitData =new HashMap<>();
    @Getter
    private static Map<UUID, String> playerisequip =new HashMap<>();
    @Getter
    private static boolean dataSharing=false;
    private static boolean isenable=false;
    @Override
    public void onEnable() {
        plugin = this;
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase("DataSharing")) {
                Bukkit.getConsoleSender().sendMessage("§a无邪版跨服同步前置插件运行！");
                Bukkit.getConsoleSender().sendMessage("§a同台机器所有服务器此插件数据实现同步共享！");
                dataSharing = true;
                break;
            }
        }
        String Version = Bukkit.getBukkitVersion().split("-")[0].replace(" ", "");
        serverVersion = Integer.parseInt(Version.split("[.]")[1]);
        Bukkit.getConsoleSender().sendMessage("§a欢迎使用 §evv装备栏");
        ConfigData.loadConfig();
        Bukkit.getPluginManager().registerEvents(new OnListener(), this);
        Bukkit.getPluginManager().registerEvents(new KeyBoardPressListener(), this);
        Bukkit.getPluginManager().registerEvents(new SXOnLoadItemDataListener(), this);
        Bukkit.getPluginManager().registerEvents(new AdditemChestGui(), this);
        if (dataSharing) {
            Bukkit.getPluginManager().registerEvents(new FileChangeListener(), this);
        }
        Bukkit.getPluginCommand("sve").setExecutor(this);
        /*
        if (Bukkit.getPluginManager().isPluginEnabled("SxGem")) {
            Bukkit.getConsoleSender().sendMessage("§aSxGem is Enable!!");
            sxGemIsEnable = true;
        }*/
        gp();
    }
    @Override
    public void onDisable() {
        dt();
        isenable =false;
        playerdata = new HashMap<>();
        playerisequip = new HashMap<>();
        suitData =new HashMap<>();
    }

    private void dt() {
        if(dataSharing&&isenable) {
            ConfigData.getAddItemGuiPluginYaml().fileUpdateRunCancel();
            ConfigData.getConfigPluginYaml().fileUpdateRunCancel();
            ConfigData.getEquipShowGuiPluginYaml().fileUpdateRunCancel();
            ConfigData.getSuitDataPluginYaml().fileUpdateRunCancel();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(command.getName().equalsIgnoreCase("sve")){
                if(args.length>0&&args[0].equals("reload")){
                    if(sender.isOp()||sender.hasPermission("sve.admin")||sender instanceof Server) {
                        dt();
                        ConfigData.loadConfig();
                        playerdata = new HashMap<>();
                        suitData = new HashMap<>();
                        gp();
                        sender.sendMessage("§athe plugin is reload !");
                        return true;
                    }else{
                        sender.sendMessage("§4你没有权限！");
                        return true;
                    }
                }else if(args.length>0&&args[0].equals("open")){
                    if(sender instanceof Player) {
                        if (args.length==1) {
                            new ShowEquipmentGui(SVEquipment.getPlayerdata().get(((Player) sender).getUniqueId())).show();
                        }else{
                            if(sender.isOp()){
                                if(args[1]!=null){
                                    Player player =Bukkit.getPlayer(args[1]);
                                    if(player!=null&&player.isOnline()){
                                        new ShowEquipmentGui(SVEquipment.getPlayerdata().get(player.getUniqueId())).show();
                                        sender.sendMessage("§a你为 "+player.getName()+" 打开了vv装备栏！");
                                        return true;
                                    }
                                }
                            }else{
                                sender.sendMessage("§4你没有权限！");
                                return true;
                            }
                        }
                    }else{
                        if(args.length>1&&args[1]!=null){
                            Player player =Bukkit.getPlayer(args[1]);
                            if(player!=null&&player.isOnline()){
                                new ShowEquipmentGui(SVEquipment.getPlayerdata().get(player.getUniqueId())).show();
                                return true;
                            }
                        }
                    }
                }else{
                    if(sender.isOp()||sender.hasPermission("sve.admin")||sender instanceof Server){
                        sender.sendMessage("§7-----------------------");
                        sender.sendMessage("§7/sve reload 重载插件！");
                        sender.sendMessage("§7/sve open <player> 打开gui！");
                        sender.sendMessage("§7-----------------------");
                    }else{
                        sender.sendMessage("§7-----------------------");
                        sender.sendMessage("§7/sve open 打开gui！");
                        sender.sendMessage("§7-----------------------");
                    }
                    return true;
                }
                return true;
            }
        return false;
        }

    private void gp() {
        for(Player player: Bukkit.getOnlinePlayers()){
            playerdata.put(player.getUniqueId(),new PlayerData(player));
            playerdata.get(player.getUniqueId()).getPlayerData();
            if(ConfigData.getConfig().getBoolean("EnableSuit")){
                SVEquipment.getSuitData().put(player.getUniqueId(),new SuitData(player));
                SVEquipment.getSuitData().get(player.getUniqueId()).getSuitList();
            }
        }
    }
}

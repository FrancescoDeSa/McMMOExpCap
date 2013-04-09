package com.FrancescoDeSa;

import java.io.File;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class McMMOCap extends JavaPlugin {
	
    @Override 
    public void onEnable(){
    
		McMMOCap.logger = getLogger();
		logger.info("McMMOCap enabled");
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		logger.info("Plugin folder: "+pluginFolder);
		(new File(pluginFolder)).mkdirs();
		listener = new McMMOListener(this);
		
		File mcapFile = new File(pluginFolder+File.separator+"McMMOCap.dat");
		session = new mcapSession(mcapFile);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(listener,this);
		if (!setupPermissions() ) {
            logger.warning("WARNING: Can't link to vault. Disabling plugin McMMOCap...");
            pm.disablePlugin(this);
            return;
        }
		McMMOCap.settings = new mcapSettings(this);
    }
 
    @Override
    public void onDisable() {
		session.save();
    	logger.info("McMMOCap disabled");
		
    }
    
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
    
    public boolean canGainXP(Player player, int amount){
    	//OP control
    	if(player.hasPermission("mcap.exempt"))return true;
    	if(player.isOp() && settings.noCapForOp == true)return true;
    	//Player cap control
    	if(settings.playerAssociation.containsKey(player) && session.Check(player,settings.playerAssociation.get(player), amount))return true;
    	//Group cap control
    	String[] groups = permission.getPlayerGroups(player);
    	for(String group : groups){
        	if(settings.groupAssociation.containsKey(group)){
        		if(session.Check(player, settings.groupAssociation.get(group), amount)) return true;
        		break;
        	}
    	}
    	
    	return false;
    }
    public static Logger logger;
    public static Permission permission;
    public static mcapSettings settings;
    public static McMMOListener listener;
    public mcapSession session;
}
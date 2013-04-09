package com.FrancescoDeSa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class mcapSettings {
	private FileConfiguration config;
	
	public boolean noCapForOp;
	public Map<Player, Integer> playerAssociation = new HashMap<Player, Integer>();
	public Map<String, Integer> groupAssociation = new HashMap<String, Integer>();
	
	public mcapSettings(McMMOCap plugin){
		this.config = plugin.getConfig();
		noCapForOp = config.getBoolean("settings.nocapforop");
		List<String> players = config.getStringList("caps.users");
		Iterator<String> it = players.iterator();
		while(it.hasNext()){
			playerAssociation.put(Bukkit.getPlayer(it.next()), config.getInt("caps.users."+it.next()));
		}

		String[] groups = config.getStringList("caps.groups").toArray(new String[0]);
		for(String gruppo : groups){
			groupAssociation.put(gruppo, config.getInt("caps.groups."+gruppo));
		}
	}
}

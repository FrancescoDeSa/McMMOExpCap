package com.FrancescoDeSa;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;

public class McMMOListener implements Listener{
	
	private McMMOCap plugin;
	
	public McMMOListener(McMMOCap plugin){
		this.plugin = plugin;
	}
	@EventHandler
	public void onXpGain (McMMOPlayerXpGainEvent event){
		Player player = event.getPlayer();
		if(!plugin.canGainXP(player,event.getXpGained())){
			event.setCancelled(true);
		}
		else plugin.session.register(player,event.getXpGained());
	}
}

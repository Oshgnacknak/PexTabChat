package de.oshgnacknak.PexTabChat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.md_5.bungee.api.ChatColor;

public class EventListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	void onChat(AsyncPlayerChatEvent e) {
		if (Config.global.chatformat.enabled) {
			e.setFormat(Main.formatChat(e.getPlayer(), Config.global.chatformat.format, e.getMessage()));
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if (Config.global.joinquit.enabled) {
			e.setJoinMessage(Main.formatMSG(p, Config.global.joinquit.join));
		}
		
		if (Config.global.tablisttext.enabled) {
			String header = ChatColor.translateAlternateColorCodes('&', Config.global.tablisttext.header);
			String footer = ChatColor.translateAlternateColorCodes('&', Config.global.tablisttext.footer);
			TablistManager.sendTabHeaderAndFooter(p, header , footer);
		} 
		
		if (Config.global.tablistnames) {
			TablistManager.global.addPlayer(p);
		} 
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	void onQuit(PlayerQuitEvent e) {
		if (Config.global.joinquit.enabled) {
			e.setQuitMessage(Main.formatMSG(e.getPlayer(), Config.global.joinquit.quit));
		}
	}
}

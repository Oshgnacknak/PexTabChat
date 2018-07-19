package de.oshgnacknak.PexTabChat;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Main extends JavaPlugin {

	static private Main plugin;

	@Override
	public void onEnable() {
		if (!getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
			System.err.println("PexTabChat: PermissionsEx not enabled, disabling.");
			getServer().getPluginManager().disablePlugin(this);
		}
		
		plugin = this;

		getServer().getPluginManager().registerEvents(new EventListener(), this);

		try {
			Config.global.load();
		} catch (Exception e) {
			System.err.println("PexTabChat: Cannot load config, disabling.");
			e.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}

		TablistManager.global.update();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("pextabchatreload")) {
			try {
				reloadConfig();
				Config.global.load();
				sender.sendMessage(ChatColor.GREEN + "PexTabChat: reloaded.");
			} catch (IOException | InvalidConfigurationException e) {
				sender.sendMessage(ChatColor.RED + "PexTabChat: Cannot load config.");
				e.printStackTrace();
			}

			TablistManager.global.update();
		}
		return true;
	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	static Main getPlugin() {
		return plugin;
	}

	public static String formatMSG(Player p, String format) {
		PermissionUser u = PermissionsEx.getUser(p);

		format = format.replaceAll("\\{prefix\\}", u.getPrefix());
		format = format.replaceAll("\\{suffix\\}", u.getSuffix());
		format = format.replaceAll("\\{display\\}", p.getDisplayName());
		format = format.replaceAll("\\{name\\}", p.getName());

		return ChatColor.translateAlternateColorCodes('&', format);
	}

	public static String formatChat(Player p, String format, String message) {

		format = formatMSG(p, format);

		format = format.replaceAll("\\{message\\}",
				p.hasPermission("pextabchat.color") ? ChatColor.translateAlternateColorCodes('&', message) : message);

		return format;
	}
}

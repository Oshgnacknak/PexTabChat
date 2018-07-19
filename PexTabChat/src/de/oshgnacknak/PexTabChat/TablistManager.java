package de.oshgnacknak.PexTabChat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import ru.tehkode.libs.com.google.gson.JsonParseException;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class TablistManager {

	// SIGLETON
	public static final TablistManager global = new TablistManager();

	private Scoreboard sb;

	private HashMap<String, String> sbTeams = new HashMap<>();

	private TablistManager() {
	}

	public void update() {
		sb = Main.getPlugin().getServer().getScoreboardManager().getNewScoreboard();

		List<PermissionGroup> groups = new ArrayList<PermissionGroup>(PermissionsEx.getPermissionManager().getGroupList());
		groups.sort(new Comparator<PermissionGroup>() {
			public int compare(PermissionGroup a, PermissionGroup b) {
				return a.getRank() - b.getRank();
			};
		});

		for (int i = 0; i < groups.size(); ++i) {
			PermissionGroup g = groups.get(i);
			
			String tName = String.format("%010d", i);
			
			sb.registerNewTeam(tName);
			sb.getTeam(tName).setPrefix(ChatColor.translateAlternateColorCodes('&', g.getPrefix()));
			sb.getTeam(tName).setSuffix(ChatColor.translateAlternateColorCodes('&', g.getSuffix()));
			
			sbTeams.put(g.getName(), tName);
		}
	}
	
	@SuppressWarnings({ "deprecation" })
	public void addPlayer(Player p) {
		PermissionUser u = PermissionsEx.getUser(p);
		
		sb.getTeam(sbTeams.get(u.getGroups()[0].getName())).addPlayer(p);
		System.out.println("Groups: " + Arrays.toString(u.getGroupNames()));
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setScoreboard(sb);
		}
		
	}

	public static void sendTabHeaderAndFooter(Player p, String header, String footer) {

		PacketPlayOutPlayerListHeaderFooter tabPacket = null;
		try {
			IChatBaseComponent tabHeader = ChatSerializer.a("{'text':'" + header + "'}");
			IChatBaseComponent tabFooter = ChatSerializer.a("{'text':'" + footer + "'}");

			tabPacket = new PacketPlayOutPlayerListHeaderFooter(tabHeader);
			Field field = tabPacket.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(tabPacket, tabFooter);
		} catch (JsonParseException e) {
			System.err.println(
					"Cannot changer headers and footers. Please only use this syntax(header: '\n&cbla\n' within config.yml's tablist section");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tabPacket != null) {
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(tabPacket);
			}
		}
	}

}

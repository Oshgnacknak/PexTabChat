package de.oshgnacknak.PexTabChat;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

//This class is for loading and holding the config.yml in the working memory

public class Config {
	// SIGLETON
	public static final Config global = new Config();

	private Config() {
	}

	public TablistText tablisttext;
	public Format chatformat;
	public boolean tablistnames;
	public Joinquit joinquit;

	public void load() throws IOException, InvalidConfigurationException {
		File file = new File("plugins/" + Main.getPlugin().getName() + "/config.yml");
		
		file.getParentFile().mkdirs();
		if (!file.exists()) {
			Main.getPlugin().getConfig().options().copyDefaults(true);
			Main.getPlugin().saveConfig();
		}
		
		YamlConfiguration config = new YamlConfiguration();
		config.load(file);
		
		tablisttext = new TablistText(config.getBoolean("tablisttext.enabled"), config.getString("tablisttext.header"),
				config.getString("tablisttext.footer"));

		chatformat = new Format(config.getBoolean("chatformat.enabled"), config.getString("chatformat.format"));

		tablistnames = config.getBoolean("tablistnames");

		joinquit = new Joinquit(config.getBoolean("joinquit.enabled"), config.getString("joinquit.join"),
				config.getString("joinquit.quit"));
	}

	public class TablistText {
		public final boolean enabled;
		public final String header;
		public final String footer;

		public TablistText(boolean enabled, String header, String footer) {
			this.enabled = enabled;
			this.header = header;
			this.footer = footer;
		}
	}

	public class Format { // For chatformat
		public final boolean enabled;
		public final String format;

		public Format(boolean enabled, String format) {
			this.enabled = enabled;
			this.format = format;
		}
		
		@Override
			public String toString() {
				return "enabled=" + enabled + " format" + format;
			}
	}

	public class Joinquit {
		public final boolean enabled;
		public final String join;
		public final String quit;

		public Joinquit(boolean enabled, String join, String quit) {
			this.enabled = enabled;
			this.join = join;
			this.quit = quit;
		}
	}
}

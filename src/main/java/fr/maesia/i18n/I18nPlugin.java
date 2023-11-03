package fr.maesia.i18n;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

public class I18nPlugin extends JavaPlugin {
	private static final Logger LOGGER = Logger.getLogger("I18nPlugin");
	private NamespacedKey localeKey = null;
	
	private I18n i18n = null;
	private List<Object> availabaleLocale = new ArrayList<>();
	
	public I18nPlugin() {
		super(); 
	}
	
	/**
	 * Used by MockBukkit for UnitTest
	 */
	public I18nPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}
	
	@Override
	public void onEnable() {
		I18nPlugin.INSTANCE = this;
		
		this.saveDefaultConfig();
		Configuration config = this.getConfig();
		
		Object configDefaultLocale = config.getString("locale.default", "fr_FR");
		
		this.availabaleLocale.addAll(config.getStringList("locale.available"));
		if(this.availabaleLocale.isEmpty())
			this.availabaleLocale.add(I18nProvider.get().getDefaultLocale().toString());
		
		I18nProvider.get().setDefaultLocale(configDefaultLocale);
		
		this.i18n = I18nProvider.get().getI18n(this);
		
		try {
			I18nProvider.get().loadTranslation(this, "/fr_FR.properties");
			//I18nProvider.get().loadTranslation(this, "/en_US.properties");
		} catch (IOException e) {
			LOGGER.severe("Can't load language files !");
			e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		this.localeKey = new NamespacedKey(this, "i18n_locale");
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this, this.i18n, this.localeKey, this.availabaleLocale), this);
		this.getCommand("localeset").setExecutor(new LocaleSetCommand(this, this.localeKey, this.availabaleLocale));
	}
	
	public List<Object> getAvailabaleLocale() {
		return Collections.unmodifiableList(this.availabaleLocale);
	}
	
	public static String tl(String key, Object... replaces) {
		return I18nPlugin.INSTANCE.i18n.translate(key, replaces);
	}
	
	public static String tl(Player player, String key, Object... replaces) {
		return I18nPlugin.INSTANCE.i18n.translate(player, key, replaces);
	}
	
	private static I18nPlugin INSTANCE = null;
}

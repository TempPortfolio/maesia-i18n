package fr.maesia.i18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class I18nPlugin extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {
	private static final Logger LOGGER = Logger.getLogger("I18nPlugin");
	private NamespacedKey localeKey = null;
	
	private I18n i18n = null;
	private List<String> availabaleLocale = new ArrayList<>();
	
	@Override
	public void onEnable() {
		I18nPlugin.INSTANCE = this;
		this.saveDefaultConfig();
		Configuration config = this.getConfig();
		
		Object configDefaultLocale = config.getString("locale.default", "fr_FR");
		
		try {
			this.i18n = I18nProvider.get().getI18n(this);
			I18nProvider.get().setDefaultLocale(configDefaultLocale);
			
			I18nProvider.get().loadTranslation(this, "/fr_FR.properties");
		} catch (IOException e) {
			LOGGER.severe("Can't load language files !");
			e.printStackTrace();
			this.setEnabled(false);
			return;
		}
		
		this.availabaleLocale.addAll(config.getStringList("locale.available"));
		if(this.availabaleLocale.isEmpty())
			this.availabaleLocale.add(I18nProvider.get().getDefaultLocale().toString());
		
		this.localeKey = new NamespacedKey(this, "i18n_locale");
		
		Bukkit.getPluginManager().registerEvents(this, this);
		this.getCommand("localeset").setExecutor(this);
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		PersistentDataContainer container = player.getPersistentDataContainer();
		Object locale = container.get(this.localeKey, PersistentDataType.STRING);
		
		if(locale == null)
			locale = this.i18n.getDefaultLocale();
		
		player.setMetadata("i18n_locale", new FixedMetadataValue(this, locale));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(tl("command.you_must_be_player", command.getName()));
			return true;
		}
		
		Player player = (Player) sender;
		
		if(args.length < 1) {
			player.sendMessage(tl(player, "command.localeset.no_locale"));
			return true;
		}
		
		String locale = args[0];
		
		if(!this.availabaleLocale.contains(locale)) {
			player.sendMessage(tl(player, "command.localeset.not_available", locale));
			return true;
		}
		
		PersistentDataContainer container = player.getPersistentDataContainer();
		container.set(this.localeKey, PersistentDataType.STRING, locale);
		player.setMetadata("i18n_locale", new FixedMetadataValue(this, locale));
		
		player.sendMessage(tl(player, "command.localeset.success", locale));
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tabs = new ArrayList<>();
		
		if(args.length == 1)
			tabs.addAll(this.availabaleLocale);
		
		return tabs;
	}
	
	public I18n getI18n() {
		return this.i18n;
	}
	
	public static String tl(String key, Object... replaces) {
		return I18nPlugin.get().getI18n().translate(key, replaces);
	}
	
	public static String tl(Player player, String key, Object... replaces) {
		return I18nPlugin.get().getI18n().translate(player, key, replaces);
	}
	
	private static I18nPlugin INSTANCE = null;
	public static I18nPlugin get() {
		return I18nPlugin.INSTANCE;
	}
}

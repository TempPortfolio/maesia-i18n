package fr.maesia.i18n;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class PlayerJoinListener implements Listener {
	private final Plugin plugin;
	private final I18n i18n;
	private final NamespacedKey localeKey;
	private final List<Object> availabaleLocale;
	
	public PlayerJoinListener(Plugin plugin, I18n i18n, NamespacedKey localeKey, List<Object> availabaleLocale) {
		this.plugin = plugin;
		this.i18n = i18n;
		this.localeKey = localeKey;
		this.availabaleLocale = availabaleLocale;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		PersistentDataContainer container = player.getPersistentDataContainer();
		Object locale = container.get(this.localeKey, PersistentDataType.STRING);
		
		if(locale == null || !this.availabaleLocale.contains(locale))
			locale = this.i18n.getDefaultLocale();
		
		player.setMetadata(this.localeKey.getKey(), new FixedMetadataValue(this.plugin, locale));
	}
	
}

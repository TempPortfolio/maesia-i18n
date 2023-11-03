package fr.maesia.i18n;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Provide translation
 */
public interface I18n {
	
	/**
	 * Get final message with player
	 * locale stored in her metadata
	 * with replaced values.
	 * 
	 * @param player
	 * @param key
	 * @param replaces
	 * @return message
	 */
	public String translate(Player player, String key, Object... replaces);
	
	/**
	 * Get final message with default
	 * locale and replaced values.
	 * 
	 * @param key
	 * @param replaces
	 * @return message
	 */
	public String translate(String key, Object... replaces);
	
	/**
	 * Get final message with correct
	 * locale and replaced values.
	 * 
	 * @param locale
	 * @param key
	 * @param replaces
	 * @return message
	 */
	public String translate(Object locale, String key, Object... replaces);
	
	/**
	 * Know if specified locale is
	 * registered.
	 * 
	 * @param locale
	 * @return true if locale is registered else false
	 */
	public boolean hasLocale(Object locale);
	
	/**
	 * Get default locale.
	 * This default locale is used when no locale
	 * was specified with translate method.
	 * 
	 * @return default locale
	 */
	public Object getDefaultLocale();
	
	/**
	 * Send a message to all online players
	 * on the server with her metadata locale.
	 * 
	 * @param key
	 * @param replaces
	 */
	public default void broadcast(String key, Object... replaces) {
		this.broadcast(Bukkit.getOnlinePlayers(), key, replaces);
	}
	
	/**
	 * Send a message to all specified players
	 * with her metadata locale.
	 * 
	 * @param players
	 * @param key
	 * @param replaces
	 */
	public default void broadcast(Collection<? extends Player> players, String key, Object... replaces) {
		players.forEach(p -> p.sendMessage(this.translate(p, key, replaces)));
	}
}

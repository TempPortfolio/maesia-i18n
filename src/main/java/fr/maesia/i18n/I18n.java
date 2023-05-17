package fr.maesia.i18n;

import org.bukkit.entity.Player;

public interface I18n {
	public String translate(Player player, String key, Object... replaces);
	
	public String translate(String key, Object... replaces);
	
	public String translate(Object locale, String key, Object... replaces);
	
	/*public default void broadcast(Object locale, Object... replaces) { //TODO w cache system
		this.broadcast(Bukkit.getOnlinePlayers(), locale, replaces);
	}
	
	public void broadcast(Collection<Player> players, Object locale, Object... replaces);*/
	
	public boolean hasLocale(Object locale);
	
	public Object getDefaultLocale();
}

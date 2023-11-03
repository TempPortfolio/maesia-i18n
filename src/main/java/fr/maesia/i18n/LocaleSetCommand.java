package fr.maesia.i18n;

import static fr.maesia.i18n.I18nPlugin.tl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * This command is used by players to set their locale.
 */
public class LocaleSetCommand implements CommandExecutor, TabExecutor {
	private final Plugin plugin;
	private final NamespacedKey localeKey;
	private final List<Object> availabaleLocale;
	
	public LocaleSetCommand(Plugin plugin, NamespacedKey localeKey, List<Object> availabaleLocale) {
		this.plugin = plugin;
		this.localeKey = localeKey;
		this.availabaleLocale = availabaleLocale;
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
		player.setMetadata(this.localeKey.getKey(), new FixedMetadataValue(this.plugin, locale));
		
		player.sendMessage(tl(player, "command.localeset.success", locale));
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tabs = new ArrayList<>();
		
		if(args.length == 1)
			tabs.addAll(this.availabaleLocale.stream().map(l -> l.toString()).toList());
		
		return tabs;
	}
	
}

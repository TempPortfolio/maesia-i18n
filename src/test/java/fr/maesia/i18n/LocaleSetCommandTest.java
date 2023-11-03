package fr.maesia.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

public class LocaleSetCommandTest {
	private ServerMock server;
	private I18nPlugin plugin;
	private NamespacedKey localeKey;
	
	@BeforeEach
	public void setUp() throws IOException {
		this.server = MockBukkit.mock();
		this.plugin = MockBukkit.load(I18nPlugin.class);
		
		this.localeKey = NamespacedKey.fromString("i18n_locale", this.plugin);
	}
	
	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}
	
	@Test
	public void testSetLocaleCommand() {
		PlayerMock player = new PlayerMock(this.server, "test");
		
		Object locale = this.plugin.getAvailabaleLocale().get(0);
		
		PluginCommand command = this.plugin.getCommand("localeset");
		command.getExecutor().onCommand(player, command, "localeset", new String[] {locale.toString()});
		
		PersistentDataContainer container = player.getPersistentDataContainer();
		Object persistentLocale = container.get(this.localeKey, PersistentDataType.STRING);
		
		assertEquals(locale, persistentLocale);
		
		Object metaLocale = player.getMetadata(this.localeKey.getKey()).get(0).value();
		
		assertEquals(locale, metaLocale);
	}
}

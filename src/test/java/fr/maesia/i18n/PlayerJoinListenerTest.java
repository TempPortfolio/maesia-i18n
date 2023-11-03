package fr.maesia.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

public class PlayerJoinListenerTest {
	private ServerMock server;
	private MockPlugin plugin;
	private NamespacedKey localeKey;
	private PlayerMock player;
	private PlayerJoinEvent event;
	private PlayerJoinListener listener;
	private Object defaultLocale = MockProvider.LOCALE_AN;
	private List<Object> allowedLocale = Arrays.asList(MockProvider.LOCALE_AN, MockProvider.LOCALE_FR);
	
	@BeforeEach
	public void seUp() {
		this.server = MockBukkit.mock();
		
		this.plugin = MockBukkit.createMockPlugin();
		
		this.localeKey = new NamespacedKey(this.plugin, "i18n_locale");
		this.player = new PlayerMock(this.server, "test", UUID.randomUUID());
		
		this.event = new PlayerJoinEvent(
				player, 
				"Welcome !"
			);
		
		this.listener = new PlayerJoinListener(
				this.plugin,
				MockProvider.createI18n(this.defaultLocale),
				localeKey,
				this.allowedLocale
			);
	}
	
	@AfterEach
	public void tearDown()
	{
		MockBukkit.unmock();
	}
	
	@Test
	@DisplayName("Test metedata attribution like a first connection.")
	public void testMetadataAttribution() {
		this.listener.onPlayerJoin(this.event);
		
		assertTrue(this.player.hasMetadata("i18n_locale"));
		
		List<MetadataValue> metadatas = this.player.getMetadata("i18n_locale");
		Object locale = metadatas.get(0).value();
		
		assertEquals(this.defaultLocale, locale);
	}
	
	
	@Test
	@DisplayName("Test if metadata is equals to persistent data locale")
	public void testMetadataWithPersistent() {
		Object persistentLocale = MockProvider.LOCALE_FR;
		
		PersistentDataContainer container = this.player.getPersistentDataContainer();
		container.set(this.localeKey, PersistentDataType.STRING, persistentLocale.toString());
		
		assertTrue(this.defaultLocale != persistentLocale, "Persistent locale must different to default locale");
		
		this.listener.onPlayerJoin(this.event);
		
		assertTrue(this.player.hasMetadata("i18n_locale"));
		
		List<MetadataValue> metadatas = this.player.getMetadata("i18n_locale");
		Object locale = metadatas.get(0).value();
		
		assertEquals(persistentLocale, locale);
	}
	
	@Test
	@DisplayName("Test persistent locale availability checking")
	public void testLocaleAvailabilityCheking() {
		Object persistentLocale = MockProvider.LOCALE_PL;
		
		assertTrue(!this.allowedLocale.contains(persistentLocale));
		
		PersistentDataContainer container = this.player.getPersistentDataContainer();
		container.set(this.localeKey, PersistentDataType.STRING, persistentLocale.toString());
		
		this.listener.onPlayerJoin(this.event);
		
		assertTrue(this.player.hasMetadata("i18n_locale"));
		
		List<MetadataValue> metadatas = this.player.getMetadata("i18n_locale");
		Object locale = metadatas.get(0).value();
		
		assertEquals(this.defaultLocale, locale);
	}
}

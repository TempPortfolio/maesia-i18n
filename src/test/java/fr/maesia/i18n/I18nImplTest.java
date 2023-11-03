package fr.maesia.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

public class I18nImplTest {
	private ServerMock server;
	
	@BeforeEach
	public void setUp()
	{
		server = MockBukkit.mock();
	}
	
	@AfterEach
	public void tearDown()
	{
		MockBukkit.unmock();
	}
	
	@Test
	public void testTranslate() {
		I18nImpl i18n = MockProvider.createI18n(MockProvider.LOCALE_AN);
		
		assertEquals(MockProvider.MESSAGE_HELLO_FR         , i18n.translate(MockProvider.LOCALE_FR, MockProvider.KEY_HELLO));
		assertEquals(MockProvider.REPLACED_HELLO_PLAYER_AN , i18n.translate(MockProvider.LOCALE_AN, MockProvider.KEY_HELLO_PLAYER, MockProvider.REPLACE_HELLO_PLAYER));
		assertEquals(MockProvider.REPLACED_ACCOUNT_INFO_FR , i18n.translate(MockProvider.LOCALE_FR, MockProvider.KEY_ACCOUNT_INFO, MockProvider.REPLACE_ACCOUNT_INFO));
		assertEquals(MockProvider.MESSAGE_ACCOUNT_INFO_AN  , i18n.translate(MockProvider.LOCALE_AN, MockProvider.KEY_ACCOUNT_INFO));
	}
	
	@Test
	public void testNoLocaleTranslate() {
		I18nImpl i18n = MockProvider.createI18n(MockProvider.LOCALE_FR);
		
		assertEquals(MockProvider.MESSAGE_HELLO_FR, i18n.translate(MockProvider.KEY_HELLO));
	}
	
	@Test
	public void testPlayerTranslate() {
		I18nImpl i18n = MockProvider.createI18n(MockProvider.LOCALE_AN);
		
		PlayerMock player = MockProvider.createPlayer(this.server, MockProvider.LOCALE_FR, "test_player");
		
		assertEquals(MockProvider.MESSAGE_HELLO_FR, i18n.translate(player, MockProvider.KEY_HELLO));
	}
	
	@Test
	public void testBroadcast() {
		I18nImpl i18n = MockProvider.createI18n(MockProvider.LOCALE_AN);
		
		PlayerMock playerA = MockProvider.createPlayer(this.server, MockProvider.LOCALE_FR, "test_player_a");
		PlayerMock playerB = MockProvider.createPlayer(this.server, MockProvider.LOCALE_AN, "test_player_b");
		
		i18n.broadcast(Arrays.asList(playerA, playerB), MockProvider.KEY_HELLO);
		
		assertEquals(MockProvider.MESSAGE_HELLO_FR, playerA.nextMessage());
		assertEquals(MockProvider.MESSAGE_HELLO_AN, playerB.nextMessage());
	}
	
	@Test
	public void testBroadcastAll() {
		I18nImpl i18n = MockProvider.createI18n(MockProvider.LOCALE_AN);
		
		PlayerMock playerA = MockProvider.createPlayer(this.server, MockProvider.LOCALE_FR, "test_player_a");
		PlayerMock playerB = MockProvider.createPlayer(this.server, MockProvider.LOCALE_AN, "test_player_b");
		
		this.server.addPlayer(playerA);
		this.server.addPlayer(playerB);
		
		i18n.broadcast(MockProvider.KEY_HELLO);
		
		assertEquals(MockProvider.MESSAGE_HELLO_FR, playerA.nextMessage());
		assertEquals(MockProvider.MESSAGE_HELLO_AN, playerB.nextMessage());
	}
	
	@Test
	public void testGetDefaultLocale() {
		I18nImpl i18n = MockProvider.createI18n(MockProvider.LOCALE_FR);
		
		assertEquals(MockProvider.LOCALE_FR, i18n.getDefaultLocale());
	}
	
	@Test
	public void testModifierSystem() {
		Map<Object, Map<String, String>> translations = new HashMap<>();
		Map<String, String> messages = new HashMap<>();
		
		translations.put(MockProvider.LOCALE_AN, messages);
		messages.put("test.key", "Test messages {0}");
		
		I18nImpl i18n = new I18nImpl(translations, MockProvider.LOCALE_AN);
		Integer value = Integer.valueOf(50);
		
		Modifier<Integer> modifier = new Modifier<>() {
			@Override
			public Object modify(Object locale, Integer object) {
				assertEquals(MockProvider.LOCALE_AN, locale);
				assertEquals(value, object);
				return object + 10;
			}
		};
		
		i18n.addModifier(Integer.class, modifier);
		
		assertEquals("Test messages 60", i18n.translate("test.key", Integer.valueOf(value)));
	}
}

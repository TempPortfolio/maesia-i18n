package fr.maesia.i18n;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.metadata.FixedMetadataValue;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

public final class MockProvider {
	private static final Map<Object, Map<String, String>> translations = new HashMap<>();
	
	private static final Map<String, String> translationMapAN = new HashMap<>();
	private static final Map<String, String> translationMapFR = new HashMap<>();
	
	public static final String KEY_HELLO        = "message.hello";
	public static final String KEY_HELLO_PLAYER = "message.hello_player";
	public static final String KEY_ACCOUNT_INFO = "account.info";
	
	public static final Object   REPLACE_HELLO_PLAYER = "Spigot";
	public static final Object[] REPLACE_ACCOUNT_INFO = {"John", 45};
	
	public static final String MESSAGE_HELLO_AN = "Hello !";
	public static final String MESSAGE_HELLO_FR = "Bonjour !";
	public static final String MESSAGE_HELLO_PLAYER_AN = "Hello {0} !";
	public static final String MESSAGE_HELLO_PLAYER_FR = "Bonjour {0} !";
	public static final String MESSAGE_ACCOUNT_INFO_AN = "{0} account's has an amount of {1}$";
	public static final String MESSAGE_ACCOUNT_INFO_FR = "Le compte de {0} a un montant de {1}$";
	
	public static final String REPLACED_HELLO_PLAYER_AN = "Hello " + REPLACE_HELLO_PLAYER + " !";
	public static final String REPLACED_HELLO_PLAYER_FR = "Bonjour " + REPLACE_HELLO_PLAYER + " !";
	public static final String REPLACED_ACCOUNT_INFO_AN = REPLACE_ACCOUNT_INFO[0] + " account's has an amount of " + REPLACE_ACCOUNT_INFO[1] +"$";
	public static final String REPLACED_ACCOUNT_INFO_FR = "Le compte de " + REPLACE_ACCOUNT_INFO[0] + " a un montant de " + REPLACE_ACCOUNT_INFO[1] +"$";
	
	public static final Object LOCALE_AN = "an_AN";
	public static final Object LOCALE_FR = "fr_FR";
	public static final Object LOCALE_PL = "pl_PL";
	public static final Object LOCALE_TEST = "test_TEST";
	public static final Object LOCALE_FORMAT = "format_test";
	
	static {
		translationMapAN.put(KEY_HELLO, MESSAGE_HELLO_AN);
		translationMapFR.put(KEY_HELLO, MESSAGE_HELLO_FR);
		
		translationMapAN.put(KEY_HELLO_PLAYER, MESSAGE_HELLO_PLAYER_AN);
		translationMapFR.put(KEY_HELLO_PLAYER, MESSAGE_HELLO_PLAYER_FR);
		
		translationMapAN.put(KEY_ACCOUNT_INFO, MESSAGE_ACCOUNT_INFO_AN);
		translationMapFR.put(KEY_ACCOUNT_INFO, MESSAGE_ACCOUNT_INFO_FR);
		
		translations.put(LOCALE_AN, translationMapAN);
		translations.put(LOCALE_FR, translationMapFR);
	}
	
	public static Map<Object, Map<String, String>> getTranslations() {
		return Collections.unmodifiableMap(translations);
	}
	
	public static I18nImpl createI18n(Object locale) {
		return new I18nImpl(translations, locale);
	}
	
	public static PlayerMock createPlayer(ServerMock server, Object locale, String name) {
		PlayerMock player = new PlayerMock(server, name, UUID.randomUUID());
		
		player.setMetadata("i18n_locale", new FixedMetadataValue(MockBukkit.createMockPlugin(), locale));
		
		return player;
	}
	
	public static InputStream createConfigReaderFr() {
		String config = KEY_HELLO + "=" + MESSAGE_HELLO_FR + "\n"
				+ KEY_HELLO_PLAYER + "=" + MESSAGE_HELLO_PLAYER_FR + "\n"
				+ KEY_ACCOUNT_INFO + "=" + MESSAGE_ACCOUNT_INFO_FR + "\n";
		
		return new ByteArrayInputStream(config.getBytes());
	}
	
	public static InputStream createConfigStreamAn() {
		String config = KEY_HELLO + "=" + MESSAGE_HELLO_AN + "\n"
				+ KEY_HELLO_PLAYER + "=" + MESSAGE_HELLO_PLAYER_AN + "\n"
				+ KEY_ACCOUNT_INFO + "=" + MESSAGE_ACCOUNT_INFO_AN + "\n";
		
		return new ByteArrayInputStream(config.getBytes());
	}
	
	private MockProvider() { }
}

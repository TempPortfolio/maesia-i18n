package fr.maesia.i18n;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;

public class I18nProviderTest {
	private Plugin plugin;
	
	@BeforeEach
	public void setUp() {
		MockBukkit.mock();
		this.plugin = MockBukkit.createMockPlugin();
	}
	
	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}
	
	@Test
	public void testAddTranslation() throws IOException {
		BufferedReader in = this.toReader(MockProvider.createConfigStreamAn());
		
		I18nProvider.get().addTranslation(this.plugin, MockProvider.LOCALE_AN, in);
		I18n i18n = I18nProvider.get().getI18n(this.plugin);
		
		assertEquals(MockProvider.MESSAGE_HELLO_AN, i18n.translate(MockProvider.LOCALE_AN, MockProvider.KEY_HELLO));
	}
	
	@Test
	public void testDefaultLocale() {
		I18nProvider provider = I18nProvider.get();
		
		Object locale = MockProvider.LOCALE_FR;
		
		provider.setDefaultLocale(locale);
		
		assertEquals(locale, provider.getDefaultLocale());
		
		assertEquals(locale, provider.getI18n(this.plugin).getDefaultLocale());
	}
	
	@Test
	public void testLoad() throws IOException {
		I18nProvider provider = I18nProvider.get();
		
		provider.loadTranslation(this.plugin, "/test_TEST.properties");
		
		I18n i18n = provider.getI18n(this.plugin);
		
		assertEquals("This is a test", i18n.translate(MockProvider.LOCALE_TEST, "message.test"));
	}
	
	private BufferedReader toReader(InputStream in) {
		return new BufferedReader(new InputStreamReader(in));
	}
}

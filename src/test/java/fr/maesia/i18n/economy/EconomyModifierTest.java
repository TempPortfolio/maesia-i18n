package fr.maesia.i18n.economy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import fr.maesia.i18n.I18n;
import fr.maesia.i18n.I18nProvider;
import fr.maesia.i18n.MockProvider;

public class EconomyModifierTest {
	private I18n i18n;
	
	@BeforeEach
	public void setUp() throws IOException {
		MockBukkit.mock();
		MockPlugin plugin = MockBukkit.createMockPlugin();
		
		I18nProvider.get().loadTranslation(plugin, "/" + MockProvider.LOCALE_FORMAT + ".properties");
		this.i18n = I18nProvider.get().getI18n(plugin);
	}
	
	@AfterEach
	public void tearDown() {
		MockBukkit.unmock();
	}
	
	@Test
	public void testEcoModifier() {
		EconomyModifier modifier = new EconomyModifier(this.i18n);
		
		Eco eco = new Eco(BigDecimal.valueOf(456215652));
		
		Object result = modifier.modify(MockProvider.LOCALE_FORMAT, eco);
		
		assertEquals("456m Po", result);
	}
	
}

package fr.maesia.i18n.time;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import fr.maesia.i18n.I18n;
import fr.maesia.i18n.I18nProvider;
import fr.maesia.i18n.MockProvider;

public class TimeModifierTest {
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
	public void testTimeModifier() {
		TimeModifier modifier = new TimeModifier(this.i18n);
		
		long msTime =
				(31_536_000_000l * 1) + //1 year
				(86_400_000l * 4)     + //4 days
				(60_000 * 5)          + //5 minutes
				(1000 * 41);            //41 seconds
		
		Time time = new Time(msTime, true);
		
		Object result = modifier.modify(MockProvider.LOCALE_FORMAT, time);
		
		assertEquals("1y 4d 5min 41sec", result);
		
		time = new Time(msTime, false);
		
		result = modifier.modify(MockProvider.LOCALE_FORMAT, time);
		
		assertEquals("1 year 4 days 5 minutes 41 seconds", result);
	}
	
}

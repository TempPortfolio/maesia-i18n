package fr.maesia.i18n.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.maesia.i18n.I18nPlugin;
import fr.maesia.i18n.Modifier;

public class EconomyModifier implements Modifier<Eco> {
	private static final LinkedHashMap<Integer, String> units = new LinkedHashMap<>();
	static {
		units.put(3, "format.eco.kilo");
		units.put(6, "format.eco.million");
		units.put(9, "format.eco.billion");
	}
	
	@Override
	public Object modify(Object locale, Eco eco) {
		BigDecimal amount = eco.getAmount();
		String formated = amount.toBigInteger().toString();
		
		Map.Entry<Integer, String> unit = null;
		
		for(Map.Entry<Integer, String> entry : units.entrySet())
			if(formated.length() > entry.getKey())
				unit = entry;
		
		if(unit != null)
			formated = formated.substring(0, formated.length() - unit.getKey()) + this.tl(locale, unit.getValue());
		else
			formated = amount.setScale(2, RoundingMode.HALF_UP).toString();
		
		if(eco.displayCurrency())
			formated += " " + this.tl(locale, "format.eco.currency");
		
		return formated;
	}
	
	public String tl(Object locale, String key, Object... replaces) {
		return I18nPlugin.get().getI18n().translate(locale, key, replaces);
	}
}

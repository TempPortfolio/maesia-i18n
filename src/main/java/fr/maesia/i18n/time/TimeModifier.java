package fr.maesia.i18n.time;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import fr.maesia.i18n.I18nPlugin;
import fr.maesia.i18n.Modifier;

public class TimeModifier implements Modifier<Time> {
	private static final Logger LOGGER = Logger.getLogger("TimeModifier");
	private Map<Object, Map<String, String[]>> translations = new HashMap<>();
	
	@Override
	public Object modify(Object locale, Time time) {
		long year = -1;
		long day = -1;
		long hour = -1;
		long minute = -1;
		long second = -1;
		
		boolean concise = time.useInitials();
		long ms = time.getTime();
		int precision = time.getPrecision();
		String value = "";
		
		if(time.isAllowed(Unit.YEAR) && (precision--) > 0) {
			year = ms / 31536000000l;
			ms -= year * 31536000000l;
			
			if(year > 0)
				value += " " + this.tl(locale, "format.time.year", year, concise, year == 1 ? 0 : 1);
		}
		
		if(time.isAllowed(Unit.DAY) && (precision--) > 0) {
			day = ms / 86400000l;
			ms -= day * 86400000l;

			if(day > 0)
				value += " " + this.tl(locale, "format.time.day", day, concise, day == 1 ? 0 : 1);
		}
		
		if(time.isAllowed(Unit.HOUR) && (precision--) > 0) {
			hour = ms / 3600000l;
			ms -= hour * 3600000l;

			if(hour > 0)
				value += " " + this.tl(locale, "format.time.hour", hour, concise, hour == 1 ? 0 : 1);
		}
		
		if(time.isAllowed(Unit.MINUTE) && (precision--) > 0) {
			minute = ms / 60000l;
			ms -= minute * 60000l;

			if(minute > 0)
				value += " " + this.tl(locale, "format.time.minute", minute, concise, minute == 1 ? 0 : 1);
		}
		
		if(time.isAllowed(Unit.SECOND) && (precision--) > 0) {
			second = ms / 1000;
			ms -= ms * 1000;
			
			if(second > 0)
				value += " " + this.tl(locale, "format.time.second", second, concise, second == 1 ? 0 : 1);
		}
		
		return value.replaceFirst(" ", "");
	}
	
	private Map<String, String[]> load(Object locale) {
		Map<String, String[]> tls = this.translations.get(locale);
		
		if(tls == null) {
			tls = new HashMap<>();
			
			for(String str : Arrays.asList("year", "month", "week", "day", "hour", "minute", "second")) {
				String key = "format.time." + str;
				String tl = I18nPlugin.get().getI18n().translate(locale, key);
				String[] values = removeFirstOfFour(tl.split("([a-z]|( [a-z]))\\:"));
				tls.put(key, values);
			}
			
			this.translations.put(locale, tls);
		}
		
		return tls;
	}
	
	private String tl(Object locale, String key, long replace, boolean concise, int index) {
		Map<String, String[]> tls = this.load(locale);
		
		String[] messages = tls.get(key);
		
		if(messages == null || messages.length != 3) {
			LOGGER.warning("Can't use key '" + key + "' with locale '" + locale + "'");
			return key;
		}
		
		return replace + messages[index];
	}
	
	private static String[] removeFirstOfFour(String[] array) {
		if(array.length != 4)
			return array;
		
		String[] newArray = new String[array.length - 1];
		
		for(int i = 0; i < newArray.length; i++)
			newArray[i] = array[i + 1];
		
		return newArray;
	}
}

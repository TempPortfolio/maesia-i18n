package fr.maesia.i18n;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import fr.maesia.i18n.economy.Eco;
import fr.maesia.i18n.economy.EconomyModifier;
import fr.maesia.i18n.time.Time;
import fr.maesia.i18n.time.TimeModifier;

/**
 * Implementation of I18n.
 */
public class I18nImpl implements I18n {
	private static final Logger LOGGER = Logger.getLogger("I18nImpl");

	//                Locale      Key     Message
	private final Map<Object, Map<String, String>> translations;
	private final Map<Class<?>, Modifier<?>> modifiers = new HashMap<>();
	private Object defaultLocale;

	public I18nImpl(Map<Object, Map<String, String>> translations, Object defaultLocale) {
		this.translations = translations;
		this.defaultLocale = defaultLocale;
		
		this.addModifier(Time.class, new TimeModifier(this));
		this.addModifier(Eco.class, new EconomyModifier(this));
	}
	
	public <T> void addModifier(Class<T> target, Modifier<T> modifier) {
		this.modifiers.put(target, modifier);
	}

	@Override
	public Object getDefaultLocale() {
		return this.defaultLocale;
	}

	@Override
	public String translate(Player player, String key, Object... replaces) {
		String locale = null;

		List<MetadataValue> values = player.getMetadata("i18n_locale");
		if(values.size() > 0)
			locale = values.get(0).asString();

		return translate(locale, key, replaces);
	}

	@Override
	public String translate(String key, Object... replaces) {
		return this.translate(this.defaultLocale, key, replaces);
	}

	@Override
	public String translate(Object locale, String key, Object... replaces) {
		if(!translations.containsKey(locale)) {
			LOGGER.warning("Unknow locale '" + locale + "' so use default locale '" + this.defaultLocale + "'");
			locale = this.defaultLocale;
		}

		Map<String, String> localeTranslations = translations.get(locale);

		if(localeTranslations == null) {
			LOGGER.warning("Default locale not found '" + locale + "' !");
			return key;
		}

		String message = localeTranslations.get(key);

		if(message == null) {
			LOGGER.warning("Message not found for key '" + key + "' with locale '" + locale + "'");
			return key;
		}

		for(int i = 0; i < replaces.length; i++) {
			Object replace = replaces[i];
			Modifier<?> modifier = this.modifiers.get(replace.getClass());
			
			if(modifier != null)
				replace = modifier.accept(locale, replace);
			
			message = message.replace("{" + i + "}", replace.toString());
		}

		return message;
	}

	@Override
	public boolean hasLocale(Object locale) {
		return translations.containsKey(locale);
	}
}

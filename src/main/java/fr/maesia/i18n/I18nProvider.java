package fr.maesia.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

/**
 * Load UTF-8 translation properties files.
 * Provide I18n objects.
 */
public final class I18nProvider {
	//                Plugin      Locale      Key     Message
	private final Map<Plugin, Map<Object, Map<String, String>>> translations = new HashMap<>();
	private Object defaultLocale = "";
	
	/**
	 * Add translation for specified plugin
	 * by reading file referred by path.
	 * The locale must be the name of the file.
	 * The file must be UTF-8 properties.
	 * The file must be in source plugin.
	 * 
	 * Example of file name : an_US.properties
	 * 
	 * @param plugin
	 * @param path in source
	 * @throws IOException
	 */
	public void loadTranslation(Plugin plugin, String path) throws IOException {
		String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();

		Class<?> clazz;

		try {
			clazz = Class.forName(callerClassName);
		}
		catch(ReflectiveOperationException e) {
			throw new IOException(e);
		}

		String locale = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));

		try(InputStream in = clazz.getResourceAsStream(path)) {
			this.addTranslation(plugin, locale, in);
		}
	}
	
	/**
	 * Set default locale that will be given
	 * at each new I18n.
	 * 
	 * @param locale
	 */
	public void setDefaultLocale(Object locale) {
		this.defaultLocale = locale;
	}
	
	/**
	 * Get default locale.
	 * 
	 * @return default locale
	 */
	public Object getDefaultLocale() {
		return this.defaultLocale;
	}
	
	/**
	 * Get I18n who contains all registered translations
	 * by the specified plugin.
	 * 
	 * @param plugin
	 * @return I18n
	 */
	public I18n getI18n(Plugin plugin) {
		if(!translations.containsKey(plugin))
			translations.put(plugin, new HashMap<>());

		return new I18nImpl(translations.get(plugin), this.defaultLocale);
	}
	
	/**
	 * Add translation for specified plugin and locale
	 * by reading properties formated File in UTF-8
	 * 
	 * @param plugin who add translation
	 * @param locale
	 * @param reader
	 * @throws IOException
	 */
	public void addTranslations(Plugin plugin, Object locale, File file) throws IOException {
		try(BufferedReader reader = Files.newBufferedReader(file.toPath())) {
			addTranslation(plugin, locale, reader);
		}
		catch(IOException exception) {
			throw exception;
		}
	}
	
	/**
	 * Add translation for specified plugin and locale
	 * by reading properties formated InputStream in UTF-8
	 * 
	 * @param plugin who add translation
	 * @param locale
	 * @param reader
	 * @throws IOException
	 */
	public void addTranslation(Plugin plugin, Object locale, InputStream in) throws UnsupportedEncodingException, IOException {
		this.addTranslation(plugin, locale, new BufferedReader(new InputStreamReader(in, "UTF-8")));
	}
	
	/**
	 * Add translation for specified plugin and locale
	 * by reading properties formated reader in UTF-8
	 * 
	 * @param plugin who add translation
	 * @param locale
	 * @param reader
	 * @throws IOException
	 */
	public void addTranslation(Plugin plugin, Object locale, BufferedReader reader) throws IOException {
		if(!this.translations.containsKey(plugin))
			this.translations.put(plugin, new HashMap<>());

		Map<String, String> localeTranslations = this.translations.get(plugin).get(locale);

		if(localeTranslations == null) {
			localeTranslations = new HashMap<>();
			this.translations.get(plugin).put(locale, localeTranslations);
		}

		boolean readln = false;
		String key = "";
		String value = "";

		String line;
		while((line = reader.readLine()) != null) {
			if(value.endsWith("\\")) {
				value += line;
				readln = true;
				continue;
			}
			else if(!readln) {
				int separator = line.indexOf('=');

				if(separator == -1 || line.charAt(0) == '#')
					continue;

				key = line.substring(0, separator);
				value = line.substring(separator + 1, line.length());

				if(line.endsWith("\\"))
					continue;

			}
			value = value.replace("\\", "\n");

			localeTranslations.put(key, value);

			value = "";
			readln = false;
		}

		if(this.defaultLocale.toString().isEmpty())
			this.defaultLocale = locale;
	}
	
	private static final I18nProvider INSTANCE = new I18nProvider();
	public static I18nProvider get() {
		return I18nProvider.INSTANCE;
	}
}

package fr.maesia.i18n;

/**
 * A modifier is used by I18n system to
 * replace a specific object by other value.
 * 
 * @param <T> To catch
 */
@FunctionalInterface
public interface Modifier<T> {
	
	@SuppressWarnings("unchecked")
	public default Object accept(Object locale, Object obj) {
		return this.modify(locale, (T) obj);
	}
	
	public Object modify(Object locale, T object);
}

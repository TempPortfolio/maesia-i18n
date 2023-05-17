package fr.maesia.i18n;

@FunctionalInterface
public interface Modifier<T> {
	
	@SuppressWarnings("unchecked")
	public default Object accept(Object locale, Object obj) {
		return this.modify(locale, (T) obj);
	}
	
	public Object modify(Object locale, T object);
}

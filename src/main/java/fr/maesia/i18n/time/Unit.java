package fr.maesia.i18n.time;

public enum Unit {
	YEAR  ('y'),
	MONTH ('o'),
	WEEK  ('w'),
	DAY   ('d'),
	HOUR  ('h'),
	MINUTE('m'),
	SECOND('s');
	
	private char id;
	
	private Unit(char id) {
		this.id = id;
	}
	
	public static Unit toUnit(char id) {
		for(Unit u : Unit.values())
			if(u.id == id)
				return u;
		throw new RuntimeException("Unknow Unit id '" + id + "'");
	}
	
	public static Unit[] toUnits(String ids) {
		Unit[] units = new Unit[ids.length()];
		for(int i = 0; i < ids.length(); i++)
			units[i] = toUnit(ids.charAt(i));
		return units;
	}
}

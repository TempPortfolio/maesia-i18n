package fr.maesia.i18n.time;

/**
 * Used to format time.
 */
public class Time {
	private long time;
	private int precision = Integer.MAX_VALUE;
	private Unit[] units = Unit.values();
	private boolean initials = false; 
	
	public Time(long time) {
		this.time = time;
	}
	
	public Time(long time, Unit... alowed) {
		this.time = time;
		this.units = alowed;
	}
	
	public Time(long time, String alowed) {
		this.time = time;
		this.units = Unit.toUnits(alowed);
	}
	
	public Time(long time, boolean initials) {
		this.time = time;
		this.initials = initials;
	}
	
	public Time(long time, int precision) {
		this.time = time;
		this.precision = precision;
	}
	
	public long getTime() {
		return this.time;
	}
	
	public int getPrecision() {
		return this.precision;
	}
	
	public boolean useInitials() {
		return this.initials;
	}
	
	public boolean isAllowed(Unit verif) {
		for(Unit unit : this.units)
			if(verif == unit)
				return true;
		return false;
	}
}

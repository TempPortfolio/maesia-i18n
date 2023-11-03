package fr.maesia.i18n.economy;

import java.math.BigDecimal;

/**
 * Used to format economy amounts.
 */
public class Eco {
	private final BigDecimal amount;
	private boolean displayCurrency = true;
	
	public Eco(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Eco(BigDecimal amount, boolean displayCurrency) {
		this.amount = amount;
		this.displayCurrency = displayCurrency;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}
	
	public boolean displayCurrency() {
		return this.displayCurrency;
	}
}

package de.sample.cdi.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Money {

	private double amount;
	
	@SuppressWarnings("unused")
	private Money() {
	}

	public Money(double amount) {
		this.amount = amount;
	}


	public double getAmount() {
		return amount;
	}

	public Money add(Money money) {
		return new Money(money.getAmount() + this.getAmount());
	}

	public Money minus(Money money) {
		return new Money(this.getAmount() - money.getAmount());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		if (Double.doubleToLongBits(amount) != Double
				.doubleToLongBits(other.amount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Money [amount=" + amount + "]";
	}
	
	
}

package de.sample.cdi.entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {

	@Id
	private String number;

	@Embedded
	private Money balance;

	@SuppressWarnings("unused")
	private Account() {
	}

	public Account(String number) {
		this.number = number;
		this.balance = new Money(0);
	}

	public String getNumber() {
		return number;
	}

	public Money getBalance() {
		return balance;
	}

	public void deposit(Money amount) {
		this.balance = this.balance.minus(amount);
	}

	public void withdraw(Money amount) {
		this.balance = this.balance.add(amount);
	}

}

package de.sample.cdi.entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQuery(name = "getTransfers", query = "select t from Transfer t where t.from.number =:accountNumber")
public class Transfer {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	private Account from;

	@OneToOne
	private Account to;

	@Embedded
	private Money amount;

	@SuppressWarnings("unused")
	private Transfer() {
	}

	public Transfer(Account from, Account to, Money amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public Account getFrom() {
		return from;
	}

	public Account getTo() {
		return to;
	}

	public Money getAmount() {
		return amount;
	}

}

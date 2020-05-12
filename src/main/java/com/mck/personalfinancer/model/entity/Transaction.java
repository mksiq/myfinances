package com.mck.personalfinancer.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.mck.personalfinancer.model.enums.TransactionStatus;
import com.mck.personalfinancer.model.enums.TransactionType;

@Entity
@Table(name ="transaction",schema = "finances")
public class Transaction {

	@Id
	@Column(name = "id")
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "description")
	private String description;
	@Column(name = "month")
	private Integer month;
	@Column(name = "year")
	private Integer year;
	@ManyToOne
	@JoinColumn(name="id_user")
	private User user;
	@Column(name = "value")
	private BigDecimal value;
	@Column(name = "insert_date")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate insertDate;
	@Column(name = "type")
	@Enumerated(value = EnumType.STRING)
	private TransactionType type;
	@Column(name = "status")
	@Enumerated(value = EnumType.STRING)
	private TransactionStatus status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public LocalDate getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(LocalDate insertDate) {
		this.insertDate = insertDate;
	}
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public TransactionStatus getStatus() {
		return status;
	}
	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Transaction other = (Transaction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", description=" + description + ", month=" + month + ", year=" + year
				+ ", user=" + user + ", value=" + value + ", insertDate=" + insertDate + ", type=" + type + ", status="
				+ status + "]";
	}
}

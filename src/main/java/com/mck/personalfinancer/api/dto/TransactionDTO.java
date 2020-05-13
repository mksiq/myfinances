package com.mck.personalfinancer.api.dto;

import java.math.BigDecimal;

public class TransactionDTO {

	private Long id;
	private String description;
	private Integer month;
	private Integer year;
	private BigDecimal value;
	private Long userId;
	private String status;
	private String type;
	
	public TransactionDTO() {
		super();
	}
		
	public TransactionDTO(Long id, String description, Integer month, Integer year, BigDecimal value, Long userId,
			String status, String type) {
		super();
		this.id = id;
		this.description = description;
		this.month = month;
		this.year = year;
		this.value = value;
		this.userId = userId;
		this.status = status;
		this.type = type;
	}


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
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

package com.example.coindesk.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class CurrencyModel {
	
	public static final String LOCAL_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	
	// 幣別
	@Column
	private String currency;
	
	// 幣別中文名稱
	@Column
	private String chName;
	
	//匯率
	@Column
	private BigDecimal exchangeRate;
	
	@Column
	private LocalDateTime updateTime;
	
	@Transient
	private String updateTimeFormat;

}

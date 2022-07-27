package com.example.coindesk.form;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class CurrencyAddForm implements Serializable{
	
	private static final long serialVersionUID = -2128471360096346179L;

	private String currency;
	
	private String chName;
	
	private BigDecimal exchangeRate;
	
}

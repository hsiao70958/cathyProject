package com.example.coindesk.form;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class CurrencyTransferDetail implements Serializable{

	private static final long serialVersionUID = -2503485173752145120L;

	private String code;

	private String symbol;

	private String rate;

	private String description;

	private BigDecimal rate_float;
	
	private String chName;
	
	private BigDecimal exchangeRate;

}

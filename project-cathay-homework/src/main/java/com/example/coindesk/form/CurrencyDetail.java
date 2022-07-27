package com.example.coindesk.form;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class CurrencyDetail implements Serializable{

	private static final long serialVersionUID = 8305256712264035944L;

	private String code;
	
	private String symbol;
	
	private String rate;
	
	private String description;
	
	private BigDecimal rate_float;
	
	
}

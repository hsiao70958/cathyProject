package com.example.coindesk.form;

import java.io.Serializable;
import java.math.BigDecimal;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class CurrencyUpdateForm implements Serializable {

	private static final long serialVersionUID = -8459898005631291906L;
	
	@NotNull
	private long id;

	private String chName;
	
	private BigDecimal exchangeRate;
	
}

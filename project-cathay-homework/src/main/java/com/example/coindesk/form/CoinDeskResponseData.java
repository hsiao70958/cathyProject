package com.example.coindesk.form;

import java.util.Map;

import lombok.Data;

@Data
public class CoinDeskResponseData {
	
	private Map<String, String> time;
	
	private String disclaimer;
	
	private String chartName;
	
	private Map<String, Object> bpi;
	
}

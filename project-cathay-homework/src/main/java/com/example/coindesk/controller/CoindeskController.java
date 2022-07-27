package com.example.coindesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.coindesk.form.CurrencyAddForm;
import com.example.coindesk.form.CurrencyUpdateForm;
import com.example.coindesk.model.CurrencyModel;
import com.example.coindesk.service.CurrencyService;
import com.sun.istack.NotNull;

@RestController
public class CoindeskController {
	
	@Autowired
	private CurrencyService currencyService;

	// 1. 查詢幣別對應表資料API
	@RequestMapping("/api/currency/get")
	public Object getCurrency() {
		
		return currencyService.getAllCurrency();
		
	}
	
	// 2. 新增幣別對應表資料API
	@RequestMapping("/api/currency/add")
	public Object addCurrency(@RequestBody CurrencyAddForm form) {
		
		if (form.getCurrency() == null) {
			return currencyService.getResponseData(400, "currency不可為空", form);
		}
		
		return currencyService.addCurrency(form);
	}
	
	// 3.更新幣別對應表資料API
	@RequestMapping("/api/currency/update")
	public Object updateCurrency(@RequestBody CurrencyUpdateForm form) {
		
		CurrencyModel currencyModel = currencyService.findById(form.getId());
		if (currencyModel == null) {
			return currencyService.getResponseData(400, "無此id", form);
		}
		return currencyService.updateCurrency(currencyModel, form);
	}
	
	// 4.刪除幣別對應API
	@RequestMapping("/api/currency/delete")
	public Object deleteCurrency(@RequestParam @NotNull long id) {
		
		CurrencyModel currencyModel = currencyService.findById(id);
		if (currencyModel == null) {
			return currencyService.getResponseData(400, "無此id", id);
		}
		return currencyService.deleteCurrency(currencyModel);
	}
	
	// 5.Coindesk API , 顯示內容
	@RequestMapping("/post/coindesk/api")
	public Object coindeskApi() {
		
		return currencyService.postCoindeskApi();
	}
	
	// 6.Coindesk API , 資料轉換
	@RequestMapping("/post/coindesk/api/transfer")
	public Object coindeskApiTransfer() {
		
		return currencyService.coindeskDataTransfer();
	}
	
}

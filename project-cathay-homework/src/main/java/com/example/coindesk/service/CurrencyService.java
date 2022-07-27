package com.example.coindesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.coindesk.form.CoinDeskResponseData;
import com.example.coindesk.form.CurrencyAddForm;
import com.example.coindesk.form.CurrencyDetail;
import com.example.coindesk.form.CurrencyTransferDetail;
import com.example.coindesk.form.CurrencyUpdateForm;
import com.example.coindesk.model.CurrencyModel;
import com.example.coindesk.repository.CurrencyRepository;

@Service
public class CurrencyService {
	
	public static String coindeskApi = "https://api.coindesk.com/v1/bpi/currentprice.json";
	
	@Autowired
	private CurrencyRepository currencyRepo;
	
	public CurrencyModel findByCurrency(String currency) {
		return currencyRepo.findByCurrency(currency);
	}
	
	public CurrencyModel findById(Long id) {
		return currencyRepo.findById(id);
	}
	
	public Object getAllCurrency() {
		
		Iterable<CurrencyModel> currencys = currencyRepo.findAll();
		
		currencys.forEach(c -> c.setUpdateTimeFormat(c.getUpdateTime().format(DateTimeFormatter.ofPattern(CurrencyModel.LOCAL_DATETIME_FORMAT))));
		
		return getResponseData(200, "success", currencys);
	}
	
	public Object addCurrency(CurrencyAddForm inputData) {
		String currency = inputData.getCurrency();
		CurrencyModel result = findByCurrency(currency);
		if (result == null) {
			CurrencyModel currencyModel = new CurrencyModel();
			currencyModel.setCurrency(currency);
			currencyModel.setChName(inputData.getChName());
			currencyModel.setExchangeRate(inputData.getExchangeRate());
			currencyModel.setUpdateTime(LocalDateTime.now());
			currencyRepo.save(currencyModel);
			return getResponseData(200 ,"新增成功", currencyModel);
		} else {
			return getResponseData(400, "新增失敗,幣別已存在", inputData);
		}
	}
	
	public Object updateCurrency(CurrencyModel target, CurrencyUpdateForm inputData) {
		if (inputData.getChName() != null) target.setChName(inputData.getChName());
		if (inputData.getExchangeRate() != null) target.setExchangeRate(inputData.getExchangeRate());
		target.setUpdateTime(LocalDateTime.now());
		currencyRepo.save(target);
		return getResponseData(200, "更新成功", target);
	}
	
	public Object deleteCurrency(CurrencyModel target) {
		currencyRepo.delete(target);
		return getResponseData(200, "刪除成功", target);
	}
	
	public CoinDeskResponseData postCoindeskApi() {
		CoinDeskResponseData coinDeskResponseData = new CoinDeskResponseData();
		JSONObject result = doApi(coindeskApi, HttpMethod.GET, new HashMap<>(), new HashMap<>());
		if(result != null) {
			Map<String, String> time = new HashMap<>();
			JSONObject timeObject = result.getJSONObject("time");
			time.put("updated", timeObject.getString("updated"));
			time.put("updatedISO", timeObject.getString("updatedISO"));
			time.put("updateduk", timeObject.getString("updateduk"));
			coinDeskResponseData.setTime(time);
			coinDeskResponseData.setDisclaimer(result.getString("disclaimer"));
			coinDeskResponseData.setChartName(result.getString("chartName"));
			Map<String, Object> bpi = new HashMap<>();
			currencyTransfer(bpi, result.getJSONObject("bpi").getJSONObject("USD"));
			currencyTransfer(bpi, result.getJSONObject("bpi").getJSONObject("GBP"));
			currencyTransfer(bpi, result.getJSONObject("bpi").getJSONObject("EUR"));
			coinDeskResponseData.setBpi(bpi);
		}
		return coinDeskResponseData;
	}
	
	private void currencyTransfer(Map<String, Object> bpi, JSONObject currencyObject) {
		CurrencyDetail currencyDetail = new CurrencyDetail();
		currencyDetail.setCode(currencyObject.getString("code"));
		currencyDetail.setSymbol(currencyObject.getString("symbol"));
		currencyDetail.setRate(currencyObject.getString("rate"));
		currencyDetail.setDescription(currencyObject.getString("description"));
		currencyDetail.setRate_float(currencyObject.getBigDecimal("rate_float"));
		bpi.put(currencyDetail.getCode(), currencyDetail);
	}
	
	public Object coindeskDataTransfer() {
		
		CoinDeskResponseData coinDeskData = postCoindeskApi();
		
		coinDeskData.getTime().put("updated", LocalDateTime.now().format(DateTimeFormatter.ofPattern(CurrencyModel.LOCAL_DATETIME_FORMAT)));
		
		Map<String, Object> bpi = coinDeskData.getBpi();
		
		if (bpi != null) {
			for (CurrencyModel c : currencyRepo.findAll()) {
				if (bpi.containsKey(c.getCurrency())) {
					CurrencyDetail currencyDetail = (CurrencyDetail)bpi.get(c.getCurrency());
					CurrencyTransferDetail currencyTransferDetail = new CurrencyTransferDetail();
					currencyTransferDetail.setCode(currencyDetail.getCode());
					currencyTransferDetail.setSymbol(currencyDetail.getSymbol());
					currencyTransferDetail.setRate(currencyDetail.getRate());
					currencyTransferDetail.setDescription(currencyDetail.getDescription());
					currencyTransferDetail.setRate_float(currencyDetail.getRate_float());
					currencyTransferDetail.setChName(c.getChName());
					currencyTransferDetail.setExchangeRate(c.getExchangeRate());
					bpi.put(c.getCurrency(), currencyTransferDetail);
				}
			}
			coinDeskData.setBpi(bpi);
		}
		return coinDeskData;
	}
	
	public static JSONObject doApi(String url, HttpMethod httpMethod, Map<String, Object> paramterMap, Map<String, String> paramHeader) {
	    HttpHeaders httpHeader = new HttpHeaders();
	    httpHeader.setContentType(MediaType.APPLICATION_JSON);
	    httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    for (Entry<String, String> e : paramHeader.entrySet()) {
	      httpHeader.set(e.getKey(), e.getValue());
	    }

	    int httpRequestTimeOut = 10000;
	    
	    HttpEntity<?> queryRequest = new HttpEntity<>(paramterMap, httpHeader);
	    HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	    httpRequestFactory.setConnectionRequestTimeout(httpRequestTimeOut);
	    httpRequestFactory.setConnectTimeout(httpRequestTimeOut);
	    httpRequestFactory.setReadTimeout(httpRequestTimeOut);
	    
	    RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
	    ResponseEntity<String> response = restTemplate.exchange(url, httpMethod, queryRequest, String.class);
	    if (response.getStatusCode().equals(HttpStatus.OK)) {
	      return new JSONObject(response.getBody());
	    }
		return null;
	}
	
	public Map<String, Object> getResponseData (int code,String response, Object result) {
		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("code", code);
		resultData.put("response", response);
		if (result != null) resultData.put("result", result);
		return resultData;
	}
	
}

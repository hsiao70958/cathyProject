package com.example.coindesk.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.coindesk.ProjectCathayHomeworkApplication;

@SpringBootTest(classes = ProjectCathayHomeworkApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyTest {
	
	private HttpHeaders httpHeaders;
	
	@Autowired
	private TestRestTemplate restTimeplate;
	
	
	@BeforeEach
	public void init() {
		httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	}
	
	@Test
	void getCurrency() throws Exception {
		List<String> expectCurrency = Arrays.asList("USD", "GBP", "EUR");
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/currency/get");
		Map<String, Object> paramsMap = new HashMap<>();
		HttpEntity<?> queryRequest = new HttpEntity<>(paramsMap, httpHeaders);
		ResponseEntity<String> response = restTimeplate.exchange(builder.build().toUri().toString(), HttpMethod.POST, queryRequest, String.class);
//		System.out.println(response.getBody());
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(new JSONObject(response.getBody()).getInt("code")).isEqualTo(200);
		assertThat(response.getBody()).contains(expectCurrency);
	}
	
	@Test
	void addCurrency() throws Exception {
		List<String> expectCurrency = Arrays.asList("TWD");
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/currency/add");
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("currency", "TWD");
		paramsMap.put("chName", "台幣");
		paramsMap.put("exchangeRate", 1.00);
		HttpEntity<?> queryRequest = new HttpEntity<>(paramsMap, httpHeaders);
		ResponseEntity<String> response = restTimeplate.exchange(builder.build().toUri().toString(), HttpMethod.POST, queryRequest, String.class);
//		System.out.println(response.getBody());
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(new JSONObject(response.getBody()).getInt("code")).isEqualTo(200);
		assertThat(response.getBody()).contains(expectCurrency);
	}
	
	@Test
	void updateCurrency() throws Exception {
		List<String> expectCurrency = Arrays.asList("新美元");
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/currency/update");
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("id", 1);
		paramsMap.put("chName", "新美元");
		HttpEntity<?> queryRequest = new HttpEntity<>(paramsMap, httpHeaders);
		ResponseEntity<String> response = restTimeplate.exchange(builder.build().toUri().toString(), HttpMethod.POST, queryRequest, String.class);
//		System.out.println(response.getBody());
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(new JSONObject(response.getBody()).getInt("code")).isEqualTo(200);
		assertThat(response.getBody()).contains(expectCurrency);
	}
	
	@Test
	void deleteCurrency() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/currency/delete");
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("id", 3);
		HttpEntity<?> queryRequest = new HttpEntity<>(paramsMap, httpHeaders);
		ResponseEntity<String> response = restTimeplate.exchange(builder.build().toUri().toString(), HttpMethod.POST, queryRequest, String.class);
		System.out.println(response.getBody());
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
		assertThat(new JSONObject(response.getBody()).getInt("code")).isEqualTo(200);
	}
	
	@Test
	void callCoinDeskApi() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/post/coindesk/api");
		Map<String, Object> paramsMap = new HashMap<>();
		HttpEntity<?> queryRequest = new HttpEntity<>(paramsMap, httpHeaders);
		ResponseEntity<String> response = restTimeplate.exchange(builder.build().toUri().toString(), HttpMethod.POST, queryRequest, String.class);
//		System.out.println(response.getBody());
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
	}
	
	@Test
	void callCoinDeskApiTransfer() throws Exception {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/post/coindesk/api/transfer");
		Map<String, Object> paramsMap = new HashMap<>();
		HttpEntity<?> queryRequest = new HttpEntity<>(paramsMap, httpHeaders);
		ResponseEntity<String> response = restTimeplate.exchange(builder.build().toUri().toString(), HttpMethod.POST, queryRequest, String.class);
//		System.out.println(response.getBody());
		assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
	}
	
}

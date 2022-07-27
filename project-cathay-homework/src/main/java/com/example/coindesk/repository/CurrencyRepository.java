package com.example.coindesk.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.coindesk.model.CurrencyModel;

public interface CurrencyRepository extends CrudRepository<CurrencyModel, Integer> {
	
	CurrencyModel findByCurrency(String currency);
	
	CurrencyModel findById(Long id);

}

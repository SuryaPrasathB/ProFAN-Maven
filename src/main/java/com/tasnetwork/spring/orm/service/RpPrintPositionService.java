package com.tasnetwork.spring.orm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tasnetwork.spring.orm.repository.OperationProcessRepo;
import com.tasnetwork.spring.orm.repository.RpPrintPositionRepo;

@Component
public class RpPrintPositionService {

	@Autowired
	private RpPrintPositionRepo rpPrintPositionRepo;
}

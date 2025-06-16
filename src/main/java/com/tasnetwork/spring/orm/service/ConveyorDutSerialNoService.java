package com.tasnetwork.spring.orm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tasnetwork.spring.orm.repository.ConveyorDutSerialNoRepo;
import com.tasnetwork.spring.orm.repository.OperationParamRepo;

@Component
public class ConveyorDutSerialNoService {

	@Autowired
	private ConveyorDutSerialNoRepo conveyorDutSerialNoRepo;
}

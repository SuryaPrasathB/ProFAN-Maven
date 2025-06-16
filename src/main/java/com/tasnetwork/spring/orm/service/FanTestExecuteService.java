package com.tasnetwork.spring.orm.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.tasnetwork.spring.orm.repository.FanTestExecuteRepo;

public class FanTestExecuteService {
	@Autowired
	private FanTestExecuteRepo fanTestExecuteRepo;

}

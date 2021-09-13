package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.ScheduleConfiguration;
import com.example.demo.config.SecondScheduleConfiguration;

@RestController
public class SchedulerRestController {

	@Autowired
	private ScheduleConfiguration scheduleConfiguration;

	@Autowired
	private SecondScheduleConfiguration secondScheduleConfiguration;

	@GetMapping("/updateSchedule")
	public String updateSchedule(@RequestParam("cronExp") String cronExp) {
		scheduleConfiguration.updateSchedule(cronExp);
		return "Success";
	}

	@GetMapping("/updateSchedule2")
	public String updateSchedule2(@RequestParam("cronExp") String cronExp) {
		secondScheduleConfiguration.updateSchedule(cronExp);
		return "Success";
	}
}

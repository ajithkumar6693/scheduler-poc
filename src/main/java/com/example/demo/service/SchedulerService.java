package com.example.demo.service;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

	public void runScehdule() {
		System.out.println("SchedulerService.runScehdule() ---- start ----");
		System.out.println("runScehdule ::" + new Date());
		System.out.println("SchedulerService.runScehdule() ---- End ----");
		System.out.println();
		System.out.println();
	}

	// @Scheduled(cron= "*/5 * * * * *")
	public void runTestScehdule() {
		System.out.println("NEW SchedulerService.runTestScehdule() ---- start ----");
		System.out.println("runTestScehdule ::" + new Date());
		System.out.println("NEW SchedulerService.runTestScehdule() ---- End ----");
		System.out.println();
		System.out.println();
	}

	// @Scheduled(cron = "0/45 * * * * *")
	// public void runTestScehdule1() {
	// System.out.println("NEW SchedulerService.runTestScehdule() ---- start
	// ----");
	// System.out.println("runTestScehdule ::" + new Date());
	// System.out.println("NEW SchedulerService.runTestScehdule() ---- End
	// ----");
	// System.out.println();
	// System.out.println();
	// }

}

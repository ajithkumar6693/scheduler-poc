package com.example.demo.config;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import com.example.demo.service.SchedulerService;

@Configuration
public class ScheduleConfiguration implements SchedulingConfigurer {

	private static final Logger log = LoggerFactory.getLogger(ScheduleConfiguration.class);

	@Autowired
	private SchedulerService schedulerService;

	private ScheduledTaskRegistrar taskRegistrar;

	private String cronTabExpression;

	private ScheduledFuture<?> scheduledFuture;

	// @Override
	// public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
	// log.info("ScheduleConfiguration.configureTasks :: ");
	// }

	@Bean
	@Primary
	public TaskScheduler dynamicTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("DYNAMIC_SCHEDULER");
		scheduler.setPoolSize(5);
		scheduler.setAwaitTerminationSeconds(210);
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		scheduler.setRemoveOnCancelPolicy(true);
		scheduler.initialize();
		return scheduler;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		log.info("ScheduleConfiguration.configureTasks new :: ");
		if (this.taskRegistrar == null) {
			this.taskRegistrar = taskRegistrar;
		}

		if (this.cronTabExpression == null) {
			cronTabExpression = "*/3 * * * * *";
		}

		taskRegistrar.setScheduler(dynamicTaskScheduler());

		// taskRegistrar.addTriggerTask(() -> {
		// schedulerService.runScehdule();
		// }, new Trigger() {
		// @Override
		// public Date nextExecutionTime(TriggerContext triggerContext) {
		// // String cron = cronConfig();
		// log.info("updating cron == " + cronTabExpression);
		// CronTrigger trigger = new CronTrigger(cronTabExpression);
		// Date nextExec = trigger.nextExecutionTime(triggerContext);
		// System.out.println("getTriggerTaskList :: " +
		// taskRegistrar.getTriggerTaskList());
		//
		// return nextExec;
		// }
		// });

		scheduledFuture = taskRegistrar.getScheduler().schedule(() -> {
			schedulerService.runScehdule();
		}, new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				log.info("updating cron == " + cronTabExpression);
				CronTrigger trigger = new CronTrigger(cronTabExpression);
				Date nextExec = trigger.nextExecutionTime(triggerContext);

				System.out.println("getScheduledTasks :: " + taskRegistrar.getCronTaskList());
				System.out.println("getFixedDelayTaskList :: " + taskRegistrar.getFixedDelayTaskList());
				System.out.println("getFixedRateTaskList :: " + taskRegistrar.getFixedRateTaskList());
				System.out.println("getScheduledTasks :: " + taskRegistrar.getScheduledTasks());
				System.out.println("getScheduler :: " + taskRegistrar.getScheduler());
				System.out.println("getTriggerTaskList :: " + taskRegistrar.getTriggerTaskList());
				return nextExec;
			}
		});

	}

	public synchronized void updateSchedule(String cronTabExpression) {
		this.cronTabExpression = cronTabExpression;

		// do not interrupt the current run if it kicked in.
		boolean cancel = this.scheduledFuture.cancel(false);
		log.info("cancel == " + cancel);

		configureTasks(this.taskRegistrar);
	}

}

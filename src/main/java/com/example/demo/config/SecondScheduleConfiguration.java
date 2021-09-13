package com.example.demo.config;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import com.example.demo.service.SchedulerService;

@Configuration
public class SecondScheduleConfiguration implements SchedulingConfigurer {

	private static final Logger log = LoggerFactory.getLogger(SecondScheduleConfiguration.class);

	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	private TaskScheduler taskScheduler;

	private ScheduledTaskRegistrar taskRegistrar;

	private String cronTabExpression;

	private ScheduledFuture<?> scheduledFuture;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		log.info("SecondScheduleConfiguration.configureTasks new :: ");
		if (this.taskRegistrar == null) {
			this.taskRegistrar = taskRegistrar;
		}

		if (this.cronTabExpression == null) {
			cronTabExpression = "*/30 * * * * *";
		}

		taskRegistrar.setScheduler(taskScheduler);

		scheduledFuture = taskRegistrar.getScheduler().schedule(() -> {
			schedulerService.runTestScehdule();
		}, new Trigger() {
			@Override
			public Date nextExecutionTime(TriggerContext triggerContext) {
				log.info("SecondScheduleConfiguration updating cron == " + cronTabExpression);
				CronTrigger trigger = new CronTrigger(cronTabExpression);
				Date nextExec = trigger.nextExecutionTime(triggerContext);

				System.out
						.println("SecondScheduleConfiguration.getScheduledTasks :: " + taskRegistrar.getCronTaskList());
				System.out.println("SecondScheduleConfiguration.getFixedDelayTaskList :: "
						+ taskRegistrar.getFixedDelayTaskList());
				System.out.println(
						"SecondScheduleConfiguration.getFixedRateTaskList :: " + taskRegistrar.getFixedRateTaskList());
				System.out.println(
						"SecondScheduleConfiguration.getScheduledTasks :: " + taskRegistrar.getScheduledTasks());
				System.out.println("SecondScheduleConfiguration.getScheduler :: " + taskRegistrar.getScheduler());
				System.out.println(
						"SecondScheduleConfiguration.getTriggerTaskList :: " + taskRegistrar.getTriggerTaskList());
				return nextExec;
			}
		});

	}

	public synchronized void updateSchedule(String cronTabExpression) {
		this.cronTabExpression = cronTabExpression;

		// do not interrupt the current run if it kicked in.
		boolean cancel = this.scheduledFuture.cancel(false);
		log.info("SecondScheduleConfiguration.cancel == " + cancel);

		configureTasks(this.taskRegistrar);
	}

}

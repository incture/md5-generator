package oneapp.incture.workbox.demo.successfactors.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.successfactors.services.ParseSuccessFactorsTasks;



@Component
public class SFEventsScheduler {

	@Autowired
	ParseSuccessFactorsTasks parseSFTasks;

	// @Scheduled(fixedDelay = 6000)
	public void updateEvents() {
		System.err.println("[WBP-Dev][PMC]SFEventsScheduler started : " + new Date());
		parseSFTasks.persistSFTasks();
		System.err.println("[WBP-Dev][PMC]SFEventsScheduler ended : " + new Date());
	}

	public String updateSFEvents(String userId) {
		return parseSFTasks.persistSuccessFactorsTasks(userId).getMessage();
		}
	
	@Scheduled(fixedDelay = 5000)
	public void addTasks() {
		System.err.println("[WBP-Dev][PMC]SFEventsScheduler started : " + new Date());
		parseSFTasks.parseSuccessFactorTasks();
		System.err.println("[WBP-Dev][PMC]SFEventsScheduler ended : " + new Date());
	}

}

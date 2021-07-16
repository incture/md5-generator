package oneapp.incture.workbox.demo.adapter.rest;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkboxMasterDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.successfactors.services.ParseSuccessFactorsTasks;
import oneapp.incture.workbox.demo.successfactors.util.SFEventsScheduler;

@RestController
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/successFactors", produces = "application/json")
public class SuccessFactorsRest {

	@Autowired
	SFEventsScheduler sfEvents;

	@Autowired
	UserDetailsDao userDetails;

	@Autowired
	ParseSuccessFactorsTasks parseSFTasks;

	@RequestMapping(value = "/persistTasks", method = RequestMethod.GET)
	public String forwarding() {
		sfEvents.updateEvents();
		return "Success";
	}

	@RequestMapping(value = "/getTasks", method = RequestMethod.GET)
	public WorkboxMasterDto getTasks() {
		return parseSFTasks.parseSFTasks();
	}

	@RequestMapping(value = "/getTasksSF/{userId}", method = RequestMethod.GET)
	public WorkboxMasterDto getTasksSF(@PathVariable("userId") String userId) throws ParseException {
		try {
			System.err.println("[WBP-Dev]SuccessFactorsRest.getTasksSF() userId" + userId);
		} catch (Exception e) {
			e.printStackTrace();

		}
		parseSFTasks.parseSuccessFactorTimeSheetTasks(userId);
		parseSFTasks.parseSuccessFactorPendingTasks(userId);
		return parseSFTasks.parseSuccessFactorTasks(userId);
	}

	@RequestMapping(value = "/persistSFTasks/{userId}", method = RequestMethod.GET)
	public String persistSuccessFactorsTasks(@PathVariable("userId") String userId) {
		System.err.println("[WBP-Dev]SuccessFactorsRest.persistSuccessFactorsTasks()" + userId);
		Map<String, List<String>> usersMap = null;

		usersMap = userDetails.getAllSuccessFactorsUserDetails();
		if (!ServicesUtil.isEmpty(usersMap) && !ServicesUtil.isEmpty(usersMap.get(userId))) {
			userId = usersMap.get(userId).get(1);
		} else {
			userId = "69121";
		}
		return sfEvents.updateSFEvents(userId);
	}
	
	@RequestMapping(value = "/addTasks", method = RequestMethod.GET)
	public String parseSF() {
		sfEvents.addTasks();
		return "Success";
	}

}

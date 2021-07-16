package oneapp.incture.workbox.demo.adminConsole.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adminConsole.dto.AdminControlDto;
import oneapp.incture.workbox.demo.adminConsole.sevices.AdminControlFacadeLocal;

@Configuration
@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/admin", produces = "application/json")
public class AdminConsoleRest {

	@Autowired
	private AdminControlFacadeLocal adminControlFacadeLocal;

	@RequestMapping(value = "/configurations", method = RequestMethod.GET)
	public AdminControlDto getAdminConfigurationData() {
		return adminControlFacadeLocal.getAdminConfigurationData();
	}

	@RequestMapping(value = "/delete/{processName}", method = RequestMethod.POST)
	public ResponseMessage deleteProcessConfig(@PathVariable("processName") String processName) {
		return adminControlFacadeLocal.deleteProcessConfig(processName);
	}

	@RequestMapping(value = "/configurations", method = RequestMethod.POST)
	public ResponseMessage createUpdateDataAdmin(@RequestBody AdminControlDto adminControlDto) {
		return adminControlFacadeLocal.createUpdateDataAdmin(adminControlDto);
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String hello() {
		return "Hello from Spring!";
	}

}

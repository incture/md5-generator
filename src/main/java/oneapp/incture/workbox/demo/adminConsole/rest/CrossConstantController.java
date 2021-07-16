package oneapp.incture.workbox.demo.adminConsole.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adhocTask.dto.CrossConstantResponseDto;
import oneapp.incture.workbox.demo.adhocTask.services.TaskCreationService;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/crossConstant",produces="application/json")
public class CrossConstantController {

	@Autowired
	TaskCreationService taskCreationService;
	
	@RequestMapping(value = "/getConstants/{name}",method=RequestMethod.GET,produces = "application/json")
	public CrossConstantResponseDto getConstants(@PathVariable String name)
	{
		return taskCreationService.getConstants(name);
	}
}

package oneapp.incture.workbox.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.inbox.dto.UserCustomFilterDto;
import oneapp.incture.workbox.demo.inbox.entity.UserCustomFilter;
import oneapp.incture.workbox.demo.inbox.sevices.UserCustomFilterFacade;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/userCustomFilter", produces = "application/json")
public class UserCustomFilterRest {

	@Autowired
	UserCustomFilterFacade customFilterFacade;

	@RequestMapping(value = "/getFilters", method = RequestMethod.GET)
	public List<UserCustomFilterDto> getFilters(@AuthenticationPrincipal Token token) {
		return customFilterFacade.getFilters(token);
	}

	@RequestMapping(value = "/saveViewFilter", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage saveViewFilter(@RequestBody UserCustomFilter userCustomFilter
			,@AuthenticationPrincipal Token token) {
		return customFilterFacade.saveFilterOrView(userCustomFilter,token);
	}

	@RequestMapping(value = "/saveAllViewFilter", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage saveViewFilter(@RequestBody List<UserCustomFilter> userCustomFilter
			,@AuthenticationPrincipal Token token) {
		return customFilterFacade.saveAllFilterOrView(userCustomFilter,token);
	}

	@RequestMapping(value = "/deleteView", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage deleteView(@RequestBody UserCustomFilterDto userCustomFilterDto) {
		return customFilterFacade.deleteView(userCustomFilterDto);
	}

	@RequestMapping(value = "/deleteFilter", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage deleteFilter(@RequestBody UserCustomFilterDto userCustomFilterDto) {
		return customFilterFacade.deleteFilter(userCustomFilterDto);
	}
}

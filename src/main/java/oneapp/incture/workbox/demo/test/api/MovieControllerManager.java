package oneapp.incture.workbox.demo.test.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

//import oneapp.incture.workbox.demo.test.util.RestResponse;
//import oneapp.incture.workbox.demo.test.util.SCPRestUtil;

//import sap.spring.xsuaa.cf.model.Movie;
//import sap.spring.xsuaa.cf.repository.MovieRepository;

@RestController
@RequestMapping("workbox/manager")
public class MovieControllerManager {

//	@Autowired
//	MovieRepository movieRepository;


	@GetMapping("list")
	public String movieList() {
		System.out.println("MovieControllerManager.movieList()");
		return "Movie List";
	}

	// Create Movie
	@GetMapping("add")
	public String addMovie(@AuthenticationPrincipal Token token) {

		System.err.println("MovieControllerManager.addMovie() token : " + token.toString());
		Map<String, String> result = new HashMap<>();
		result.put("grant type", token.getGrantType());
		result.put("client id", token.getClientId());
		result.put("subaccount id", token.getSubaccountId());
		result.put("logon name", token.getLogonName());
		result.put("family name", token.getFamilyName());
		result.put("given name", token.getGivenName());
		result.put("email", token.getEmail());
		result.put("authorities", String.valueOf(token.getAuthorities()));
		result.put("scopes", String.valueOf(token.getScopes()));
		result.put("passworkd", token.getPassword());
		result.put("appToken", token.getAppToken());

		String token_bearer = token.getAppToken();
		String tokenUrl = "https://hrapps.authentication.eu10.hana.ondemand.com/oauth/token";

		String body = "grant_type=password&username=shruti.patra@incture.com&password=Incture@123&client_id=sb-clone-d6948b2e-75f4-4342-bcc2-3cb5ceec57aa!b19391|workflow!b10150&response_type=toke&client_secret=b34313ff-e7bf-4ce5-96e5-6917fc5a4089$K-_IZvpC-0GxU8CSzAP4amMFEb_OuYNKHWP9-IY3Tqk=";
		String coentType = "application/x-www-form-urlencoded";
		
		return "Add Movie" + result.toString() + "Response : ";
	}
	
	@GetMapping("/sayHello")
	public Map<String, String> sayHello(@AuthenticationPrincipal Token token) {

//	        logger.info("Got the Xsuaa token: {}", token.getAppToken());
//	        logger.info(token.toString());

		System.err.println("MovieControllerManager.sayHello() Got the Xsuaa token: {}" + token.getAppToken());
		System.err.println("MovieControllerManager.sayHello() token : " + token.toString());
		Map<String, String> result = new HashMap<>();
		result.put("grant type", token.getGrantType());
		result.put("client id", token.getClientId());
		result.put("subaccount id", token.getSubaccountId());
		result.put("logon name", token.getLogonName());
		result.put("family name", token.getFamilyName());
		result.put("given name", token.getGivenName());
		result.put("email", token.getEmail());
		result.put("authorities", String.valueOf(token.getAuthorities()));
		result.put("scopes", String.valueOf(token.getScopes()));

		return result;
	}
	
	@PostMapping("/userTest")
	public Map<String, String> getLoggedInUserInfo(@AuthenticationPrincipal Token token) {

//	        logger.info("Got the Xsuaa token: {}", token.getAppToken());
//	        logger.info(token.toString());

		System.err.println("MovieControllerManager.sayHello() Got the Xsuaa token: {}" + token.getAppToken());
		System.err.println("MovieControllerManager.sayHello() token : " + token.toString());
		Map<String, String> result = new HashMap<>();
		result.put("grant type", token.getGrantType());
		result.put("client id", token.getClientId());
		result.put("subaccount id", token.getSubaccountId());
		result.put("logon name", token.getLogonName());
		result.put("family name", token.getFamilyName());
		result.put("given name", token.getGivenName());
		result.put("email", token.getEmail());
		result.put("authorities", String.valueOf(token.getAuthorities()));
		result.put("scopes", String.valueOf(token.getScopes()));
		result.put("passworkd", token.getPassword());
		result.put("appToken", token.getAppToken());

		return result;
	}

}

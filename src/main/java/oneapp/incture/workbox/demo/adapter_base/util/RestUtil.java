package oneapp.incture.workbox.demo.adapter_base.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;

@Component
public class RestUtil {

	// private static final Logger logger =
	// LoggerFactory.getLogger(RestUtil.class);

	/**
	 * @param requestURL
	 * @param samlHeaderKey
	 *            - null
	 * @param entity
	 *            - input entity for POST call
	 * @param method
	 *            - POST/GET - PMCConstant.HTTP_METHOD_POST /
	 *            PMCConstant.HTTP_METHOD_GET / PMCConstant.HTTP_METHOD_PATCH
	 * @param contentType
	 *            - Content Type - PMCConstant.APPLICATION_JSON /
	 *            PMCConstant.APPLICATION_XML etc.
	 * @param isSaml
	 *            - true if trying to do SSO using SAML2.0
	 * @param xCsrfToken
	 *            - "Fetch", if need to do a PATCH/POST operation and
	 *            x-csrf-token is required
	 * @param userId
	 *            - userId for Basic Authentication
	 * @param password
	 *            - password for Basic Authentication
	 * @param accessToken
	 *            - Access token for other types of Authentications
	 * @param tokenType
	 *            - Access token for other types of Authentications -
	 *            Bearer/Basic etc.
	 * @return RestResponse with returnObject, responseCode, httpResponse
	 */
	public static RestResponse callRestService(String requestURL, String samlHeaderKey, String entity, String method,
			String contentType, Boolean isSaml, String xCsrfToken, String userId, String password, String accessToken,
			String tokenType) {

		RestResponse restResponse = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		StringEntity input = null;
		String json = null;
		JSONObject obj = null;
		JSONArray array = null;
		Object returnJson = null;
		String newxCsrfToken = null;

//		AuthenticationHeader appToAppSSOHeader = null;
		if (requestURL != null) {
			restResponse = new RestResponse();
			if (isSaml) {
//				if (ServicesUtil.isEmpty(appToAppSSOHeader)) {
//					appToAppSSOHeader = refreshAppToAppSSOHeader(requestURL);
//				}
			}

			try {
				if (xCsrfToken != null && xCsrfToken.equalsIgnoreCase("fetch")) {
					httpRequestBase = new HttpGet(requestURL);
					httpRequestBase.setHeader("x-csrf-token", xCsrfToken);
//					if (appToAppSSOHeader != null) {
//						httpRequestBase.addHeader(appToAppSSOHeader.getName(), appToAppSSOHeader.getValue());
//					}
					if (!ServicesUtil.isEmpty(userId) && !ServicesUtil.isEmpty(password)) {
						System.err.println("RestUtil.callRestService() user :"+userId+" pass : "+password);
						httpRequestBase.addHeader("Authorization", ServicesUtil.getBasicAuth(userId, password));
					}
					httpResponse = httpClient.execute(httpRequestBase);
					Header[] headers = httpResponse.getAllHeaders();
					for (Header header : headers) {
						if (header.getName().equalsIgnoreCase("x-csrf-token")) {
							System.err.println("RestUtil.callRestService() token  : "+xCsrfToken);
							newxCsrfToken = header.getValue();
							System.err.println("RestUtil.callRestService() token value : "+newxCsrfToken);
						}
					}
				}
			} catch (IOException e) {
				System.err.println("[WBP-Dev][Workbox]Exception : " + e);
			}

			if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_GET)) {
				httpRequestBase = new HttpGet(requestURL);
			} else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_POST)) {
				httpRequestBase = new HttpPost(requestURL);
				if (!ServicesUtil.isEmpty(entity)) {
					try {
						input = new StringEntity(entity);
						input.setContentType(contentType);
					} catch (UnsupportedEncodingException e) {
						System.err.println("[WBP-Dev][Workbox]Input UnsupportedEncodingException : " + e);
					}
					((HttpPost) httpRequestBase).setEntity(input);
				}
			} else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_PATCH)) {
				httpRequestBase = new HttpPatch(requestURL);
				if (!ServicesUtil.isEmpty(entity)) {
					try {
						input = new StringEntity(entity);
						input.setContentType(contentType);
					} catch (UnsupportedEncodingException e) {
						System.err.println("[WBP-Dev][Workbox]Input UnsupportedEncodingException : " + e);
					}
					((HttpPatch) httpRequestBase).setEntity(input);
				}
			}
//			if (httpRequestBase != null && appToAppSSOHeader != null && ServicesUtil.isEmpty(appToAppSSOHeader.getName()) 
//					&& ServicesUtil.isEmpty(appToAppSSOHeader.getValue())) {
//				httpRequestBase.addHeader(appToAppSSOHeader.getName(), appToAppSSOHeader.getValue());
//			}
			if (httpRequestBase != null && newxCsrfToken!= null && !newxCsrfToken.equalsIgnoreCase("fetch")){
				httpRequestBase.addHeader("x-csrf-token", newxCsrfToken);
			}
			if(httpRequestBase != null && ServicesUtil.isEmpty(contentType)){
				httpRequestBase.addHeader("accept", contentType);
			}
			if (httpRequestBase != null && !ServicesUtil.isEmpty(userId) && !ServicesUtil.isEmpty(password)) {
				httpRequestBase.addHeader("Authorization", ServicesUtil.getBasicAuth(userId, password));
			}

			if (httpRequestBase != null && !ServicesUtil.isEmpty(accessToken) && !ServicesUtil.isEmpty(tokenType)
					&& ServicesUtil.isEmpty(userId)) {
				httpRequestBase.addHeader("Authorization", ServicesUtil.getAuthorization(accessToken, tokenType));
			}
			try {
				if(httpRequestBase != null)
					httpResponse = httpClient.execute(httpRequestBase);
				if (!ServicesUtil.isEmpty(httpResponse) && !ServicesUtil.isEmpty(httpResponse.getEntity())) {
					json = EntityUtils.toString(httpResponse.getEntity());
					try {
						if (!ServicesUtil.isEmpty(json)) {
							returnJson = new Object[2];
							if (json.charAt(0) == '{') {
								obj = new JSONObject(json);
								returnJson = obj;
							} else if (json.charAt(0) == '[') {
								array = new JSONArray(json);
								returnJson = array;
							} else {
								returnJson = json;
							}
							restResponse.setResponseObject(returnJson);
						}
					} catch (JSONException e) {
						System.err.println("[WBP-Dev][Workbox]JSONException : " + e + "JSON Object : " + json);
					}
				}
				restResponse.setHttpResponse(httpResponse);
				restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
				httpClient.close();
			} catch (IOException e) {
				System.err.println("[WBP-Dev][Workbox]IOException : " + e);
			}
		}

		// System.err.println("[WBP-Dev]RestUtil.callRestService() Response
		// :"+restResponse);
		return restResponse;
	}

//	private static AuthenticationHeader refreshAppToAppSSOHeader(String requestURL) {
//		Context ctx;
//		AuthenticationHeader appToAppSSOHeader = null;
//		AuthenticationHeaderProvider authHeaderProvider;
//		try {
//			ctx = new InitialContext();
//			authHeaderProvider = (AuthenticationHeaderProvider) ctx.lookup("java:comp/env/AuthHeaderProvider");
//			appToAppSSOHeader = authHeaderProvider.getAppToAppSSOHeader(requestURL);
//		} catch (Exception ex) {
//			System.err.println("[WBP-Dev][Workbox]Exception while fetching auth Header Provider : " + ex);
//		}
//		return appToAppSSOHeader;
//	}

}
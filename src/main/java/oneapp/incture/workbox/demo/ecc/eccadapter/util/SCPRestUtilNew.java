package oneapp.incture.workbox.demo.ecc.eccadapter.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstantsStatic;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
//import com.sap.core.connectivity.api.authentication.AuthenticationHeader;
//import com.sap.core.connectivity.api.authentication.AuthenticationHeaderProvider;


@Component("ECCSCPRestUtilNew")
public class SCPRestUtilNew {



	public enum RestUtilAuth {
		PRINCIPAL_PROPOGATION_AUTH, APP_TO_APP_SSO_AUTH;
	}

	private static final Logger logger = LoggerFactory.getLogger(SCPRestUtil.class);

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
					// httpRequestBase.setHeader("x-csrf-token", xCsrfToken);
					httpRequestBase.addHeader("X-CSRF-Token", xCsrfToken);
//					if (appToAppSSOHeader != null) {
//						httpRequestBase.addHeader(appToAppSSOHeader.getName(), appToAppSSOHeader.getValue());
//					}

					if (!ServicesUtil.isEmpty(userId) && !ServicesUtil.isEmpty(password)) {
						httpRequestBase.addHeader("Authorization", ServicesUtil.getBasicAuth(userId, password));
					}
					// if (!ServicesUtil.isEmpty(accessToken) &&
					// !ServicesUtil.isEmpty(tokenType)) {
					// httpRequestBase.addHeader("Authorization",
					// ServicesUtil.getAuthorization(accessToken, tokenType));
					// }

					httpResponse = httpClient.execute(httpRequestBase);
					Header[] headers = httpResponse.getAllHeaders();
					for (Header header : headers) {
						System.err.println("[WBP-Dev]SCPRestUtil.callRestService() headers : " + header.getName() + " = "
								+ header.getValue());
						if (header.getName().equalsIgnoreCase("x-csrf-token")) {
							System.err.println("[WBP-Dev]SCPRestUtil.callRestService() x-csrf-token is : " + header.getValue());
							newxCsrfToken = header.getValue();
						}
					}
				}
			} catch (IOException e) {
				System.err.println("[WBP-Dev]Exception : " + e.getMessage());
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
						logger.error("Input UnsupportedEncodingException : " + e.getMessage());
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
						logger.error("Input UnsupportedEncodingException : " + e.getMessage());
					}
					((HttpPatch) httpRequestBase).setEntity(input);
				}
			}
//			if (httpRequestBase != null && appToAppSSOHeader != null) {
//				httpRequestBase.addHeader(appToAppSSOHeader.getName(), appToAppSSOHeader.getValue());
//			}
			if (httpRequestBase != null && newxCsrfToken != null && !newxCsrfToken.equalsIgnoreCase("fetch"))
				httpRequestBase.addHeader("X-CSRF-Token", newxCsrfToken);
			if(httpRequestBase != null)
				httpRequestBase.addHeader("accept", contentType);
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
				// System.err.println("[WBP-Dev]callRestService
				// httpResponse"+httpResponse);
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
							}
							restResponse.setResponseObject(returnJson);
						}
					} catch (JSONException e) {
						logger.error("JSONException : " + e + "JSON Object : " + json);
					}
				}
				restResponse.setHttpResponse(httpResponse);
				restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
				httpClient.close();
			} catch (IOException e) {
				logger.error("IOException : " + e);
			}
		}
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
//			logger.error("Exception while fetching auth Header Provider : " + ex.getMessage());
//		}
//		return appToAppSSOHeader;
//	}

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
	public static RestResponse callECCRestService(String requestURL, String samlHeaderKey, String entity, String method,
			String contentType, Boolean isSSO, String xCsrfToken, String userId, String password, String accessToken,
			String tokenType, RestUtilAuth auth, String proxyHost, Integer proxyPort) {

		RestResponse restResponse = null;
		DefaultProxyRoutePlanner proxyRoutePlanner = null;
		CloseableHttpClient httpClient = null;
		HttpHost proxy = null;
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
			if (!ServicesUtil.isEmpty(proxyHost) && !ServicesUtil.isEmpty(proxyPort)) {
				proxy = new HttpHost(proxyHost, proxyPort, HttpHost.DEFAULT_SCHEME_NAME);
				proxyRoutePlanner = new DefaultProxyRoutePlanner(proxy);
			}
			httpClient = HttpClientBuilder.create().setRoutePlanner(proxyRoutePlanner).build();
			if (!ServicesUtil.isEmpty(httpClient)) {
				restResponse = new RestResponse();
				if (isSSO) {
//					appToAppSSOHeader = null;
//					// if (ServicesUtil.isEmpty(appToAppSSOHeader)) {
//					appToAppSSOHeader = refreshAppToAppSSOHeader(requestURL, auth);
					// System.err.println("[WBP-Dev][inside if
					// ][appToAppSSOHeader]"+appToAppSSOHeader);
					// }
				}


//				if(!ServicesUtil.isEmpty(appToAppSSOHeader))
//					System.err.println("[WBP-Dev][appToAppSSOHeader][1]" + appToAppSSOHeader.getName()+" "+appToAppSSOHeader.getValue());

				try {
					if (xCsrfToken != null && xCsrfToken.equalsIgnoreCase("fetch")) {
						System.err.println("[WBP-Dev][callRestService][fetch]" + "");

						httpRequestBase = new HttpGet(requestURL);
						// httpRequestBase.addHeader("Authorization",
						// ServicesUtil.getBasicAuth(userId, password));
						httpRequestBase.addHeader("X-CSRF-Token", xCsrfToken);

						if (!ServicesUtil.isEmpty(userId) && !ServicesUtil.isEmpty(password)) {
							httpRequestBase.addHeader("Authorization", ServicesUtil.getBasicAuth(userId, password));
						}
//						if (appToAppSSOHeader != null) {
//							httpRequestBase.addHeader(appToAppSSOHeader.getName(), appToAppSSOHeader.getValue());
//						}
						httpRequestBase.addHeader("SAP-Connectivity-SCC-Location_ID", PMCConstant.CONN_LOCATION);
						httpRequestBase.addHeader("SAP-Connectivity-ConsumerAccount", PropertiesConstantsStatic.CONN_TENANT);
						httpResponse = httpClient.execute(httpRequestBase);
						System.err.println("[WBP-Dev][callRestService][x-csrf-token][httpResponse]" + httpResponse);
						Header[] headers = httpResponse.getAllHeaders();
						for (Header header : headers) {
							// System.err.println("[WBP-Dev][callRestService][header.getName()]"
							// + header.getName()
							// + ": header.getValue()" + header.getValue());

							if (header.getName().equalsIgnoreCase("x-csrf-token")) {
								System.err.println("[WBP-Dev][callRestService][header.getName()]" + header.getName()
								+ ": header.getValue()" + header.getValue());
								newxCsrfToken = header.getValue();
							}
						}
//						appToAppSSOHeader = refreshAppToAppSSOHeader(requestURL, auth);
//						System.err.println("[WBP-Dev][appToAppSSOHeader][2]" + appToAppSSOHeader);
					}
				} catch (IOException e) {
					System.err.println("[WBP-Dev]Exception : " + e.getMessage());
				}
				System.err.println("[WBP-Dev][requestUrl]" + requestURL + "");

				if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_GET)) {
					httpRequestBase = new HttpGet(requestURL);
				}else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_DELETE)) {
					httpRequestBase = new HttpDelete(requestURL);
				}
				else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_POST)) {
					httpRequestBase = new HttpPost(requestURL);
					if (!ServicesUtil.isEmpty(entity)) {
						try {
							input = new StringEntity(entity);
							input.setContentType(contentType);
						} catch (UnsupportedEncodingException e) {
							logger.error("Input UnsupportedEncodingException : " + e.getMessage());
						}
						((HttpPost) httpRequestBase).setEntity(input);
					}
				} else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_PUT)) {
					httpRequestBase = new HttpPut(requestURL);
					if (!ServicesUtil.isEmpty(entity)) {
						try {
							input = new StringEntity(entity);
							input.setContentType(contentType);
							
						} catch (UnsupportedEncodingException e) {
							logger.error("Input UnsupportedEncodingException : " + e.getMessage());
						}
						((HttpPut) httpRequestBase).setEntity(input);
					}
				} else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_PATCH)) {
					httpRequestBase = new HttpPatch(requestURL);
					if (!ServicesUtil.isEmpty(entity)) {
						try {
							input = new StringEntity(entity);
							input.setContentType(contentType);
						} catch (UnsupportedEncodingException e) {
							logger.error("Input UnsupportedEncodingException : " + e.getMessage());
						}
						((HttpPatch) httpRequestBase).setEntity(input);
					}
				}
//				if (httpRequestBase != null && appToAppSSOHeader != null) {
//					httpRequestBase.addHeader(appToAppSSOHeader.getName(), appToAppSSOHeader.getValue());
//				}

				// httpRequestBase.addHeader("SAP-Connectivity-ConsumerAccount",
				// PMCConstant.CONN_TENANT);
				if(httpRequestBase != null){
					httpRequestBase.addHeader("SAP-Connectivity-SCC-Location_ID", PMCConstant.CONN_LOCATION);
					httpRequestBase.addHeader("SAP-Connectivity-ConsumerAccount", PropertiesConstantsStatic.CONN_TENANT);
				}
				if (httpRequestBase != null && newxCsrfToken != null && !newxCsrfToken.equalsIgnoreCase("fetch"))
					httpRequestBase.addHeader("X-CSRF-Token", newxCsrfToken);
				if(httpRequestBase != null)
					httpRequestBase.addHeader("accept", contentType);
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
								// System.err.println("[WBP-Dev][returnJson]"+returnJson);
								restResponse.setResponseObject(returnJson);
							}
						} catch (JSONException e) {
							logger.error("JSONException : " + e + "JSON Object : " + json);
						}
					}
					restResponse.setHttpResponse(httpResponse);
					restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
					httpClient.close();
				} catch (IOException e) {
					System.err.println("[WBP-Dev]IOException : " + e);
				}
			}
		}
		return restResponse;
	}

	// Note : This method is only for Approvals Via ChatBot
	public static RestResponse callRestService(String requestURL, String samlHeaderKey, String entity, String method,
			String contentType, Boolean isSSO, String xCsrfToken, String userId, String password, String accessToken,
			String tokenType, RestUtilAuth auth, String proxyHost, Integer proxyPort, String headerName,
			String headerValue) {

		RestResponse restResponse = null;
		DefaultProxyRoutePlanner proxyRoutePlanner = null;
		CloseableHttpClient httpClient = null;
		HttpHost proxy = null;
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		StringEntity input = null;
		String json = null;
		JSONObject obj = null;
		JSONArray array = null;
		Object returnJson = null;
		String newxCsrfToken = null;

		// AuthenticationHeader appToAppSSOHeader = null;
		if (requestURL != null) {
			if (!ServicesUtil.isEmpty(proxyHost) && !ServicesUtil.isEmpty(proxyPort)) {
				proxy = new HttpHost(proxyHost, proxyPort, HttpHost.DEFAULT_SCHEME_NAME);
				proxyRoutePlanner = new DefaultProxyRoutePlanner(proxy);
			}
			httpClient = HttpClientBuilder.create().setRoutePlanner(proxyRoutePlanner).build();
			if (!ServicesUtil.isEmpty(httpClient)) {
				restResponse = new RestResponse();
				/*
				 * if (isSSO) { appToAppSSOHeader = null; // if
				 * (ServicesUtil.isEmpty(appToAppSSOHeader)) { appToAppSSOHeader
				 * = refreshAppToAppSSOHeader(requestURL, auth); //
				 * System.err.println("[WBP-Dev][inside if //
				 * ][appToAppSSOHeader]"+appToAppSSOHeader); // } }
				 */

				// System.err.println("[WBP-Dev][appToAppSSOHeader][1]" +
				// appToAppSSOHeader);
				try {
					if (xCsrfToken != null && xCsrfToken.equalsIgnoreCase("fetch")) {
						System.err.println("[WBP-Dev][callRestService][fetch]" + "");

						httpRequestBase = new HttpGet(requestURL);
						// httpRequestBase.addHeader("Authorization",
						// ServicesUtil.getBasicAuth(userId, password));
						httpRequestBase.addHeader("X-CSRF-Token", xCsrfToken);
						//						 if (appToAppSSOHeader != null) {
						//						 httpRequestBase.addHeader(appToAppSSOHeader.getName(),
						//						 appToAppSSOHeader.getValue());
						//						 }

						if (!ServicesUtil.isEmpty(headerName) && !ServicesUtil.isEmpty(headerValue)) {
							httpRequestBase.addHeader(headerName, headerValue);
						}

						httpRequestBase.addHeader("SAP-Connectivity-SCC-Location_ID", PMCConstant.CONN_LOCATION);
						httpRequestBase.addHeader("SAP-Connectivity-ConsumerAccount", PropertiesConstantsStatic.CONN_TENANT);
						httpResponse = httpClient.execute(httpRequestBase);
						System.err.println("[WBP-Dev]chatbot [callRestService][x-csrf-token][httpResponse]" + httpResponse);
						Header[] headers = httpResponse.getAllHeaders();
						for (Header header : headers) {
							// System.err.println("[WBP-Dev][callRestService][header.getName()]"
							// + header.getName()
							// + ": header.getValue()" + header.getValue());

							if (header.getName().equalsIgnoreCase("x-csrf-token")) {
								System.err.println("[WBP-Dev][callRestService][header.getName()]" + header.getName()
								+ ": header.getValue()" + header.getValue());
								newxCsrfToken = header.getValue();
							}
						}
						// appToAppSSOHeader =
						// refreshAppToAppSSOHeader(requestURL, auth);
						// System.err.println("[WBP-Dev][appToAppSSOHeader][2]" +
						// appToAppSSOHeader);
					}
				} catch (IOException e) {
					System.err.println("[WBP-Dev]Exception : " + e.getMessage());
				}
				System.err.println("[WBP-Dev][requestUrl]" + requestURL + "");

				if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_GET)) {
					httpRequestBase = new HttpGet(requestURL);
				} else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_POST)) {
					httpRequestBase = new HttpPost(requestURL);
					if (!ServicesUtil.isEmpty(entity)) {
						try {
							input = new StringEntity(entity);
							input.setContentType(contentType);
						} catch (UnsupportedEncodingException e) {
							logger.error("Input UnsupportedEncodingException : " + e.getMessage());
						}
						((HttpPost) httpRequestBase).setEntity(input);
					}
				}
				else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_PUT)) {
					httpRequestBase = new HttpPut(requestURL);
					if (!ServicesUtil.isEmpty(entity)) {
						try {
							input = new StringEntity(entity);
							input.setContentType(contentType);
						} catch (UnsupportedEncodingException e) {
							logger.error("Input UnsupportedEncodingException : " + e.getMessage());
						}
						((HttpPut) httpRequestBase).setEntity(input);
					}
				}else if (method.equalsIgnoreCase(PMCConstant.HTTP_METHOD_PATCH)) {
					httpRequestBase = new HttpPatch(requestURL);
					if (!ServicesUtil.isEmpty(entity)) {
						try {
							input = new StringEntity(entity);
							input.setContentType(contentType);
						} catch (UnsupportedEncodingException e) {
							logger.error("Input UnsupportedEncodingException : " + e.getMessage());
						}
						((HttpPatch) httpRequestBase).setEntity(input);
					}
				}
				// if (appToAppSSOHeader != null) {
				// httpRequestBase.addHeader(appToAppSSOHeader.getName(),
				// appToAppSSOHeader.getValue());
				// }

				if (httpRequestBase != null && !ServicesUtil.isEmpty(headerName) && !ServicesUtil.isEmpty(headerValue)) {
					httpRequestBase.addHeader(headerName, headerValue);
				}

				// httpRequestBase.addHeader("SAP-Connectivity-ConsumerAccount",
				// PMCConstant.CONN_TENANT);
				if(httpRequestBase != null){
					httpRequestBase.addHeader("SAP-Connectivity-SCC-Location_ID", PMCConstant.CONN_LOCATION);
					httpRequestBase.addHeader("SAP-Connectivity-ConsumerAccount", PropertiesConstantsStatic.CONN_TENANT);
				}
				if (httpRequestBase != null && newxCsrfToken != null && !newxCsrfToken.equalsIgnoreCase("fetch"))
					httpRequestBase.addHeader("X-CSRF-Token", newxCsrfToken);
				if(httpRequestBase != null)
					httpRequestBase.addHeader("accept", contentType);
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
								// System.err.println("[WBP-Dev][returnJson]"+returnJson);
								restResponse.setResponseObject(returnJson);
							}
						} catch (JSONException e) {
							logger.error("JSONException : " + e + "JSON Object : " + json);
						}
					}
					restResponse.setHttpResponse(httpResponse);
					restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
					httpClient.close();
				} catch (IOException e) {
					logger.error("IOException : " + e);
				}
			}
		}
		return restResponse;
	}

//	public static AuthenticationHeader refreshAppToAppSSOHeader(String requestURL, RestUtilAuth auth) {
//		Context ctx;
//		AuthenticationHeader appToAppSSOHeader = null;
//		AuthenticationHeaderProvider authHeaderProvider;
//		if (!ServicesUtil.isEmpty(auth)) {
//			try {
//				ctx = new InitialContext();
//				authHeaderProvider = (AuthenticationHeaderProvider) ctx.lookup("java:comp/env/AuthHeaderProvider");
//				switch (auth) {
//				case APP_TO_APP_SSO_AUTH:
//					appToAppSSOHeader = authHeaderProvider.getAppToAppSSOHeader(requestURL);
//					break;
//				case PRINCIPAL_PROPOGATION_AUTH:
//					appToAppSSOHeader = authHeaderProvider.getPrincipalPropagationHeader();
//					break;
//				}
//			} catch (Exception ex) {
//				logger.error("Exception while fetching auth Header Provider : " + ex.getMessage());
//			}
//		}
//		// System.err.println("[WBP-Dev][appToAppSSOHeader]"+appToAppSSOHeader);
//		return appToAppSSOHeader;
//	}

}

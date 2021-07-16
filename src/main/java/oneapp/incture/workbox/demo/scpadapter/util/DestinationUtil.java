package oneapp.incture.workbox.demo.scpadapter.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyStore;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import com.sap.cloud.account.TenantContext;
//import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
//import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

public class DestinationUtil {

	private static final Logger logger = LoggerFactory.getLogger(DestinationUtil.class);

//	private static TenantContext tenantContext;

	public static String executeWithDest(String destinationName, String absoluteUrl, String httpMethod,
			String contentType, String tenantId, String payload, String proxyType, boolean hasCert, String locationId,
			String keyStorePassword) {
//
//		try {
//			DestinationConfiguration destConfiguration = null;
//			if (!ServicesUtil.isEmpty(destinationName)) {
//				destConfiguration = getDest(destinationName);
//			}
//			HttpURLConnection connection = injectHeaders(destConfiguration, absoluteUrl, httpMethod, contentType,
//					tenantId, payload, proxyType, hasCert, locationId, keyStorePassword);
//			return getDataFromConnection(connection);
//		} catch (Exception e) {
//			logger.error("[PMC][DestinationUtil][executeWithDest][error]" + e.getMessage());
//		}
		return null;
	}
	
//	public static void main(String[] args) {
//		DestinationUtil.executeWithDest("destincationName", "/absoluteURL", "GET/POST", "application/json", null, null, "Internet", false, null, null);
//	}

//	private static DestinationConfiguration getDest(String destinationName) {
//		if (!ServicesUtil.isEmpty(destinationName)) {
//			try {
//				Context ctx = new InitialContext();
//				ConnectivityConfiguration configuration = (ConnectivityConfiguration) ctx
//						.lookup("java:comp/env/connectivityConfiguration");
//				tenantContext = (TenantContext) ctx.lookup("java:comp/env/tenantContext");
//				DestinationConfiguration destConfiguration = configuration.getConfiguration(destinationName);
//				return destConfiguration;
//			} catch (Exception e) {
//				logger.error("[PMC][DestinationUtil][getDest][error]" + e + "[getMessage]" + e.getMessage());
//			}
//		}
//		return null;
//	}

	private static String getDataFromConnection(HttpURLConnection urlConnection) {

		try {
			StringBuilder jsonString = new StringBuilder();
			if (!ServicesUtil.isEmpty(urlConnection)) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					jsonString.append(line);
				}
				br.close();
				return jsonString.toString();
			}
		} catch (Exception e) {
			logger.error("[PMC][DestinationUtil][getDataFromConnection][error]" + e.getMessage());
		}
		return null;
	}

//	@SuppressWarnings("deprecation")
//	private static HttpURLConnection injectHeaders(DestinationConfiguration destConfiguration, String absoluteUrl,
//			String httpMethod, String contentType, String tenantId, String payload, String proxyType, boolean hasCert,
//			String locationId, String keyStorePassword) {
//
//		try {
//			HttpURLConnection urlConnection = null;
//			String authentication = "";
//			Proxy proxy = null;
//
//			if (!ServicesUtil.isEmpty(destConfiguration)) {
//
//				if (!ServicesUtil.isEmpty(destConfiguration.getProperty("User"))) {
//					String user = destConfiguration.getProperty("User");
//					String password = destConfiguration.getProperty("Password");
//					authentication = ServicesUtil.getBasicAuth(user, password);
//				}
//				String baseUrl = destConfiguration.getProperty("URL");
//				absoluteUrl = baseUrl + absoluteUrl;
//				proxyType = destConfiguration.getProperty("ProxyType");
//				if (!ServicesUtil.isEmpty(proxyType)) {
//					proxy = getProxy(proxyType);
//				}
//			}
//			URL url = new URL(absoluteUrl);
//			if (!ServicesUtil.isEmpty(proxy)) {
//				urlConnection = (HttpURLConnection) url.openConnection(proxy);
//			} else {
//				urlConnection = (HttpURLConnection) url.openConnection();
//			}
//
//			urlConnection.setRequestProperty("Accept", contentType);
//			urlConnection.setRequestMethod(httpMethod);
//
//			if (hasCert) {
//				KeyStore keyStore = destConfiguration.getKeyStore();
//				KeyStore trustStore = destConfiguration.getTrustStore();
//				TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//				tmf.init(trustStore);
//				KeyManagerFactory keyManagerFactory = KeyManagerFactory
//						.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//				keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
//				SSLContext sslcontext = SSLContext.getInstance("TLS");
//				sslcontext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
//				SSLSocketFactory sslSocketFactory = sslcontext.getSocketFactory();
//				((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
//			}
//			if (httpMethod.equals(PMCConstant.HTTP_METHOD_POST)) {
//				urlConnection.setDoOutput(true);
//			}
//			if (!ServicesUtil.isEmpty(authentication)) {
//				urlConnection.setRequestProperty("Authorization", authentication);
//			}
//			if (PMCConstant.ON_PREMISE_PROXY.equals(proxyType)) {
//				urlConnection.setRequestProperty("SAP-Connectivity-ConsumerAccount",
//						tenantContext.getTenantId());
//				urlConnection.setRequestProperty("SAP-Connectivity-SCC-Location_ID", locationId);
//			}
//			if (!ServicesUtil.isEmpty(payload)) {
//				OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
//				wr.write(payload);
//				wr.flush();
//			}
//			return urlConnection;
//
//		} catch (Exception e) {
//			logger.error("[PMC][DestinationUtil][injectHeaders][error]" + e.getMessage());
//		}
//		return null;
//	}

	private static Proxy getProxy(String proxyType) {
		Proxy proxy = Proxy.NO_PROXY;
		String proxyHost = null;
		String proxyPort = null;

		if (PMCConstant.ON_PREMISE_PROXY.equals(proxyType)) {
			proxyHost = System.getenv("HC_OP_HTTP_PROXY_HOST");
			proxyPort = System.getenv("HC_OP_HTTP_PROXY_PORT");
		} else {
			proxyHost = System.getProperty("https.proxyHost");
			proxyPort = System.getProperty("https.proxyPort");
		}

		if (proxyPort != null && proxyHost != null) {
			int proxyPortNumber = Integer.parseInt(proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPortNumber));
		}
		return proxy;
	}

}
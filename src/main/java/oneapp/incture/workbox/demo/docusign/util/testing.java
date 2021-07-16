package oneapp.incture.workbox.demo.docusign.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;



public class testing {
	
	public static RestResponse callPostService(String requesturl,String accessToken,String payload){
		int code=500;
		RestResponse restResponse = new RestResponse();
		HttpResponse response = null;
		try{
			//URIBuilder signignBuilder = new URIBuilder(DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes/"+envelopeId+"/views/recipient");
			//HttpPost Signingpost = new HttpPost(DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes/"+envelopeId+"/views/recipient");
			HttpPost post = new HttpPost(requesturl);
			post.setHeader("Accept","application/json");
			post.setHeader("Authorization","Bearer "+accessToken);
			
			StringEntity entity = new StringEntity(payload,ContentType.APPLICATION_JSON);
			

			
			post.setEntity(entity);
			
			
			HttpClient client = new DefaultHttpClient();
			response = client.execute(post);
			code = response.getStatusLine().getStatusCode();
			System.err.println(response);
			JSONObject jsonObject =new JSONObject(EntityUtils.toString(response.getEntity()));
			restResponse.setHttpResponse(response);
			restResponse.setResponseCode(code);
			restResponse.setResponseObject(jsonObject);
			return restResponse;
		}catch (ClientProtocolException e) {
			System.err.println("[WBP-Dev][Docusign][EnvelopeService][getSigningUrl][ClientProtocolException]Error");
		}catch (IOException e) {
			System.err.println("[WBP-Dev][Docusign][EnvelopeService][getSigningUrl][EntityIOException]Error");
		}
		restResponse.setHttpResponse(response);
		restResponse.setResponseCode(code);
		restResponse.setResponseObject(response);
		return restResponse;
	}
	public static RestResponse callGetService(String requestUrl,String accessToken){
		RestResponse restResponse = new RestResponse();
		JSONObject jsonObject = null;
		HttpResponse response = null;
		int code = 500;
		try{
			HttpGet get =new HttpGet(requestUrl);
			get.setHeader("Accept", "application/json");
			get.setHeader("Authorization", "Bearer " + accessToken);
	
			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(get);
	
			code = response.getStatusLine().getStatusCode();
			System.err.println(response);
			if(code==200){
				String responsejson = EntityUtils.toString(response.getEntity());
				jsonObject = new JSONObject(responsejson);
				restResponse.setResponseObject(jsonObject);
				restResponse.setHttpResponse(response);
				restResponse.setResponseCode(code);
				
				return restResponse;
			}
			
		}catch (ClientProtocolException e) {
			System.err.println("[WBP-Dev][Docusign][EnvelopeService][getEnvelope][ClientProtocolException]Error");
		}catch (IOException e) {
			System.err.println("[WBP-Dev][Docusign][EnvelopeService][getEnvelope][EntityIOException]Error");
		}
		restResponse.setResponseCode(code);
		restResponse.setHttpResponse(response);
		return restResponse;
	}
	
	public static void main(String[] args) throws ParseException {
		
		///////////////////////////////////////////////////////////////////////
		//String accessToken = getJwtAccessToken();
		//System.out.println(accessToken);
		Date date = new Date();
		Date currentDate = new Date(System.currentTimeMillis() -60 * 60 * 5 * 1000);
		String dateString = null;
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateString = sdfr.format(currentDate);
		//System.out.println(currentDate);
		//System.out.println(dateString);
		
		Instant instant = Instant.now().minusMillis(1000*60*5);
		Instant instant2 = Instant.parse("2020-10-27T05:44:10.8970000Z");
		Date date2 = Date.from(instant2);
		System.out.println(instant2.toString());
		System.out.println(date2);
		
		
		
		
//		String requestUrl = DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes?from_date="+dateString;
//		System.out.println(requestUrl);
//		RestResponse response = callGetService(requestUrl, accessToken);
//		System.out.println(response);
		//////////////////////////////////////////////////////////////////////
		
		
		
//		String envelopeId = "6065aa5b-d384-420b-8eec-9cf012b4452e";
//		String recipientUserId = "748c9c0f-fffc-4c13-84fe-81f3fd6990dc";
//		String accessTokenRecipient = getJwtAccessToken();
//		String requestUrl = DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes/"+envelopeId+"/recipients";
//		System.out.println(requestUrl);
//		RestResponse response = callGetService(requestUrl, accessTokenRecipient);
//		System.out.println(response);
		
		
		
		///////////////////////////////////////////////////////////////////////
		
//		String accessTokenForURL = getJwtAccessToken();
//		String requesturl = DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes/"+envelopeId+"/views/recipient";
//		String payload = "{\"userName\":\""+recipientUserId+"\",\"email\":\"info.incture@gmail.com\",\"returnUrl\":\"https://sreerajavk.github.io/\",\"authenticationMethod\":\"Email\"}";
//		RestResponse restResponse = callPostService(requesturl, accessTokenForURL, payload);
//		System.out.println(restResponse);
		
	}
	
	 @Autowired
	 ResourceLoader resourceLoader;

	private static String privateKeyPath = "D:/WorkBox Adapter/Prkey.txt";
	private static String publicKeyPath = "D:/WorkBox Adapter/PKey.txt";

	
	public static String getJwtAccessToken(){
		String jwtAssertion = generateJWTAssertion(publicKeyPath, privateKeyPath,DocusignConstant.oAuthBasePath,DocusignConstant.integrationKey,DocusignConstant.userIdAdmin,3600);	
		String accessToken = generateAccessToken(jwtAssertion);
		return accessToken;
	}
	
	public static String getJwtAccessTokenUserId(String userIdFromAdmin){
		String jwtAssertionusingUserid = generateJWTAssertion(publicKeyPath, privateKeyPath,DocusignConstant.oAuthBasePath,DocusignConstant.integrationKey,userIdFromAdmin,3600);	
		String accessTokenfromuserid = generateAccessToken(jwtAssertionusingUserid);
		return accessTokenfromuserid;
	}
	
	

	public static String generateAccessToken(String assertion) {
		int code;
		String accessToken = "";
		HttpPost post = new HttpPost("https://account-d.docusign.com/oauth/token");

		// add request parameter, form parameters
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("grant_type", " urn:ietf:params:oauth:grant-type:jwt-bearer"));
		urlParameters.add(new BasicNameValuePair("assertion", assertion));
		try{
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(post);

			code = response.getStatusLine().getStatusCode();
		
			if (code == 200) {
				String tokenResponse = EntityUtils.toString(response.getEntity());
				JSONObject myObject = new JSONObject(tokenResponse);
				accessToken = myObject.getString("access_token");
			}
		}catch (UnsupportedEncodingException e) {
			System.err.println("[WBP-Dev][Docusign][AccessToken][Encoding]Error");
		}catch (ClientProtocolException e) {
			System.err.println("[WBP-Dev][Docusign][AccessToken][ClientProtocol]Error");
		} catch (IOException e) {
			System.err.println("[WBP-Dev][Docusign][AccessToken][IOException]Error");
		}

		return accessToken;
	}

	private static RSAPrivateKey readPrivateKeyFromFile(String privateKeyPath,String algorithm) throws IOException {
		File pemFile = new File(privateKeyPath);
		if (!pemFile.isFile() || !pemFile.exists()) {
			throw new FileNotFoundException(String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
		}
		PemReader reader = new PemReader(new FileReader(pemFile));
		try {
			PemObject pemObject = reader.readPemObject();
			byte[] bytes = pemObject.getContent();
			RSAPrivateKey privateKey = null;
			try {
				Security.addProvider(new BouncyCastleProvider());
				KeyFactory kf = KeyFactory.getInstance(algorithm, "BC");
				EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
				privateKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
			} catch (NoSuchAlgorithmException e) {
				System.err.println("[WBP-Dev][Docusign][AccessToken] Could not reconstruct the private key, the given algorithm could not be found.");
			} catch (InvalidKeySpecException e) {
				System.err.println("[WBP-Dev][Docusign][AccessToken] Could not reconstruct the private key");
			} catch (NoSuchProviderException e) {
				System.err.println("[WBP-Dev][Docusign][AccessToken] Could not reconstruct the private key, invalid provider.");
			}

			return privateKey;

		} finally {
			reader.close();
		}
	}

	private static RSAPublicKey readPublicKeyFromFile(String publicKeyPath,String algorithm) throws IOException {
		
	

		File pemFile = new File(publicKeyPath);
		if (!pemFile.isFile() || !pemFile.exists()) {
			throw new FileNotFoundException(String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
		}
		PemReader reader = new PemReader(new FileReader(pemFile));
		try {
			PemObject pemObject = reader.readPemObject();
			byte[] bytes = pemObject.getContent();
			RSAPublicKey publicKey = null;
			try {
				KeyFactory kf = KeyFactory.getInstance(algorithm);
				EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
				publicKey = (RSAPublicKey) kf.generatePublic(keySpec);
			} catch (NoSuchAlgorithmException e) {
				System.err.println("[WBP-Dev][Docusign][AccessToken] Could not reconstruct the public key, the given algorithm could not be found.");
			} catch (InvalidKeySpecException e) {
				System.err.println("[WBP-Dev][Docusign][AccessToken] Could not reconstruct the public key");
			}

			return publicKey;
		} finally {
			reader.close();
		}
	}

	public static String generateJWTAssertion(String publicKeyFilename, String privateKeyFilename, String oAuthBasePath,
			String clientId, String userId, long expiresIn){
		String token = null;
			if (expiresIn <= 0L) {
				throw new IllegalArgumentException("expiresIn should be a non-negative value");
			}
			if (publicKeyFilename == null || "".equals(publicKeyFilename) || privateKeyFilename == null
					|| "".equals(privateKeyFilename) || oAuthBasePath == null || "".equals(oAuthBasePath)
					|| clientId == null || "".equals(clientId) || userId == null || "".equals(userId)) {
				throw new IllegalArgumentException("One of the arguments is null or empty");
			}
			System.err.println("inside jwtassertioncreation");

		try {
			RSAPublicKey publicKey = readPublicKeyFromFile(publicKeyPath,"RSA");
			RSAPrivateKey privateKey = readPrivateKeyFromFile(privateKeyPath,"RSA");
			Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			long now = System.currentTimeMillis();
			token = JWT.create().withIssuer(clientId).withSubject(userId).withAudience(oAuthBasePath)
					.withNotBefore(new Date(now)).withExpiresAt(new Date(now + expiresIn * 1000))
					.withClaim("scope", "signature").sign(algorithm);
		} catch (JWTCreationException e) {
			System.err.println("[WBP-Dev][Docusign][AccessToken][JWTCreation]Error");
		} catch (IOException e) {
			System.err.println("[WBP-Dev][Docusign][AccessToken][Assertion][IOException]Error");
		}
		System.err.println("inside jwtassertioncreation"+token);
		return token;
	}
	
}

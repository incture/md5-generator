package oneapp.incture.workbox.demo.docusign.util;
import java.io.IOException;
import java.io.StringReader;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Component
public class AccessToken {
	
	 @Autowired
	 ResourceLoader resourceLoader;
	 
	 @Autowired
	 AddKeys addKeys;

	private String privateKeyPath = "D:/WorkBox Adapter/Prkey.txt";
	private String publicKeyPath = "D:/WorkBox Adapter/PKey.txt";
	
	public String getJwtAccessToken(){
		String jwtAssertion = generateJWTAssertion(publicKeyPath, privateKeyPath,DocusignConstant.oAuthBasePath,DocusignConstant.integrationKey,DocusignConstant.userIdAdmin,3600);	
		String accessToken = generateAccessToken(jwtAssertion);
		return accessToken;
	}
	
	public String getJwtAccessTokenUserId(String userIdFromAdmin){
		String jwtAssertionusingUserid = generateJWTAssertion(publicKeyPath, privateKeyPath,DocusignConstant.oAuthBasePath,DocusignConstant.integrationKey,userIdFromAdmin,3600);	
		String accessTokenfromuserid = generateAccessToken(jwtAssertionusingUserid);
		return accessTokenfromuserid;
	}
	
	

	public String generateAccessToken(String assertion) {
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

	private RSAPrivateKey readPrivateKeyFromFile(String privateKeyPath,String algorithm) throws IOException {
		//String encprivKey = addKeys.getFile(DocusignConstant.privateObjectID).getEncodedFileContent();
		String encprivKey = addKeys.getFileUsingSharepoint(DocusignConstant.privateDocumentId);
		byte[] prkey =  Base64.getDecoder().decode(encprivKey);
		//File pemFile = new File(privateKeyPath);
//		if (!pemFile.isFile() || !pemFile.exists()) {
//			throw new FileNotFoundException(String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
//		}
		//PemReader reader = new PemReader(new FileReader(pemFile));
		PemReader reader = new PemReader(new StringReader(new String(prkey)));
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

	private RSAPublicKey readPublicKeyFromFile(String publicKeyPath,String algorithm) throws IOException {
		//String encpublickey = addKeys.getFile(DocusignConstant.publicObjectID).getEncodedFileContent();
		String encpublickey=addKeys.getFileUsingSharepoint(DocusignConstant.publicDocumentId);
		
		byte[] pukey = Base64.getDecoder().decode(encpublickey);
	

//		File pemFile = new File(publicKeyPath);
//		if (!pemFile.isFile() || !pemFile.exists()) {
//			throw new FileNotFoundException(String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
//		}
//		PemReader reader = new PemReader(new FileReader(pemFile));
		PemReader reader = new PemReader(new StringReader(new String(pukey)));
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

	public String generateJWTAssertion(String publicKeyFilename, String privateKeyFilename, String oAuthBasePath,
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

package oneapp.incture.workbox.demo.sharepoint.util;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.microsoft.aad.msal4j.IAccount;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.SilentParameters;
import com.microsoft.aad.msal4j.UserNamePasswordParameters;



public class TokenGenerator {
	static  PublicClientApplication app=null;
	static String result;
	
	private TokenGenerator() {
	    throw new IllegalAccessError("Utility class");
	  }

//  public static void main(String args[]) 
//  {
//	  getAccessTokenFromUserCredentials();
//  }
	
  static String getAccessTokenFromUserCredentials()  {

	  try{
	   app = PublicClientApplication.builder(SharePointConstant.PUBLIC_CLIENT_ID)
	    .authority(SharePointConstant.AUTHORITY_ORGANIZATION).build();
	  }catch(Exception e){
		  System.err.println("TokenGenerator.getAccessTokenFromUserCredentials() error : "+e);
	  }
	  UserNamePasswordParameters parameters = UserNamePasswordParameters
	    .builder(Collections.singleton(SharePointConstant.GRAPH_DEFAULT_SCOPE), SharePointConstant.USER_NAME,
	    		SharePointConstant.USER_PASSWORD.toCharArray())
	    .build();

	  CompletableFuture<IAuthenticationResult> future = app.acquireToken(parameters);

	  future.handle((res, ex) -> {
	   if (ex != null) {
	    System.err.println("[WBP-Dev]Oops! We have an exception - " + ex.getMessage());
	    return "Unknown!";
	   }
	   Collection<IAccount> accounts = app.getAccounts().join();
	   CompletableFuture<IAuthenticationResult> future1;
	   try {
	    future1 = app.acquireTokenSilently(SilentParameters
	      .builder(Collections.singleton(SharePointConstant.GRAPH_DEFAULT_SCOPE), accounts.iterator().next())
	      .forceRefresh(true).build());
	   } catch (MalformedURLException e) {
	    throw new RuntimeException();
	   }
	   future1.join();
	   IAccount account = app.getAccounts().join().iterator().next();
	   app.removeAccount(account).join();
	   accounts = app.getAccounts().join();
	   System.err.println("[WBP-Dev]Num of account - " + accounts.size());
	   System.err.println("[WBP-Dev]Returned ok - " + res);
	   System.err.println("[WBP-Dev]Access Token - " + res.accessToken());
	   TokenGenerator.result=res.accessToken();
	   System.err.println("[WBP-Dev]ID Token - " + res.idToken());
	   return res;
	  }).join();
	return result;

	 }
}
  


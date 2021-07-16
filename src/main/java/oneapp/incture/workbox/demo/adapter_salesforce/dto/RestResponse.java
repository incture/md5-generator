package oneapp.incture.workbox.demo.adapter_salesforce.dto;

import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;

public class RestResponse {

	private Object responseObject;
	private int responseCode;
	private HttpResponse httpResponse;
	private HttpURLConnection urlConnection;

	public Object getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(Object responseObject) {
		this.responseObject = responseObject;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public HttpResponse getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	public HttpURLConnection getUrlConnection() {
		return urlConnection;
	}

	public void setUrlConnection(HttpURLConnection urlConnection) {
		this.urlConnection = urlConnection;
	}

	@Override
	public String toString() {
		return "RestResponse [responseObject=" + responseObject + ", responseCode=" + responseCode + ", httpResponse="
				+ httpResponse + ", urlConnection=" + urlConnection + "]";
	}
}

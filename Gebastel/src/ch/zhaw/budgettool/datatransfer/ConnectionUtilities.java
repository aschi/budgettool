package ch.zhaw.budgettool.datatransfer;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.message.BasicHeader;

public class ConnectionUtilities {

	
	public static HttpPost getPostRequest(String action, String[] params, String username, String password) throws AuthenticationException{
		HttpPost req = new HttpPost(Configuration.basePath + action + (params != null ? "/" + join(params, "/") : ""));
		req.addHeader(new BasicHeader("Accept", "application/json"));
		if(username != null && password != null){
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
					username, password);
			req.addHeader(new BasicScheme().authenticate(creds, req));
		}
		

		return req;
	}
	
	public static HttpGet getGetRequest(String action, String[] params, String username, String password) throws AuthenticationException{
		HttpGet req = new HttpGet(Configuration.basePath + action + "/" + (params != null ? "/" + join(params, "/") : ""));
		req.addHeader(new BasicHeader("Accept", "application/json"));
		if(username != null && password != null){
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
					username, password);
			req.addHeader(new BasicScheme().authenticate(creds, req));
		}
		return req;
	}
	
	public static String join(String r[],String d)
	{
	        if (r.length == 0) return "";
	        StringBuilder sb = new StringBuilder();
	        int i;
	        for(i=0;i<r.length-1;i++)
	            sb.append(r[i]+d);
	        return sb.toString()+r[i];
	}
	
}

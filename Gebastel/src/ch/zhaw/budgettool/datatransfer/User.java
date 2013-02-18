package ch.zhaw.budgettool.datatransfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import ch.zhaw.budgettool.datatransfer.JSONClasses.UserData;

import com.google.gson.Gson;

public class User {
	private DefaultHttpClient httpclient;
	private HttpHost target;
	private Gson gson;

	private String username;
	private String password;
	private int id;

	public User() {
		gson = new Gson();
		httpclient = new DefaultHttpClient();
		target = new HttpHost(Configuration.host, Configuration.port,
				Configuration.protocol);
	}

	public User(String username, String password) {
		this();
	
	}

	public boolean login(String username, String password){
		this.username = username;
		this.password = password;
		try {
			String[] params = {Integer.toString(this.id)};
			HttpGet req = ConnectionUtilities.getGetRequest("users/view", params, this.username, this.password);
			HttpResponse response = httpclient.execute(target, req);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			
			StringBuffer sb = new StringBuffer();
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			UserData data = gson.fromJson(sb.toString(), UserData.class);
	
			if(data == null){
				return false;
			}else{
				this.id = Integer.parseInt(data.getUser().getUser().getId());
				return true;
			}
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	public UserData view(){
		try {
			String[] params = {Integer.toString(this.id)};
			HttpGet req = ConnectionUtilities.getGetRequest("users/view", params, this.username, this.password);
			HttpResponse response = httpclient.execute(target, req);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			
			StringBuffer sb = new StringBuffer();
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			UserData data = gson.fromJson(sb.toString(), UserData.class);
			
			return data;

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void edit(String username, String password) {
		try {
			String[] params = {Integer.toString(this.id)};
			HttpPost req = ConnectionUtilities.getPostRequest("users/edit", params, this.username, this.password);
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("data[User][username]",
					username));
			nameValuePairs.add(new BasicNameValuePair("data[User][new_password]",
					password));

			req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(target, req);
			
			this.username = username;
			this.password = password;
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void delete(){
		try {
			String[] params = {Integer.toString(this.id)};
			HttpPost req = ConnectionUtilities.getPostRequest("users/delete", params, this.username, this.password);
			HttpResponse response = httpclient.execute(target, req);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public UserData add(String username, String password) {
		this.username = username;
		this.password = password;
		try {
			HttpPost req = ConnectionUtilities.getPostRequest("users/add", null, null, null);
			
			//add content
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("data[User][username]",
					username));
			nameValuePairs.add(new BasicNameValuePair("data[User][password]",
					password));

			req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(target, req);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			
			StringBuffer sb = new StringBuffer();
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			UserData data = gson.fromJson(sb.toString(), UserData.class);
			this.id = Integer.parseInt(data.getUser().getUser().getId());
			return data;
		} catch (IOException | AuthenticationException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}

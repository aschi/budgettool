package ch.zhaw.budgettool.datatransfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import ch.zhaw.budgettool.datatransfer.JSONClasses.Available;
import ch.zhaw.budgettool.datatransfer.JSONClasses.GroupData;

import com.google.gson.Gson;

public class Group implements TransferClass{
	private DefaultHttpClient httpclient;
	private HttpHost target;
	private Gson gson;

	private String groupname;
	private String password;
	private double budget;
	private int id;
	private User user;
	private String errorMsg;
	
	public Group(User user) {
		this.user = user;
		
		gson = new Gson();
		httpclient = new DefaultHttpClient();
		target = new HttpHost(Configuration.host, Configuration.port,
				Configuration.protocol);
	}
	
	public GroupData add(String groupname, String password, double budget){
		this.groupname = groupname;
		this.password = password;
		this.budget = budget;
		
		try {
			HttpPost req = ConnectionUtilities.getPostRequest("groups/add", null, this.user.getUsername(), this.user.getPassword());
			
			//add content
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("data[Group][group_name]",
					groupname));
			nameValuePairs.add(new BasicNameValuePair("data[Group][password]",
					password));
			nameValuePairs.add(new BasicNameValuePair("data[Group][budget]",
					Double.toString(budget)));

			req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(target, req);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			
			StringBuffer sb = new StringBuffer();
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			GroupData data = gson.fromJson(sb.toString(), GroupData.class);
			this.id = Integer.parseInt(data.getGroup().getGroup().getId());
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AuthenticationException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	public boolean groupNameAvailable(String groupname){
		try {
			String[] params = {groupname};
			HttpGet req = ConnectionUtilities.getGetRequest("groups/groupNameAvailable", params, this.user.getUsername(), this.user.getPassword());
			HttpResponse response = httpclient.execute(target, req);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			
			StringBuffer sb = new StringBuffer();
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			Available av = gson.fromJson(sb.toString(), Available.class);
			return av.getAvailable();
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
	
	public GroupData view(){
		try {
			String[] params = {Integer.toString(this.id)};
			HttpGet req = ConnectionUtilities.getGetRequest("groups/view", params, this.user.getUsername(), this.user.getPassword());
			HttpResponse response = httpclient.execute(target, req);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			
			StringBuffer sb = new StringBuffer();
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			GroupData data = gson.fromJson(sb.toString(), GroupData.class);
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
	
	public void edit(String groupname, String password, double budget){
		try {
			String[] params = {Integer.toString(this.id)};
			HttpPost req = ConnectionUtilities.getPostRequest("groups/edit", params, this.user.getUsername(), this.user.getPassword());
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("data[Group][group_name]",
					groupname));
			if(password != ""){
				nameValuePairs.add(new BasicNameValuePair("data[Group][new_password]",
						password));	
			}
			
			nameValuePairs.add(new BasicNameValuePair("data[Group][budget]",
					Double.toString(budget)));

			req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(target, req);
			
			this.groupname = groupname;
			this.password = password;
			this.budget = budget;
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
			HttpPost req = ConnectionUtilities.getPostRequest("groups/delete", params, this.user.getUsername(), this.user.getPassword());
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
	
	public void leaveGroup(){
		try {
			String[] params = {Integer.toString(this.id)};
			HttpPost req = ConnectionUtilities.getPostRequest("groups/leaveGroup", params, this.user.getUsername(), this.user.getPassword());
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
	
	//
	public GroupData joinGroup(String groupname, String password){
		try {
			HttpPost req = ConnectionUtilities.getPostRequest("groups/joinGroup", null, this.user.getUsername(), this.user.getPassword());
			
			//add content
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("data[Group][group_name]",
					groupname));
			
			nameValuePairs.add(new BasicNameValuePair("data[Group][password]",
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
			
			GroupData data = gson.fromJson(sb.toString(), GroupData.class);
			this.groupname = groupname;
			this.password = password;
			this.id = Integer.parseInt(data.getGroup().getGroup().getId());
			this.budget = Double.parseDouble(data.getGroup().getGroup().getBudget());
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AuthenticationException e){
			e.printStackTrace();
		}
		return null;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}

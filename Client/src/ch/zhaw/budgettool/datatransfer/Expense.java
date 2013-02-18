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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import ch.zhaw.budgettool.datatransfer.JSONClasses.ExpenseData;

import com.google.gson.Gson;

public class Expense implements TransferClass{
	private DefaultHttpClient httpclient;
	private HttpHost target;
	private Gson gson;

	private String description;
	private double value;
	private String date;
	private int id;
	private User user;
	
	public Expense(User user) {
		this.user = user;
		
		gson = new Gson();
		httpclient = new DefaultHttpClient();
		target = new HttpHost(Configuration.host, Configuration.port,
				Configuration.protocol);
	}
	
	public ExpenseData add(String description, double value, String date){
		this.description = description;
		this.value = value;
		this.date = date;
		
		try {
			HttpPost req = ConnectionUtilities.getPostRequest("expenses/add", null, this.user.getUsername(), this.user.getPassword());
			
			//add content
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("data[Expense][description]",
					description));
			nameValuePairs.add(new BasicNameValuePair("data[Expense][value]",
					Double.toString(value)));
			nameValuePairs.add(new BasicNameValuePair("data[Expense][date]",
					date));

			req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(target, req);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String line = "";
			
			StringBuffer sb = new StringBuffer();
			
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			ExpenseData data = gson.fromJson(sb.toString(), ExpenseData.class);
			this.id = Integer.parseInt(data.getExpense().getExpense().getId());
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AuthenticationException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void delete(){
		try {
			String[] params = {Integer.toString(this.id)};
			HttpPost req = ConnectionUtilities.getPostRequest("expenses/delete", params, this.user.getUsername(), this.user.getPassword());
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}

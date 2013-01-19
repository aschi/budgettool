package ch.zhaw.budgettool.datatransfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class User {
	private DefaultHttpClient httpclient;
	private HttpHost target;

	private String username;
	private String password;
	private int id;

	public User() {
		httpclient = new DefaultHttpClient();
		target = new HttpHost(Configuration.host, Configuration.port,
				Configuration.protocol);
	}

	public void add(String username, String password) {
		URL url;
		HttpPost req = new HttpPost(Configuration.basePath + "users/add");
		req.setHeader(new Header() {
			public String getValue() {
				return null;
			}

			public String getName() {
				return null;
			}

			public HeaderElement[] getElements() throws ParseException {
				return null;
			}
		});
	}

}

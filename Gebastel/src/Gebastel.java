import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import ch.zhaw.budgettool.JSONClasses.LoginRequest;
import ch.zhaw.budgettool.JSONClasses.LoginResponse;

import com.google.gson.Gson;

public class Gebastel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Gson gson = new Gson();
			
			
			
			URL url = new URL("http://aschi.org:80/budgettool/");
			URLConnection conn;

			conn = url.openConnection();
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			
			BufferedWriter out = new BufferedWriter(new PrintWriter(
					conn.getOutputStream()));

			out.write(gson.toJson(new LoginRequest("aschi", "password")));
			out.flush();
		
			BufferedReader r = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = r.readLine()) != null) {
				// process each line in some way
				sb.append(line);
			}
			
			System.out.println(sb.toString());
			
			LoginResponse lr = gson.fromJson(sb.toString(), LoginResponse.class);
			System.out.println(lr);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

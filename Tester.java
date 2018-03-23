//import com.google.gson.*;
//import java.net.HttpURLConnection;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import org.apache.commons.io.IOUtils;

public class Tester {

	
	public static void main(String[] args)
	{
		String french = " ";
		String toast = " ";
		String cinnamon = " ";
		String whiskey = " ";
		
		Scanner keyboard = new Scanner(System.in);
		french = keyboard.next();
		toast = keyboard.next();
		cinnamon = keyboard.next();
		whiskey = keyboard.next();
		keyboard.close();
		
		try {
		URL connected = 
		new URL("http://api.yummly.com/v1/api/recipes?_app_id=ff685a45&_app_key=3ee0e0125e7c0acdc06aa2c4c1f91758&q=" +
		french + "+" + toast  + "&allowedIngredient[]=" + cinnamon + "&allowedIngredient[]=" + whiskey);
		
		URLConnection myConnected = connected.openConnection();
		myConnected.connect();
		InputStream in = myConnected.getInputStream();
		String encoding = myConnected.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		System.out.println(body);
		}
		catch(MalformedURLException e) 
		{
			System.out.println("Failed connection1");
		}
		catch(IOException e)
		{
			System.out.println("Failed connection2");
		}
		
		
		
	}
	
}

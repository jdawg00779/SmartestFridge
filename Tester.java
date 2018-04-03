
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Desktop;
import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.*;




public class Tester 
{

	
	public static void main(String[] args) throws JSONException
	{
		//Currently receiving strings as user input to test Yummly API
		String french = " ";
		String toast = " ";
		String cinnamon = " ";
		String whiskey = " ";
		ArrayList<String> RECIPE_IDS = new ArrayList<String>();

		//Scanner objects for user input.
		Scanner keyboard = new Scanner(System.in);
		french = keyboard.next();
		toast = keyboard.next();
		cinnamon = keyboard.next();
		whiskey = keyboard.next();
		keyboard.close();
		
		//Opening a connection to Yummly.
		try {
		//Creating a URL connection
		URL connected =
		//Information placed into the URL comes from the Scanner input above.
		//Yummly allows for user input just like a search query.
		new URL("http://api.yummly.com/v1/api/recipes?_app_id=ff685a45&_app_key=3ee0e0125e7c0acdc06aa2c4c1f91758&q=" +
		french + "+" + toast  + "&allowedIngredient[]=" + cinnamon + "&allowedIngredient[]=" + whiskey);
		
		//Opens a URL connection.
		URLConnection myConnected = connected.openConnection();
		myConnected.connect();
		//Creating a receiver of input from the URL Connectiton.
		InputStream in = myConnected.getInputStream();
		
		/*
		 * JSON Parsing Tool
		 * Parses the JSON that is returned by Yummly.
		 */
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = (JsonObject) parser.parse(
				new InputStreamReader(in, "UTF-8"));
		
		/*
		 * JSON Array of recipe search matches.
		 */
		JsonArray jMatches = jsonObject.getAsJsonArray("matches");
		System.out.println(jMatches.toString());
		/*
		 * Looping through the array of matches to see
		 * recipes.
		 */
		for(JsonElement jM : jMatches)
		{
			/*
			 * "KEY" : VALUE
			 * "matchObj": text
			 * Each one of these objects is identified by it's
			 * key.  The key appears in quotes in JSON.  The
			 * value is preceded by a :
			 */
			JsonObject matchObj = jM.getAsJsonObject();
			JsonPrimitive recipeObj = matchObj.getAsJsonPrimitive("recipeName");
			JsonPrimitive recipeID = matchObj.getAsJsonPrimitive("id");
			System.out.println(matchObj.toString());
			System.out.println(recipeObj.toString());
			System.out.println(recipeID.toString());
			if(recipeID.toString().startsWith("\"") && recipeID.toString().endsWith("\""))
				{
				String adj = recipeID.toString().replaceAll("\"", "");
				RECIPE_IDS.add(adj);
				}
			System.out.println(RECIPE_IDS.toString());
			
		}
		
		}
		catch(MalformedURLException e) 
		{
			System.out.println("Failed connection1");
		}
		catch(IOException e)
		{
			System.out.println("Failed connection2");
		}
		
		/*
		 * In this section, we are retrieving the recipe for the search results.
		 */
		try
		{
			/*
			 * Iterate through the RECIPE_IDS Array List in order to get the recipe IDs.
			 */
			for(String r : RECIPE_IDS)
			{
				
				/*
				 * Linking to the recipeIDs.
				 */
			URL jRecipeConnection = new URL("http://api.yummly.com/v1/api/recipe/" + r + "?_app_id=ff685a45&_app_key=3ee0e0125e7c0acdc06aa2c4c1f91758");
			URLConnection jConnection = jRecipeConnection.openConnection();
			jConnection.connect();
			
			InputStream jIn = jConnection.getInputStream();
			
			/*
			 * Obtaining the link to access the recipes by parsing the JSON Object.
			 * Created a new parser to distinguish the difference between the two.
			 */
			JsonParser jParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jParser.parse(
					new InputStreamReader(jIn, "UTF-8"));
			/*
			 * Parsing for the html JSON Primitive.
			 */
			JsonObject jAttrib = jsonObject.getAsJsonObject("attribution");
			JsonPrimitive jHtml = jAttrib.getAsJsonPrimitive("html");
			
			/*
			 * Array List created to hold the URLs of recipes.
			 */
			ArrayList<String> recipeList = new ArrayList<String>();
			recipeList = (linkCollect(jHtml.toString()));

			/*
			 * Iterating through the Array List of URLs.
			 * This opens multiple webpages of the returned recipes.
			 * 
			 */
			for(String rlink : recipeList)
			{
				URI uriLink = new URI(rlink);
				if(Desktop.isDesktopSupported())
				{
					Desktop.getDesktop().browse(uriLink);
				}
			}
		}
		
		
		} 
		catch(MalformedURLException e) 
		{
			System.out.println("Failed connection1");
		}
		catch(IOException e)
		{
			System.out.println("Failed connection2");
		}
		catch (URISyntaxException e) 
		{
			e.printStackTrace();
		}
	}
	private static ArrayList<String> linkCollect(String text) {
		ArrayList<String> links = new ArrayList<String>();
		 
		String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while(m.find()) {
		String urlStr = m.group();
		if (urlStr.startsWith("(") && urlStr.endsWith(")"))
		{
		urlStr = urlStr.substring(1, urlStr.length() - 1);
		}
		links.add(urlStr);
		}
		return links;
		}
}	


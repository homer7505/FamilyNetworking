package de.family_networking.push_notification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import de.family_networking.push_notification.entity.OW_Request;
import de.family_networking.push_notification.entity.OW_Response;

public class PushNotificationSubscriptionHandler
{
	private static final String OW_URL = "https://family-networking.de/b5c9d516-9b9b-45d2-b77a-3936c8a7bae9v.php";

	public PushNotificationSubscriptionHandler()
	{
	}

	public OW_Response isUserSubscriped( String userId )
	{
		OW_Request request = new OW_Request();
		request.setFunction( 0 );
		request.setUserId( userId );

		return connectOW( request );
	}

	public OW_Response subscripeUser( String userId, String subsctitionId )
	{
		OW_Request request = new OW_Request();
		request.setFunction( 1 );
		request.setUserId( userId );
		request.setSubscriptionId( subsctitionId );

		return connectOW( request );
	}

	public OW_Response updateUser( String userId, String subsctitionId )
	{
		OW_Request request = new OW_Request();
		request.setFunction( 2 );
		request.setUserId( userId );
		request.setSubscriptionId( subsctitionId );

		return connectOW( request );
	}

	public boolean isSuccess( OW_Response ow_response )
	{
		if ( ow_response != null && ow_response.getStatus() != null && ow_response.getStatus().equals( "200" ) )
		{
			return true;
		}
		else if ( ow_response != null )
		{
			System.out.println( "Status: " + ow_response.getStatus() );
			System.out.println( "Statement: " + ow_response.getStatement() );
			System.out.println( "Error: " + ow_response.getError() );
		}

		return false;
	}

	private OW_Response connectOW( OW_Request request )
	{
		HttpsURLConnection conn = null;
		OW_Response ow_response;
		try
		{
			String parameters = mapToHttpRequestParameter( request );

			URL url = new URL(OW_URL);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(parameters);
			writer.flush();
			writer.close();
			os.close();

			conn.connect();

			int responseCode = conn.getResponseCode();
			if ( responseCode == 200 )
			{
				String result = "";
				String line;
				BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line=br.readLine()) != null)
				{
					result+=line;
				};
				ow_response = new OW_Response( result );
			}
			else
			{
				String error = " {status:" + responseCode + ",error:" + conn.getResponseMessage() + "}";
				ow_response = new OW_Response( error );
			}

		}
		catch ( Exception e )
		{
			throw new RuntimeException( e );
		}
		finally
		{
			if(conn != null)
			{
				conn.disconnect();
			}
		}

		return ow_response;

	}

	private String mapToHttpRequestParameter( OW_Request request ) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();
		result.append(URLEncoder.encode("function", "UTF-8"));
		result.append("=");
		result.append(URLEncoder.encode(Integer.toString( request.getFunction() ), "UTF-8"));
		result.append("&");
		result.append(URLEncoder.encode("userId", "UTF-8"));
		result.append("=");
		result.append(URLEncoder.encode( request.getUserId(), "UTF-8"));

		String subscriptionId = request.getSubscriptionId();
		if ( subscriptionId != null )
		{
			result.append("&");
			result.append(URLEncoder.encode("subscriptionId", "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode( request.getSubscriptionId(), "UTF-8"));
		}
		return result.toString();
	}
}

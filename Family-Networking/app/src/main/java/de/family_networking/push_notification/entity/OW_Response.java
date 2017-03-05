package de.family_networking.push_notification.entity;

import java.util.StringTokenizer;

public class OW_Response extends OW_Request
{
	private String status;
	private String statement;
	private String error;

	public OW_Response( String response )
	{
		if ( response.length() > 2 )
		{
			StringTokenizer tokenizer = new StringTokenizer( response.substring( 1, response.length() - 1 ), "," );

			while ( tokenizer.hasMoreTokens() )
			{
				String token = tokenizer.nextToken();

				String[] key_values = token.split( ":" );

				if ( key_values[0].equals( "status" ) )
				{
					this.status = key_values[1];
				}
				else if ( key_values[0].equals( "statement" ) )
				{
					this.statement = key_values[1];
				}
				else if ( key_values[0].equals( "error" ) )
				{
					this.error = key_values[1];
				}
				else if ( key_values[0].equals( "userId" ) )
				{
					this.userId = key_values[1];
				}
				else if ( key_values[0].equals( "subscriptionId" ) )
				{
					this.subscriptionId = key_values[1];
				}
			}
		}
		else
		{
			this.status = "200";
		}

	}

	public String getStatus()
	{
		return status;
	}

	public String getStatement()
	{
		return statement;
	}

	public String getError()
	{
		return error;
	}
}

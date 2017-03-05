package de.family_networking.push_notification.entity;

import java.lang.reflect.Field;

public class OW_Request
{
	private int function;
	protected String userId = null;
	protected String subscriptionId = null;

	public OW_Request()
	{
	}

	public int getFunction()
	{
		return function;
	}

	public void setFunction( int function )
	{
		this.function = function;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId( String userId )
	{
		this.userId = userId;
	}

	public String getSubscriptionId()
	{
		return subscriptionId;
	}

	public void setSubscriptionId( String subscriptionId )
	{
		this.subscriptionId = subscriptionId;
	}

	public int getParameterCount()
	{
		Field[] fields = getClass().getDeclaredFields();

		int length = 1;

		for ( Field field : fields )
		{
			try
			{
				Class<?> c = field.getType();
				Object o = field.get( this );

				if ( !c.isPrimitive() && o != null )
				{
					length++;
				}
			}
			catch ( Exception e )
			{
				throw new RuntimeException( e );
			}

		}

		return length;
	}
}

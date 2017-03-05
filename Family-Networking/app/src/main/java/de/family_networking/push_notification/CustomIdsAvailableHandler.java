package de.family_networking.push_notification;

import com.onesignal.OneSignal;

/**
 * Created by Lars Nitzsche on 24.10.2016.
 */

public class CustomIdsAvailableHandler implements OneSignal.IdsAvailableHandler
{
    private String userId;
    private String registrationId;

    public CustomIdsAvailableHandler()
    {
    }

    @Override
    public void idsAvailable(String userId, String registrationId)
    {
        this.userId = userId;
        if (registrationId != null)
        {
            this.registrationId = registrationId;
        }
    }

    public String getUserId()
    {
        return userId;
    }

    public String getRegistrationId()
    {
        return registrationId;
    }
}

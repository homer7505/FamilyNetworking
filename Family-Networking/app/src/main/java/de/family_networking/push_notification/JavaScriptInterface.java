package de.family_networking.push_notification;

import android.webkit.JavascriptInterface;

import de.family_networking.push_notification.entity.OW_Response;

/**
 * Created by Lars Nitzsche on 22.10.2016.
 */
public class JavaScriptInterface
{
    private CustomIdsAvailableHandler customIdsAvailableHandler;

    public JavaScriptInterface(CustomIdsAvailableHandler customIdsAvailableHandler)
    {
        this.customIdsAvailableHandler = customIdsAvailableHandler;
    }

    @JavascriptInterface
    public void processHTML(String html)
    {
        if(html.contains( "\"userId\":"))
        {
            int start = html.indexOf("\"userId\":");
            String ow_userId = html.substring( start, html.length() );
            ow_userId = ow_userId.substring("\"userId\":".length(), ow_userId.indexOf(","));
            String os_userID = this.customIdsAvailableHandler.getUserId();

            if(os_userID != null)
            {

                PushNotificationSubscriptionHandler handler = new PushNotificationSubscriptionHandler();

                OW_Response response = handler.isUserSubscriped(ow_userId);

                if ( handler.isSuccess(response) )
                {
                    if ( response.getSubscriptionId() == null )
                    {
                        // Der User ist noch nicht registriert
                        System.out.println(" Der User " + ow_userId + " ist noch nicht registriert. ");
                        response = handler.subscripeUser(ow_userId, os_userID);
                        if ( handler.isSuccess(response) )
                        {
                            System.out.println(" Der User " + ow_userId + " wurde erfolgreich registriert. ");
                        }
                    }
                    else
                    {
                        // Der User ist registriert
                        if ( !os_userID.equals(response.getSubscriptionId()) )
                        {
                            System.out.println(" Die SubscriptionId von User " + ow_userId + " hat sich geändert. ");
                            response = handler.updateUser(ow_userId, os_userID);
                            if ( handler.isSuccess(response) )
                            {
                                System.out.println(" Der User " + ow_userId + " wurde erfolgreich geupdated. ");
                            }
                        }
                        else
                        {
                            System.out.println(" Der User " + ow_userId + " ist registriert. ");
                        }
                    }
                }
            }
            else
            {
                System.out.println("OneSignal Registrierung fehlgeschlagen.");
            }
        }
    }
}

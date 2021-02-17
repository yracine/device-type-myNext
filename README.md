# device-type-MyNext integration


*** The devices can be created using the Samsung connect app ****

My Next devices

Author:             Yves Racine

linkedIn profile:   www.linkedin.com/in/yracine

Date:               2020-04-10

Credits:             github @gboudreau for the initial Nest APIs discovery


For SmartTHings users, you can now download the code at 

<b>
http://www.ecomatiqhomes.com/store 
</b>

For Hubitat users, you can now download the code at 

<b>
http://www.ecomatiqhomes.com/hubitatstore 
</b>


P.S. Technical support packages are also available.
**************************************************************************************************



Setup time: about 15-25 minutes depending on your ST skills.

<b>NOTE: If you have many Nest devices (around 7 and more), due to the ST platform's rate limiting, it is strongly recommended to split your Nest devices into several instances of MyNextManager, and copy the required login info (you can create 2 copies of MyNextManager smartapp code in the IDE, and change the name at the end of the file, ex MyNextManagerProtects for your protects, MyNextManagerTstatAndSensors for your tstats and sensors).</b>


PREREQUISITES
==============
Some technical skills & manual tasks are required to setup your Nest devices under ST as the Nest Web APIs used
are not the "official" ones and don't have an oAuth flow (see LOGIN INFO For NEst or Google Account users below).


- a) [Nest] Your Nest products fully operational and connected to Nest Home via the internet

    Please note that all Nest thermostat devices are supported <b>except</b> the Nest thermostat E in the UK (with the heat link).       <b>Next Cams & Door Bells, and the Nest Secure Alarm are not supported. </b>  
    
- b) [Nest] <b>Nest setup completed for your Nest devices under your Nest Primary account  (don't use any Nest secondary accounts for the integation with SmartThings!!) 

- c) [ST IDE] <b>Determine your shard, please consult this thread: </b>


https://community.smartthings.com/t/faq-how-to-find-out-what-shard-cloud-slice-ide-url-your-account-location-is-on/53923

Or the SmartThings documentation here for more details:

http://docs.smartthings.com/en/latest/publishing/index.html#ensure-proper-location

<b> If you are on a different shard, you need to change the links below for your right shard. 
As an example, in North America, </b>

replace https://graph.api.smartthings.com/ide/devices by https://graph-na02-useast1.api.smartthings.com

Or use https://account.smartthings.com to point to the right shard (automatically).



LOGIN INFO REQUIRED FOR NEST ACCOUNT Users (Not migrated to a Google account) 
=============================================================================

* <b> Once you log in to your Nest account as described in the steps below, please keep the connection active to avoid any disconnect in ST (i.e., do not log off, but you can close your browser and even turn off your desktop/mobile), don't change your Nest account password or 2FA settings or re-login to keep your cookie valid. .</b>

* Google recently introduced reCAPTCHA when logging to Nest. That means username and password cannot be used directly any more. 
Instead, you have to obtain  `user_id ` and  `access_token` for your account by logging in manually. 

* For some visual guidelines, refer to screenshots in the link below

https://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.2314:_My_Next_devices_are_not_updating_or_my_auth_tokens_are_lost

________________________________________________________________________
<b>7 Steps for Nest users who have NOT migrated to a Google account:</b>
________________________________________________________________________


1. [Chrome] To do that, open `developer tools` in your Chrome browser, switch to the `Network` tab, 

2. [Chrome] Hit `preserve Logs` in the Network tab 

3. [Chrome] Log in to home.nest.com and look for the request similar to https://home.nest.com/session?_=157XXXXXX. 

4. [Chrome] You can use the filter "session" to get the exact info needed.

5. [Chrome] You will find `user_id` and `access_token`  in the response to the request.

6. [Chrome->ST] MyNestManager- header section of the code: Copy over the Nest login information to the corresponding nest_* fields in the code (ST IDE) for MyNextManager, refer to step 2g) below at https://github.com/yracine/device-type-myNext/blob/master/README.md#2-create-a-new-smartapp-mynextmanager.

In the header (code) section of MyNextManager, you will see "INSERT THE NEST LOGIN INFORMATION BELOW".

FYI,the login info for Nest account users looks like the following (your Nest user id will be different):

nest_user_id=4783742
nest_access_token=b.4783742.xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

7. [ST IDE] MyNextManager- header (code) section: After copying and pasting the login info, press "save" and "publish" to save/publish the code with your Nest login variables. Don't insert anything in the google section.

Do not copy the double quotes from the nest login information in the variables. 

______________
<b>Notes:</b>
______________

* If you lose your auth tokens, then you'd need to redo the steps above and copy over the new nest access_token back to the ST IDE. You can then reset the Nest connection by executing MyNextManager under Automation/Smartapps in the Samsung connect app by pressing "Next" till "Save").

* You don't need to re-install the devices, just reset the Nest connection.


LOGIN INFO REQUIRED FOR GOOGLE ACCOUNT USERS (MIGRATED NEST USERS)
===================================================================


______________
<b>Notes:</b>
______________

* <b> Once you log in to your Nest account as described in the steps below, please keep the connection active to avoid any disconnect in ST (i.e., do not log off, but you can close your browser and even turn off your desktop/mobile), don't change your Google account password or 2FA settings, and don't re-login to keep your cookie valid. .</b>

* If you lose your auth tokens, then you'd need to redo the steps below and copy over the new google cookie and issue_token_url back to the ST IDE. You can then reset the Nest connection by executing MyNextManager (under the '+' sign in the upper right corner of the Samsung connect app) by pressing "Next" till "Save").

* You don't need to re-install the devices, just reset the Nest connection.

______________________________________________________________
<b>14 Steps for users who have migrated to a Google account:</b>
______________________________________________________________

Similar as the Nest account user, open `developer tools` in your Chrome browser,switch to the `Network` tab, hit `preserve Logs`.
For some visual guidelines, refer to screenshots in the link below

https://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.2314:_My_Next_devices_are_not_updating_or_my_auth_tokens_are_lost

The values of `issue_token` and `cookie` are specific to your Google Account. To get them, follow these steps (only needs to be done once, as long as you stay logged into your Google Account). 

1. [Chrome] Open a Chrome browser tab in Incognito Mode (or clear your cache).
2. [Chrome] Open Developer Tools (View/Developer/Developer Tools).
3. [Chrome] Click on 'Network' tab. 
4. [Chrome] Make sure 'Preserve Log' is checked.
5. [Chrome] In the 'Filter' box, enter `issueToken`
7. [Chrome] Go to `home.nest.com`, and click 'Sign in with Google'. Log into your account.
8. [Chrome] One network call (beginning with `iframerpc`) will appear in the Dev Tools window. Click on it.
9. [Chrome] In the Headers tab, under General, copy the entire `Request URL` (beginning with `https://accounts.google.com`, ending with `nest.com`). This is your `"google_issue_token_url"` in the App Settings section of MyNextManager (ST IDE).Refer to https://github.com/yracine/device-type-myNext/blob/master/README.md#2-create-a-new-smartapp-mynextmanager.
10. [Chrome] In the 'Filter' box, enter `oauth2/iframe`
11. [Chrome] Several network calls will appear in the Dev Tools window. Click on the last `iframe` call.
12.[Chrome] In the Headers tab, under Request Headers, copy the entire `cookie` (usually it starts with `OCAK=...` or `SID=...` or `SMSV=...`  or with other values - **include the whole string which is several lines long and has many field/value pairs** - do not include the `cookie:` name). This google "cookie" needs to be copied over in the code (header) section of the MyNextManager smartapp  under the instructions saying 'INSERT GOOGLE LOGIN INFO BELOW'.  See https://github.com/yracine/device-type-myNext/blob/master/README.md#2-create-a-new-smartapp-mynextmanager.
13. [Chrome->ST IDE]   MyNextManager- Make sure that the whole cookie from Google is contained in google_cookiep1.  There is no need to split the cookie anymore (the other google_cookie p2-p6 variables are used for backward compatibility. Please make sure to avoid inserting extra spaces or any other characters when you copy the google cookie as Google will not accept it and the list of devices will be empty.
14. [ST IDE]  MyNextManager- After copying and pasting the login info, press "save" and "publish" at the right corner of the ST IDE to save/publish the code with your variables.



# 1) Depending on your contribution, create one or multiple Device Handler Type(s) - DTH for My NextTstat or MyNextAlarm (for Protects) or NextSensor

For each device (MyNextTstat, My NextAlarm, My NextSensor),

a) [ST IDE]  Go to https://graph.api.smartthings.com/ide/devices  (or whatever your shard is and click on My Device Handlers in the IDE's top menu)

b) [ST IDE] Hit the "+New Device Handler" at the top right corner

c) [ST IDE] Hit the "From Code" tab on the left corner

d) [ST IDE] Copy and paste the code from the corresponding txt file in the zip

<b>Following your financial contribution at www.ecomatiqhomes.com/store, the code has been sent to you by Sellfy via your paypal verified email address.</b>

e) [ST IDE] Hit the create button at the bottom

f) [ST IDE] Hit the "publish/for me" button at the top right corner (in the code window)

If the instructions above are not clear enough, you can refer to the troubleshooting section below with some pictures:

http://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.231:_I_don.27t_know_how_to_create_a_custom_smartapp


# 2) Create a new smartapp (MyNextManager)

a) Go to https://graph.api.smartthings.com/ide/apps (or whatever your shard is, and click on My Smartapps in the IDE's top menu)

b) [ST IDE] Hit the "+New SmartApp" at the top right corner

c) [ST IDE] Hit the "From Code" tab on the left corner

d) [ST IDE] Copy and paste the code from the corresponding txt file in the zip  

<b>Following your financial contribution at www.ecomatiqhomes.com/store, the code has been sent to you by Sellfy via your paypal verified email address.</b>

e) [ST IDE] Hit the create button at the bottom

f) [ST IDE] <b>Make sure that "enable OAuth" in Smartapp is active </b>

* Goto app settings (top right corner, click on it)
* Click on Oauth (middle of the page), and enable OAuth in Smart app
* Hit "Update" at the bottom
 back to the code window, and hit the "publish/for me" button at the top right corner

g) [ST IDE] Copy the login information from Nest or Google under the right global variables (see instructions above)

h) [ST IDE] Click on the "save" and "publish" buttons at the right corner to save your login information from Nest/Google as global variables.

If the instructions above are not clear enough, you can refer to the troubleshooting section below with some pictures:

http://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.231:_I_don.27t_know_how_to_create_a_custom_smartapp



# 3) Under the new Samsung connect app, execute MyNextManager (under + in the upper right corner/Smartapp)

[ST IDE] <b>  Go to the IDE in order to watch for any exceptions/errors in the logs.
    
https://graph.api.smartthings.com/ide/logs (or whatever your shard is, under Live Logging in the IDE)

You can click at the top of the window on the smartapp name to filter the logs.

[Samsung Connect App] To execute MyNextManager, find the MyNextManager smartapp under the '+' sign in the upper right corner/Smartapp, My NextManager should be in the middle of the list (Custom section). To start the Authentication with Nest, press Next on the first page.  

[ST IDE] <b> Check the logs for any installation errors.  Your login info may not have been copied correctly in the MyNextManager's App Settings section.
    
*************************************************************************************************************************************
N.B. If you have any errors:

[Samsung Connect App] If you get a blank screen after pressing 'Next or you get the following error: "Error - bad state' or 'Java.lang.NullPointerException: Cannot get property 'accessToken' on null object" in the IDE', you'd need to enable oAuth as specified in step 2f) above.

[Samsung Connect App] <b> At the end of the authorization flow,  if you have the following error message: "Unexpected error" even if you press several times, this probably means that you have not "saved & published" one of the Device Handler Types (MyNextTstat,MyNextAlarm,MyNextSensor) under the right shard.  Refer to the prerequisites & step 1 for more details.
 
[Samsung Connect App] Also, depending on the ST platform status, you may have to press "Save" several times if you have the following error message: "Error processing your request - please try again".  This is due to some ST platform timeouts due to rate limiting.</b> 
 
*************************************************************************************************************************************


# 4) Your device(s) should now be created among your Things

After about 1 minute, You should see your newly Next devices instantiated under:

[ST IDE] a) https://graph.api.smartthings.com/device/list (or whatever your shard is and click on My Devices in the IDE's top menu)

And also

[Samsung Connect App] b)At the bottom of the main screen under 'No room assigned' at the bottom of the page.

# 5) (Optional) Set device's preferences 


a) [ST IDE] Go to https://graph.api.smartthings.com/device/list   (or whatever your shard is and click on My Devices in the IDE's top menu)

b) [ST IDE] Click on the Next Devices that you just created

c) [ST IDE] Click on Preferences (edit)


You only need to edit the following parameters

[ST IDE]
    (a) <trace> when needed, set to true to get more tracing (no spaces)
    (b) <logFilter:1..5> Values=[Level 1=ERROR only,2=<Level 1+WARNING>,3=<2+INFO>,4=<3+DEBUG>,5=<4+TRACE>]


P.S. Don't enter any values for the internal ID or the structure ID as those values are provided by the Service Manager during the authentification flow


# 6) Use some of my smartapps (optional) - For all Nest users

Some complimentary smartapps at my github, refer to:

https://github.com/yracine/device-type-myNext/tree/master/smartapps

/****************************************************

<b>a) NestChangeMode</b>

/****************************************************

Change your Nest Away/Home settings according to your ST (location) hello home mode.

/****************************************************

<b>b) NestGetTips/b>

/****************************************************

The smartapp allows the user to get comfort & energy tips based on his/her indoor/outdoor conditions at home.


/****************************************************

<b>c) TstatStateTriggerHA</b>

/****************************************************

The above smartapp allows a ST user to trigger some switch(es) (turn on/off or flash) and/or trigger a hello phrase routine when the thermostat is cooling/heating/running the fan/or idle.


/****************************************************

<b>d) MonitorAndSetNestHumdity</b>

/****************************************************

The above smartapp allows a ST user to trigger the humidifier/dehumidifier connected to Nest or some humdifier/dehumidifier switch(es) based on the indoor/outdoor conditions at your home in order to reach the target humidity level.

/****************************************************

<b>e) Zoning smartapps

/****************************************************


The following zoned Heating/cooling smartapps have many features to leverage your Nest Thermostat.

* User friendly Dashboard and workflow pages for easier setup  (i.e. Dashboard->GeneralSetup->RoomsSetup->ZonesSetup->SchedulesSetup-> NotificationSettings).
* Flexible schedule definition: you can define up to 12 schedules for your zones (mornings, weekdays, evenings, nights, weekends, etc.)
* Remote Sensors: choice of  temp calculation method (Average, Median, Minimum, Maximum, Heat/Min-Cool/Max) based on all room temp sensors inside the zone so that your heat/cool settings are adjusted at the main thermostat even if your rooms are far away. The average calculation is similar to the ecobee3's follow me feature with its remote sensors.
* For each schedule, ST users can define the start & end times, the thermostat's heating/cooling setpoints (or climates for ecobee), the max temp adjustment based on the chosen calculation method (avg, median, min, max, etc.)
* You can set your schedules to run only for some routines/ST hello modes (ex. 'Away', 'Home', 'Night', or your own customized ones )
* For each schedule, you can assign the included zone(s) according to your own requirements (ex. Upstairs bedrooms, 1st level, basement, etc)
* You can define up to 8 zones for your home which can include up to 16 rooms
* Ability to set your smart thermostat to 'Away' or 'Present' based on all rooms' motion sensors
* Ability to set the thermostat's fanMode ('on', 'auto', 'circulate') during a specific schedule
* Ability to trigger the thermostat's fan based on some indoor temp differential. The temp differential can be customized for each schedule.
* Ability to adjust your main thermostat's settings (every 5 minutes) based on an outdoor temp sensor (optional)
* and many more features...




<b>
http://www.ecomatiqhomes.com/store 
</b>


/****************************************************

<b>ScheduleTstatZones</b>

/****************************************************

For more details:

http://thingsthataresmart.wiki/index.php?title=ScheduleTstatZones

The smartapp that enables Multi Zoned Heating/Cooling Solutions based on any ST connected thermostats/sensors- - coupled with smart vents (optional, can be any Flair, Keen Home, EcoVent, EcoNet smart vents) for better temp settings control throughout your home". 

The smartapp will use the thermostat's setpoints as baselines for targeted heating/cooling based on the concept of virtual/physical zones.

The smartapp can also control multiple Minisplit/Windows unit and portable heaters/coolers inside your scheduled zones (based on the Flair HVACUnit device, see http://thingsthataresmart.wiki/index.php?title=My_Flair_HVac_Unit).



/****************************************************

<b> ScheduleRoomTempControl</b>

/****************************************************

For more details:

http://thingsthataresmart.wiki/index.php?title=ScheduleRoomTempControl

The smartapp allows you to define some rooms' thresholds for better control of hotter/colder rooms in your premises. Smart vents are required for creating the physical zones.




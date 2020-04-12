# device-type-MyNext integration

My Next devices

Author:             Yves Racine

linkedIn profile:   www.linkedin.com/in/yracine

Date:               2020-04-10

Credits:             github @gboudreau for the initial Nest APIs discovery


You can now download the code at 

<b>
http://www.ecomatiqhomes.com/store 
</b>

P.S. Technical support packages are also available.
**************************************************************************************************


Setup time: about 15-25 minutes depending on your ST skills.


PREREQUISITES
==============
Some technical skills are required to setup your Nest devices under ST as the Nest Web APIs used
are not the "official" ones and don't have an oAuth flow.

- a) [Nest] Your Nest products fully operational and connected to Nest Home via the internet

    Please note that all Nest thermostat devices are supported <b>except</b> the Nest thermostat E in the UK (with the heat link) and the Nest Secure Alarm.  <b>Next Cams & Door Bells are not supported. </b>  
    

- b) [Nest] <b>Nest setup completed for your Nest devices under your Nest Primary account  (don't use any Nest secondary accounts for the integation with SmartThings!!).  
  
- (c) [ST CLASSIC APP] The installation must proceed with the SmartThings classic mobile app, so you have to download it first from your appStore. This is required as any custom DTHs can only be instantiated or created via the ST classic mobile app </b>

- (d) [ST CLASSIC APP] <b>Location set for your ST account under the ST classic mobile app </b>


<b>Under the ST classic mobile app</b>, click on the 3-horizontal lines- "hamburger"- menu in the upper left corner, and then the "gear'" icon to review your location and save it.  You can refer to the SmartThings' documentation for more details.

https://support.smartthings.com/hc/en-us/articles/205956850-How-to-edit-Location-settings

- (e) [ST IDE] <b>Determine your shard, please consult this thread: </b>


https://community.smartthings.com/t/faq-how-to-find-out-what-shard-cloud-slice-ide-url-your-account-location-is-on/53923

Or the SmartThings documentation here for more details:

http://docs.smartthings.com/en/latest/publishing/index.html#ensure-proper-location

<b> If you are on a different shard, you need to change the links below for your right shard. 
As an example, in North America, </b>

replace https://graph.api.smartthings.com/ide/devices by https://graph-na02-useast1.api.smartthings.com

Or use  https://consigliere-regional.api.smartthings.com/ to point to the right shard.

FYI,the login info for Nest account users looks like the following:

nest_user_id=4783742
nest_access_token=b.4783742.xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

LOGIN INFO REQUIRED FOR NEST ACCOUNT Users (Not migrated to a Google account) 
=============================================================================

<b>Once you log in to your Nest account as described in the steps below, please keep the connection active to avoid any disconnect in ST, and don't change your Nest account password or 2FA settings (and don't upgrade to a Google account).</b>

Google recently introduced reCAPTCHA when logging to Nest. That means username and password cannot be used directly any more. 

Instead, you have to obtain  `user_id ` and  `access_token` for your account by logging in manually. If you change your Nest account's password or 2FA settings, you will need to remove your old Nest devices (from all automation scenes/routines/smartapps) and redo the installation steps.

1. [Chrome] To do that, open `developer tools` in your Chrome browser, switch to the `Network` tab, hit `preserve Logs`, log in to home.nest.com and look for the request similar to https://home.nest.com/session?_=157XXXXXX. You can use the filter "session" to get the exact info needed.

For some visual guidelines, refer to screenshots #h), i) and j) in the link below


http://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.231:_I_don.27t_know_how_to_create_a_custom_smartapp




2. [Chrome] You will find `user_id` and `access_token`  in the response to the request.
3. [Chrome->ST] Copy over the Nest login information to the corresponding nest_* fields in App Settings (ST IDE)

Do not copy the double quotes in the ST IDE variables. 


<b>Notes:</b>

If you lose your auth tokens, then you'd need to redo the steps above and copy over the new nest access_token back to the ST IDE. You can then reset the Nest connection by executing MyNextManagerV2 under Automation/Smartapps in the ST classic mobile app by pressing "Next" till "Save").

You don't need to re-install the devices, just reset the Nest connection.


LOGIN INFO REQUIRED FOR GOOGLE ACCOUNT USERS (MIGRATED NEST USERS)
===================================================================


<b>Notes:</b>

<b>Once you log in to your Google account as described in the steps below, please keep the connection active to avoid any disconnect in ST (always keep your home.nest.com window open), and don't change your Google account password or 2FA settings.</b>  

If you lose your auth tokens, then you'd need to redo the steps below and copy over the new google cookie and issue_token_url back to the ST IDE. You can then reset the Nest connection by executing MyNextManagerV2 under Automation/Smartapps in the ST classic mobile app by pressing "Next" till "Save").

You don't need to re-install the devices, just reset the Nest connection.

<b>Steps:</b>

Similar as the Nest account user, open `developer tools` in your Chrome browser,switch to the `Network` tab, hit `preserve Logs`.
For some visual guidelines, refer to screenshots #h), i) and j) in the link below

http://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.231:_I_don.27t_know_how_to_create_a_custom_smartapp



The values of `issue_token` and `cookie` are specific to your Google Account. To get them, follow these steps (only needs to be done once, as long as you stay logged into your Google Account). 

Please note that if you change your Google account's password or 2FA settings, you will need to remove your old Nest devices (from all automation scenes/routines/smartapps) and redo the installation steps.

1. [Chrome] Open a Chrome browser tab in Incognito Mode (or clear your cache).
2. [Chrome] Open Developer Tools (View/Developer/Developer Tools).
3. [Chrome] Click on 'Network' tab. Make sure 'Preserve Log' is checked.
4. [Chrome] In the 'Filter' box, enter `issueToken`
5. [Chrome] Go to `home.nest.com`, and click 'Sign in with Google'. Log into your account.
6. [Chrome] One network call (beginning with `iframerpc`) will appear in the Dev Tools window. Click on it.
7. [Chrome] In the Headers tab, under General, copy the entire `Request URL` (beginning with `https://accounts.google.com`, ending with `nest.com`). This is your `"google_issue_token"` in App settings.
8. [Chrome] In the 'Filter' box, enter `oauth2/iframe`
9. [Chrome] Several network calls [Chrome->ST] will [Chrome->ST] appear in the Dev Tools window. Click on the last `iframe` call.
10.[Chrome] In the Headers tab, under Request Headers, copy the entire `cookie` (beginning `OCAK=...` - **include the whole string which is several lines long and has many field/value pairs** - do not include the `cookie:` name)
11. [Chrome->ST IDE] This is your "cookie" in the Settings section of the smartapp (ST IDE). In order to copy the whole cookie, you'd need to split it into different google_cookie_p* fields in the Settings section as the SmartThings platform doesn't support long text variables. If the text is too long, SmartThings will report an exception (500) when you try to save a too long cookie field. 
12. [Chrome->ST IDE]  Make sure that all your google_cookie_p* fields contain the whole cookie from Google.




# 1) Depending on your contribution, create one or multiple Device Handler Type(s) - DTH for My NextTstatV2 or MyNextAlarmV2 or NextSensorV2

For each device (My NextTstatV2, My NextAlarmV2, My NextSensorV2),

a) [ST IDE]  Go to https://graph.api.smartthings.com/ide/devices  (or whatever your shard is and click on My Device Handlers in the IDE's top menu)

b) [ST IDE] Hit the "+New Device Handler" at the top right corner

c) [ST IDE] Hit the "From Code" tab on the left corner

d) [ST IDE] Copy and paste the code from the corresponding txt file in the zip

<b>Following your financial contribution at www.ecomatiqhomes.com/store, the code has been sent to you by Sellfy via your paypal verified email address.</b>

e) [ST IDE] Hit the create button at the bottom

f) [ST IDE] Hit the "publish/for me" button at the top right corner (in the code window)


# 2) Create a new smartapp (MyNextManagerV2)

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

h) [ST IDE] Click on App Settings and copy the required login info whether you're still a Nest account user or a Google Account user.

i) [ST IDE] Click on the update button at the bottom to save your login information from Nest/Google.

If the instructions above are not clear enough, you can refer to the troubleshooting section below with some pictures:

http://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.231:_I_don.27t_know_how_to_create_a_custom_smartapp


# 3) Under the ST classic mobile app, execute MyNextManagerV2 (MarketSpace>Smartapps>MyApps)

[ST IDE] <b>  Go to the IDE in order to watch for any exceptions/errors in the logs.
    
https://graph.api.smartthings.com/ide/logs (or whatever your shard is, under Live Logging in the IDE)

You can click at the top of the window on the smartapp name to filter the logs.

[ST CLASSIC APP] <b>Click on the Smartapps link in the upper section of the following Marketspace screen (last icon in the bottom menu), and then Smartapps/MyApps (last item in the list).</b>

[ST CLASSIC APP] To execute MyNextManagerV2, find the MyNextManagerV2 smartapp under MarketSpace>Smartapps>MyApps, My NextManagerV2 should be in the middle of the list. To start the Authentication with Nest, press Next on the first page.  

[ST IDE] <b> Check the logs for any installation errors.  Your login info may not have been copied correctly in the MyNextManagerV2's App Settings section.
    
As a reminder for the Google account users, in order to copy the whole cookie, you'd need to split it into different fields in AppSettings as the SmartThings platform doesn't support long text variables.
    
*************************************************************************************************************************************
N.B. If you have any errors:

[ST CLASSIC APP] If you get a blank screen after pressing 'Next or you get the following error: "Error - bad state' or 'Java.lang.NullPointerException: Cannot get property 'accessToken' on null object" in the IDE', you'd need to enable oAuth as specified in step 2f) above.

[ST CLASSIC APP] <b> At the end of the authorization flow,  if you have the following error message: "Unexpected error" even if you press several times, this probably means that you have not "saved & published" one of the Device Handler Types (MyNextTstatV2,MyNextAlarmV2,MyNextSensorV2) under the right shard.  Refer to the prerequisites & step 1 for more details.
 
[ST CLASSIC APP] Also, depending on the ST platform status, you may have to press "Save" several times if you have the following error message: "Error processing your request - please try again".  This is due to some ST platform timeouts due to rate limiting.</b> 
 
*************************************************************************************************************************************


# 4) Your device(s) should now be created among your Things

After about 1 minute, You should see your newly Next devices instantiated under:

[ST IDE] a) https://graph.api.smartthings.com/device/list (or whatever your shard is and click on My Devices in the IDE's top menu)

And also

[ST CLASSIC APP] b) Under the ST classic mobile app, under MyHome/Things (main menu at the bottom of the screen).

# 5) To populate the UI fields for your newly created device(s), press the "refresh" tile </b>

[ST CLASSIC APP] If the fields are blank, you may have to hit the 'refresh' button on your newly created Next devices as the smartThings UI is not always responsive. 


# 6) (Optional) Set device's preferences 


a) [ST IDE] Go to https://graph.api.smartthings.com/device/list   (or whatever your shard is and click on My Devices in the IDE's top menu)

b) [ST IDE] Click on the Next Devices that you just created

c) [ST IDE] Click on Preferences (edit)

N.B. [ST CLASSIC APP] You can also edit the preferences under Things/Your Device/Edit Device using the app.

You only need to edit the following parameters

[ST IDE]
    (a) <trace> when needed, set to true to get more tracing (no spaces)
    (b) <logFilter:1..5> Values=[Level 1=ERROR only,2=<Level 1+WARNING>,3=<2+INFO>,4=<3+DEBUG>,5=<4+TRACE>]


P.S. Don't enter any values for the internal ID or the structure ID as those values are provided by the Service Manager during the authentification flow


# 7) Use some of my smartapps (optional) - For Nest account users.

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

<b>d) Zoning smartapps

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




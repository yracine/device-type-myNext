# device-type-MyNext integration

My Next devices

Author:             Yves Racine

linkedIn profile:   www.linkedin.com/in/yracine

Date:               2020-04-10


You can now download the code at 

<b>
http://www.ecomatiqhomes.com/store 
</b>

P.S. Technical support packages are also available.
**************************************************************************************************


Setup time: about 15-25 minutes depending on your ST skills.


PREREQUISITES
==============

- a) Your Nest products fully operational and connected to Nest Home via the internet

    Please note that all Nest devices are supported <b>except</b> the Nest thermostat E in the UK (with the heat link) and the Nest Secure Alarm.  

- b) <b>Nest setup completed for your Nest devices under your Nest Primary account  (don't use any Nest secondary accounts for the integation with SmartThings!!).  
  

- (c) <b>Location set for your ST account under the ST classic mobile app </b>

<b>Under the ST classic mobile app</b>, click on the 3-horizontal lines- "hamburger"- menu in the upper left corner, and then the "gear'" icon to review your location and save it.  You can refer to the SmartThings' documentation for more details.

https://support.smartthings.com/hc/en-us/articles/205956850-How-to-edit-Location-settings

- (d) <b>Determine your shard, please consult this thread: </b>


https://community.smartthings.com/t/faq-how-to-find-out-what-shard-cloud-slice-ide-url-your-account-location-is-on/53923

Or the SmartThings documentation here for more details:

http://docs.smartthings.com/en/latest/publishing/index.html#ensure-proper-location

<b> If you are on a different shard, you need to change the links below for your right shard. 
As an example, in North America, </b>

replace https://graph.api.smartthings.com/ide/devices by https://graph-na02-useast1.api.smartthings.com


Or use  https://consigliere-regional.api.smartthings.com/ to point to the right shard.

- (e) <b>For the Nest Cam, if you want to get live streaming under ST, you'd need to share publicly and turn off the 2-step authentication for your cam (as the Nest-ST integration cannot support it) to make it work. </b>

For more details, refer to 

http://thingsthataresmart.wiki/index.php?title=MyNextCam#Issue_.2311:_I_cannot_get_live_stream_from_My_Nest_Cam



LOGIN INFO REQUIRED FOR NEST ACCOUNT USERS (read-write modes under ST)
======================================================================

Google recently introduced reCAPTCHA when logging to Nest. That means username and password cannot be used directly any more. Instead, you have to obtain user_id and access_token for your account by logging in manually. If you change your Nest account's password or 2FA settings, you will need to remove your old Nest devices (from all automation scenes/routines/smartapps) and redo the installation steps.

- To do that, open developer tools in your Chrome browser, switch to the "Network" tab, log in to home.nest.com and look for the request similar to https://home.nest.com/session?_=1578693398448. You can use the filter "session" to get the exact info needed.
- You will find `user_id` and `access_token`  in the response to the request.
- Copy over the Nest login information to the corresponding nest_& fields in App Settings



LOGIN INFO REQUIRED FOR GOOGLE ACCOUNT USERS (read-only mode devices under ST)
==============================================================================

The values of "issue_token" and "cookie" are specific to your Google Account. To get them, follow these steps (only needs to be done once, as long as you stay logged into your Google Account). If you change your Google account's password or 2FA settings, you will need to remove your old Nest devices (from all automation scenes/routines/smartapps) and redo the installation steps.

1. Open a Chrome browser tab in Incognito Mode (or clear your cache).
2. Open Developer Tools (View/Developer/Developer Tools).
3. Click on 'Network' tab. Make sure 'Preserve Log' is checked.
4. In the 'Filter' box, enter `issueToken`
5. Go to `home.nest.com`, and click 'Sign in with Google'. Log into your account.
6. One network call (beginning with `iframerpc`) will appear in the Dev Tools window. Click on it.
7. In the Headers tab, under General, copy the entire `Request URL` (beginning with `https://accounts.google.com`, ending with `nest.com`). This is your `"issue_token"` in `configuration.yaml`.
8. In the 'Filter' box, enter `oauth2/iframe`
9. Several network calls will appear in the Dev Tools window. Click on the last `iframe` call.
10. In the Headers tab, under Request Headers, copy the entire `cookie` (beginning `OCAK=...` - **include the whole string which is several lines long and has many field/value pairs** - do not include the `cookie:` name)
- This is your "cookie" in the Settings section of the smartapp. In order to copy the whole cookie, you'd need to split it into different google_cookie_p* fields in the Settings section as the SmartThings platform doesn't support long text variables. If the text is too long, SmartThings will report an exception when you try to save a too long cookie field. 
- Make sure that all your google_cookie_p* fields contain the whole cookie from Google.



# 1) Depending on your contribution, create one or multiple Device Handler Type(s) - DTH for My NextTstatV2 or MyNextAlarmV2 or NextSensorV2

For each device (My NextTstatV2, My NextAlarmV2, My NextSensorV2),

a) Go to https://graph.api.smartthings.com/ide/devices  (or whatever your shard is and click on My Device Handlers in the IDE's top menu)

b) Hit the "+New Device Handler" at the top right corner

c) Hit the "From Code" tab on the left corner

d) Copy and paste the code from the corresponding txt file in the zip

<b>The code has been sent to you via your paypal verified email address.</b>

e) Hit the create button at the bottom

f) Hit the "publish/for me" button at the top right corner (in the code window)


# 2) Create a new smartapp (MyNextManagerV2)

a) Go to https://graph.api.smartthings.com/ide/apps (or whatever your shard is, and click on My Smartapps in the IDE's top menu)

b) Hit the "+New SmartApp" at the top right corner

c) Hit the "From Code" tab on the left corner

d) Copy and paste the code from the corresponding txt file in the zip  

<b>The code has been sent to you via your paypal verified email address.</b>

e) Hit the create button at the bottom

f) <b>Make sure that "enable OAuth" in Smartapp is active </b>

* Goto app settings (top right corner, click on it)
* Click on Oauth (middle of the page), and enable OAuth in Smart app
* Hit "Update" at the bottom

g) Go back to the code window, and hit the "publish/for me" button at the top right corner

h) Click on App Settings and copy the required login info whether you're still a Nest account user or a Google Account user.

i) Click on the update button at the bottom to save your login information from Nest/Google.

If the instructions above are not clear enough, you can refer to the troubleshooting section below with some pictures:

http://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.231:_I_don.27t_know_how_to_create_a_custom_smartapp


# 3) Under the ST classic mobile app, execute MyNextManagerV2 (MarketSpace>Smartapps>MyApps)

<b> Go to the IDE in order to watch for any exceptions/errors in the logs.
    
https://graph.api.smartthings.com/ide/logs (or whatever your shard is, under Live Logging in the IDE)

You can click at the top of the window on the smartapp name to filter the logs.

<b>Click on the Smartapps link in the upper section of the following Marketspace screen (last icon in the bottom menu), and then Smartapps/MyApps (last item in the list).</b>

To execute MyNextManagerV2, find the MyNextManagerV2 smartapp under MarketSpace>Smartapps>MyApps, My NextManagerV2 should be in the middle of the list. To start the Authentication with Nest, press on the "Nest Connect> Required" button in the middle of the screen.  

<b> Check the logs for any installation errors.  Your login info may not have been copied correctly in the MyNextManagerV2's App Settings section.
    
As a reminder for the Google account users, in order to copy the whole cookie, you'd need to split it into different fields in AppSettings as the SmartThings platform doesn't support long text variables.
    
*************************************************************************************************************************************
N.B. If you have any errors:

If you get a blank screen after pressing 'Next or you get the following error: "Error - bad state' or 'Java.lang.NullPointerException: Cannot get property 'accessToken' on null object" in the IDE', you'd need to enable oAuth as specified in step 2f) above.

<b> At the end of the authorization flow,  if you have the following error message: "Unexpected error" even if you press several times, this probably means that you have not "saved & published" one of the Device Handler Types (MyNextTstatV2,MyNextAlarmV2,MyNextSensorV2) under the right shard.  Refer to the prerequisites & step 1 for more details.
 
Also, depending on the ST platform status, you may have to press "Save" several times if you have the following error message: "Error processing your request - please try again".  This is due to some ST platform timeouts due to rate limiting.</b> 
 
*************************************************************************************************************************************


# 4) Your device(s) should now be created among your Things

After about 1 minute, You should see your newly Next devices instantiated under:

a) https://graph.api.smartthings.com/device/list (or whatever your shard is and click on My Devices in the IDE's top menu)

And also

b) Under the ST classic mobile app, under MyHome/Things (main menu at the bottom of the screen).

# 5) To populate the UI fields for your newly created device(s), press the "refresh" tile </b>

If the fields are blank, you may have to hit the 'refresh' button on your newly created Next devices as the smartThings UI is not always responsive. 

If you have to create many Nest devices under SmartThings, due to Nest's rate limiting issues, you may have to wait before seeing some values coming from Nest.

Refer to 

https://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.238:_There_is_a_groovyx.net.http.HttpResponseException:_Too_Many_Requests_exception_in_my_logs


# 6) (Optional) Set device's preferences 


a) Go to https://graph.api.smartthings.com/device/list   (or whatever your shard is and click on My Devices in the IDE's top menu)

b) Click on the Next Devices that you just created

c) Click on Preferences (edit)

N.B. You can also edit the preferences under Things/Your Device/Edit Device using the app.

You only need to edit the following parameters


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




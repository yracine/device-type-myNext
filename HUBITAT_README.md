# device-type-MyNext Hubitat integration


My Next devices

Author:             Yves Racine

linkedIn profile:   www.linkedin.com/in/yracine

Credits:             github @gboudreau for the initial Nest APIs discovery


You can now download the code at 

<b>
http://www.ecomatiqhomes.com/store 
</b>

P.S. Technical support packages are also available.
**************************************************************************************************



Setup time: about 15-25 minutes depending on your Hubitat skills.



PREREQUISITES
==============
Some technical skills & manual tasks are required to setup your Nest devices under Hubitat as the Nest Web APIs used
are not the "official" ones and don't have an oAuth flow (see LOGIN INFO For NEst or Google Account users below).


- a) [Nest] Your Nest products fully operational and connected to Nest Home via the internet

    Please note that all Nest thermostat devices are supported <b>except</b> the Nest thermostat E in the UK (with the heat link).      
    <b>Next Cams & Door Bells, and the Nest Secure Alarm are not supported. </b>  
    
- b) [Nest] <b>Nest setup completed for your Nest devices under your Nest Primary account  (don't use any Nest secondary accounts for the integation with SmartThings!!) 



LOGIN INFO REQUIRED FOR NEST ACCOUNT Users (Not migrated to a Google account) 
=============================================================================

* <b> Once you log in to your Nest account as described in the steps below, please keep the connection active to avoid any disconnect in Hubitat (i.e., do not log off, but you can close your browser and even turn off your desktop/mobile), and don't change your Nest account password or 2FA settings.</b>

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

6. [Chrome->ST] MyNestManagerV2- AppSettings section: Copy over the Nest login information to the corresponding nest_* fields in App Settings (ST IDE) for MyNextManagerV2, refer to step 2g) below at https://github.com/yracine/device-type-myNext/blob/master/README.md#2-create-a-new-smartapp-mynextmanagerv2.

FYI,the login info for Nest account users looks like the following (your Nest user id will be different):

nest_user_id=4783742
nest_access_token=b.4783742.xXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

7. [Hubitat IDE] MyNestManagerV2- After copying and pasting the login info in MyNextManagerV's header as global variables, press "save" to save your variables.

a) go to http://192.168.xx.xx/app/list (insert your own hub's ip address)

b) click on MyNextManagerV2 in the list of smartapps.

c) Copy the nest user and auth token into the header as global variables.

Do not copy the double quotes in the global variables. 

______________
<b>Notes:</b>
______________

* If you lose your auth tokens, then you'd need to redo the steps above and copy over the new nest access_token back to the Hubitat IDE. You can then reset the Nest connection by executing MyNextManagerV2 under Automation/Smartapps in the Samsung connect app by pressing "Next" till "Save").

* You don't need to re-install the devices, just reset the Nest connection.


LOGIN INFO REQUIRED FOR GOOGLE ACCOUNT USERS (MIGRATED NEST USERS)
===================================================================


______________
<b>Notes:</b>
______________

* <b> Once you log in to your Nest account as described in the steps below, please keep the connection active to avoid any disconnect in Hubitat (i.e., do not log off, but you can close your browser and even turn off your desktop/mobile), and don't change your Google account password or 2FA settings.</b>

* If you lose your auth tokens, then you'd need to redo the steps below and copy over the new google cookie and issue_token_url back to the Hubitat IDE. You can then reset the Nest connection by executing MyNextManagerV2 (under the '+' sign in the upper right corner of the Samsung connect app) by pressing "Next" till "Save").

* You don't need to re-install the devices, just reset the Nest connection.

______________________________________________________________
<b>13 Steps for users who have migrated to a Google account:</b>
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
9. [Chrome] In the Headers tab, under General, copy the entire `Request URL` (beginning with `https://accounts.google.com`, ending with `nest.com`). This is your `"google_issue_token_url"` in the App Settings section of MyNextManagerV2 (ST IDE).Refer to https://github.com/yracine/device-type-myNext/blob/master/README.md#2-create-a-new-smartapp-mynextmanagerv2.
10. [Chrome] In the 'Filter' box, enter `oauth2/iframe`
11. [Chrome] Several network calls will appear in the Dev Tools window. Click on the last `iframe` call.
12.[Chrome] In the Headers tab, under Request Headers, copy the entire `cookie` (usually it starts with `OCAK=...` or `SID=...` or `SMSV=...`  or with other values - **include the whole string which is several lines long and has many field/value pairs** - do not include the `cookie:` name). You can copy it in your preferred editor to split it in multiple fields of similar length.  Depending on your cookie's length, you may have to split it into 3-6 fields in the IDE.
11. [Chrome->Hubitat IDE] MyNestManagerV2- header: This is your "cookie" in the Settings section of the MyNextManagerV2 smartapp in the Hubitat IDE. In order to copy the whole cookie, you'd need to split it into different google_cookie_p* fields in the Settings section as the Hubitat platform doesn't support long text variables. If the text is too long, Hubitat will report an exception (500) when you try to save a too long cookie field. Refer to step 2g) below at https://github.com/yracine/device-type-myNext/blob/master/README.md#2-create-a-new-smartapp-mynextmanagerv2.

go to 
http://192.168.xx.xx/app/list to copy the login information into the Hubitat IDE.

12. [Chrome->Hubitat IDE]   MyNestManagerV2- header: Make sure that all your google_cookie_p* fields contain the whole cookie from Google. 
Please make sure to avoid inserting extra spaces or any other characters when you split the cookie as this will not be accepted by Google, and the cookie
will not be recognized (and your list of devices will be empty).

13. [Hubitat IDE]  MyNestManagerV2 header: After copying and pasting the login info in the header, press "save" to save your global variables.

a) go to http://192.168.xx.xx/app/list (insert your own hub's ip address)

b) click on MyNextManagerV2 in the list of smartapps.

c) Copy the nest user and auth token into the header as global variables.

Do not copy the double quotes in the global variables


# 1) Depending on your contribution, create one or multiple device driver(s) - DTH for My NextTstatV2 or MyNextAlarmV2 (for Protects) or NextSensorV2

For each device (My NextTstatV2, My NextAlarmV2, My NextSensorV2),

a) Create a new device driver 

go to http://192.168.xx.xx/driver/list (insert your own hub's IP address)

b) Hit the "+New Driver" at the top right corner

c) Copy and paste the code from the corresponding txt file in the zip 

d) Hit the save button on the right inside of the screen


# 2) Create a new smartapp (MyNextManagerV2)

go to http://192.168.xx.xx/app/list (insert your own hub's IP address)

a) Hit the "+New App" at the top right corner

b) Copy and paste the code from the corresponding txt file in the zip 

c) Hit the save button on the right inside of the screen

d) Make sure that enable OAuth in Smartapp is active (click oAuth in the upper right corner)
* Hit "Update" at the bottom

If the instructions above are not clear enough, you can refer to the troubleshooting section below with some pictures:

http://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.231:_I_don.27t_know_how_to_create_a_custom_smartapp


# 3) Go to the installed apps section of hubitat, execute MyNextManagerV2 

http://192.168.xx.xx/installedapp/list (insert your own hub's IP address)

a) Click on "Add User app" in the right corner of the window

# 4) Connect Hubitat to the Nest web APIs

[Hubitat IDE] <b> Check the logs for any installation errors.  Your login info may not have been copied correctly in the MyNextManagerV2's App Settings section.

http://192.168.xx.xx/logs (insert your own hub's IP address)
    
As a reminder for the Google account users, in order to copy the whole cookie, you'd need to split it into different fields as global variables as the platform doesn't support long text variables.
    
*************************************************************************************************************************************
N.B. If you have any errors:

If you get a blank screen after pressing 'Next or you get the following error: "Error - bad state' or 'Java.lang.NullPointerException: Cannot get property 'accessToken' on null object" in the IDE', you'd need to enable oAuth as specified in step 2f) above.

<b> At the end of the authorization flow,  if you have the following error message: "Unexpected error" even if you press several times, this probably means that you have not "saved & published" one of the Device Handler Types (MyNextTstatV2,MyNextAlarmV2,MyNextSensorV2) under the right shard.  Refer to the prerequisites & step 1 for more details.
  
*************************************************************************************************************************************


# 4) Your device(s) should now be created among your installed devices

After about 1 minute, You should see your newly Next devices instantiated under:

[Hubitat IDE] go to http://192.168.xx.xx/device/list (insert your own hub's IP address)


# 5) (Optional) Set device's preferences 

a) Click on the My ecobee device that you just created


http://192.168.xx.xx/device/edit/"device number"  (Device number can vary from one location to the next)


b) Edit the preferences in the middle section of the screen) 

You only need to edit the following parameters

    (a) <trace> when needed, set to true to get more tracing (no spaces)
    (b) <logFilter:1..5> Values=[Level 1=ERROR only,2=<Level 1+WARNING>,3=<2+INFO>,4=<3+DEBUG>,5=<4+TRACE>]
    
And, press "save preferences" at the end.

N.B. The detailed logging will be set for the next 15 minutes only (deactivated after to avoid performance issues)

# 6) Use some of my smartapps (optional) - For all Nest users

Some complimentary smartapps at my github, refer to:

https://github.com/yracine/device-type-myNext/tree/master/smartapps

/****************************************************

<b>a) NestChangeMode</b>

/****************************************************

Change your Nest Away/Home settings according to your Hubitat location or  hello home mode.

/****************************************************

<b>b) NestGetTips/b>

/****************************************************

The smartapp allows the user to get comfort & energy tips based on his/her indoor/outdoor conditions at home.


/****************************************************

<b>c) TstatStateTriggerHA</b>

/****************************************************

The above smartapp allows a Hubitat user to trigger some switch(es) (turn on/off or flash) and/or trigger a hello phrase routine when the thermostat is cooling/heating/running the fan/or idle.


/****************************************************

<b>d) MonitorAndSetNestHumdity</b>

/****************************************************

The above smartapp allows a Hubitat user to trigger the humidifier/dehumidifier connected to Nest or some humdifier/dehumidifier switch(es) based on the indoor/outdoor conditions at your home in order to reach the target humidity level.

/****************************************************

<b>e) Zoning smartapps

/****************************************************


The following zoned Heating/cooling smartapps have many features to leverage your Nest Thermostat.

* User friendly Dashboard and workflow pages for easier setup  (i.e. Dashboard->GeneralSetup->RoomsSetup->ZonesSetup->SchedulesSetup-> NotificationSettings).
* Flexible schedule definition: you can define up to 12 schedules for your zones (mornings, weekdays, evenings, nights, weekends, etc.)
* Remote Sensors: choice of  temp calculation method (Average, Median, Minimum, Maximum, Heat/Min-Cool/Max) based on all room temp sensors inside the zone so that your heat/cool settings are adjusted at the main thermostat even if your rooms are far away. The average calculation is similar to the ecobee3's follow me feature with its remote sensors.
* For each schedule, Hubitat users can define the start & end times, the thermostat's heating/cooling setpoints (or climates for ecobee), the max temp adjustment based on the chosen calculation method (avg, median, min, max, etc.)
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

The smartapp that enables Multi Zoned Heating/Cooling Solutions based on any connected thermostats/sensors- - coupled with smart vents (optional, can be any Flair, Keen Home, EcoVent, EcoNet smart vents) for better temp settings control throughout your home". 

The smartapp will use the thermostat's setpoints as baselines for targeted heating/cooling based on the concept of virtual/physical zones.

The smartapp can also control multiple Minisplit/Windows unit and portable heaters/coolers inside your scheduled zones (based on the Flair HVACUnit device, see http://thingsthataresmart.wiki/index.php?title=My_Flair_HVac_Unit).



/****************************************************

<b> ScheduleRoomTempControl</b>

/****************************************************

For more details:

http://thingsthataresmart.wiki/index.php?title=ScheduleRoomTempControl

The smartapp allows you to define some rooms' thresholds for better control of hotter/colder rooms in your premises. Smart vents are required for creating the physical zones.




# device-type-MyNext integration

My Next devices

Author:             Yves Racine

linkedIn profile:   www.linkedin.com/in/yracine

Date:               2018-01-06


You can now download the code at 

<b>
http://www.ecomatiqhomes.com/store 
</b>

P.S. Technical support packages are also available.
**************************************************************************************************


Setup time: about 10-20 minutes depending on your ST skills.


PREREQUISITES
==============

- a) Your Nest products operatinal and connected to Nest Home via the internet

- b) Nest setup completed for your Nest devices.


- (c) <b>Location set for your ST account  </b>

Under the ST mobile app, click on the 3-horizontal lines- "hamburger"- menu in the upper left corner, and then the "gear'" icon to review your location and save it.

- (e) <b>Determine your shard, please consult this thread: </b>


https://community.smartthings.com/t/faq-how-to-find-out-what-shard-cloud-slice-ide-url-your-account-location-is-on/53923

Or the SmartThings documentation here:

http://docs.smartthings.com/en/latest/publishing/index.html#ensure-proper-location

<b> If you are on a different shard, you need to change the links below for your right shard. 
As an example, in North America, </b>

replace https://graph.api.smartthings.com/ide/devices by https://graph-na02-useast1.api.smartthings.com


Or use  https://consigliere-regional.api.smartthings.com/ to point to the right shard.

INSTALLATION STEPS
=====================


# 1) Depending on your contribution, create one or multiple Device Handler Type(s) - DTH for My Next Tstat or Next alarm.

For each device (My Next Tstat, My Next Alarm, My Next Cam),

a) Go to https://graph.api.smartthings.com/ide/devices (or whatever your shard is)

b) Hit the "+New Device Handler" at the top right corner

c) Hit the "From Code" tab on the left corner

d) Copy and paste the code from the corresponding txt file

<b>The code will be sent to you via your paypal verified email address.</b>

e) Hit the create button at the bottom

f) Hit the "publish/for me" button at the top right corner (in the code window)


# 2) Create a new smartapp (MyNextManager)

a) Go to https://graph.api.smartthings.com/ide/apps (or whatever your shard is)

b) Hit the "+New SmartApp" at the top right corner

c) Hit the "From Code" tab on the left corner

d) Copy and paste the code  

<b>The code will be sent to you via your paypal verified email address.</b>

e) Hit the create button at the bottom

f) <b>Make sure that "enable OAuth" in Smartapp is active </b>

* Goto app settings (top right corner, click on it)
* Click on Oauth (middle of the page), and enable OAuth in Smart app
* Hit "Update" at the bottom

g) Go back to the code window, and hit the "publish/for me" button at the top right corner 


# 3) Link your SmartThings to your Nest Account and Connect them.

a) In order to connect your ST account to Nest for the cloud-to-cloud integration, you need to create your own
Nest developer account.  You should also have your own Nest Home account setup.

Please follow the detailed steps at the ST community wiki to create your own Nest developer account.

https://developers.nest.com/

Refer to 

http://thingsthataresmart.wiki/index.php?title=My_NextServiceMgr#Issue_.231:_How_to_create_a_Nest_Developer_account

b) After creating your Nest developer account, you need to copy and paste the productId and product secret keys under
your newly created My NextManager app under the IDE.

Go to https://graph.api.smartthings.com/ide/apps (or whatever your shard is)


c) Click on the NextManager smartapp 

d) Click on the App Settings button in the upper right corner

d) Click on AppSettings in the middle of the screen which should expand the view

e) Copy and paste the productId and product secret keys to the clientId and private key fields respectively

<b>PLEASE MAKE SURE TO NOT INSERT ANY SPACES BEFORE OR AFTER THE KEYS IN EACH FIELD, JUST COPY THE FIELD AS IS!!</b>

f) Hit the "SAVE" Button at the bottom

# 4) Under the ST mobile app, execute MyNextManager

<b>Click on the Smartapps link in the upper section of the following Marketspace screen (last icon in the bottom menu), and then Smartapps/MyApps (last item in the list).</b>

To execute MyNextManager, find MyNextManager under MarketSpace>MyApps, My Next Manager should be in the middle of the list.

After being connected, click 'Next' and select your Nest devices that you want to control from Smartthings and, then press till 'Other Settings &Notification' page, and then 'Done' when finished.

If you get a blank screen after pressing 'Next or you get the following error: " Error - bad state. Unable to complete page configuration", you'd need to enable oAuth as specified in step 2f) above.


# 5) Your device(s) should now be ready to process your commands

After about 1 minute, You should see your newly Next devices instantiated under:

a) https://graph.api.smartthings.com/device/list (or whatever your shard is)

And also

b) Under the ST mobile app, under MyHome/Things (main menu at the bottom of the screen).

# 6) To populate the UI fields for your newly created device(s), press the "refresh" tile </b>

If the fields are blank, you may have to hit the 'refresh' button on your newly created Next devices as the smartThings UI is not always responsive. 


# 7) (Optional) Set device's preferences 


a) Go to https://graph.api.smartthings.com/device/list

b) Click on the Next Devices that you just created

c) Click on Preferences (edit)

N.B. You can also edit the preferences under Things/Your 


Device/Edit Device using the ST mobile app.

You only need to edit the following parameters


    (a) <trace> when needed, set to true to get more tracing (no spaces)
    (b) <logFilter:1..5> Values=[Level 1=ERROR only,2=<Level 1+WARNING>,3=<2+INFO>,4=<3+DEBUG>,5=<4+TRACE>]

    P.S. Don't enter any values for the appKey, privateKey and any serial Id numbers as the values are populated by the
    Service Manager by default during the authentification flow.


# 8) Use some of the Zoned Heating/Cooling Smartapps available (optional)

The following zoned Heating/cooling smartapps have many safeguards built-in to protect your HVAC.

- The smartapps will check the vent's internal temperature to make sure it's not too hot or too cold
- The smartapps will not close more than 50% of the smart vents inputted in the smartapp
- You can define some minimum open threshold (by default 10% in zone, 20% outside the zone) to avoid closing the vents too tight.



<b>
http://www.ecomatiqhomes.com/store 
</b>


/****************************************************

<b>a) ScheduleTstatZones</b>

/****************************************************

For more details:

http://thingsthataresmart.wiki/index.php?title=ScheduleTstatZones

The smartapp that enables Multi Zoned Heating/Cooling Solutions based on any ST connected thermostats/sensors- - coupled with smart vents (optional, can be any Flair, Keen Home, EcoVent, EcoNet smart vents) for better temp settings control throughout your home". 

The smartapp can reproduce the  Flair eveness and eveness active modes based on the thermostat's setpoints.  

The smartapp can also control multiple Minisplit/Windows unit and portable heaters/coolers inside your scheduled zones (based on the Flair HVACUnit device, see http://thingsthataresmart.wiki/index.php?title=My_Flair_HVac_Unit).



/****************************************************

<b>c) ScheduleRoomTempControl</b>

/****************************************************

For more details:

http://thingsthataresmart.wiki/index.php?title=ScheduleRoomTempControl

The smartapp that enables Multi Zoned Heating/Cooling Solutions based on any smart vents (Keen Home, Flair, Econet, etc) and any ST connected thermostats/sensors. 

The smartapp can allow you to define some rooms' thresholds for hotter/colder rooms in your premises.




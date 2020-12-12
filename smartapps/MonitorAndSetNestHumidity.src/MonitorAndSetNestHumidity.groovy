/***
 *  Copyright Yves Racine
 *  LinkedIn profile: ca.linkedin.com/pub/yves-racine-m-sc-a/0/406/4b/
 *
 *  Developer retains all right, title, copyright, and interest, including all copyright, patent rights, trade secret 
 *  in the Background technology. May be subject to consulting fees under the Agreement between the Developer and the Customer. 
 *  Developer grants a non exclusive perpetual license to use the Background technology in the Software developed for and delivered 
 *  to Customer under this Agreement. However, the Customer shall make no commercial use of the Background technology without
 *  Developer's written consent.
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *
 *  Software Distribution is restricted and shall be done only with Developer's written approval.
 *
 *
 *  Monitor and set Humidity with Nest Thermostat:
 *      Monitor humidity level indoor vs. outdoor at a regular interval (in minutes) and 
 *  N.B. Requires MyNextTstatV2 device available at 
 *          http://www.ecomatiqhomes.com/store
 */
definition(
	name: "${get_APP_NAME()}",
	namespace: "yracine",
	author: "Yves Racine",
	description: "Monitor And set Nest's humidity",
	category: "My Apps",
	iconUrl: "${getCustomImagePath()}WorksWithNest.jpg",
	iconX2Url: "${getCustomImagePath()}WorksWithNest.jpg"
)

def get_APP_VERSION() {
	return "1.3"
}

preferences {
	page(name: "dashboardPage", title: "DashboardPage")
	page(name: "humidifySettings", title: "HumidifySettings")
	page(name: "dehumidifySettings", title: "DehumidifySettings")
	page(name: "sensorSettings", title: "SensorSettings")
	page(name: "otherSettings", title: "OtherSettings")

}

def dashboardPage() {
	dynamicPage(name: "dashboardPage", title: "MonitorAndSetNestHumidity-Dashboard", uninstall: true, nextPage: sensorSettings, submitOnChange: true) {
		section("Monitor & set the Nest tstat's dehumidifer/humidifier settings") {
			input "nest", "capability.thermostat", title: "Which Nest?"
		}
		section("To this humidity level") {
			input "givenHumidityLevel", "number", title: "Humidity level (default=calculated based on outside temp)", required: false
		}
		section("At which interval in minutes (range=[10..59],default =59 min.)?") {
			input "givenInterval", "number", title: "Interval", required: false
		}
		section("Humidity differential for adjustments") {
			input "givenHumidityDiff", "number", title: "Humidity Offset Allowed [default=5%]", required: false
		}
		section("Press Next in the upper section for Initial setup") {
			if (nest) {
				nest.refresh()
				def scale = getTemperatureScale()
				def outdoorHumidity, outdoorTemp, corrOutdoorHum, indoorHumidity, indoorTemp

				String mode = nest?.currentThermostatMode.toString()
				def operatingState = nest?.currentThermostatOperatingState
				indoorHumidity = nest.currentHumidity
				indoorTemp = nest.currentTemperature
				def hasDehumidifier = nest?.currentValue("has_dehumidifier")
				def hasHumidifier = nest?.currentValue("has_humidifier")
				String useFanWhenHumidityIsHighString = (settings.useFanWhenHumidityIsHigh) ? 'true' : 'false'
				String useFanWithHumidifierSwitchesString = (settings.useFanWithHumidifierSwitches) ? 'true' : 'false'
				String dehumidifyWithACString=(settings.dehumidifyWithACFlag)? 'true': 'false'                
				def heatingSetpoint, coolingSetpoint
				if (indoorSensor) {
					indoorHumidity = indoorSensor.currentHumidity
					indoorTemp = indoorSensor.currentTemperature
				}
				if (outdoorSensor) {
					outdoorHumidity = outdoorSensor.currentHumidity
					outdoorTemp = outdoorSensor.currentTemperature
					corrOutdoorHum = (scale == 'C') ?
						calculate_corr_humidity(outdoorTemp, outdoorHumidity, indoorTemp).round() :
						calculate_corr_humidity(fToC(outdoorTemp), outdoorHumidity, fToC(indoorTemp)).round()
				}
                def idealIndoorHum                
                if (outdoorTemp) {                
                    idealIndoorHum= (scale == 'C')? find_ideal_indoor_humidity(outdoorTemp):
                        find_ideal_indoor_humidity(fToC(outdoorTemp))
                }                                     
				switch (mode) {
					case 'cool':
						coolingSetpoint = nest?.currentValue('coolingSetpoint')
						break
					case 'eco':
					case 'auto':
						coolingSetpoint = nest?.currentValue('coolingSetpoint')
					case 'heat':
					case 'emergency heat':
					case 'auto':
					case 'off':
						heatingSetpoint = nest?.currentValue('heatingSetpoint')
						break
				}
				def detailedNotifFlag = (detailedNotif) ? 'true' : 'false'
				int min_vent_time = (givenVentMinTime != null) ? givenVentMinTime : 20 //  20 min. ventilator time per hour by default
				def dParagraph = "TstatMode: $mode\n" +
					"TstatOperatingState: $operatingState\n" +
					"TstatTemperature: $indoorTemp${scale}\n"
				if (coolingSetpoint) {
					dParagraph = dParagraph + "CoolingSetpoint: ${coolingSetpoint}$scale\n"
				}
				if (heatingSetpoint) {
					dParagraph = dParagraph + "HeatingSetpoint: ${heatingSetpoint}$scale\n"
				}
				def min_humidity_diff = givenHumidityDiff ?: 5 //  5% humidity differential by default
				dParagraph = dParagraph +
					"DetailedNotification: ${detailedNotifFlag}\n" +
					"nestHasHumidifier: $hasHumidifier\n" +
					"nestHasDeHumidifier: $hasDehumidifier\n" +
					"DehumidifyWithAC: $dehumidifyWithACString\n" +
					"IndoorHumidity: ${indoorHumidity}%\n" +
					"IndoorTemp: ${indoorTemp}$scale\n" +
					"UseFanWhenHumidityIsHigh: $useFanWhenHumidityIsHighString\n" +
					"useFanWithHumidifierSwitch: $useFanWithHumidifierSwitchesString\n" +
					"HumidityOffsetAllowed: +/- $min_humidity_diff%\n"
				if (outdoorSensor) {
					dParagraph = dParagraph +
						"OutdoorHumidity: $outdoorHumidity%\n" +
						"OutdoorTemp: ${outdoorTemp}$scale\n" +
						"NormalizedOutHumidity: $corrOutdoorHum%\n" +
						"IdealIndoorHumidity: $idealIndoorHum%\n"
				}

				paragraph dParagraph
				if (humidifySwitches) {
					dParagraph = "HumidifySwitch(es): $humidifySwitches"
					paragraph dParagraph
				}
				if (dehumidifySwitches) {
					dParagraph = "DehumidifySwitch(es): $dehumidifySwitches"
					paragraph dParagraph
				}

				if (ted) {
					int max_power = givenPowerLevel ?: 3000 // Do not run above 3000w consumption level by default
					dParagraph = "PowerMeter: $ted" +
						"\nDoNotRunOver: ${max_power}W"
					paragraph dParagraph
				}

			} /* end if nest */
		}
		section("Humidifier/Dehumidifier Setup") {
			href(name: "toSensorsPage", title: "Configure your sensors", description: "Tap to Configure...", image: getImagePath() + "HumiditySensor.png", page: "sensorSettings")
			href(name: "toHumidifyPage", title: "Configure your humidifier settings", description: "Tap to Configure...", image: getImagePath() + "Humidifier.jpg", page: "humidifySettings")
			href(name: "toDehumidifyPage", title: "Configure your dehumidifier settings", description: "Tap to Configure...", image: getImagePath() + "dehumidifier.png", page: "dehumidifySettings")
			href(name: "toNotificationsPage", title: "Other Options & Notification Setup", description: "Tap to Configure...", image: getImagePath() + "Fan.png", page: "otherSettings")
		}
		section("About") {
			paragraph "${get_APP_NAME()}, the smartapp that can control your house's humidity via your connected humidifier/dehumidifier and switch(es)"
			paragraph "Version ${get_APP_VERSION()}"
			paragraph "If you like this smartapp, please support the developer via PayPal and click on the Paypal link below "
			href url: "https://www.paypal.me/ecomatiqhomes",
				title: "Paypal donation..."
			paragraph "Copyright©2014-2020 Yves Racine"
			href url: "https://github.com/yracine/device-type.mynest", style: "embedded", required: false, title: "More information..."
			description: "https://github.com/yracine/device-type.mynest/blob/master/README.md"
		}

	} /* end dashboardPage */

}

def sensorSettings() {
	dynamicPage(name: "sensorSettings", title: "Sensors to be used", install: false, nextPage: humidifySettings) {
		section("Choose Indoor humidity sensor to be used for better adjustment (optional, default=nest sensor)") {
			input "indoorSensor", "capability.relativeHumidityMeasurement", title: "Indoor Humidity Sensor", required: false
		}
		section("Choose Outdoor humidity sensor to be used (weatherStation or sensor)") {
			input "outdoorSensor", "capability.relativeHumidityMeasurement", title: "Outdoor Humidity Sensor"
		}
		section {
			href(name: "toDashboardPage", title: "Back to Dashboard Page", page: "dashboardPage")
		}
	}
}


def humidifySettings() {
	dynamicPage(name: "humidifySettings", install: false, uninstall: false, nextPage: dehumidifySettings) {
		section("Switch(es) to be turned on when needed for humidifying/ventilating the house[optional]") {
			input "humidifySwitches", "capability.switch", multiple: true, required: false
			input "useFanWithHumidifierSwitches", "bool", title: "Trigger HVAC's fan when humidifier switch is on?", required: false
		}
		section {
			href(name: "toDashboardPage", title: "Back to Dashboard Page", page: "dashboardPage")
		}
	}
}

def dehumidifySettings() {
	dynamicPage(name: "dehumidifySettings", install: false, uninstall: false, nextPage: otherSettings) {
		section("Minimum outdoor threshold for stopping dehumidification (in Farenheits/Celsius) [optional]") {
			input "givenMinTemp", "decimal", title: "Min Outdoor Temp [default=10°F/-15°C]", description: 'optional', required: false
		}
		section("¨Dehumidify With AC - Use your AC to dehumidify when humidity is high and dehumidifier is not available [optional]") {
			input "dehumidifyWithACFlag",  "bool", title: "Use AC as dehumidifier (By default=false)?", description: 'optional', required: false
		}
		section("Switch(es) to be turned on when needed for dehumidifying/ventilating the house[optional]") {
			input "dehumidifySwitches", "capability.switch", multiple: true, required: false
			input "useFanWhenHumidityIsHigh", "bool", title: "Trigger HVAC's fan when humidity is high?", required: false
		}

		section {
			href(name: "toDashboardPage", title: "Back to Dashboard Page", page: "dashboardPage")
		}
	}
}



def otherSettings() {
	def enumModes = location.modes.collect {
		it.name
	}

	dynamicPage(name: "otherSettings", title: "Other Settings", install: true, uninstall: false) {
		section("Check energy consumption at [optional, to avoid using HRV/ERV/Humidifier/Dehumidifier at peak]") {
			input "ted", "capability.powerMeter", title: "Power meter?", description: 'optional', required: false
			input "givenPowerLevel", "number", title: "power?", description: 'optional', required: false
		}
		section("What do I use for the Master on/off switch to enable/disable processing? (optional)") {
			input "powerSwitch", "capability.switch", required: false
		}
		section("Logging") {
			input "detailedNotif", "bool", title: "Detailed Logging?", required:
				false
		}
		if (isST()) {      
	    		section("Enable Amazon Echo/Ask Alexa Notifications for events logging (optional)") {
	    			input(name: "askAlexaFlag", title: "Ask Alexa verbal Notifications [default=false]?", type: "bool",
		    			description: "optional", required: false)
	    			input(name: "listOfMQs", type: "enum", title: "List of the Ask Alexa Message Queues (default=Primary)", options: state?.askAlexaMQ, multiple: true, required: false,
			    		description: "optional")
	    			input "AskAlexaExpiresInDays", "number", title: "Ask Alexa's messages expiration in days (optional,default=2 days)?", required: false
	    		}
        	}            
		section("Set Humidity Level only for specific mode(s) [default=all]") {
			input(name: "selectedMode", type: "enum", title: "Choose Mode", options: enumModes,
				required: false, multiple: true, description: "Optional")
		}
		section([mobileOnly: true]) {
			label title: "Assign a name for this SmartApp", required: false
		}
		section {
			href(name: "toDashboardPage", title: "Back to Dashboard Page", page: "dashboardPage")
		}
	}
}

boolean isST() { 
    return (getHub() == "SmartThings") 
}

private getHub() {
    def result = "SmartThings"
    if(state?.hub == null) {
        try { [value: "value"]?.encodeAsJson(); } catch (e) { result = "Hubitat" }
        state?.hub = result
    }
    log.debug "hubPlatform: (${state?.hub})"
    return state?.hub
}


def installed() {
	initialize()
}

def updated() {
	// we have had an update
	// remove everything and reinstall
	unschedule()
	unsubscribe()
	initialize()
}

def initialize() {
	state.currentRevision = null // for further check with thermostatRevision later

	if (powerSwitch) {
		subscribe(powerSwitch, "switch.off", offHandler)
		subscribe(powerSwitch, "switch.on", onHandler)
	}
	int delay = givenInterval ?: 59 // By default, do it every hour
	if ((delay < 10) || (delay > 59)) {
		log.error "Scheduling delay not in range (${delay} min.), exiting"
		send("error, inputted delay $delay in minutes not in range  [10..59]")
		return
	}
	if (detailedNotif) {
		log.debug "initialize>scheduling Humidity Monitoring and adjustment every ${delay} minutes"
	}

	state?.poll = [last: 0, rescheduled: now()]

	subscribe(location, "askAlexaMQ", askAlexaMQHandler)

	//Subscribe to different events (ex. sunrise and sunset events) to trigger rescheduling if needed
	subscribe(location, "sunrise", rescheduleIfNeeded)
	subscribe(location, "sunset", rescheduleIfNeeded)
	subscribe(location, "mode", rescheduleIfNeeded)
	subscribe(location, "sunriseTime", rescheduleIfNeeded)
	subscribe(location, "sunsetTime", rescheduleIfNeeded)

	rescheduleIfNeeded()
}

def askAlexaMQHandler(evt) {
	if (!evt) return
	switch (evt.value) {
		case "refresh":
			state?.askAlexaMQ = evt.jsonData && evt.jsonData?.queues ? evt.jsonData.queues : []
			log.debug("askAlexaMQHandler>new refresh value=$evt.jsonData?.queues")
			break
	}
}


def appTouch(evt) {
	rescheduleIfNeeded()

}

def rescheduleIfNeeded(evt) {
	if (evt) log.debug("rescheduleIfNeeded>$evt.name=$evt.value")
	int delay = givenInterval ?: 59 // By default, do it every hour
	BigDecimal currentTime = now()
	BigDecimal lastPollTime = (currentTime - (state?.poll["last"] ?: 0))

	if (lastPollTime != currentTime) {
		Double lastPollTimeInMinutes = (lastPollTime / 60000).toDouble().round(1)
		log.info "rescheduleIfNeeded>last poll was  ${lastPollTimeInMinutes.toString()} minutes ago"
	}
	if (((state?.poll["last"] ?: 0) + (delay * 60000) < currentTime)) {
		log.info "rescheduleIfNeeded>scheduling setHumidityLevel in ${delay} minutes.."
		schedule("0 0/${delay} * * * ?", setHumidityLevel)
		setHumidityLevel()
	}


	// Update rescheduled state

	if (!evt) state.poll["rescheduled"] = now()
}



def offHandler(evt) {
	log.debug "$evt.name: $evt.value"

	if (dehumidifySwitches) {
		if (detailedNotif) {
			log.trace("turning off all dehumidify/fan switches")
		}
		dehumidifySwitches.off()
	}

	if (humidifySwitches) {
		if (detailedNotif) {
			log.trace("turning off all humidify/fan switches")
		}
		humidifySwitches.off()
	}

}

def onHandler(evt) {
	log.debug "$evt.name: $evt.value"
	setHumidityLevel()
}



def setHumidityLevel() {
	int scheduleInterval = givenInterval ?: 59 // By default, do it every hour

	def todayDay = new Date().format("dd", location.timeZone)
	if ((!state?.today) || (todayDay != state?.today)) {
		state?.exceptionCount = 0
		state?.sendExceptionCount = 0
		state?.today = todayDay
	}

	state?.poll["last"] = now()



	//schedule the rescheduleIfNeeded() function
	if (((state?.poll["rescheduled"] ?: 0) + (scheduleInterval * 60000)) < now()) {
		log.info "takeAction>scheduling rescheduleIfNeeded() in ${scheduleInterval} minutes.."
		schedule("0 0/${scheduleInterval} * * * ?", rescheduleIfNeeded)
		// Update rescheduled state
		state?.poll["rescheduled"] = now()
	}

	boolean foundMode = selectedMode.find {
		it == (location.mode as String)
	}
	if ((selectedMode != null) && (!foundMode)) {
		if (detailedNotif) {
			log.trace("setHumidityLevel does not apply,location.mode= $location.mode, selectedMode=${selectedMode},foundMode=${foundMode}, turning off all equipments")
		}
		if (dehumidifySwitches) {
			if (detailedNotif) {
				log.trace("setHumidityLevel does not apply,location.mode= $location.mode, turning off all dehumidify/fan switches")
			}
			dehumidifySwitches.off()
		}
		if (humidifySwitches) {
			if (detailedNotif) {
				log.trace("setHumidityLevel does not apply,location.mode= $location.mode, turning off all humidify/fan switches")
			}
			humidifySwitches.off()
		}
		return
	}

	if (detailedNotif) {
		send("monitoring every ${scheduleInterval} minute(s)")
		log.debug "Scheduling Humidity Monitoring & Change every ${scheduleInterval}  minutes"
	}


	if (powerSwitch?.currentSwitch == "off") {
		if (detailedNotif) {
			send("Virtual master switch ${powerSwitch.name} is off, processing on hold...")
			log.debug("Virtual master switch ${powerSwitch.name} is off, processing on hold...")
		}
		return
	}
	def min_humidity_diff = givenHumidityDiff ?: 5 //  5% humidity differential by default
	def min_temp // Min temp in Farenheits for using HRV/ERV,otherwise too cold

	def scale = getTemperatureScale()
	if (scale == 'C') {
		min_temp = (givenMinTemp) ? givenMinTemp : -15 // Min. temp in Celcius for using HRV/ERV,otherwise too cold
	} else {

		min_temp = (givenMinTemp) ? givenMinTemp : 10 // Min temp in Farenheits for using HRV/ERV,otherwise too cold
	}
	int max_power = givenPowerLevel ?: 3000 // Do not run above 3000w consumption level by default


	//  Polling of all devices

	def MAX_EXCEPTION_COUNT = 10
	String exceptionCheck, msg
	try {
		nest.poll()
		exceptionCheck = nest.currentVerboseTrace.toString()
		if ((exceptionCheck) && ((exceptionCheck.contains("exception") || (exceptionCheck.contains("error")) &&
				(!exceptionCheck.contains("Java.util.concurrent.TimeoutException"))))) {
			// check if there is any exception or an error reported in the verboseTrace associated to the device (except the ones linked to rate limiting).
			state?.exceptionCount = state.exceptionCount + 1
			log.error "setHumidityLevel>found exception/error after polling, exceptionCount= ${state?.exceptionCount}: $exceptionCheck"
		} else {
			// reset exception counter            
			state?.exceptionCount = 0
		}
	} catch (e) {
		log.error "setHumidityLevel>exception $e while trying to poll the device, exceptionCount= ${state?.exceptionCount}"
	}
	if ((state?.exceptionCount >= MAX_EXCEPTION_COUNT) || ((exceptionCheck) && (exceptionCheck.contains("Unauthorized")))) {
		// need to authenticate again    
		msg = "too many exceptions/errors or unauthorized exception, $exceptionCheck (${state?.exceptionCount} errors), may need to re-authenticate at nest..."
		log.error msg
		send(msg, askAlexaFlag)
		return
	}

	if (outdoorSensor && outdoorSensor.hasCapability("Polling")) {
		try {
			outdoorSensor.poll()
		} catch (e) {
			msg = "not able to poll ${outdoorSensor}'s temp value"
			log.warn msg
			if (detailedNotif) {
				send(msg, askAlexaFlag)
			}
		}
	} else if (outdoorSensor && outdoorSensor.hasCapability("Refresh")) {
		try {
			outdoorSensor.refresh()
		} catch (e) {
			msg = "not able to refresh ${outdoorSensor}'s temp value"
			log.warn msg
			if (detailedNotif) {
				send(msg, askAlexaFlag)
			}
		}
	}
	if (ted) {

		try {
			ted.poll()
			int powerConsumed = ted.currentPower.toInteger()
			if (powerConsumed > max_power) {

				// peak of energy consumption, turn off all devices

				if (detailedNotif) {
					msg = "all off,power usage is too high=${ted.currentPower}"
					log.info msg
					send(msg, askAlexaFlag)
				}

				if (dehumidifySwitches) {
					if (detailedNotif) {
						log.trace("all off,power usage is too high, turning off all dehumidify/fan switches")
					}
					dehumidifySwitches.off()
				}
				if (humidifySwitches) {
					if (detailedNotif) {
						log.trace("all off,power usage is too high, turning off all humidify/fan switches")
					}
					humidifySwitches.off()
				}
				return

			}
		} catch (e) {
			log.error "Exception $e while trying to get power data "
		}
	}


	def heatTemp = nest.currentHeatingSetpoint
	def coolTemp = nest.currentCoolingSetpoint
	def nestHumidity = nest.currentHumidity
	def indoorHumidity = 0
	def indoorTemp = nest.currentTemperature
	def hasDehumidifier = nest?.currentValue("has_dehumidifier")
	def hasHumidifier = nest?.currentValue("has_humidifier")
	def outdoorHumidity

	// use the readings from another sensor if better precision neeeded
	if (indoorSensor) {
		indoorHumidity = indoorSensor.currentHumidity
		indoorTemp = indoorSensor.currentTemperature
	}

	def outdoorSensorHumidity = outdoorSensor.currentHumidity
	def outdoorTemp = outdoorSensor.currentTemperature
	// by default, the humidity level is calculated based on a sliding scale target based on outdoorTemp

	def target_humidity = givenHumidityLevel ?: (scale == 'C') ? find_ideal_indoor_humidity(outdoorTemp) :
		find_ideal_indoor_humidity(fToC(outdoorTemp))

	String nestMode = nest.currentThermostatMode.toString()
	if (detailedNotif) {
		log.debug "MonitorAndSetnestHumidity>location.mode = $location.mode"
		log.debug "MonitorAndSetnestHumidity>nest Mode = $nestMode"
	}

	outdoorHumidity = (scale == 'C') ?
		calculate_corr_humidity(outdoorTemp, outdoorSensorHumidity, indoorTemp) :
		calculate_corr_humidity(fToC(outdoorTemp), outdoorSensorHumidity, fToC(indoorTemp))


	//  If indoorSensor specified, use the more precise humidity measure instead of nestHumidity

	if ((indoorSensor) && (indoorHumidity < nestHumidity)) {
		nestHumidity = indoorHumidity
	}

	if (detailedNotif) {
		log.trace("nest's humidity: ${nestHumidity} vs. indoor humidity ${indoorHumidity}")
		log.debug "outdoorSensorHumidity = $outdoorSensorHumidity%, normalized outdoorHumidity based on ambient temperature = $outdoorHumidity%"
		send("normalized outdoor humidity is ${outdoorHumidity}%,sensor outdoor humidity ${outdoorSensorHumidity}%,vs. indoor Humidity ${nestHumidity}%",
			askAlexaFlag)
		log.trace("Evaluate: nest humidity: ${nestHumidity} vs. outdoor humidity ${outdoorHumidity}, humidity differential allowed= ${min_humidity_diff}, " +
			"coolingSetpoint: ${coolTemp} , heatingSetpoint: ${heatTemp}, target humidity=${target_humidity}")
		log.trace("hasHumidifier=${hasHumidifier},hasDehumidifier=${hasDehumidifier}")
		//			"useDehumidifierAsHRV=${useDehumidifierAsHRVFlag}, useFanWhenHumidityIsHigh=${useFanWhenHumidityIsHigh}")
	}

	if (((nestMode in ['heat', 'off', 'auto', 'eco']) && (hasDehumidifier == 'true')) &&
		(nestHumidity >= (target_humidity + min_humidity_diff)) &&
		(nestHumidity >= outdoorHumidity) &&
		(outdoorTemp > min_temp)) {

		if (detailedNotif) {
			log.trace "$nest is in ${nestMode} mode and its humidity > target humidity level=${target_humidity}, need to dehumidify the house " +
				"normalized outdoor humidity is within range (${outdoorHumidity}) & outdoor temp is ${outdoorTemp},not too cold, using connected dehumidifier and $dehumidifySwitches switch(es)"
			send("dehumidify to ${target_humidity}% in ${nestMode} mode using connected dehumidifier and $dehumidifySwitches switch(es)", askAlexaFlag)
		}

		nest.setThermostatSettings(null, ['auto_dehum_enabled': false, 'target_humidity':target_humidity, "target_humidity_enabled":true])

		if (settings.useFanWhenHumidityIsHigh) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan as requested")
				send("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan as requested")
			}
			nest.fanOn() // set fan on
		}
		if (dehumidifySwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, turning on all dehumidify/fan switches")
			}
			dehumidifySwitches.on()
		}

	} else if (((nestMode in ['heat', 'off', 'auto', 'eco']) && (hasDehumidifier == 'true')) &&
		(nestHumidity >= (target_humidity + min_humidity_diff)) &&
		(nestHumidity >= outdoorHumidity) &&
		(outdoorTemp <= min_temp)) {


		//      Turn off the dehumidifer because it's too cold, wait till the next cycle.

		nest.setThermostatSettings(null, ['auto_dehum_enabled': false, 'target_humidity':target_humidity, "target_humidity_enabled":false])

		if (detailedNotif) {
			log.trace "nest is in ${nestMode} mode and its humidity > target humidity level=${target_humidity}, need to dehumidify the house " +
				"normalized outdoor humidity is lower (${outdoorHumidity}), but outdoor temp is ${outdoorTemp}: too cold to dehumidify, using $dehumidifySwitches switch(es) only"
			send("Too cold (${outdoorTemp}°) to dehumidify to ${target_humidity}, using $dehumidifySwitches switch(es) only", askAlexaFlag)
		}
		if (settings.useFanWhenHumidityIsHigh) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan as requested")
				send("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan as requested")
			}
			nest.fanOn() // set fan on
		}
		if (dehumidifySwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, turning on all dehumidify/fan switches")
			}
			dehumidifySwitches.on()
		}

	} else if ((((nestMode in ['heat', 'off', 'auto', 'eco']) && hasHumidifier == 'true')) &&
		(nestHumidity < (target_humidity - min_humidity_diff) && 
		(nestHumidity < outdoorHumidity))) {

		nest.setThermostatSettings(null, ['auto_dehum_enabled': false, 'target_humidity':target_humidity, "target_humidity_enabled":true])
		if (detailedNotif) {
			log.trace("In ${nestMode} mode, nest's humidity provided is way lower than target humidity level=${target_humidity}, and lower than outdoorHumidity= ${outdoorHumidity}, need to humidify the house with connected humidifier and $humidifySwitches switch(es)")
			send("humidify to ${target_humidity} in ${nestMode} mode using connected humidifier and $humidifySwitches switch(es)", askAlexaFlag)
		}
		//      Need a minimum differential to humidify the house to the target if any humidifier available

		if (humidifySwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and is way lower than target humidity, turning on all humidify/fan switches")
			}
			humidifySwitches.on()
			if (settings.useFanWithHumidifierSwitches) {
				if (detailedNotif) {
					log.trace("Indoor humidity is ${nestHumidity}% and below the target humidity, triggering the HVAC fan as requested")
					send("Indoor humidity is ${nestHumidity}% and below the target humidity, triggering the HVAC fan as requested")
				}
				nest.fanOn() // set fan on
			}
		}

	} else if ((((nestMode in ['heat', 'off', 'auto', 'eco']) && hasHumidifier == 'false')) &&
		(nestHumidity < (target_humidity - min_humidity_diff))) {

		if (detailedNotif) {
			log.trace("In ${nestMode} mode, nest's humidity provided is way lower than target humidity level=${target_humidity}, need to humidify the house, but no humidifier is connected to nest, using $humidifySwitches switch(es) only")
		}
		//      Need a minimum differential to humidify the house to the target if any humidifier available

		if (humidifySwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and is way lower than target humidity, turning on all humidify/fan switches")
				send("In ${nestMode} mode, nest's humidity provided is way lower than target humidity level=${target_humidity}, need to humidify the house, using $humidifySwitches switch(es)",
					askAlexaFlag)
			}
			humidifySwitches.on()
			if (settings.useFanWithHumidifierSwitches) {
				if (detailedNotif) {
					log.trace("Indoor humidity is ${nestHumidity}% and below the target humidity, triggering the HVAC fan as requested")
					send("Indoor humidity is ${nestHumidity}% and below the target humidity, triggering the HVAC fan as requested")
				}
				nest.fanOn() // set fan on
			}
		}

	} else if (((nestMode == 'cool' && dehumidifyWithACFlag == true) && (hasDehumidifier == 'false')) &&
		(nestHumidity > (target_humidity + min_humidity_diff)) &&
		(outdoorHumidity > target_humidity)) {

		nest.setThermostatSettings(null, ['auto_dehum_enabled': true, 'target_humidity':target_humidity, "target_humidity_enabled":true])

		if (detailedNotif) {
			log.trace("nest humidity provided is way higher than target humidity level=${target_humidity}, need to dehumidify with AC, because normalized outdoor humidity is too high=${outdoorHumidity}")
			send("dehumidifyWithAC in cooling mode, indoor humidity is ${nestHumidity}% and normalized outdoor humidity (${outdoorHumidity}%) is too high to dehumidify, using $dehumidifySwitches switch(es) only",
				askAlexaFlag)
		}
		//      If mode is cooling and outdoor humidity is too high then use the A/C to lower humidity in the house if there is no dehumidifier

		if (settings.useFanWhenHumidityIsHigh) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan as requested")
				send("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan as requested")
			}
			nest.fanOn() // set fan on
		}
		if (dehumidifySwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, turning on all dehumidify/fan switches")
			}
			dehumidifySwitches.on()
		}

	} else if (((nestMode == 'cool' && !dehumidifyWithACFlag) && (hasDehumidifier == 'false')) &&
		(nestHumidity > (target_humidity + min_humidity_diff)) &&
		(outdoorHumidity > target_humidity)) {


		if (detailedNotif) {
			log.trace("nest humidity provided is way higher than target humidity level=${target_humidity}, need to dehumidify with AC, no dehumidifier is available, flag is set to false in the smartapp...")
			send("nest humidity provided is way higher than target humidity level=${target_humidity}, need to dehumidify with AC, no dehumidifier is available, flag is set to false in the smartapp, using $dehumidifySwitches switch(es) only",
				askAlexaFlag)
		}
		nest.setThermostatSettings(null, ['auto_dehum_enabled': false, 'target_humidity':target_humidity, "target_humidity_enabled":true])

		if (dehumidifySwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, turning on all dehumidify/fan switches")
			}
			dehumidifySwitches.on()
		}


	} else if ((outdoorHumidity > nestHumidity) && (nestHumidity > (target_humidity + min_humidity_diff))) {

		//      If indoor humidity is greater than target, but outdoor humidity is way higher than indoor humidity, 
		//      just wait for the next cycle & do nothing for now.

		if (detailedNotif) {
			log.trace("Indoor humidity is ${nestHumidity}%, but outdoor humidity (${outdoorHumidity}%) is too high to dehumidify")
			send("indoor humidity is ${nestHumidity}%, but outdoor humidity ${outdoorHumidity}% is too high to dehumidify, using $dehumidifySwitches switch(es) only", askAlexaFlag)
		}
		nest.setThermostatSettings(null, ['auto_dehum_enabled': false,  "target_humidity_enabled":false])

		if (settings.useFanWhenHumidityIsHigh) {
			nest.fanOn() // set fan on
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan as requested")
				send("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan as requested")
			}
		}
		if (dehumidifySwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, turning on all dehumidify/fan switches")
			}
			dehumidifySwitches.on()
		}

	} else if ((nestHumidity > (target_humidity + min_humidity_diff)) && (settings.useFanWhenHumidityIsHigh)) {
		if (detailedNotif) {
			log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan and $dehumidifySwitches switch(es) as requested")
			send("Indoor humidity is ${nestHumidity}% and above the target humidity, triggering the HVAC fan and $dehumidifySwitches switch(es) as requested")
		}
		nest.fanOn() // set fan on
		if (dehumidifySwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and above the target humidity, turning on all dehumidify/fan switches")
			}
			dehumidifySwitches.on()
		}

	} else {

		nest.setThermostatSettings(null, ['target_humidity':target_humidity, "target_humidity_enabled":false,"auto_dehum_enabled":false])
		if (detailedNotif) {
			log.trace("All off, humidity level (${nestHumidity}%) within range")
			send("all off, humidity level (${nestHumidity}%) within range", askAlexaFlag)
		}
		if (settings.useFanWhenHumidityIsHigh) {
			nest.fanOff() // set fan to off
		}
		if (dehumidifySwitches) {
			if (detailedNotif) {
				log.trace("humidity level (${nestHumidity}%) within range, turning off all dehumidify/fan switches")
			}
			dehumidifySwitches.off()
		}
      
		if (humidifySwitches) {
			if (detailedNotif) {
				log.trace("humidity level (${nestHumidity}%) within range, turning off all humidify/fan switches")
			              
			}
			humidifySwitches.off()
		}
		if (settings.useFanWithHumidifierSwitches) {
			if (detailedNotif) {
				log.trace("Indoor humidity is ${nestHumidity}% and close to target humidity, setting the HVAC's fan to off")
				send("Indoor humidity is ${nestHumidity}% and close to target humidity, setting the HVAC's fan to off")
			}
			nest.fanOff() // set fan to off
		}
	
	}

	log.debug "End of Fcn"
}


private def bolton(t) {

	//  Estimates the saturation vapour pressure in hPa at a given temperature,  T, in Celcius
	//  return saturation vapour pressure at a given temperature in Celcius


	Double es = 6.112 * Math.exp(17.67 * t / (t + 243.5))
	return es

}


private def calculate_corr_humidity(t1, rh1, t2) {


	log.debug("calculate_corr_humidity t1= $t1, rh1=$rh1, t2=$t2")

	Double es = bolton(t1)
	Double es2 = bolton(t2)
	Double vapor = rh1 / 100.0 * es
	Double rh2 = ((vapor / es2) * 100.0).round(2)

	log.debug("calculate_corr_humidity rh2= $rh2")

	return rh2
}


private send(String msg, askAlexa = false) {
	int MAX_EXCEPTION_MSG_SEND = 5

	// will not send exception msg when the maximum number of send notifications has been reached
	if (msg.contains("exception")) {
		atomicState?.sendExceptionCount = atomicState?.sendExceptionCount + 1
		log.debug("checking sendExceptionCount=${atomicState?.sendExceptionCount} vs. max=${MAX_EXCEPTION_MSG_SEND}")
		if (atomicState?.sendExceptionCount >= MAX_EXCEPTION_MSG_SEND) {
			log.warn("send>reached $MAX_EXCEPTION_MSG_SEND exceptions, exiting")
			return
		}
	}
	def message = "${get_APP_NAME()}>${msg}"


	if (sendPushMessage == "Yes") {
		sendPush(message)
	}
	if (askAlexa) {
		def expiresInDays = (AskAlexaExpiresInDays) ?: 2
		sendLocationEvent(
			name: "AskAlexaMsgQueue",
			value: "${get_APP_NAME()}",
			isStateChange: true,
			descriptionText: msg,
			data: [
				queues: listOfMQs,
				expires: (expiresInDays * 24 * 60 * 60) /* Expires after 2 days by default */
			]
		)
	} /* End if Ask Alexa notifications*/

	if (phoneNumber) {
		sendSms(phoneNumber, message)
	}
}



private int find_ideal_indoor_humidity(outsideTemp) {

	// -30C => 30%, at 0C => 45%

	int targetHum = 45 + (0.5 * outsideTemp)
	return (Math.max(Math.min(targetHum, 60), 30))
}


// catchall
def event(evt) {
	log.debug "value: $evt.value, event: $evt, settings: $settings, handlerName: ${evt.handlerName}"
}

def cToF(temp) {
	return (temp * 1.8 + 32)
}

def fToC(temp) {
	return (temp - 32) / 1.8
}

def getCustomImagePath() {
	return "https://raw.githubusercontent.com/yracine/device-type-myNext/master/icons/"
}    

def getImagePath() {
	return "https://raw.githubusercontent.com/yracine/device-type.myecobee/master/icons/"
}

def get_APP_NAME() {
	return "MonitorAndSetNestHumidity"
}

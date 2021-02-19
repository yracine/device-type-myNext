/**
 *  NestChangeMode
 *
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
 * Change the mode manually (by pressing the app's play button) and automatically at the Nest thermostat(s)
 * If you need to set it for both Away and Home modes, you'd need to save them as 2 distinct apps
 * Don't forget to set the app to run only for the target mode.
 *
 *  N.B. Requires MyNextTstat device available at 
 *          http://www.ecomatiqhomes.com/#!store/tc3yr 
 */
definition(
	name: "NestChangeMode",
	namespace: "yracine",
	author: "Yves Racine",
	description:
	"Change the Nest mode (Away, Home) manually (by pressing the app's play button) or automatically at the Nest thermostat(s) based on the location mode(s)",
	category: "My Apps",
	iconUrl: "${getCustomImagePath()}WorksWithNest.jpg",
	iconX2Url: "${getCustomImagePath()}WorksWithNest.jpg"
)

private def get_APP_VERSION() {
	return "1.5"
}    


preferences {


	page(name: "selectThermostats", title: "Thermostats", install: false , uninstall: true, nextPage: "selectProgram") {
		section("About") {
			paragraph "NestChangeMode, the smartapp that sets your Nest structure to a 'Away' or 'Home' or just the tstats to 'Eco'" + 
                		" based on the hub's location mode."
			paragraph "Version ${get_APP_VERSION()}" 
			paragraph "If you like this smartapp, please support the developer via PayPal and click on the Paypal link below " 
				href url:"https://www.paypal.me/ecomatiqhomes",
					title:"Paypal donation..."
			paragraph "Copyright©2018-2020 Yves Racine"
				href url:"http://github.com/yracine/device-type-myNext", style:"embedded", required:false, title:"More information...", 
					description: "http://github.com/yracine"
		}
        
	}
	page(name: "selectProgram", title: "Nest mode", content: "selectProgram")
	page(name: "Notifications", title: "Notifications & other Options", install: true, uninstall: true) {
        if (isST()) {        
    		section("Notifications") {
	    			input "sendPushMessage", "enum", title: "Send a push notification?", options:["Yes", "No"], required:
			    		false
    				input "phone", "phone", title: "Send a Text Message?", required: false
	    	}
		}
		section([mobileOnly:true]) {
        	label title: "Assign a name for this SmartApp", required: false
    	}
	}
}


def selectProgram() {
    def NestPrograms=['Away', 'Home']
	def enumModes=[]
	location.modes.each {
		enumModes << it.name
	}    

	return dynamicPage(name: "selectProgram", title: "Select Nest Mode", install: false, uninstall: true, nextPage:
			"Notifications") {
		section("Change the following Nest thermostat(s)...") {
			input "thermostats", "capability.thermostat", title: "Which thermostat(s)", multiple: true
		}
		section("Select Nest Mode") {
			input "givenClimate", "enum", title: "Change the Nest structure to this mode (Away, Home)?", options: NestPrograms, required: true
			input "ecoFlag", "bool", title: "Or just set the thermostat(s) to eco?", required: false
            
		}
		section("When hub's location  mode changes to ('Away', 'Home')[optional]") {
			input "newMode", "enum", options: enumModes, multiple:true, required: false
		}
		section("Enter a delay in minutes [optional, default=immediately after ST hello mode change] ") {
			input "delay", "number", title: "Delay in minutes [default=immediate]", description:"no delay by default",required:false
		}
		section("Do the mode change manually only - not automatically (by pressing the arrow next to its name under Automations/Smartapps to execute the smartapp)") {
			input "manualFlag", "bool", title: "Manual only [default=false]", description:"optional",required:false
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
	unsubscribe()
	initialize()	
}

private def initialize() {

	if (!manualFlag) {
		subscribe(location, "mode", changeMode)
	} else {
		takeAction()  
	}    
	subscribe(app, appTouch)
}

def appTouch(evt) {
	log.debug ("NestChangeMode>location.mode= $location.mode, givenClimate=${givenClimate}, about to takeAction")

	takeAction() 
}


def changeMode(evt) {
	def message


	if (delay) {
		try {
			unschedule(takeAction)
		} catch (e) {
			log.debug ("NestChangeMode>exception when trying to unschedule: $e")    
		}    
	}    
    
	Boolean foundMode=false        
	newMode.each {
        
		if (it==location.mode) {
			foundMode=true            
		}            
	}        
	log.debug ("NestChangeMode>location.mode= $location.mode, newMode=${newMode}")
        
	if ((newMode != null) && (!foundMode)) {
        
		log.debug "NestChangeMode>location.mode= $location.mode, newMode=${newMode},foundMode=${foundMode}, not doing anything"
		return			
	}

	if ((!delay) || (delay==null)) {
		log.debug ("NestChangeMode>about to call takeAction()")
		takeAction()    
	} else {
		runIn((delay*60), "takeAction")   
 	}    
}

private void takeAction() {
	def message = "NestChangeMode>setting ${thermostats} to ${givenClimate}.."
	send(message)
	log.debug (message)
    
	thermostats.each {
		if (settings.ecoFlag) {
			it?.eco()        
		} else if (givenClimate.toString()=='Away') {
			it?.away()
		} else {
			it?.present()
		}            
	}        
}




private send(msg) {
	if (sendPushMessage != "No") {
		log.debug("sending push message")
		sendPush(msg)
	}
	if (phone) {
		log.debug("sending text message")
		sendSms(phone, msg)
	}

	log.debug msg
}

def getCustomImagePath() {
	return "https://raw.githubusercontent.com/yracine/device-type-myNext/master/icons/"
}    

private def get_APP_NAME() {
	return "NestChangeMode"
}

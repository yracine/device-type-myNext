/*
 *  NestGetTips
 *  Copyright 2015 Yves Racine
 *  LinkedIn profile: http://www.linkedin.com/in/yracine
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
 *  Requires: MyNextTstat, see www.ecomatiqhomes.com/store
 */
definition(
    name: "NestGetTips",
    namespace: "yracine",
    author: "Yves Racine",
    description: "Get Energy Saving Tips from My Nest Tstat",
    category: "My Apps",
    iconUrl: "${getCustomImagePath()}WorksWithNest.jpg",
    iconX2Url: "${getCustomImagePath()}WorksWithNest.jpg"
)


preferences {

	page (name: "generalSetupPage", title: "General Setup", uninstall:true, nextPage: "displayTipsPage") {
		section("About") {
			paragraph "NestGetTips, the smartapp that Get Comfort & Energy Saving Tips from My Nest Tstat"
			paragraph "Version 1.1" 
			paragraph "If you like this smartapp, please support the developer via PayPal and click on the Paypal link below " 
				href url: "https://www.paypal.me/ecomatiqhomes",
					title:"Paypal donation..."
			paragraph "Copyright©2018 Yves Racine"
				href url:"http://github.com/yracine/device-type.mynest", style:"embedded", required:false, title:"More information..."  
					description: "http://github.com/yracine/device-type.mynest/blob/master/README.md"
		}
		section("Get tips for this Nest thermostat") {
			input "nest", "capability.thermostat", title: "MyNext Tstat only (won't work with any other tstats)..."
		}  
		section("Level Of Tip") {
			input (name:"level", title: "Which level ([0..2], 2 is the highest, 0=all tips?", type:"number", 
    			required:false, description:"optional")
		}  
		section("Tip processing reset") {
			input (name:"resetTipFlag", title: "Do you want to re-start over and reset tips?", type:"bool")
		}  
	}
	page (name: "displayTipsPage", content: "displayTipsPage", install: false, uninstall:true)
	page(name: "OtherOptions", title: "Other Options", install: true, uninstall: true) {
        section([mobileOnly:true]) {
			label title: "Assign a name for this SmartApp", required: false
		}
	}
}

def displayTipsPage() {
	if (resetTipFlag) {
		log.debug("displayPageTips>about to call resetTips()")    
		nest.resetTips()
	}    
	log.debug("displayPageTips>about to call getTips()")  
	def level=(settings.level)?:0    
	nest.getTips(level)
	def tip1 = nest.currentTip1Text    
	def tip2 = nest.currentTip2Text    
	def tip3 = nest.currentTip3Text    
	def tip4 = nest.currentTip4Text    
	def tip5 = nest.currentTip5Text
	def tip1Level = nest.currentTip1Level    
	def tip2Level = nest.currentTip2Level    
	def tip3Level = nest.currentTip3Level    
	def tip4Level = nest.currentTip4Level    
	def tip5Level = nest.currentTip5Level
    
	return dynamicPage (name: "displayTipsPage", title: "Display Current Tips",  nextPage: "OtherOptions") {
    
		section("Tips") {
	        
			if (tip1) {    
				paragraph image: "${getCustomImagePath()}/tip.jpg","** Tip1 - Level $tip1Level **\n\n ${tip1}\n"
                
			} else {
				paragraph image: "${getCustomImagePath()}/tip.jpg", "Based on the input data available, no tips may apply at this time for this level. " +
					"You can try with a different level of tips or later when the indoor/outdoor conditions have changed!"
			}            
			            
			if (tip2) {    
				paragraph image: "${getCustomImagePath()}tip.jpg", "** Tip2 - Level$tip2Level **\n\n  ${tip2}\n"    
			}        
			if (tip3) {    
				paragraph image: "${getCustomImagePath()}tip.jpg",  "** Tip3 - Level $tip3Level **\n\n  ${tip3}\n"    
			}
			if (tip4) {    
				paragraph image: "${getCustomImagePath()}tip.jpg", "** Tip4 - Level $tip4Level **\n\n  ${tip4}\n"    
			}        
			if (tip5) {    
				paragraph image: "${getCustomImagePath()}tip.jpg", "** Tip5 - Level $tip5Level **\n\n  ${tip5}\n"    
			}
		}  
	}  
 
}
def installed() {

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
}



def getCustomImagePath() {
	return "https://raw.githubusercontent.com/yracine/device-type-myNext/master/icons/"
}

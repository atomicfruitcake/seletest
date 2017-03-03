# Seletest
Selenium Testing framework

# What is this?
Seletest combines Selenium, TestNG, Docker, Selendroid, and BrowerMobProxy so you can create automated browser tests with ease. 
Seletest can also update JIRA tickets based on Test results and update slack, completely automated.
The Seletest framework is designed to allow testers to begin scripting automated tests quickly without having to
set up test reporting alongside selenium. 
Seletest supports dockerised testing (if docker is installed), network traffic capture with BrowserMobProxy, and mobile web testing with Selendroid. Example tests can be found in src.tests Seletest comes with a slackbot to update a slack channel with your test results

# How do I use it?
## Initial Setup
Clone this repo and run the following commands

createGradleWrapper (If gradle is not already on your classpath)
gradle clean build   
gradle eclipse (If using Eclipse, recommended)  
gradle idea (If using IntelliJ IDEA)  


Now just import the project in to your IDE of choice

## Custom settings and Properties
The settings.txt file is used to pass custom settings to the test. This is where you should enter test usernames, test passwords and environment names etc.... 
The Properties file should contain all the data required to run the tests. Test usernames, passwords and environments should be updated here from the settings file.  The settings file should be written in the following format:  

```
Environment  
Username  
Password  
Browser  
Update jira toggle  
Take screenshot on fail toggle  
Name of test Suite  
Update Slack Toggle  
```
git bash
And here is an example settings.txt  

```
SIT  
test@mailinator.com  
@&*12Af%  
Chrome  
Yes Update Jira  
Yes Screenshot  
test  
No Update Slack  
```

Also populate all the required URLs for starting browser and assert pages. If using JIRA or Slack, you will want to add the URL and/or webhook API details for the relevant instances. This should be done in /src/common/Properties.java

## Scripting
You are now ready to script tests. Example tests have been included in the src/tests folder. Note, the preferred way to click a web element is to use 

```java
CommonFunctions.clickElement(driver, "cssSelector of element");
```

This can be used to select by name/id/value etc... in the following form for maximum flexibility  

```java
CommonFunctions.clickElement(driver, "[id='elementID']");
```

#Supported Browsers
The following table shows browser support for Standard (Tests using BrowserBasePage), Docker (tests using DockerBasePage), and Proxy (tests using ProxyBasePage).

| Browser           | Standard | Docker | Proxy |
|-------------------|----------|--------|-------|
| Chrome            | Yes      | Yes    | No    |
| Firefox           | Yes      | Yes    | Yes   |
| Internet Explorer | Yes      | No     | No    |
| PhantomJS         | Yes      | No     | No    |
| HTML Unit         | Yes      | No     | No    |  
 
 To change browsers, edit the 4th Line of the settings file.

#Docker Testing

#Tips
* Use the CommonFunctions functions over the standard Selenium functions. If a functions does not exist in CommonFunctions, add it and then submit it to this project!
* Remove all hardcoded data from tests and keep it in properties. 
* Currently only Chrome is supported. To add other browsers, add the drivers to the WebDrivers folder and update the properties file.
* Build your test methods in CommonMethods and build tests in pieces. This allows for maintable tests and fast scripting of complex E2E tests
* If using Docker or Selendroid to run tests, make sure you have any required docker installed and/or that you have a real or emulated android device running.
* The DockerRun.sh script can be used to start up a dockerised selenium grid running chrome and firefox images
* Be aware that BrowserMobProxy tests will not work if there is a downstream VPN.

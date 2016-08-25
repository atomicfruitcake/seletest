package AllTests;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.TestNG;
import org.testng.collections.Lists;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author atomicfruitcake
 *
 */
public class CommonFunctions {

	private static final Logger LOGGER = Logger.getLogger(CommonFunctions.class
			.getName());

	static File directory = null;
	static String ENV = TestConfigImpl.testConfig.getEnvironment();
	
	public static void startBrowser(WebDriver driver, String url) {
		LOGGER.info("Starting browser with URL: " + url);
		driver.get(url);
		driver.manage().window().maximize();

	}

	public static String defaultReturner(String value, String defaultValue) {
		if ((value == null) || (value.isEmpty() == true)) {
			return defaultValue;
		} else {
			return value;
		}
	}

	public static String environmentSelector() {
		Map<String, String> environmentSelect = new HashMap<String, String>();
		environmentSelect.put("ENV_NAME", "ENV_URL");
		// Fill with all required environments
		return environmentSelect.get(ENV);;
	}

	public static String getIpAddressAppServer() {
		Map<String, String> ipSelect = new HashMap<String, String>();
		ipSelect.put("ENV_NAME, ", "ENV_IP");
		// FIll with all required app servers
		return ipSelect.get(ENV);
	}

	public static Map<String, String> setTestNGParameters() {
		Map<String, String> TESTPARAMETERS = new HashMap<String, String>();
		TESTPARAMETERS.put("name", "FN Test Suite");
		TESTPARAMETERS.put("preserve-order", "true");
		TESTPARAMETERS.put("parallel", "false");
		TESTPARAMETERS.put("verbose", "10");
		return TESTPARAMETERS;
	}

	static JavascriptExecutor js;

	public static void setItemInLocalStorage(String item, String value,
			WebDriver driver) {
		js = (JavascriptExecutor) driver;
		js.executeScript(String.format(
				"window.sessionStorage.setItem('%s','%s');", item, value));
	}

	public static void javascriptClickFix(WebDriver driver, WebElement element) {
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
	}

	public static void scrollToBottomOfPage(WebDriver driver) {
		js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	public static void waitForElement(WebDriver driver, String cssSelector) {
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		WebElement element = new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By
						.cssSelector(cssSelector)));
		Assert.assertTrue(element.isDisplayed());
	}

	public static void clickElement(WebDriver driver, String cssSelector) {
		waitForElement(driver, cssSelector);
		// Temporary fix, greatly reduces brittleness
		CommonFunctions.threadSleep(1);
		waitForElement(driver, cssSelector);
		WebElement element = driver.findElement(By.cssSelector(cssSelector));
		element.click();
	}

	public static void clickElementWithJs(WebDriver driver, String cssSelector) {
		waitForElement(driver, cssSelector);
		WebElement element = driver.findElement(By.cssSelector(cssSelector));
		javascriptClickFix(driver, element);
	}

	public static void clickElementByXpath(WebDriver driver,
			String cssSelector, String xpath) {
		waitForElement(driver, cssSelector);
		WebElement element = driver.findElement(By.xpath(xpath));
		element.click();
	}

	public static void clickElementById(WebDriver driver, String cssSelector,
			String id) {
		waitForElement(driver, cssSelector);
		WebElement element = driver.findElement(By.id(id));
		element.click();
	}

	public static void clickElementByName(WebDriver driver, String cssSelector,
			String name) {
		waitForElement(driver, cssSelector);
		WebElement element = driver.findElement(By.name(name));
		element.click();
	}

	public static void clickElementByClassName(WebDriver driver,
			String className, String name) {
		waitForElement(driver, className);
		WebElement element = driver.findElement(By.className(className));
		element.click();
	}

	public static void sendKeysToElement(WebDriver driver, String cssSelector,
			String keys) {
		waitForElement(driver, cssSelector);
		WebElement element = driver.findElement(By.cssSelector(cssSelector));
		element.sendKeys(keys);
	}

	public static void clearTextFromElement(WebDriver driver, String cssSelector) {
		waitForElement(driver, cssSelector);
		WebElement element = driver.findElement(By.cssSelector(cssSelector));
		element.clear();
	}

	public static void clickLinkByHref(WebDriver driver, String href) {
		List<WebElement> anchors = driver.findElements(By.tagName("a"));
		Iterator<WebElement> i = anchors.iterator();

		while (i.hasNext()) {
			WebElement anchor = i.next();
			if (anchor.getAttribute("href").contains(href)) {
				anchor.click();
				break;
			}
		}
	}

	public static void mouseHover(WebDriver driver, String cssSelector)
			throws AWTException {
		threadSleep(3);
		Point point = driver.findElement(By.cssSelector(cssSelector))
				.getLocation();
		Robot robot = new Robot();
		robot.mouseMove(point.getX(), point.getY());
	}

	public static String getCurrentUrl(WebDriver driver) {
		return driver.getCurrentUrl();
	}

	public static void sleep(int x, WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(x, TimeUnit.SECONDS);
	}

	public static void threadSleep(int x) {
		int time = x * 1000;
		try {
			Thread.sleep(time);
		} catch (InterruptedException a) {
			a.printStackTrace();
		}
	}

	public static void selectFromDropDown(WebDriver driver, String cssSelector,
			int index) {
		CommonFunctions.waitForElement(driver, cssSelector);
		WebElement dropDownListBox = driver.findElement(By
				.cssSelector(cssSelector));
		Select clickProvider = new Select(dropDownListBox);
		clickProvider.selectByIndex(index);
	}

	public static void screenshot(String name, WebDriver driver) {

		Date dDate = new Date();
		SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy.MM.dd_HHmmss");
		String screenshot_handle = name;
		File screenshot = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(screenshot, new File("screenshots\\"
					+ screenshot_handle + "_" + fullFormat.format(dDate)
					+ ".png"));
		}

		catch (Exception ex) {
			LOGGER.severe("Error creating screenshot");
			System.out.println(ex.getMessage());
			System.out.println(ex.getStackTrace());
		}
	}

	public static void pageAssert(WebDriver driver, String page) {
		LOGGER.info("Asserting current URL is equal to: " + page);
		String CurrentURL = driver.getCurrentUrl();
		Assert.assertEquals(CurrentURL, page);
	}

	public static void pageAssertFalse(WebDriver driver, String page) {
		LOGGER.info("Asserting current URL is not equal to: " + page);
		String CurrentURL = driver.getCurrentUrl();
		Assert.assertFalse(CurrentURL.equals(page));
	}

	public static boolean pageAssertBool(WebDriver driver, String page) {
		LOGGER.info("Asserting current URL is equal to: " + page);
		if (driver.getCurrentUrl() == page) {
			return true;
		} else {
			return false;
		}
	}

	public static void pageAssertTest(WebDriver driver, String page, String jiraID) throws IOException, InterruptedException {
		LOGGER.info("Asserting current URL is equal to: " + page);
		if(driver.getCurrentUrl()==page){
			LOGGER.info("Passing Ticket: " + jiraID);
			JIRAUpdater.PassTicket(jiraID);
		} else {
			LOGGER.info("Failing Ticket: " + jiraID);
			JIRAUpdater.FailTicket(jiraID);
		}
	}
	
	public static boolean verifyExists(WebDriver driver, String cssSelector) {
		waitForElement(driver, cssSelector);
		if (driver.findElement(By.cssSelector(cssSelector)) != null) {
			return true;
		} else {
			return false;
		}
	}

	public static void pageAssertTestUpdater(WebDriver driver, String page,
			String jiraID) throws IOException, InterruptedException {
		if (CommonFunctions.pageAssertBool(driver, page) == true) {
			JIRAUpdater.PassTicket(jiraID);
		}
	}

	public static boolean verifyDoesNotExist(WebDriver driver, String cssSelector) {
		if (driver.findElement(By.cssSelector(cssSelector)) == null) {
			return true;
		} else {
			return false;
		}
	}

	public static void verifyClickable(WebDriver driver, String cssSelector) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By
				.cssSelector(cssSelector)));
	}

	public static String getTextFromField(WebDriver driver, String cssSelector) {
		WebElement inputBox = driver.findElement(By.cssSelector(cssSelector));
		String textInsideInputBox = inputBox.getAttribute("value");
		return textInsideInputBox;
	}

	// Build TestNG XML file
	public static void buildTestngXml() throws IOException {
		LOGGER.info("Building TestNG XML file");
		Document document = DocumentHelper.createDocument();
		document.addDocType("suite", "SYSTEM",
				"http://testng.org/testng-1.0.dtd");
		Element suite = document.addElement("suite")
				.addAttribute("name", "testSuite")
				.addAttribute("preserve-order", "true")
				.addAttribute("parallel", "false")
				.addAttribute("verbose", "10");
		Element listeners = suite.addElement("listeners");
		listeners.addElement("listener").addAttribute("class-name",
				"org.uncommons.reportng.HTMLReporter");
		listeners.addElement("listener").addAttribute("class-name",
				"org.uncommons.reportng.JUnitXMLReporter");
		Element test = suite.addElement("test").addAttribute("name", "tests");
		Element packages = test.addElement("packages");
		for (int i = 0; i < R1_REGRESSION_PACKAGES.length; i++)  {
			packages.addElement("package").addAttribute("name",
				R1_REGRESSION_PACKAGES[i]);
			}
		FileOutputStream fos = new FileOutputStream("testng.xml");
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(fos, format);
		writer.write(document);
		writer.flush();
	}

	public static void runTestngXml() throws InterruptedException {
		LOGGER.info("Running TestNG XML file");
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		suites.add("testng.xml");
		testng.setTestSuites(suites);
		testng.run();
	}

	public static void runTestSuite(TestNG testNG) {
		testNG.run();
	}

	public static String guiOutputMethod(int lineNumber) throws IOException {
		try {
			return Files.readAllLines(Paths.get("consoleout.txt")).get(
					lineNumber);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getRandomString(int i) {
		return RandomStringUtils.randomAlphanumeric(i);
	}

	public static String getRandomUsername() {
		return getRandomString(20).toLowerCase() + "@mailinator.com";
	}
	
	public static void runTerminalCommand(String curlCommand)
			throws InterruptedException {
		// Runs a terminal command from string input
		StringBuffer output = new StringBuffer();
		try {
			Process process = Runtime.getRuntime().exec(curlCommand);
			process.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
			System.out.println(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void tunnelIntoAppServer(String shellCommand, int port)
			throws JSchException, IOException {
		LOGGER.info("Tunneling into app server to run command: " + shellCommand);
		JSch jsch = new JSch();
		Session session = jsch.getSession(APPSERVERUSERNAME, APPSERVERIP, port);
		session.setPassword(APPSERVERPASSWORD);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				channel.getInputStream()));
		channel.setCommand(shellCommand);
		channel.connect();
		String msg = null;
		while ((msg = in.readLine()) != null) {
			System.out.println(msg);
		}

		channel.disconnect();
		session.disconnect();
	}

	public static String getOS() {
		return System.getProperty("os.name");
	}

	public static String operatingSystem() {
		if (getOS().startsWith("Windows")) {
			return "Windows";
		}
		if (getOS().startsWith("Mac")) {
			return "OSX";
		} else
			return "OSX";
	}

	// Updates Slack channel as Testbot
	private static String slackWebhookAPI = SLACKWEBHOOK_API;
	private static String curlPOSTFlags = "curl -X POST --data-urlencode ";

	public static void updateSlackTestBot(String content)
			throws InterruptedException {

		String testReport = "'text': 'Test Report: ";
		String slackChannel = "";
		String payload = "\"payload={" + slackChannel + testReport + content
				+ "'}\" ";

		String testBotCurl = curlPOSTFlags + payload + slackWebhookAPI;
		System.out.println(testBotCurl);
		CommonFunctions.runTerminalCommand(testBotCurl);
	}

	public static void createDino() {
		System.out.println("       __");
		System.out.println("      /oo\\");
		System.out.println("     |    |");
		;
		System.out.println(" ^^  (vvvv)  ^^");
		System.out.println(" \\\\  /\\__/\\  //");
		System.out.println("  \\\\/      \\//");
		System.out.println("   /        \\    ");
		System.out.println("  |          |    ^");
		System.out.println("  /          \\___/ | ");
		System.out.println(" (            )     |");
		System.out.println("  \\----------/    _/");
		System.out.println("   //    \\\\_____/");
		System.out.println("   W      W");
	}

}

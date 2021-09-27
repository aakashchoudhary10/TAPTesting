package com.qainfotech.automation.tatocAutomation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Advance_Test {

	static String driverlocation = "/tatocAutomation/chromedriver.exe";
	static WebDriver driver;
	static String parentwindow;
	static String childtatocWindow;
	static Actions action;
	static String dbURL = "jdbc:mysql://10.0.1.86:3306/tatoc";
	static String Mysqlusername = "tatocuser";
	static String Mysqlpassword = "tatoc01";
	static String QueryGateName;
	static String QueryGatePassKey;
	static String SymbolName;
	static String ExpectedResultQueryGate = "Query Gate - Advanced Course - T.A.T.O.C";
	static WebDriverWait wait;
	
	@Test(priority=1)
	static void setupwebdriver() {
		System.setProperty("chromedriver", driverlocation);
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		action = new Actions(driver);
	}

	@Test(priority=2)
	static void launchQAITVPN() throws InterruptedException {
		driver.get("https://vpn.qainfotech.com");
		System.out.println(driver.getTitle());
		WebElement username_input = driver.findElement(By.xpath("//input[@id='username']"));
		username_input.sendKeys("aakashchoudhary");
		WebElement userpass_input = driver.findElement(By.xpath("//input[@id='credential']"));
		userpass_input.sendKeys("Cengage104");
		WebElement loginbtn = driver.findElement(By.xpath("//button[@id='login_button']"));
		loginbtn.click();
		Thread.sleep(2000);
		System.out.println(driver.getTitle());
	}

	@Test(priority=3)
	static void launchtatoc() throws InterruptedException {
		Thread.sleep(5000);
		WebElement quickConnection = driver.findElement(By.xpath("//span[text()='Quick Connection']"));
		quickConnection.click();
		driver.findElement(By.cssSelector("#url")).sendKeys("http://10.0.1.86/tatoc");
		parentwindow = driver.getWindowHandle();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		System.out.println(driver.getTitle());
		ArrayList<String> allwindowhandles = new ArrayList<String>(driver.getWindowHandles());
		for(String window : allwindowhandles) {
			if(window!=parentwindow) {
				driver.switchTo().window(window);
				childtatocWindow = window;
			}
		}
	}

	@Test(priority=4)
	static void AdvanceTest_Select() {
		driver.findElement(By.xpath("//a[text()='Advanced Course']")).click();
		System.out.println(driver.getTitle());
	}
	
	@Test(priority=5)
	static void HoverMenu() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println(driver.getTitle());
		action.moveToElement(driver.findElement(By.cssSelector(".menutop.m2"))).moveToElement(driver.findElement(By.xpath("//span[text()='Go Next']"))).click().build().perform();
		
	}
	
	
	/* Database Connection */
	@Test (priority=6)
	public static void dataBaseConnection() throws ClassNotFoundException, SQLException {
		SymbolName = driver.findElement(By.cssSelector("div#symboldisplay")).getText();
		System.out.println(SymbolName);
		Class.forName("com.mysql.jdbc.Driver"); //class.forName load the Driver class
		System.out.println("load the Driver class");
		Connection con = DriverManager.getConnection(dbURL, Mysqlusername, Mysqlpassword);
		System.out.println("Created Connection to Database");
		String sqlID = "select id from identity where symbol=\""+SymbolName+"\";"; //Saved Query to store testTable name in sqlStrName
		System.out.println(sqlID);
		Statement st = con.createStatement(); //You can use the Statement Object to send queries
		ResultSet rsid = st.executeQuery(sqlID);
		rsid.next();
		String ID = rsid.getString(1);
		System.out.println(ID);
		String sqlStrname = "select name from credentials where id="+ID+";"; //Saved Query to store testTable password in sqlStrPasskey
		String sqlStrpasskey = "select passkey from credentials where id="+ID+";";
		ResultSet rsName = st.executeQuery(sqlStrname);
		rsName.next();
		QueryGateName = rsName.getString(1);
		System.out.println(QueryGateName);
		ResultSet rspassword = st.executeQuery(sqlStrpasskey);
		rspassword.next();
		QueryGatePassKey = rspassword.getString(1);
		System.out.println(QueryGatePassKey);
		driver.findElement(By.cssSelector("#name")).sendKeys(QueryGateName);
		System.out.println("User Enter Name");
		driver.findElement(By.cssSelector("#passkey")).sendKeys(QueryGatePassKey);
		System.out.println("User Enter PassKey");
		driver.findElement(By.cssSelector("#submit")).click();
		System.out.println("User Click on Submit button");
	}
}

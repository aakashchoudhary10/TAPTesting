package com.qainfotech.automation.tatocAutomation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Basic_Test {

	static String driverlocation = "/tatocAutomation/chromedriver.exe";
	static WebDriver driver;
	static String B1Color;
	static String B2Color;
	static String parentwindow;
	static String childtatocWindow;
	static Actions action;

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
		Thread.sleep(2000);
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
	static void basicTest_Select() {
		driver.findElement(By.xpath("//a[text()='Basic Course']")).click();
		System.out.println(driver.getTitle());
	}

	@Test(priority=5)
	static void gridgate() throws InterruptedException {
		driver.findElement(By.xpath("//div[@class='greenbox']")).click();
		Thread.sleep(2000);
	}

	@Test(priority=6)
	static void framedungeon() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println(driver.getTitle());
		Thread.sleep(2000);
		driver.switchTo().frame(driver.findElement(By.cssSelector("#main")));
		WebElement b1 = driver.findElement(By.xpath("//div[text()='Box 1']"));
		String b1box = b1.getAttribute("class");
		B1Color = b1box;
		System.out.println("Box 1 Color:" + B1Color);
		Thread.sleep(2000);
		driver.switchTo().frame(driver.findElement(By.cssSelector("#child")));
		WebElement b2 = driver.findElement(By.xpath("//div[text()='Box 2']"));
		String b2box = b2.getAttribute("class");
		B2Color = b2box;
		System.out.println("Box 2 Color:" + B2Color);
		int n=0;
		while(n<1) {
			if (B1Color.equals(B2Color)) {
				System.out.println("Both Box Color Matched"+" Box 1 Color: "+B1Color+" Box 2 Color: "+B2Color);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(driver.findElement(By.cssSelector("#main")));
				System.out.println("Switch to Main Frame");
				driver.findElement(By.xpath("//a[text()='Proceed']")).click();
				System.out.println("Click on proceed button");
				n++;
			}
			else {
				Thread.sleep(2000);
				System.out.println("Both Box Color Not Matched"+" Box 1 Color: "+B1Color+" Box 2 Color: "+B2Color);
				driver.switchTo().defaultContent();
				driver.switchTo().frame(driver.findElement(By.cssSelector("#main")));
				System.out.println("Switch to Main Frame");
				driver.findElement(By.xpath("//a[text()='Repaint Box 2']")).click();
				System.out.println("Clicked Repaint");
				driver.switchTo().frame(driver.findElement(By.cssSelector("#child")));
				WebElement b3 = driver.findElement(By.xpath("//div[text()='Box 2']"));
				String b3box = b3.getAttribute("class");
				B2Color = b3box;
				System.out.println("Updated B2 Color: "+ B2Color);
			}
			System.out.println("Out from Else");
		}
	}

	@Test(priority=7)
	static void dragaround() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println(driver.getTitle());
		//div[@id='dropbox']
		//div[@id='dragbox']
		action.clickAndHold(driver.findElement(By.xpath("//div[@id='dragbox']"))).moveToElement(driver.findElement(By.xpath("//div[@id='dropbox']"))).release().build().perform();
		driver.findElement(By.xpath("//a[text()='Proceed']")).click();
		System.out.println("Click on proceed button");	
	}
	
	@Test(priority=8)
	static void popupwindow() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println(driver.getTitle());
		driver.findElement(By.xpath("//a[text()='Launch Popup Window']")).click();
		ArrayList<String> windowhandles = new ArrayList<String>(driver.getWindowHandles());
		for(String window:windowhandles) {
			if(window!=parentwindow|window!=childtatocWindow) {
				driver.switchTo().window(window);
			}	
		}
		driver.findElement(By.cssSelector("#name")).sendKeys("Aakash");
		driver.findElement(By.cssSelector("#submit")).click();
		driver.switchTo().window(childtatocWindow);
		driver.findElement(By.xpath("//a[text()='Proceed']")).click();
		System.out.println("Click on proceed button");	
	}
	
	@Test(priority=9)
	static void cookiehandling() throws InterruptedException {
		Thread.sleep(2000);
		System.out.println(driver.getTitle());
		driver.findElement(By.xpath("//a[text()='Generate Token']")).click();
		WebElement Tokenspan = driver.findElement(By.xpath("//span[@id='token']"));
		String Token[] = Tokenspan.getText().split(":");
		String TokenValue = Token[1].trim();
		System.out.println("Token Attribute: "+TokenValue);
		driver.manage().addCookie(new Cookie("Token", TokenValue));
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			System.out.println(cookie);
		}
		driver.findElement(By.xpath("//a[text()='Proceed']")).click();
		System.out.println("Click on proceed button");
	}
	
	@Test(priority=10)
	static void endofbasictatocCourse() throws InterruptedException {
		Thread.sleep(2000);
		String ExpectedTatocEndTitle = "End - T.A.T.O.C";
		String ActualEndTatocTitle= driver.getTitle();
		System.out.println(ActualEndTatocTitle);
		Assert.assertEquals(ActualEndTatocTitle, ExpectedTatocEndTitle);
	}
	



}

package es.codeurjc.ais.tictactoe;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SeleniumUtils {
	private static SeleniumUtils utils;
	
	public static SeleniumUtils getInstance() {
		if (utils == null)
			utils = new SeleniumUtils();
		return utils;
	}
	
	public void getUrl(WebDriver driver, String url) {
		driver.get(url);
	}
	
	public void removeWebDriver(WebDriver driver) {
		if (driver != null) {
			driver.quit();
		}
	}
	
	public void sendUserKeys(WebDriver driver, String name) {
        driver.findElement(By.id("nickname")).sendKeys(name);
        driver.findElement(By.id("startBtn")).click();
	}
	
	public void sendUserMovement(WebDriver driver, int cell) {
		  driver.findElement(By.id("cell-"+cell)).click();
	}
	
	private SeleniumUtils() {}
}

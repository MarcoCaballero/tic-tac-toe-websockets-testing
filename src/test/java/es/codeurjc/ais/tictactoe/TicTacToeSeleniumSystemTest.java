package es.codeurjc.ais.tictactoe;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.ChromeDriverManager;

@RunWith(Parameterized.class)
public class TicTacToeSeleniumSystemTest {
	private static SeleniumUtils seleniumUtils;

	private static final String SUT_HOST = "http://localhost:8080";

	private WebDriver driverOPlayer;
	private WebDriver driverXPlayer;
	private List<WebDriver> drivers = new CopyOnWriteArrayList<>();

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] values = { { new int[] { 1, 6, 3, 4, 7, 2 }, "OPlayer wins! XPlayer looses." },
				{ new int[] { 0, 1, 2, 3, 4, 5, 6 }, "XPlayer wins! OPlayer looses." },
				{ new int[] { 1, 0, 3, 2, 5, 4, 6, 7, 8 }, "Draw!" }, };

		return Arrays.asList(values);
	}

	@Parameter(0)
	public int[] moves;
	@Parameter(1)
	public String alertResult;

	@BeforeClass
	public static void beforeAll() {
		seleniumUtils = SeleniumUtils.getInstance();
		ChromeDriverManager.getInstance().setup();
		WebApp.start();
	}

	@AfterClass
	public static void afterAll() {
		WebApp.stop();
	}

	@Before
	public void beforeEach() {
		driverXPlayer = new ChromeDriver();
		drivers.add(driverXPlayer);
		driverOPlayer = new ChromeDriver();
		drivers.add(driverOPlayer);
	}

	@After
	public void afterEach() {
		seleniumUtils.removeWebDriver(driverXPlayer);
		seleniumUtils.removeWebDriver(driverOPlayer);
		drivers.clear();
	}

	@Test
	public void test() {
		seleniumUtils.getUrl(driverXPlayer, SUT_HOST);
		seleniumUtils.sendUserKeys(driverXPlayer, "XPlayer");

		seleniumUtils.getUrl(driverOPlayer, SUT_HOST);
		seleniumUtils.sendUserKeys(driverOPlayer, "OPlayer");

		for (int idx = 0; idx < moves.length; idx++) {
			seleniumUtils.sendUserMovement((idx % 2 == 0) ? driverXPlayer : driverOPlayer, moves[idx]);
		}

		for (WebDriver webDriver : drivers) {
			WebDriverWait waitPlayer = new WebDriverWait(webDriver, 10);
			waitPlayer.until(ExpectedConditions.alertIsPresent());
			assertThat(webDriver.switchTo().alert().getText().toLowerCase(), equalTo(alertResult.toLowerCase()));
		}
	}

}

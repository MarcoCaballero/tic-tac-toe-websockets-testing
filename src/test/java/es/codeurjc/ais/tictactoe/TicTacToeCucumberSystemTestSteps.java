package es.codeurjc.ais.tictactoe;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.ChromeDriverManager;

public class TicTacToeCucumberSystemTestSteps {

	private SeleniumUtils utils;
	private static final String SUT_HOST = "http://localhost:8080";

	private WebDriver driverOPlayer;
	private WebDriver driverXPlayer;
	private List<WebDriver> drivers = new CopyOnWriteArrayList<>();


	@Given("^TicTacToe is running and reachable through the browser$")
	public void tictactoe_is_running_and_reachable_through_the_browser() throws Throwable {
		utils = SeleniumUtils.getInstance();
		ChromeDriverManager.getInstance().setup();
		WebApp.start();
	}

	@When("^XPlayer has a win line$")
	public void xplayer_has_a_win_line() throws Throwable {
		int[] moves = { 0, 1, 2, 3, 4, 5, 6 };
		for (int idx = 0; idx < moves.length; idx++) {
			utils.sendUserMovement((idx % 2 == 0) ? driverXPlayer : driverOPlayer, moves[idx]);
		}
	}

	@When("^OPlayer has a win line$")
	public void oplayer_has_a_win_line() throws Throwable {
		int[] moves = { 1, 6, 3, 4, 7, 2 };
		for (int idx = 0; idx < moves.length; idx++) {
			utils.sendUserMovement((idx % 2 == 0) ? driverXPlayer : driverOPlayer, moves[idx]);
		}
	}

	@When("^anybody has a win line$")
	public void anybody_has_a_win_line() throws Throwable {
		int[] moves = { 1, 0, 3, 2, 5, 4, 6, 7, 8 };
		for (int idx = 0; idx < moves.length; idx++) {
			utils.sendUserMovement((idx % 2 == 0) ? driverXPlayer : driverOPlayer, moves[idx]);
		}

	}

	@Then("^the game displays the XPlayer wins alert$")
	public void the_game_displays_the_xplayer_wins_alert() throws Throwable {
		for (WebDriver webDriver : drivers) {
			WebDriverWait waitPlayer = new WebDriverWait(webDriver, 10);
			waitPlayer.until(ExpectedConditions.alertIsPresent());
			assertThat(webDriver.switchTo().alert().getText().toLowerCase(),
					equalTo("XPlayer wins! OPlayer looses.".toLowerCase()));
		}
		for (WebDriver webDriver : drivers) {
			utils.removeWebDriver(webDriver);
		}
		drivers.clear();
	}

	@Then("^the game displays the OPlayer wins alert$")
	public void the_game_displays_the_oplayer_wins_alert() throws Throwable {
		for (WebDriver webDriver : drivers) {
			WebDriverWait waitPlayer = new WebDriverWait(webDriver, 10);
			waitPlayer.until(ExpectedConditions.alertIsPresent());
			assertThat(webDriver.switchTo().alert().getText().toLowerCase(),
					equalTo("OPlayer wins! XPlayer looses.".toLowerCase()));
		}

		for (WebDriver webDriver : drivers) {
			utils.removeWebDriver(webDriver);
		}
		drivers.clear();

	}

	@Then("^the game displays the proper alert$")
	public void the_game_displays_the_proper_alert() throws Throwable {
		for (WebDriver webDriver : drivers) {
			WebDriverWait waitPlayer = new WebDriverWait(webDriver, 10);
			waitPlayer.until(ExpectedConditions.alertIsPresent());
			assertThat(webDriver.switchTo().alert().getText().toLowerCase(), equalTo("Draw!".toLowerCase()));
		}
		for (WebDriver webDriver : drivers) {
			utils.removeWebDriver(webDriver);
		}
		drivers.clear();

	}

	@And("^the first player to move is XPlayer$")
	public void the_first_player_to_move_is_xplayer() throws Throwable {
		driverXPlayer = new ChromeDriver();
		drivers.add(driverXPlayer);
		utils.getUrl(driverXPlayer, SUT_HOST);
		utils.sendUserKeys(driverXPlayer, "XPlayer");
	}

	@And("^the second player to move is YPlayer$")
	public void the_second_player_to_move_is_yplayer() throws Throwable {
		driverOPlayer = new ChromeDriver();
		drivers.add(driverOPlayer);
		utils.getUrl(driverOPlayer, SUT_HOST);
		utils.sendUserKeys(driverOPlayer, "OPlayer");
	}

}

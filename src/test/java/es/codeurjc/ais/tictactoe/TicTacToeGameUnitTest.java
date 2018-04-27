package es.codeurjc.ais.tictactoe;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import es.codeurjc.ais.tictactoe.TicTacToeGame.CellMarkedValue;
import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

public class TicTacToeGameUnitTest {

	private TicTacToeGame sutGame;
	private Player xPlayer;
	private Player oPlayer;
	private Connection xPlayerConnection;
	private Connection oPlayerConnection;
	private CellMarkedValue markedCell;
	private WinnerValue winner;

	private List<Connection> connections = new CopyOnWriteArrayList<>();

	@Before
	public void setUp() {
		sutGame = new TicTacToeGame();

		xPlayer = new Player(0, "X", "xPlayer");
		oPlayer = new Player(1, "O", "oPlayer");

		xPlayerConnection = mock(Connection.class);
		oPlayerConnection = mock(Connection.class);

		connections.add(xPlayerConnection);
		connections.add(oPlayerConnection);

		sutGame.addConnection(xPlayerConnection);
		sutGame.addConnection(oPlayerConnection);

		sutGame.addPlayer(xPlayer);
		sutGame.addPlayer(oPlayer);
	}

	@Test
	public void verify_playersConnection_events_test() {

		verify(xPlayerConnection, times(2)).sendEvent(eq(EventType.JOIN_GAME), argThat(hasItems(xPlayer, oPlayer)));
		verify(oPlayerConnection, times(2)).sendEvent(eq(EventType.JOIN_GAME), argThat(hasItems(xPlayer, oPlayer)));

		reset(xPlayerConnection);
		reset(oPlayerConnection);
	}

	@Test
	public void verify_markOnBoard_events_firstPut_wins_test() {

		int[] winLine = { 6, 4, 2 };
		for (int cellID = 0; cellID < 7; cellID++) {
			int playerID = (cellID % 2 == 0) ? 0 : 1;
			assertTrue(sutGame.checkTurn(playerID));
			sutGame.mark(cellID);
			
			for (Connection playerConnection : connections) {
				verifySendEventMark(playerConnection, cellID, playerID);
			}
		}

		for (Connection playerConnection : connections) {
			verifySendGameOver(playerConnection, xPlayer, winLine);
		}
		

		reset(xPlayerConnection);
		reset(oPlayerConnection);
		

		sutGame.restart();
	}

	@Test
	public void verify_markOnBoard_events_firstPut_loses_test() {

		
		int[] match = { 1, 6, 3, 4, 7, 2 };
		for (int pos = 0; pos < 6; pos++) {
			int playerID = (pos % 2 == 0) ? 0 : 1;
			assertTrue(sutGame.checkTurn(playerID));
			sutGame.mark(match[pos]);
			for (Connection playerConnection : connections) {
				verifySendEventMark(playerConnection, match[pos], playerID);
			}
		}
		int[] winLine = { 6, 4, 2 };

		for (Connection playerConnection : connections) {
			verifySendGameOver(playerConnection, oPlayer, winLine);
		}

		reset(xPlayerConnection);
		reset(oPlayerConnection);
		

		sutGame.restart();
	}

	private void verifySendEventMark(Connection connection, int cellID, int playerID) {

		ArgumentCaptor<CellMarkedValue> argument = ArgumentCaptor.forClass(CellMarkedValue.class);

		verify(connection, atLeastOnce()).sendEvent(eq(EventType.MARK), argument.capture());
		markedCell = argument.getValue();
		assertThat(markedCell.player, (playerID == xPlayer.getId()) ? equalTo(xPlayer) : equalTo(oPlayer));
		assertThat(markedCell.cellId, equalTo(cellID));

	}

	private void verifySendGameOver(Connection connection, Player winPlayer, int[] winLine) {

		ArgumentCaptor<WinnerValue> winnerArgument = ArgumentCaptor.forClass(WinnerValue.class);

		verify(connection, atLeastOnce()).sendEvent(eq(EventType.GAME_OVER), winnerArgument.capture());
		winner = winnerArgument.getValue();
		assertThat(winner.player, equalTo(winPlayer));
		assertThat(winner.pos, equalTo(winLine));
	}

}

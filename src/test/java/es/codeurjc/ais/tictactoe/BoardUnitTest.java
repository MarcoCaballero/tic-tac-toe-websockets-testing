package es.codeurjc.ais.tictactoe;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.codeurjc.ais.tictactoe.TicTacToeGame.Cell;

public class BoardUnitTest {

	private Board sutBoard;

	@Before
	public void setUp() {
		sutBoard = new Board();
		sutBoard.enableAll();
	}

	@Test
	public void anybodyWinsYet() {
		assertNull(sutBoard.getCellsIfWinner("O"));
		assertNull(sutBoard.getCellsIfWinner("X"));
		assertFalse(sutBoard.checkDraw());
	}

	@Test
	public void playerWithLabelXWins() {
		int[] winningLine = this.setBoardCellsToWinX().getWinningLine();
		assertThat(sutBoard.getCellsIfWinner("X"), equalTo(winningLine));
		assertNull(sutBoard.getCellsIfWinner("O"));
		assertFalse(sutBoard.checkDraw());
	}

	@Test
	public void playerWithLabelYWins() {
		int[] winningLine = this.setBoardCellsToWinY().getWinningLine();
		assertNull(sutBoard.getCellsIfWinner("X"));
		assertThat(sutBoard.getCellsIfWinner("O"), equalTo(winningLine));
		assertFalse(sutBoard.checkDraw());
	}

	@Test
	public void gameResultAsDraw() {
		setBoardCellsToDraw();
		assertNull(sutBoard.getCellsIfWinner("O"));
		assertNull(sutBoard.getCellsIfWinner("X"));
		assertTrue(sutBoard.checkDraw());
	}

	private void setBoardCellsToDraw() {
		String match[] = { "O", "X", "O", "X", "O", "X", "X", "O", "X" };
		setBoardCells(match);
	}

	private BoardUnitTest setBoardCellsToWinX() {
		String match[] = { "X", "X", "O", "X", "O", "O", "X" };
		setBoardCells(match);
		return this;
	}

	private BoardUnitTest setBoardCellsToWinY() {
		String match[] = { "O", "O", "X", "O", "X", "X", "O" };
		setBoardCells(match);
		return this;
	}
	
	private void setBoardCells(String match[]) {
		for (int cellID = 0; cellID < match.length; cellID++) {
			Cell cell = sutBoard.getCell(cellID);
			cell.value = match[cellID];
			cell.active = true;
		}
	}

	private int[] getWinningLine() {
		return new int[] { 0, 3, 6 };
	}

}

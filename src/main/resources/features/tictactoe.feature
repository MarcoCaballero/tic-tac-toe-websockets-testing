Feature: tic-tac-toe-e2e

As player I want to play against other player and check the results.

Background: 
    Given TicTacToe is running and reachable through the browser
    And the first player to move is XPlayer
    And the second player to move is YPlayer

Scenario: The first play, XPlayer, wins
	
    When XPlayer has a win line
    Then the game displays the XPlayer wins alert
    
Scenario: The second player, OPlayer, wins
	
    When OPlayer has a win line
    Then the game displays the OPlayer wins alert
    
Scenario: The game ends with Draw
	
    When anybody has a win line
    Then the game displays the proper alert
# Tasks

## Initialize the game

Implement the Game constructor to initialize empty n-by-m board, that is, an array filled with zeros. The inner arrays are rows.

- An array of integer arrays (representing an array of rows) is created as the board, and stored as a private member of the Game instance.
- Every cell of the board is initialized with 0.
- Getter getBoard() and setter setBoard() are connected to the stored board.
- The rows of the board are independent (that is, changing one row does not affect the others).

## Get player move

Implement getMove() that asks for user input and returns the coordinates of a valid move on board.
- Coordinates are specified as a letter and a number, for example, A2 is the first row of the second column, and C1 is the third row of the first column.
- The function returns an array of integers containing the row and column number corresponding to the input.
- The returned coordinates start from 0.
- The integers indicate a valid (empty) position on the board.
- If the user provides coordinates that are outside of board, keep asking.
- If the user provides coordinates for a place that is taken, keep asking.
- If the user provides input that does not follow the format of coordinates, keep asking.

## Implement making a move

Implement mark() that writes the value of player (a 1 or 2) into the row & col element of the board.

- If the cell at row and col is empty, it is marked with player.
- It does not do anything if the coordinates are out of bounds.
- It does not do anything if the cell is already marked.

## Check for winners

Implement hasWon() that returns true if player (of value 1 or 2) has howMany marks in a row on the board.

- If player has howMany marks in a row on the board, true is returned.
- If player does not have howMany marks in a row on the board, false is returned.

## Check for a full board

Implement isFull() that returns true if the board is full.

- If there are no empty fields on the board, true is returned.
- If there are empty fields on the board, false is returned.

## Print board

Implement printBoard() that prints the board to the screen.

- Players 1 and 2 are indicated with X and O, and empty fields are indicated with dots (.).
- There are coordinates displayed around the board.
- A 3-by-8 board is displayed in the following format.

```
1 2 3 4 5 6 7 8
A . . . . . . . .
B . . . . . . . .
C . . . . . . . .
```

## Print result

Implement printResult() that displays the result of the game.

- If player 1 wins, print "X won!"
- If player 2 wins, print "O won!"
- If nobody wins, print "It's a tie!"

## Game logic

Use the implemented methods to write a @play() method that runs a whole two-player game. Parameter howMany sets the win condition of the game.

- Player 1 starts the game.
- Players alternate their moves (1, 2, 1, 2...).
- The game uses howMany to set the win condition.
- The board is displayed before each move and also at the end of game.
- The game ends when someone wins or if the board is full.
- The game handles bad input (wrong coordinates) without crashing.

## Quit game

Allow players to quit the game anytime by typing quit.

- When the player types quit instead of coordinates, the program exits.
package org.connect4;

import java.util.Arrays;

public class Board {
  // These are the values that each cell can have
  public static enum TokenType {
    NONE,
    RED,
    YELLOW,
  };

  // These are win/board states
  public static enum BoardState {
    NONE,
    RED_WIN,
    YELLOW_WIN,
    TIE
  };

  // Board dimensions
  // NOTE: lower row indices are higher on the board
  public static final int ROWS = 6;
  public static final int COLS = 7;

  public static final int WIN_LENGTH = 4;

  private TokenType[][] board;

  // Default constructor
  public Board() {
    board = new TokenType[ROWS][COLS];

    // Fills the board with empty states
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        board[row][col] = TokenType.NONE;
      }
    }
  }

  // Copy constructor
  public Board(Board other) {
    this.board = other.getBoard();
  }

  // Constructs the board based off of an array
  // 'R' is red and "Y" is yellow. All other characters are empty
  public Board(char[][] charBoard) {
    if (charBoard.length != ROWS)
      throw new IllegalArgumentException("The input board has an incorret number of rows");

    // Because the sub-arrays can have different lengths, one must check every row
    for (int row = 0; row < ROWS; row++) {
      if (charBoard[row].length != COLS)
        throw new IllegalArgumentException("The input board has an incorret number of columns");
    }

    board = new TokenType[ROWS][COLS];
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        switch (charBoard[row][col]) {
          case 'R': {
            board[row][col] = TokenType.RED;
            break;
          }
          case 'Y': {
            board[row][col] = TokenType.YELLOW;
            break;
          }
          default: {
            board[row][col] = TokenType.NONE;
            break;
          }
        }
      }
    }
  }

  // Returns true if column col is full and cannot be added to
  public boolean isColumnFull(int col) {
    return board[0][col] != TokenType.NONE;
  }

  // Drops a token on a column
  public void dropToken(int col, TokenType token) {
    // Ensure that the arguments are valid
    if (token == TokenType.NONE)
      throw new IllegalArgumentException("An empty token cannot be dropped");
    if (col < 0 || col >= COLS)
      throw new IllegalArgumentException("This column does not exist on the board");

    // If the token is full, tokens cannot be dropped
    if (isColumnFull(col))
      throw new IllegalStateException("Cannot drop token: column is full");

    // Finds the first empty row and inserts
    for (int row = ROWS - 1; row >= 0; row--) {
      if (board[row][col] == TokenType.NONE) {
        board[row][col] = token;
        return;
      }
    }
  }

  // Pulls a token out of the column
  public void pullToken(int col) {
    if (col < 0 || col >= COLS)
      throw new IllegalArgumentException("This column does not exist on the board");

    // If the column is empty, tokens cannot be removed
    if (board[ROWS - 1][col] == TokenType.NONE)
      throw new IllegalStateException("Cannot pull token: column is empty");

    // Finds the first filled row and removes the token
    for (int row = 0; row < ROWS; row++) {
      if (board[row][col] != TokenType.NONE) {
        board[row][col] = TokenType.NONE;
        return;
      }
    }
  }

  // Returns the win state of the board
  public BoardState getBoardState() {

    for (int row = ROWS - 1; row > 0; row--) {
      for (int col = 0; col < COLS; col++) {
        // This is the type that will be checked
        TokenType currentType = board[row][col];

        // Ensure that type NONE can't win
        if (currentType == TokenType.NONE)
          continue;

        boolean validRow = false;
        boolean validCol = false;
        boolean validNeg = false;
        boolean validPos = false;

        // Check for horizontal wins
        if (col <= COLS - WIN_LENGTH) {
          validRow = true;
          for (int offset = 0; offset < WIN_LENGTH; offset++) {

            if (board[row][col + offset] != currentType) {
              validRow = false;
            }
          }
        }

        // Check for vertical wins
        if (row <= ROWS - WIN_LENGTH) {
          validCol = true;
          for (int offset = 0; offset < WIN_LENGTH; offset++) {

            if (board[row + offset][col] != currentType) {
              validCol = false;
            }
          }
        }

        // Check for diagonal negative wins
        if (col <= COLS - WIN_LENGTH && row <= ROWS - WIN_LENGTH) {
          validNeg = true;
          for (int offset = 0; offset < WIN_LENGTH; offset++) {

            if (board[row + offset][col + offset] != currentType) {
              validNeg = false;
            }
          }
        }

        // Check for diagonal positive wins
        if (col >= WIN_LENGTH - 1 && row <= ROWS - WIN_LENGTH) {
          validPos = true;
          for (int offset = 0; offset < WIN_LENGTH; offset++) {

            if (board[row + offset][col - offset] != currentType) {
              validPos = false;
            }
          }

        }

        // If a win has occured, return that
        if (validRow || validCol || validNeg || validPos) {
          return currentType == TokenType.RED ? BoardState.RED_WIN : BoardState.YELLOW_WIN;
        }
      }
    }

    // Check for a full board for tie
    boolean fullBoard = true;
    for (int col = 0; col < COLS; col++) {
      if (!isColumnFull(col)) {
        fullBoard = false;
      }
    }

    return fullBoard ? BoardState.TIE : BoardState.NONE;
  }

  // Returns a deep copy of the 2d array board
  public TokenType[][] getBoard() {
    TokenType[][] copy = new TokenType[board.length][];
    for (int i = 0; i < board.length; i++) {
      copy[i] = board[i].clone();
    }
    return copy;
  }

  // Draws the board to the console
  public void display() {
    // Escape codes
    final String RESET = "\u001B[0m";
    final String RED = "\u001B[31m";
    final String YELLOW = "\u001B[33m";
    final String CLEAR = "\033[H\033[2J";

    // Clear the console
    System.out.print(CLEAR);

    String result = "";

    // Adds the numbers on top
    for (int col = 0; col < COLS; col++) {
      result += " " + (col + 1) + " ";
    }
    result += "\n";

    // Creates the grid
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        switch (board[row][col]) {
          case TokenType.RED: {
            result += RED + " O " + RESET;
            break;
          }
          case TokenType.YELLOW: {
            result += YELLOW + " O " + RESET;
            break;
          }
          case TokenType.NONE: {
            result += " . ";
            break;
          }
        }
      }
      result += "\n";
    }

    System.out.println(result);
    System.out.flush();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Board))
      return false;
    Board b = (Board) o;
    return Arrays.deepEquals(this.board, b.board);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(board);
  }
}

package org.connect4;

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
  private final int rows = 6;
  private final int cols = 7;

  private TokenType[][] board;

  // Default constructor
  public Board() {
    board = new TokenType[6][7];

    // Fills the board with empty states
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        board[row][col] = TokenType.NONE;
      }
    }
  }

  // Copy constructor
  public Board(Board other) {
    this.board = other.getBoard().clone();
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
    if (col < 0 || col >= cols)
      throw new IllegalArgumentException("This column does not exist on the board");

    // If the token is full, tokens cannot be dropped
    if (isColumnFull(col))
      throw new IllegalStateException("Cannot drop token: column is full");

    // Finds the first empty row and inserts
    for (int row = rows - 1; row >= 0; row--) {
      if (board[row][col] == TokenType.NONE) {
        board[row][col] = token;
        return;
      }
    }
  }

  // Pulls a token out of the column
  public void pullToken(int col) {
    if (col < 0 || col >= cols)
      throw new IllegalArgumentException("This column does not exist on the board");

    // If the column is empty, tokens cannot be removed
    if (board[rows - 1][col] == TokenType.NONE)
      throw new IllegalStateException("Cannot pull token: column is empty");

    // Finds the first filled row and removes the token
    for (int row = 0; row < rows; row++) {
      if (board[row][col] != TokenType.NONE) {
        board[row][col] = TokenType.NONE;
        return;
      }
    }
  }

  // Returns the win state of the board
  public BoardState getBoardState() {

    // The amount in a row required to win
    final int winLength = 4;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
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
        if (col <= cols - winLength) {
          validRow = true;
          for (int offset = 0; offset < winLength; offset++) {

            if (board[row][col + offset] != currentType) {
              validRow = false;
            }
          }
        }

        // Check for vertical wins
        if (row <= rows - winLength) {
          validCol = true;
          for (int offset = 0; offset < winLength; offset++) {

            if (board[row + offset][col] != currentType) {
              validCol = false;
            }
          }
        }

        // Check for diagonal negative wins
        if (col <= cols - winLength && row <= rows - winLength) {
          validNeg = true;
          for (int offset = 0; offset < winLength; offset++) {

            if (board[row + offset][col + offset] != currentType) {
              validNeg = false;
            }
          }
        }

        // Check for diagonal positive wins
        if (col >= winLength && row <= rows - winLength) {
          validPos = true;
          for (int offset = 0; offset < winLength; offset++) {

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
    for (int col = 0; col < cols; col++) {
      if (!isColumnFull(col)) {
        fullBoard = false;
      }
    }

    return fullBoard ? BoardState.TIE : BoardState.NONE;
  }

  // Returns the 2d array board
  public TokenType[][] getBoard() {
    return board;
  }

  // Gets the amount of columns in the board
  public int getCols() {
    return cols;
  }

  // Draws the board to the console
  public void display() {
    // Escape codes
    final String RESET = "\u001B[0m";
    final String RED = "\u001B[31m";
    final String YELLOW = "\u001B[33m";

    String result = "";

    // Adds the numbers on top
    for (int col = 0; col < cols; col++) {
      result += " " + (col + 1) + " ";
    }
    result += "\n";

    // Creates the grid
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
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
  }
}

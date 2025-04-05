package org.connect4;

public class Bot {

  Board.TokenType botToken;
  Board.TokenType oppToken;

  Board board;

  public Bot(Board.TokenType playingAs) {
    if (playingAs == Board.TokenType.NONE)
      throw new IllegalArgumentException("The bot cannot be playing as none");

    botToken = playingAs;
    oppToken = playingAs == Board.TokenType.RED ? Board.TokenType.YELLOW : Board.TokenType.RED;
  }

  // Returns the bot's chosen column to drop
  public int getMove(Board gameBoard) {
    // Create a copy board to be able to modify
    board = new Board(gameBoard);

    // Iterates over all possible moves and
    int bestCol = 0;
    int bestScore = Integer.MIN_VALUE;
    for (int col = 0; col < board.getCols(); col++) {
      if (board.isColumnFull(col))
        continue;

      // Place test move
      board.dropToken(col, botToken);

      int score = minimax(false, 7);
      if (score > bestScore) {
        bestScore = score;
        bestCol = col;
      }

      // Revert change to board
      board.pullToken(col);
    }

    return bestCol;
  }

  // Returns the score of the branch
  private int minimax(boolean botTurn, int depth) {

    if (depth == 0) {
      return 0;
    }

    switch (board.getBoardState()) {
      case Board.BoardState.RED_WIN: {
        return botToken == Board.TokenType.RED ? 2 : -1;
      }
      case Board.BoardState.YELLOW_WIN: {
        return botToken == Board.TokenType.YELLOW ? 2 : -1;
      }
      case Board.BoardState.TIE: {
        return 1;
      }
      case Board.BoardState.NONE: {
        break; // Ts is just for LSP to shut it
      }
    }

    int bestScore = botTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (int col = 0; col < board.getCols(); col++) {
      if (board.isColumnFull(col))
        continue;

      board.dropToken(col, botTurn ? botToken : oppToken);

      int score = minimax(!botTurn, depth - 1);

      // bot tries to maximize score, opp tries to minimize
      if ((botTurn && score > bestScore) || (!botTurn && score < bestScore)) {
        bestScore = score;
      }

      board.pullToken(col);
    }

    return bestScore;
  }

}

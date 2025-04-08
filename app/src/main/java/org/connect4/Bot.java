package org.connect4;

public class Bot {

  Board.TokenType botToken;
  Board.TokenType oppToken;

  final int SEARCH_DEPTH = 7;
  final int BOT_WIN_SCORE = Integer.MAX_VALUE - SEARCH_DEPTH - 1;
  final int BOT_LOSS_SCORE = Integer.MIN_VALUE;

  Board board;

  int[] checkOrder = { 3, 2, 4, 1, 5, 0, 6 };
  int checkCount = 0;

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

    checkCount = 0;

    // Iterates over all possible moves and
    int bestCol = 0;
    int bestScore = Integer.MIN_VALUE;
    for (int i = 0; i < checkOrder.length; i++) {
      int col = checkOrder[i];
      if (board.isColumnFull(col))
        continue;

      // Place test move
      board.dropToken(col, botToken);

      int score = minimax(false, SEARCH_DEPTH);
      if (score > bestScore) {
        bestScore = score;
        bestCol = col;
      }

      // Revert change to board
      board.pullToken(col);
    }

    System.out.println("<BOT> Variations Checked: " + checkCount);
    System.out.println("<BOT> Best Possible Score: " + bestScore);

    return bestCol;
  }

  // Returns the score of the branch
  private int minimax(boolean botTurn, int depth) {

    if (depth == 0) {
      checkCount++;
      return 0;
    }

    switch (board.getBoardState()) {
      case Board.BoardState.RED_WIN: {
        return botToken == Board.TokenType.RED ? BOT_WIN_SCORE : BOT_LOSS_SCORE;
      }
      case Board.BoardState.YELLOW_WIN: {
        return botToken == Board.TokenType.YELLOW ? BOT_WIN_SCORE : BOT_LOSS_SCORE;
      }
      case Board.BoardState.TIE: {
        return 50;
      }
      case Board.BoardState.NONE: {
        break; // Ts is just for LSP to shut it
      }
    }

    int bestScore = botTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;

    for (int i = 0; i < checkOrder.length; i++) {
      int col = checkOrder[i];

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

    // Weight losing later higher
    if (bestScore < 0) {
      bestScore++;
    }

    return bestScore;
  }
}

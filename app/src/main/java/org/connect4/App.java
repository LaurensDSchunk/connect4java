package org.connect4;

import java.util.Scanner;

public class App {
  private static Board board = new Board();
  private static Scanner scanner = new Scanner(System.in);
  private static Bot bot = new Bot(Board.TokenType.RED);

  public static void main(String[] args) {
    // Main game loop
    while (board.getBoardState() == Board.BoardState.NONE) {
      board.display();

      board.dropToken(getColumnInput(), Board.TokenType.YELLOW);
      if (board.getBoardState() != Board.BoardState.NONE)
        break;

      board.dropToken(bot.getMove(board), Board.TokenType.RED);
    }

    // Game over display
    board.display();

    switch (board.getBoardState()) {
      case Board.BoardState.NONE: {
        throw new RuntimeException("How does this even happen lol");
      }
      case Board.BoardState.TIE: {
        System.out.println("It's a tie!");
        break;
      }
      case Board.BoardState.RED_WIN: {
        System.out.println("Red wins!");
        break;
      }
      case Board.BoardState.YELLOW_WIN: {
        System.out.println("Yellow wins!");
        break;
      }
    }
  }

  public static int getColumnInput() {
    int number = -1;

    while ((number < 1 || number > 7)) {
      System.out.print("Enter a column 1-7: ");
      if (scanner.hasNextInt()) {
        number = scanner.nextInt();
        if (number < 1 || number > 7) {
          System.out.println("Please enter a number between 1 and 7.");
          continue;
        }
        if (board.isColumnFull(number - 1)) {
          System.out.println("That column is full!");
          number = -1;
        }
      } else {
        System.out.println("Invalid input. Please enter a number between 1 and 7.");
        scanner.next(); // Consume and discard the invalid token
      }
    }

    return number - 1;
  }

  public static void testBoard() {
    char[][] testLayout = {
        { ' ', ' ', 'R', 'R', ' ', ' ', ' ' },
        { ' ', 'Y', 'R', 'R', ' ', ' ', ' ' },
        { 'Y', 'Y', ' ', ' ', 'R', ' ', ' ' },
        { 'R', ' ', ' ', ' ', ' ', 'R', ' ' },
        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
        { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
    };

    Board testBoard = new Board(testLayout);

    System.out.println(testBoard.getBoardState());

  }
}

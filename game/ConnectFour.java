package game;

import java.util.Scanner;

public class ConnectFour {
    public static void main (String[] args) {
        // create 
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        AI ai = new AI(board);

        // selecting game mode
        Boolean selected = false;
        Boolean aibool = false;
        while (!selected) {
            // choose game mode
            System.out.println("Please select a mode by entering its number:");
            System.out.println("1) AI Opponent");
            System.out.println("2) Human Opponent");
            // get response
            String s = scanner.nextLine();
            switch (s) {
                case "1":
                    selected = true;
                    // create new AI opponent
                    aibool = true;
                    System.out.println("Please select the difficulty of the AI: (1-10)");
                    while (true) {
                        String ailevel = scanner.nextLine();
                        try {
                            int aiNum = Integer.parseInt(ailevel);
                            if ((aiNum > 0) && (aiNum < 11)) {
                                ai.setDifficulty(aiNum);
                                break;
                            }
                            // didn't break, assume invalid
                            System.out.println("Invalid input. Please enter a number between 1 and 10.");
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please enter a number between 1 and 10.");
                        }
                    }
                    break;
                case "2":
                    selected = true;
                    // opponent another player
                    break;
                default:
                    System.out.println("Invalid input. Please enter either 1 or 2.");
            }
        }

        // start game & main game loop
        Boolean gameComplete = false;
        Boolean playerOneTurn = true;
        while (!gameComplete) {
            // check for full board before starting the next turn
            if (board.checkForTie()) {
                System.out.println("TIE GAME!");
                break;
            }
            if (playerOneTurn) {
                String startMessage = (aibool) ? "Your turn! " : "Player 1's turn! ";
                System.out.println(startMessage + "Select a column to place a token.");
                
            } else {
                if (!aibool) {
                    System.out.println("Player 2's turn! Select a column to place a token.");
                } else {
                    // ai's turn
                    int difficulty = ai.getDifficulty();
                    if (difficulty > 7) {
                        System.out.println("The AI's turn! (This may take a few seconds)");
                    } else {
                        System.out.println("The AI's turn!");
                    }
                    
                    
                    int aiMoveCol = ai.getMove(board.board, playerOneTurn);
                    board.place(aiMoveCol, playerOneTurn);
                    System.out.println("The AI places a token in column " + aiMoveCol + ".");
                    // board.checkForWin just for specifically ai
                    if (board.checkForWin(aiMoveCol, playerOneTurn)) {
                        System.out.println("THE AI WINS!");
                        board.printBoard();
                        gameComplete = true;
                        break;
                    } else {
                        // game incomplete, go to player's turn
                        playerOneTurn = !playerOneTurn;
                        continue;
                    }
                }
                
            }
            // game board is printed
            board.printBoard();

            // player makes a move by typing coords
            Boolean validInput = false;

            int moveCol=0;
            while (!validInput) {
                try {
                    int response = Integer.parseInt(scanner.nextLine());
                    if (response >= 0 && response < board.getNumCols()) {
                        if (board.place(response, playerOneTurn)) {
                            moveCol = response;
                            validInput = true;
                        } else {
                            System.out.println("Invalid response, that column is full.");
                        }
                            
                    } else {
                        System.out.println("Invalid response, use a valid number.");
                    }
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid response, use a valid number.");
                }
                
            }

            if (board.checkForWin(moveCol, playerOneTurn)) {
                // the current player just won the game!
                gameComplete = true;
                if (playerOneTurn) {
                    if (aibool) {
                        System.out.println("YOU WIN!");
                    } else {
                        System.out.println("PLAYER 1 WINS!");
                    }
                } else {
                    if (!aibool) {
                        System.out.println("PLAYER 2 WINS!");
                    }
                    // ai's check for win above in its turn
                }
                board.printBoard();
                
            } else {
                // game incomplete, go to next turn
                playerOneTurn = !playerOneTurn;
            }
        }
        System.out.println("Thank you for playing!");
        scanner.close();
    }
}

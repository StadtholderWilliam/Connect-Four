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
                    System.out.println("(Difficulties 7-10 are noticeably slower)");
                    while (true) {
                        String ailevel = scanner.nextLine();
                        try {
                            int ainum = Integer.parseInt(ailevel);
                            if ((ainum > 0) && (ainum < 11)) {
                                ai.setdifficulty(ainum);
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
        Boolean gamecomplete = false;
        Boolean playeroneturn = true;
        while (!gamecomplete) {
            // check for full board before starting the next turn
            if (board.checkfortie()) {
                System.out.println("TIE GAME!");
                break;
            }
            if (playeroneturn) {
                String startmessage = (aibool) ? "Your turn! " : "Player 1's turn! ";
                System.out.println(startmessage + "Select a column to place a token.");
                
            } else {
                if (!aibool) {
                    System.out.println("Player 2's turn! Select a column to place a token.");
                } else {
                    // ai's turn
                    int difficulty = ai.getdifficulty();
                    if (difficulty > 6) {
                        if (difficulty > 8) {
                            if (difficulty > 9) {
                                System.out.println("The AI's turn! (This may take several minutes)");
                            } else {
                                System.out.println("The AI's turn! (This may take about half a minute)");
                            }
                        } else {
                            System.out.println("The AI's turn! (This may take a few seconds)");
                        }
                        
                    } else {
                        System.out.println("The AI's turn!");
                    }
                    
                    
                    int aimovecol = ai.getmove(board.board, playeroneturn);
                    board.place(aimovecol, playeroneturn);
                    System.out.println("The AI places a token in column " + aimovecol + ".");
                    // board.checkforwin just for specifically ai
                    if (board.checkforwin(aimovecol, playeroneturn)) {
                        System.out.println("THE AI WINS!");
                        board.printboard();
                        gamecomplete = true;
                        break;
                    } else {
                        // game incomplete, go to player's turn
                        playeroneturn = !playeroneturn;
                        continue;
                    }
                }
                
            }
            // game board is printed
            board.printboard();

            // player makes a move by typing coords
            Boolean validinput = false;

            int movecol=0;
            while (!validinput) {
                try {
                    int response = Integer.parseInt(scanner.nextLine());
                    if (response >= 0 && response < board.getnumcols()) {
                        if (board.place(response, playeroneturn)) {
                            movecol = response;
                            validinput = true;
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

            if (board.checkforwin(movecol, playeroneturn)) {
                // the current player just won the game!
                gamecomplete = true;
                if (playeroneturn) {
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
                board.printboard();
                
            } else {
                // game incomplete, go to next turn
                playeroneturn = !playeroneturn;
            }
        }
        System.out.println("Thank you for playing!");
        scanner.close();
    }
}

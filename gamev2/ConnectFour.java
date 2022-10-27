package gamev2;

public class ConnectFour {

    Board board;
    AI ai;
    boolean aiOpponent;
    boolean playerTurn;

    public ConnectFour(int rows, int cols, int win, boolean aibool, int difficulty) {
        // create board, ai
        board = new Board(rows, cols, win);
        ai = new AI(board, difficulty);
        aiOpponent = aibool;
        playerTurn = true;

        // main game loop
    }

    public int placeMove(int col) {
        // expecting error checking to take place in GUI before calling this
        for (int i=board.getNumRows()-1; i>=0; i--) {
            if (board.board[i][col] == board.emptyChar) {
                // a little bit redundant, but we need the row of the tile above
                board.place(col, playerTurn);
                // returning the row above it so the GUI knows where to mark the next spot
                return i-1;
            }
        }
        // this column is full and should not have anything placed.
        System.out.println("GAME ERROR - PLACING IN INVALID COLUMN");
        return -1;
    }

    public boolean getPlayerTurn() {
        return playerTurn;
    }

    int aiMove() {
        // get ai's move
        int aiMoveCol = ai.getMove(board.board, playerTurn);
        board.place(aiMoveCol, playerTurn);
        return aiMoveCol;
    }

    GameState endTurn(int placeCol) {
        // want to return the board, whether the game is complete, and who's next
        // as soon as there's a winning move, that'll handle itself
        boolean complete = board.checkForTie();
        boolean win = board.checkForWin(placeCol, playerTurn);
        // flip player turn, turn has ended
        playerTurn = !playerTurn;
        return new GameState(complete, win);
    }

    public class GameState {
        // helper class for returning to gui
        boolean tiedGame;
        boolean wonGame;

        GameState(boolean tied, boolean won) {
            tiedGame = tied;
            wonGame = won;
        }
    }
}

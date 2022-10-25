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

        // main game loop
    }

    BoardState getBoardState() {
        // want to return the board, whether the game is complete, and who's next
        // as soon as there's a winning move, that'll handle itself
        boolean complete = board.checkForTie();
        BoardState state = new BoardState(board.board, complete, playerTurn);
        return state;

    }

    public class BoardState {
        // helper class for returning to gui
        char[][] curBoard;
        boolean gameComplete;
        boolean playerTurn;

        BoardState(char[][] board, boolean complete, boolean player) {
            curBoard = board;
            gameComplete = complete;
            playerTurn = player;
        }
    }
}

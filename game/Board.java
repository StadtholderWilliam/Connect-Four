package game;

public class Board {
    char[][] board;
    private int numRows, numCols, winNum;
    public char player1Char, player2Char;

    public Board() {
        numRows = 6;
        numCols = 7;
        winNum = 4;
        player1Char = 'X';
        player2Char = 'O';
        board = new char[numRows][numCols];
        // set up empty board
        for (int i=0; i<numRows;i++) {
            for (int j=0; j<numCols;j++) {
                board[i][j] = '-';
            }
        }
    }

    public int getNumCols() {
        return numCols;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getWinNum() {
        return winNum;
    }

    public void printBoard() {
        for (int i=0; i<numCols; i++) {
            System.out.print('_');
        }
        System.out.print('\n');
        // NOTE: if numCols is ever 2 or more digits, there will be visual errors.
        for (int i=0; i<numRows; i++) {
            System.out.println(board[i]);
        }
        for (int i=0; i<numCols; i++) {
            System.out.print('_');
        }
        System.out.print('\n');
        for (int i=0; i<numCols; i++) {
            System.out.print(i);
        }
        System.out.print('\n');
    }

    // return: boolean of whether target column can be successfully placed.
    // bool playerturn: true if player 1's turn, false if player 2's turn.
    public boolean place(int column, boolean playerTurn) {
        for (int i=numRows-1; i>=0; i--) {
            if (board[i][column] == '-') {
                board[i][column] = (playerTurn) ? player1Char : player2Char;
                return true;
            }
        }
        return false;
    }

    // checks whether the last piece placed down 
    // return: boolean of whether given player won
    public boolean checkForWin(int column, boolean playerTurn) {
        char token = (playerTurn) ? player1Char : player2Char;
        // check each direction from point of change rather than eval entire board
        int row = 0;
        // find the row of most recently placed token
        for (int i=0; i<numRows; i++) {
            if (board[i][column] == token) {
                row = i;
                break;
            }
        }

        // check left-right
        int count = 0;
        for (int len=0; len<winNum; len++) {
            // check left of
            if (column-len>=0) {
                if (board[row][column-len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        // len starting at 1 so it doesn't count starting spot again
        for (int len=1; len<winNum; len++) {
            // check right of
            if (column+len<numCols) {
                if (board[row][column+len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        if (count >= winNum) {
            return true;
        }

        // check down (no need for up, this will be the highest placed token)
        count = 0;
        for (int len=0; len<winNum; len++) {
            if (row+len<numRows) {
                if (board[row+len][column] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        if (count >= winNum) {
            return true;
        }

        // check top-left to bot-right diagonal
        count = 0;
        for (int len=0; len<winNum; len++) {
            // check left and up
            if ((row-len >= 0) && (column-len >= 0)) {
                if (board[row-len][column-len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        // len starting at 1 so it doesn't count starting spot again
        for (int len=1; len<winNum; len++) {
            // check right and down
            if ((row+len<numRows) && (column+len<numCols)) {
                if (board[row+len][column+len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        if (count >= winNum) {
            return true;
        }

        // check bot-left to top-right diagonal
        count = 0;
        for (int len=0; len<winNum; len++) {
            // check left and down
            if ((row+len<numRows) && (column-len >= 0)) {
                if (board[row+len][column-len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        // len starting at 1 so it doesn't count starting spot again
        for (int len=1; len<winNum; len++) {
            // check right and up
            if ((row-len >= 0) && (column+len<numCols)) {
                if (board[row-len][column+len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        if (count >= winNum) {
            return true;
        }

        return false;
    }

    public boolean checkForTie() {
        for (int i=0; i<numCols; i++) {
            if (board[0][i] == '-') {
                // board still has an empty space, not tied.
                return false;
            }
        }
        return true;
    }
}

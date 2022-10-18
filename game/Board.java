package game;

public class Board {
    char[][] board;
    private int numrows, numcols, winnum;
    public char player1char, player2char;

    public Board() {
        numrows = 6;
        numcols = 7;
        winnum = 4;
        player1char = 'X';
        player2char = 'O';
        board = new char[numrows][numcols];
        // set up empty board
        for (int i=0; i<numrows;i++) {
            for (int j=0; j<numcols;j++) {
                board[i][j] = '-';
            }
        }
    }

    public int getnumcols() {
        return numcols;
    }

    public int getnumrows() {
        return numrows;
    }

    public int getwinnum() {
        return winnum;
    }

    public void printboard() {
        for (int i=0; i<numcols; i++) {
            System.out.print('_');
        }
        System.out.print('\n');
        // NOTE: if numcols is ever 2 or more digits, there will be visual errors.
        for (int i=0; i<numrows; i++) {
            System.out.println(board[i]);
        }
        for (int i=0; i<numcols; i++) {
            System.out.print('_');
        }
        System.out.print('\n');
        for (int i=0; i<numcols; i++) {
            System.out.print(i);
        }
        System.out.print('\n');
    }

    // return: boolean of whether target column can be successfully placed.
    // bool playerturn: true if player 1's turn, false if player 2's turn.
    public boolean place(int column, boolean playerturn) {
        for (int i=numrows-1; i>=0; i--) {
            if (board[i][column] == '-') {
                board[i][column] = (playerturn) ? player1char : player2char;
                return true;
            }
        }
        return false;
    }

    // checks whether the last piece placed down 
    // return: boolean of whether given player won
    public boolean checkforwin(int column, boolean playerturn) {
        char token = (playerturn) ? player1char : player2char;
        // check each direction from point of change rather than eval entire board
        int row = 0;
        // find the row of most recently placed token
        for (int i=0; i<numrows; i++) {
            if (board[i][column] == token) {
                row = i;
                break;
            }
        }

        // check left-right
        int count = 0;
        for (int len=0; len<winnum; len++) {
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
        for (int len=1; len<winnum; len++) {
            // check right of
            if (column+len<numcols) {
                if (board[row][column+len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        if (count >= winnum) {
            return true;
        }

        // check down (no need for up, this will be the highest placed token)
        count = 0;
        for (int len=0; len<winnum; len++) {
            if (row+len<numrows) {
                if (board[row+len][column] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        if (count >= winnum) {
            return true;
        }

        // check top-left to bot-right diagonal
        count = 0;
        for (int len=0; len<winnum; len++) {
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
        for (int len=1; len<winnum; len++) {
            // check right and down
            if ((row+len<numrows) && (column+len<numcols)) {
                if (board[row+len][column+len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        if (count >= winnum) {
            return true;
        }

        // check bot-left to top-right diagonal
        count = 0;
        for (int len=0; len<winnum; len++) {
            // check left and down
            if ((row+len<numrows) && (column-len >= 0)) {
                if (board[row+len][column-len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        // len starting at 1 so it doesn't count starting spot again
        for (int len=1; len<winnum; len++) {
            // check right and up
            if ((row-len >= 0) && (column+len<numcols)) {
                if (board[row-len][column+len] == token) {
                    count++;
                    continue;
                }
            }
            break;
        }
        if (count >= winnum) {
            return true;
        }

        return false;
    }

    public boolean checkfortie() {
        for (int i=0; i<numcols; i++) {
            if (board[0][i] == '-') {
                // board still has an empty space, not tied.
                return false;
            }
        }
        return true;
    }
}

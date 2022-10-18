package game;

import java.util.Arrays;

public class AI {
    private int lookahead;
    private Board board;
    // arbitrarily large score numbers used to signify a winning board
    private int player1win = 999;
    private int player2win = -999;

    public AI(Board givenboard) {
        // instantiate lookahead as 1 so it can be set later in setdifficulty
        lookahead = 1;
        board = givenboard;
    }

    public void setdifficulty(int difficulty) {
        lookahead = difficulty;
    }

    public int getdifficulty() {
        return lookahead;
    }

    public int getmove(char[][] board, Boolean playerturn) {
        // use recursive minimax to find the best move
        char player = (playerturn) ? this.board.player1char : this.board.player2char;
        Move move = minimax(board, player, lookahead);
        // find where the tile was added
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[0].length; j++) {
                if (move.board[i][j] != board[i][j]) {
                    return j;
                }
            }
        }
        // This should not be reached - maybe the board didn't get deepcopied?
        System.out.println("AI ERROR - MOVE NOT FOUND");
        return 0;
    }

    private Move minimax(char[][] board, char player, int lookahead) {
        if (lookahead >= 1) {
            // check if already reached wincondition:
            int wincheck = boardscore(board);
            if ((wincheck >= this.player1win) || (wincheck <= this.player2win)) {
                return new Move(board, wincheck);
            }

            if (player == this.board.player1char) {
                // generate player 1's best move from the possibilities
                char[][][] movelist = movegen(board, player);
                // checks if the character is null - empty list returned
                if (movelist[0][0][0] == '\u0000') {
                    // no more moves can be made - full board.
                    return new Move(board, wincheck);
                }

                // rather than making moves of all of them, just
                // find the lowest cost one and call minimax from that
                // assuming at least 1 board in movelist if we got this far
                Move plannedmove = minimax(movelist[0], this.board.player2char, lookahead-1);
                // possible fix to thing: replace the returning move's board with its original
                // movelist version - just to cut out all of the extra planned moves ahead
                // that we don't need to know about.
                int bestofmovelist = 0;
                for (int i=1; i<movelist.length; i++) {
                    Move comparemove = minimax(movelist[i], this.board.player2char, lookahead-1);
                    if (plannedmove.boardscore < comparemove.boardscore) {
                        // better score, change planned move
                        bestofmovelist = i;
                        plannedmove.deepcopy(comparemove);
                    }
                }
                // instead of returning plannedmove which has the steps ahead board,
                // just return its movelist board with plannedmove's boardscore
                return new Move(movelist[bestofmovelist], plannedmove.boardscore);
            } else {
                // player == board.player2char
                // generate player 2's best move from the possibilities
                char[][][] movelist = movegen(board, player);
                // checks if the character is null - empty list returned
                if (movelist[0][0][0] == '\u0000') {
                    // no more moves can be made - full board.
                    return new Move(board, wincheck);
                }

                // rather than making moves of all of them, just
                // find the lowest cost one and call minimax from that
                // assuming at least 1 board in movelist if we got this far
                Move plannedmove = minimax(movelist[0], this.board.player1char, lookahead-1);
                // see above situation handling if player1char for more information on
                // handling bestofmovelist and the returning move
                int bestofmovelist = 0;
                for (int i=1; i<movelist.length; i++) {
                    Move comparemove = minimax(movelist[i], this.board.player1char, lookahead-1);
                    if (plannedmove.boardscore > comparemove.boardscore) {
                        // better score, change planned move
                        bestofmovelist = i;
                        plannedmove.deepcopy(comparemove);
                    }
                }
                return new Move(movelist[bestofmovelist], plannedmove.boardscore);
            }
        } else {
            // lookahead = 0, this is a leaf. find value of board directly
            int value = boardscore(board);
            return new Move(board, value);
        }
    }

    private char[][][] movegen(char[][] board, char player) {
        // generate all possible boards (usually one for each column)
        char [][][] nextstates = new char[board[0].length][board.length][board[0].length];
        int nextopenspot = 0;
        // go through, just putting them to the next spot in the array
        // keep track of this next spot with above int
        // at the end: if it's smaller than length, then there's missing
        // boards equal to the difference between them, just remake with the new size
        for (int i=0; i<board[0].length; i++) {
            // it's board[row][col] and we want to go column by column
            //  going from the bottom row to the top.
            // i goes through columns, j goes through rows
            for (int j=1; j<board.length; j++) {
                if (board[board.length-j][i] == '-') {
                    // deepcopy the board
                    char[][] newstate = new char[board.length][];
                    for (int k=0; k<board.length; k++) {
                        newstate[k] = Arrays.copyOf(board[k], board[k].length);
                    }
                    newstate[board.length-j][i] = player;
                    nextstates[nextopenspot] = newstate;
                    nextopenspot++;
                    // don't want to check any higher up on row, already placed
                    break;
                }
            }
        }
        // reduce size to remove empty spots
        // check if missing any columns
        if (nextopenspot < board[0].length) {
            // reduce size to remove empty spots in array
            char [][][] smallnextstates = new char[nextopenspot][board.length][board[0].length];
            for (int k=0; k<nextopenspot; k++) {
                smallnextstates[k] = nextstates[k];
            }
            return smallnextstates;
        } else {
            return nextstates;
        }
    }

    private int boardscore(char[][] board) {
        // heuristic:
        // +1 for each p1 token in a line that could reach win condition
        //  (ie not blocked by a p2 token)
        // -1 for each p2 token in a line that could reach win condition
        int score = 0;
        int numrows = this.board.getnumrows();
        int numcols = this.board.getnumcols();
        int winnum = this.board.getwinnum();

        for (int i=numrows-1; i >= 0; i--) {
            for (int j=0; j<numcols; j++) {
                if (board[i][j] == this.board.player1char) {
                    // player 1 token
                    // check left
                    if (j-winnum+1 >= 0) {
                        // start for loop going left - if token +1,
                        //  if enemy token set to 0 and break loop
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i][j-k] == this.board.player1char) {
                                addingscore++;
                            } else if (board[i][j-k] == this.board.player2char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // don't need to check for a win, will be found earlier searching right
                        score += addingscore;
                    }
                    // check topleft
                    if ((j-winnum+1 >= 0) && (i-winnum+1 >= 0)) {
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i-k][j-k] == this.board.player1char) {
                                addingscore++;
                            } else if (board[i-k][j-k] == this.board.player2char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == winnum) {
                            return this.player1win;
                        }
                        score += addingscore;
                    }
                    // check top
                    if (i-winnum+1 >= 0) {
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i-k][j] == this.board.player1char) {
                                addingscore++;
                            } else if (board[i-k][j] == this.board.player2char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == winnum) {
                            return this.player1win;
                        }
                        score += addingscore;
                    }
                    // check topright
                    if ((j+winnum-1 < numcols) && (i-winnum+1 >= 0)) {
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i-k][j+k] == this.board.player1char) {
                                addingscore++;
                            } else if (board[i-k][j+k] == this.board.player2char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == winnum) {
                            return this.player1win;
                        }
                        score += addingscore;
                    }
                    // check right
                    if (j+winnum-1 < numcols) {
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i][j+k] == this.board.player1char) {
                                addingscore++;
                            } else if (board[i][j+k] == this.board.player2char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == winnum) {
                            return this.player1win;
                        }
                        score += addingscore;
                    }

                } else if (board[i][j] == this.board.player2char) {
                    // player 2 token
                    // check left
                    if (j-winnum+1 >= 0) {
                        // start for loop going left - if token -1,
                        //  if enemy token set to 0 and break loop
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i][j-k] == this.board.player2char) {
                                addingscore--;
                            } else if (board[i][j-k] == this.board.player1char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == -winnum) {
                            return this.player2win;
                        }
                        score += addingscore;
                    }
                    // check topleft
                    if ((j-winnum+1 >= 0) && (i-winnum+1 >= 0)) {
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i-k][j-k] == this.board.player2char) {
                                addingscore--;
                            } else if (board[i-k][j-k] == this.board.player1char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == -winnum) {
                            return this.player2win;
                        }
                        score += addingscore;
                    }
                    // check top
                    if (i-winnum+1 >= 0) {
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i-k][j] == this.board.player2char) {
                                addingscore--;
                            } else if (board[i-k][j] == this.board.player1char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == -winnum) {
                            return this.player2win;
                        }
                        score += addingscore;
                    }
                    // check topright
                    if ((j+winnum-1 < numcols) && (i-winnum+1 >= 0)) {
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i-k][j+k] == this.board.player2char) {
                                addingscore--;
                            } else if (board[i-k][j+k] == this.board.player1char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == -winnum) {
                            return this.player2win;
                        }
                        score += addingscore;
                    }
                    // check right
                    if (j+winnum-1 < numcols) {
                        int addingscore = 0;
                        for (int k=0; k<winnum; k++) {
                            if (board[i][j+k] == this.board.player2char) {
                                addingscore--;
                            } else if (board[i][j+k] == this.board.player1char) {
                                addingscore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingscore == -winnum) {
                            return this.player2win;
                        }
                        score += addingscore;
                    }
                }
            }
        }

        return score;
    }

    // inner class for returning an object through minimax
    private class Move {
        char[][] board;
        int boardscore;

        Move(char[][] x, int y) {
            board = x;
            boardscore = y;
        }

        private void deepcopy(Move mv) {
            // copies all internal data of another move
            for (int i=0; i<mv.board.length; i++) {
                this.board[i] = Arrays.copyOf(mv.board[i], mv.board[i].length);
            }
            this.boardscore = mv.boardscore;
        }
    }

}

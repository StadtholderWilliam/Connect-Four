package game;

import java.util.Arrays;

public class AI {
    private int lookAhead;
    private Board board;
    // arbitrarily large score numbers used to signify a winning board
    private int PLAYER_1_WIN = 999;
    private int PLAYER_2_WIN = -999;

    public AI(Board givenBoard) {
        // instantiate lookAhead as 1 so it can be set later in setdifficulty
        lookAhead = 1;
        board = givenBoard;
    }

    public void setDifficulty(int difficulty) {
        lookAhead = difficulty;
    }

    public int getDifficulty() {
        return lookAhead;
    }

    public int getMove(char[][] board, Boolean playerTurn) {
        // use recursive minimax to find the best move
        char player = (playerTurn) ? this.board.player1Char : this.board.player2Char;
        Move move = minimax(board, player, lookAhead);
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

    private Move minimax(char[][] board, char player, int lookAhead) {
        if (lookAhead >= 1) {
            // check if already reached wincondition:
            int winCheck = boardScore(board);
            if ((winCheck >= this.PLAYER_1_WIN) || (winCheck <= this.PLAYER_2_WIN)) {
                return new Move(board, winCheck);
            }

            if (player == this.board.player1Char) {
                // generate player 1's best move from the possibilities
                char[][][] moveList = moveGen(board, player);
                // checks if the character is null - empty list returned
                if (moveList[0][0][0] == '\u0000') {
                    // no more moves can be made - full board.
                    return new Move(board, winCheck);
                }

                // rather than making moves of all of them, just
                // find the lowest cost one and call minimax from that
                // assuming at least 1 board in moveList if we got this far
                Move plannedMove = minimax(moveList[0], this.board.player2Char, lookAhead-1);
                int bestOfMoveList = 0;
                for (int i=1; i<moveList.length; i++) {
                    Move compareMove = minimax(moveList[i], this.board.player2Char, lookAhead-1);
                    if (plannedMove.boardScore < compareMove.boardScore) {
                        // better score, change planned move
                        bestOfMoveList = i;
                        plannedMove.deepCopy(compareMove);
                    }
                }
                // instead of returning plannedMove which has the steps ahead board,
                // just return its moveList board with plannedMove's boardScore
                return new Move(moveList[bestOfMoveList], plannedMove.boardScore);
            } else {
                // player == board.player2Char
                // generate player 2's best move from the possibilities
                char[][][] moveList = moveGen(board, player);
                // checks if the character is null - empty list returned
                if (moveList[0][0][0] == '\u0000') {
                    // no more moves can be made - full board.
                    return new Move(board, winCheck);
                }

                // rather than making moves of all of them, just
                // find the lowest cost one and call minimax from that
                // assuming at least 1 board in moveList if we got this far
                Move plannedMove = minimax(moveList[0], this.board.player1Char, lookAhead-1);
                // see above situation handling if player1Char for more information on
                // handling bestOfMoveList and the returning move
                int bestOfMoveList = 0;
                for (int i=1; i<moveList.length; i++) {
                    Move compareMove = minimax(moveList[i], this.board.player1Char, lookAhead-1);
                    if (plannedMove.boardScore > compareMove.boardScore) {
                        // better score, change planned move
                        bestOfMoveList = i;
                        plannedMove.deepCopy(compareMove);
                    }
                }
                return new Move(moveList[bestOfMoveList], plannedMove.boardScore);
            }
        } else {
            // lookAhead = 0, this is a leaf. find value of board directly
            int value = boardScore(board);
            return new Move(board, value);
        }
    }

    private char[][][] moveGen(char[][] board, char player) {
        // generate all possible boards (usually one for each column)
        char [][][] nextStates = new char[board[0].length][board.length][board[0].length];
        int nextOpenSpot = 0;
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
                    char[][] newState = new char[board.length][];
                    for (int k=0; k<board.length; k++) {
                        newState[k] = Arrays.copyOf(board[k], board[k].length);
                    }
                    newState[board.length-j][i] = player;
                    nextStates[nextOpenSpot] = newState;
                    nextOpenSpot++;
                    // don't want to check any higher up on row, already placed
                    break;
                }
            }
        }
        // reduce size to remove empty spots
        // check if missing any columns
        if (nextOpenSpot < board[0].length) {
            // reduce size to remove empty spots in array
            char [][][] smallNextStates = new char[nextOpenSpot][board.length][board[0].length];
            for (int k=0; k<nextOpenSpot; k++) {
                smallNextStates[k] = nextStates[k];
            }
            return smallNextStates;
        } else {
            return nextStates;
        }
    }

    private int boardScore(char[][] board) {
        // heuristic:
        // +1 for each p1 token in a line that could reach win condition
        //  (ie not blocked by a p2 token)
        // -1 for each p2 token in a line that could reach win condition
        int score = 0;
        int numRows = this.board.getNumRows();
        int numCols = this.board.getNumCols();
        int winNum = this.board.getWinNum();

        for (int i=numRows-1; i >= 0; i--) {
            for (int j=0; j<numCols; j++) {
                if (board[i][j] == this.board.player1Char) {
                    // player 1 token
                    // check left
                    if (j-winNum+1 >= 0) {
                        // start for loop going left - if token +1,
                        //  if enemy token set to 0 and break loop
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i][j-k] == this.board.player1Char) {
                                addingScore++;
                            } else if (board[i][j-k] == this.board.player2Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // don't need to check for a win, will be found earlier searching right
                        score += addingScore;
                    }
                    // check topleft
                    if ((j-winNum+1 >= 0) && (i-winNum+1 >= 0)) {
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i-k][j-k] == this.board.player1Char) {
                                addingScore++;
                            } else if (board[i-k][j-k] == this.board.player2Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == winNum) {
                            return this.PLAYER_1_WIN;
                        }
                        score += addingScore;
                    }
                    // check top
                    if (i-winNum+1 >= 0) {
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i-k][j] == this.board.player1Char) {
                                addingScore++;
                            } else if (board[i-k][j] == this.board.player2Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == winNum) {
                            return this.PLAYER_1_WIN;
                        }
                        score += addingScore;
                    }
                    // check topright
                    if ((j+winNum-1 < numCols) && (i-winNum+1 >= 0)) {
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i-k][j+k] == this.board.player1Char) {
                                addingScore++;
                            } else if (board[i-k][j+k] == this.board.player2Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == winNum) {
                            return this.PLAYER_1_WIN;
                        }
                        score += addingScore;
                    }
                    // check right
                    if (j+winNum-1 < numCols) {
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i][j+k] == this.board.player1Char) {
                                addingScore++;
                            } else if (board[i][j+k] == this.board.player2Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == winNum) {
                            return this.PLAYER_1_WIN;
                        }
                        score += addingScore;
                    }

                } else if (board[i][j] == this.board.player2Char) {
                    // player 2 token
                    // check left
                    if (j-winNum+1 >= 0) {
                        // start for loop going left - if token -1,
                        //  if enemy token set to 0 and break loop
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i][j-k] == this.board.player2Char) {
                                addingScore--;
                            } else if (board[i][j-k] == this.board.player1Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == -winNum) {
                            return this.PLAYER_2_WIN;
                        }
                        score += addingScore;
                    }
                    // check topleft
                    if ((j-winNum+1 >= 0) && (i-winNum+1 >= 0)) {
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i-k][j-k] == this.board.player2Char) {
                                addingScore--;
                            } else if (board[i-k][j-k] == this.board.player1Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == -winNum) {
                            return this.PLAYER_2_WIN;
                        }
                        score += addingScore;
                    }
                    // check top
                    if (i-winNum+1 >= 0) {
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i-k][j] == this.board.player2Char) {
                                addingScore--;
                            } else if (board[i-k][j] == this.board.player1Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == -winNum) {
                            return this.PLAYER_2_WIN;
                        }
                        score += addingScore;
                    }
                    // check topright
                    if ((j+winNum-1 < numCols) && (i-winNum+1 >= 0)) {
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i-k][j+k] == this.board.player2Char) {
                                addingScore--;
                            } else if (board[i-k][j+k] == this.board.player1Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == -winNum) {
                            return this.PLAYER_2_WIN;
                        }
                        score += addingScore;
                    }
                    // check right
                    if (j+winNum-1 < numCols) {
                        int addingScore = 0;
                        for (int k=0; k<winNum; k++) {
                            if (board[i][j+k] == this.board.player2Char) {
                                addingScore--;
                            } else if (board[i][j+k] == this.board.player1Char) {
                                addingScore = 0;
                                break;
                            }
                        }
                        // checking for a win
                        if (addingScore == -winNum) {
                            return this.PLAYER_2_WIN;
                        }
                        score += addingScore;
                    }
                }
            }
        }

        return score;
    }

    // inner class for returning an object through minimax
    private class Move {
        char[][] board;
        int boardScore;

        Move(char[][] x, int y) {
            board = x;
            boardScore = y;
        }

        private void deepCopy(Move mv) {
            // copies all internal data of another move
            for (int i=0; i<mv.board.length; i++) {
                this.board[i] = Arrays.copyOf(mv.board[i], mv.board[i].length);
            }
            this.boardScore = mv.boardScore;
        }
    }

}

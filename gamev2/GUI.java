package gamev2;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import gamev2.ConnectFour.GameState;

public class GUI implements ActionListener {
    // changeable through main menu
    static int aiDifficulty = 3;
    static boolean aiBool = true;

    // changeable through the options menu
    static int numRows = 6;
    static int numCols = 7;
    static int winNum = 4;

    // panels and frame
    JPanel mainPanel, optPanel, gamePanel;
    JFrame appFrame;

    GUI() {
        appFrame = new JFrame("Connect Four");
        Image icon = Toolkit.getDefaultToolkit().getImage("images/game.png");
        appFrame.setIconImage(icon);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setResizable(false);
        appFrame.setSize(250,300);
        CardLayout crd = new CardLayout();
        appFrame.setLayout(crd);

        mainPanel = createMainPage();
        optPanel = createOptionsPage();

        appFrame.add(mainPanel);
        appFrame.add(optPanel);
        
        optPanel.setVisible(false);
        appFrame.setVisible(true);  
    }

    JPanel createMainPage() {
        // main page of the gui - ai op, human op, options
        JPanel pnl = new JPanel();
        pnl.setLayout(null);

        ImageIcon img = new ImageIcon("images/game.png");
        JLabel l1 = new JLabel("Connect Four", img, JLabel.CENTER);
        l1.setBounds(0, 10, 250, 20);
        pnl.add(l1);

        JRadioButton aiButton = new JRadioButton("AI Opponent", true);
        aiButton.setBounds(30, 60, 100, 20);
        aiButton.setActionCommand("aitrue");
        aiButton.addActionListener(this);
        aiButton.setToolTipText("Play against a variable-difficulty AI Opponent.");
        pnl.add(aiButton);

        JRadioButton humanButton = new JRadioButton("Human Opponent");
        humanButton.setBounds(30, 100, 150, 20);
        humanButton.setActionCommand("aifalse");
        humanButton.addActionListener(this);
        humanButton.setToolTipText("Play against a human opponent using the same keyboard.");
        pnl.add(humanButton);

        ButtonGroup group = new ButtonGroup();
        group.add(aiButton);
        group.add(humanButton);

        JLabel l2 = new JLabel("Difficulty (1-10)");
        SpinnerModel difficultyModel = new SpinnerNumberModel(aiDifficulty, 1, 10, 1);
        JSpinner difficulty = new JSpinner(difficultyModel);
        difficulty.addChangeListener(
            new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    aiDifficulty = (int)(difficultyModel.getValue());
                }
            }
        );

        JPanel pDifficulty = new JPanel();
        pDifficulty.add(l2);
        pDifficulty.add(difficulty);
        pDifficulty.setBounds(30, 70, 200, 30);
        pDifficulty.setToolTipText("Difficulty levels range from 1 (Easiest) to 10 (Hardest).");
        pnl.add(pDifficulty);


        JButton start = new JButton("Start Game");
        start.setBounds(60, 150, 130, 40);
        start.setActionCommand("startgame");
        start.addActionListener(this);
        start.setToolTipText("Start a new game with the current options.");
        pnl.add(start);

        JButton options = new JButton("Options");
        options.setBounds(75, 200, 100, 25);
        options.setActionCommand("options");
        options.addActionListener(this);
        options.setToolTipText("See changeable board options.");
        pnl.add(options);

        return pnl;
    }

    JPanel createOptionsPage() {
        // option page of the gui
        JPanel pnl = new JPanel();
        pnl.setLayout(null);

        JLabel l1 = new JLabel("Options", JLabel.CENTER);
        l1.setBounds(0, 10, 250, 20);
        pnl.add(l1);

        TitledBorder title;
        title = BorderFactory.createTitledBorder("Board");

        JLabel rowLabel = new JLabel("Rows");
        SpinnerModel rowModel = new SpinnerNumberModel(numRows, 1, 20, 1);
        JSpinner rowSpinner = new JSpinner(rowModel);
        // https://stackoverflow.com/questions/5911565/how-to-add-multiple-actionlisteners-for-multiple-buttons-in-java-swing
        rowSpinner.addChangeListener(
            new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    numRows = (int)(rowModel.getValue());
                }
            }
        );
        JPanel rowPanel = new JPanel();
        rowPanel.add(rowLabel);
        rowPanel.add(rowSpinner);
        rowPanel.setToolTipText("How many rows will the board be? (Default: 6)");

        JLabel colLabel = new JLabel("Columns");
        SpinnerModel colModel = new SpinnerNumberModel(numCols, 1, 20, 1);
        JSpinner colSpinner = new JSpinner(colModel);
        colSpinner.addChangeListener(
            new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    numCols = (int)(colModel.getValue());
                }
            }
        );
        JPanel colPanel = new JPanel();
        colPanel.add(colLabel);
        colPanel.add(colSpinner);
        colPanel.setToolTipText("How many columns will the board be? (Default: 7)");

        JLabel winLabel = new JLabel("Win Number");
        SpinnerModel winModel = new SpinnerNumberModel(winNum, 1, 20, 1);
        JSpinner winSpinner = new JSpinner(winModel);
        winSpinner.addChangeListener(
            new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    winNum = (int)(winModel.getValue());
                }
            }
        );
        JPanel winPanel = new JPanel();
        winPanel.setToolTipText("How many tokens in a row do you need to win? (Default: 4)");
        //winPanel.setLayout(new BoxLayout(winPanel, BoxLayout.X_AXIS));
        winPanel.add(winLabel);
        winPanel.add(winSpinner);
        //winPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.PAGE_AXIS));
        boardPanel.add(rowPanel);
        //rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        boardPanel.add(colPanel);
        //colPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        boardPanel.add(winPanel);
        //winPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        boardPanel.setBorder(title);
        boardPanel.setBounds(50, 50, 150, 130);
        pnl.add(boardPanel);

        JButton main = new JButton("Main Menu");
        main.setBounds(75, 200, 100, 25);
        main.setActionCommand("main");
        main.addActionListener(this);
        main.setToolTipText("Return to the main menu.");
        pnl.add(main);

        return pnl;
    }

    JPanel createGamePage() {
        // tab with game grid and pieces
        ImageIcon imgEmpty = new ImageIcon("images/empty2_32.png");
        ImageIcon imgP1 = new ImageIcon("images/player1_32.png");
        ImageIcon imgP2 = new ImageIcon("images/player2_32.png");
        ImageIcon imgPlaceable = new ImageIcon("images/empty1_32.png");
        int width = imgEmpty.getIconWidth();
        int height = imgEmpty.getIconHeight();

        appFrame.setSize((numCols*width+200), (numRows*height+200));
        JPanel pnl = new JPanel();
        GridLayout lyt = new GridLayout(numRows, numCols);
        pnl.setLayout(lyt);
        pnl.setBounds(0, 0, numCols*width, numRows*height);

        ConnectFour game = new ConnectFour(numRows, numCols, winNum, aiBool, aiDifficulty);

        ArrayList<ArrayList<JButton>> grid = new ArrayList<ArrayList<JButton>>(numRows);
        // set up render loop for board buttons
        for (int i=0; i<numRows; i++) {
            ArrayList<JButton> row = new ArrayList<JButton>(numCols);
            for (int j=0; j<numCols; j++) {
                JButton tempButton = new JButton(imgEmpty);
                tempButton.setContentAreaFilled(false);
                tempButton.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (tempButton.getIcon() == imgPlaceable) {
                                // place token on gui based on player turn
                                if (game.getPlayerTurn()) {
                                    tempButton.setIcon(imgP1);
                                } else {
                                    tempButton.setIcon(imgP2);
                                }
                                
                                // make above tile valid, place move in game board
                                int col = row.indexOf(tempButton);
                                int aboveRow = game.placeMove(col);

                                if (aboveRow >= 0) {
                                    grid.get(aboveRow).get(col).setIcon(imgPlaceable);
                                }

                                // player turns split into two call so returning
                                //  to change the gui is easier
                                GameState state = game.endTurn(col);
                                if (state.tiedGame) {
                                    tieGameAlert(grid, imgPlaceable, imgEmpty);
                                } else if (state.wonGame) {
                                    // get opposite of playerTurn, it switched
                                    winGameAlert(!(game.playerTurn), grid, imgPlaceable, imgEmpty);
                                }
                                // make enemy's turn
                                if (aiBool && !state.tiedGame && !state.wonGame) {
                                    // if ai opponent, make its move in response
                                    int move = game.aiMove();
                                    for (int k=0; k<numRows; k++) {
                                        if (grid.get(k).get(move).getIcon() == imgPlaceable) {
                                            // look for placeable tile, put it there
                                            // ai will always be player2
                                            grid.get(k).get(move).setIcon(imgP2);
                                            if (k>0) {
                                                // make tile above placeable
                                                grid.get(k-1).get(move).setIcon(imgPlaceable);
                                            }
                                            break;
                                        }
                                    }
                                    state = game.endTurn(move);
                                    if (state.tiedGame) {
                                        tieGameAlert(grid, imgPlaceable, imgEmpty);
                                    } else if (state.wonGame) {
                                        winGameAlert(!(game.playerTurn), grid, imgPlaceable, imgEmpty);
                                    }
                                }

                            }
                        }
                    }
                );
                pnl.add(tempButton);
                row.add(tempButton);
            }
            grid.add(row);
        }

        // set up placeable tiles
        for (int i=0; i<numCols; i++) {
            grid.get(numRows-1).get(i).setIcon(imgPlaceable);
        }

        return pnl;
    }

    void tieGameAlert(ArrayList<ArrayList<JButton>> grid, ImageIcon place, ImageIcon empty) {
        for (int i=0; i<numCols; i++) {
            for (int j=0; j<numRows; j++) {
                JButton tar = grid.get(j).get(i);
                if (tar.getIcon() == place) {
                    tar.setIcon(empty);
                    break;
                }
            }
        }
        String s1 = "Yes";
        String s2 = "No";
        Object[] options = {s1, s2};
        int popUp = JOptionPane.showOptionDialog(appFrame,
        "TIE GAME!\n"+"Return to Main Menu?",
                "Game Complete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                s1);
        if (popUp == JOptionPane.YES_OPTION) {
            appFrame.add(mainPanel);
            appFrame.add(optPanel);
            appFrame.remove(gamePanel);
            appFrame.setSize(250,300);
            optPanel.setVisible(false);
        }
    }

    void winGameAlert(boolean player, ArrayList<ArrayList<JButton>> grid, ImageIcon place, ImageIcon empty) {
        // yes
        for (int i=0; i<numCols; i++) {
            for (int j=0; j<numRows; j++) {
                JButton tar = grid.get(j).get(i);
                if (tar.getIcon() == place) {
                    tar.setIcon(empty);
                    break;
                }
            }
        }
        char playerChar = (player) ? '1' : '2';
        String s1 = "Yes";
        String s2 = "No";
        Object[] options = {s1, s2};
        int popUp = JOptionPane.showOptionDialog(appFrame,
        "PLAYER "+playerChar+" WINS!\n"+
        "Return to Main Menu?",
                "Game Complete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                s1);
        if (popUp == JOptionPane.YES_OPTION) {
            appFrame.add(mainPanel);
            appFrame.add(optPanel);
            appFrame.remove(gamePanel);
            appFrame.setSize(250,300);
            optPanel.setVisible(false);
        }

    }

    public static void main(String[] args) {
        new GUI();
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "startgame":
                // main menu > game screen
                mainPanel.setVisible(false);
                gamePanel = createGamePage();
                appFrame.add(gamePanel);
                appFrame.remove(mainPanel);
                appFrame.remove(optPanel);
                break;
            case "options":
                // main menu > options menu
                mainPanel.setVisible(false);
                optPanel.setVisible(true);
                break;
            case "main":
                // options menu > main menu
                optPanel.setVisible(false);
                mainPanel.setVisible(true);
                break;
            case "aitrue":
                // main menu: ai opponent selected
                aiBool = true;
                break;
            case "aifalse":
                // main menu: human opponent selected
                aiBool = false;
                break;
        }
    }
}

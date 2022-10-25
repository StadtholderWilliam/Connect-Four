package gamev2;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {
    // changeable through main menu
    static int aiDifficulty = 3;

    // changeable through the options menu
    static int numRows = 6;
    static int numCols = 7;
    static int winNum = 4;

    // panels
    JPanel mainPanel, optPanel;

    GUI() {
        JFrame frame = new JFrame("Connect Four");
        Image icon = Toolkit.getDefaultToolkit().getImage("images/game.png");
        frame.setIconImage(icon);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(250,300);
        CardLayout crd = new CardLayout();
        frame.setLayout(crd);

        mainPanel = createMainPage();
        optPanel = createOptionsPage();

        frame.add(mainPanel);
        frame.add(optPanel);
        
        optPanel.setVisible(false);
        frame.setVisible(true);  
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
        aiButton.setToolTipText("Play against a variable-difficulty AI Opponent.");
        aiButton.setBounds(30, 60, 100, 20);
        pnl.add(aiButton);
        JRadioButton humanButton = new JRadioButton("Human Opponent");
        humanButton.setBounds(30, 100, 150, 20);
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

    void createGamePage(JFrame frame) {
        // tab with game grid and pieces
        frame.setSize((numCols*50+40), (numRows*50+40));
        JPanel pnl = new JPanel();
        GridLayout lyt = new GridLayout(numCols, numRows);
        pnl.setLayout(lyt);
    }

    public static void main(String[] args) {
        GUI content = new GUI();
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "start":
                // main menu > game screen
                mainPanel.setVisible(false);
                // TODO: figure how to get frame or create game page
                //createGamePage(frame);
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
        }
    }
}

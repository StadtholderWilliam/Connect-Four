import javax.swing.*;
import javax.swing.border.TitledBorder;

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
        pnl.add(aiButton);
        JRadioButton humanButton = new JRadioButton("Human Opponent");
        humanButton.setBounds(30, 100, 150, 20);
        pnl.add(humanButton);

        ButtonGroup group = new ButtonGroup();
        group.add(aiButton);
        group.add(humanButton);

        JLabel l2 = new JLabel("Difficulty (1-10)");
        SpinnerModel difficultyModel = new SpinnerNumberModel(aiDifficulty, 1, 10, 1);
        JSpinner difficulty = new JSpinner(difficultyModel);

        JPanel pDifficulty = new JPanel();
        pDifficulty.add(l2);
        pDifficulty.add(difficulty);
        pDifficulty.setBounds(30, 70, 200, 30);
        pnl.add(pDifficulty);


        JButton start = new JButton("Start Game");
        start.setBounds(60, 150, 130, 40);
        start.setActionCommand("startgame");

        start.addActionListener(this);
        pnl.add(start);
        JButton options = new JButton("Options");
        options.setBounds(75, 200, 100, 25);
        pnl.add(options);

        return pnl;
    }

    static JPanel createOptionsPage() {
        // option page of the gui
        JPanel optPanel = new JPanel();

        JLabel l1 = new JLabel("Options", JLabel.CENTER);
        l1.setBounds(0, 10, 250, 20);
        optPanel.add(l1);

        TitledBorder title;
        title = BorderFactory.createTitledBorder("Board Options");


        JLabel rowLabel = new JLabel("Rows");
        SpinnerModel rowModel = new SpinnerNumberModel(numRows, 1, 20, 1);
        JSpinner rowSpinner = new JSpinner(rowModel);
        JPanel rowPanel = new JPanel();
        rowPanel.add(rowLabel);
        rowPanel.add(rowSpinner);

        JLabel colLabel = new JLabel("Columns");
        SpinnerModel colModel = new SpinnerNumberModel(numCols, 1, 20, 1);
        JSpinner colSpinner = new JSpinner(colModel);
        JPanel colPanel = new JPanel();
        colPanel.add(colLabel);
        colPanel.add(colSpinner);


        JPanel boardPanel = new JPanel();
        boardPanel.add(rowPanel);
        boardPanel.add(colPanel);
        boardPanel.setBorder(title);

        return optPanel;
    }

    void createGamePage(JFrame frame) {
        // tab with game grid and pieces
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Connect Four");
        Image icon = Toolkit.getDefaultToolkit().getImage("images/game.png");
        frame.setIconImage(icon);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(250,300);

        JPanel mainPanel = createMainPage();
        JPanel optPanel = createOptionsPage();

        frame.add(mainPanel);
        frame.setVisible(true);  
    }

    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }
}

package view;



import controller.QuizController;
import model.Question;
import model.Quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayQuizPanel extends JPanel {

    private final QuizController controller;
    private JComboBox<String> quizSelector;
    private JTextArea questionArea;
    private JRadioButton[] optionButtons;
    private ButtonGroup group;
    private JButton prev, next, submit;
    private JLabel progress;
    private Quiz current;
    private int index;
    private List<Integer> answers;
    private static final String[] LABELS = {"A) ", "B) ", "C) ", "D) "};

    public PlayQuizPanel(QuizController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        initializeUI();
    }

    private void initializeUI() {
        JLabel header = new JLabel("Play Quiz");
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBorder(new EmptyBorder(5, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.setOpaque(false);
        quizSelector = new JComboBox<>();
        JButton load = new JButton("Load Quizzes");
        top.add(new JLabel("Select:"));
        top.add(quizSelector);
        top.add(load);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIManager.getColor("Panel.background"));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Component.borderColor")),
                new EmptyBorder(15, 15, 15, 15)
        ));

        questionArea = new JTextArea(3, 50);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);
        questionArea.setFont(new Font("Segoe UI", Font.BOLD, 15));
        questionArea.setBackground(UIManager.getColor("TextArea.background"));
        questionArea.setForeground(UIManager.getColor("Label.foreground"));
        questionArea.setBorder(null);
        card.add(questionArea);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        group = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setBackground(UIManager.getColor("Panel.background"));
            optionButtons[i].setForeground(UIManager.getColor("Label.foreground"));
            optionButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            group.add(optionButtons[i]);
            card.add(optionButtons[i]);
            card.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        nav.setOpaque(false);
        prev = new JButton("← Prev");
        next = new JButton("Next →");
        submit = new JButton("Submit");
        progress = new JLabel("No quiz");
        nav.add(prev);
        nav.add(next);
        nav.add(submit);
        nav.add(progress);

        add(top, BorderLayout.NORTH);
        add(card, BorderLayout.CENTER);
        add(nav, BorderLayout.SOUTH);

        load.addActionListener(e -> loadList());
        quizSelector.addActionListener(e -> loadSelected());
        prev.addActionListener(e -> prevQ());
        next.addActionListener(e -> nextQ());
        submit.addActionListener(e -> submitQ());

        setEnabledControls(false);
    }

    private void loadList() {
        quizSelector.removeAllItems();
        var quizzes = controller.getAllQuizzes();
        if (quizzes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No quizzes."); return;
        }
        for (Quiz q : quizzes) quizSelector.addItem(q.getTitle());
        quizSelector.setSelectedIndex(0);
        loadSelected();
    }

    private void loadSelected() {
        if (quizSelector.getSelectedIndex() == -1) return;
        String sel = (String) quizSelector.getSelectedItem();
        current = controller.getAllQuizzes().stream().filter(q -> q.getTitle().equals(sel)).findFirst().orElse(null);
        if (current == null || current.size() == 0) { JOptionPane.showMessageDialog(this, "Empty quiz."); return; }
        index = 0;
        answers = new ArrayList<>(current.size());
        for (int i = 0; i < current.size(); i++) answers.add(-1);
        setEnabledControls(true);
        showQuestion();
    }

    private void showQuestion() {
        Question q = current.getQuestions().get(index);
        questionArea.setText((index + 1) + ". " + q.getText());
        for (int i = 0; i < 4; i++)
            optionButtons[i].setText(LABELS[i] + q.getOptions().get(i));
        group.clearSelection();
        int ans = answers.get(index);
        if (ans >= 0) optionButtons[ans].setSelected(true);
        progress.setText("Question " + (index + 1) + " / " + current.size());
    }

    private void storeAnswer() {
        for (int i = 0; i < 4; i++)
            if (optionButtons[i].isSelected()) answers.set(index, i);
    }

    private void nextQ() { storeAnswer(); if (index < current.size() - 1) { index++; showQuestion(); } }
    private void prevQ() { storeAnswer(); if (index > 0) { index--; showQuestion(); } }

    private void submitQ() {
        storeAnswer();
        int score = current.calculateScore(answers);
        JOptionPane.showMessageDialog(this, "Score: " + score + " / " + current.size());
    }

    private void setEnabledControls(boolean en) {
        for (JRadioButton b : optionButtons) b.setEnabled(en);
        prev.setEnabled(en); next.setEnabled(en); submit.setEnabled(en);
    }
}

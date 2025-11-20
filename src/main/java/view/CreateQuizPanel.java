package view;

import Utility.AiQuestionGenerator;
import controller.QuizController;
import model.Question;
import model.Quiz;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CreateQuizPanel extends JPanel {

    private final QuizController controller;
    private Quiz currentQuiz;
    private JTextField titleField;
    private JTable questionTable;
    private DefaultTableModel tableModel;

    public CreateQuizPanel(QuizController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("Panel.background"));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        initializeUI();
    }

    private void initializeUI() {
        JLabel header = new JLabel("Create New Quiz");
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBorder(new EmptyBorder(5, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topControls.setOpaque(false);
        topControls.setBorder(new EmptyBorder(5, 5, 5, 5));

        titleField = new JTextField(20);
        JButton newQuizButton = createButton("Create Quiz", new Color(70, 130, 180));
        JButton addQuestionButton = createButton("Add Question", new Color(60, 180, 120));
        JButton deleteQuestionButton = createButton("Delete Selected", new Color(220, 80, 80));
        JButton aiButton = createButton("AI", new Color(140, 100, 210));

        topControls.add(new JLabel("Quiz Title:"));
        topControls.add(titleField);
        topControls.add(newQuizButton);
        topControls.add(addQuestionButton);
        topControls.add(deleteQuestionButton);
        topControls.add(aiButton);

        JScrollPane toolbarScroll = new JScrollPane(topControls,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        toolbarScroll.setBorder(null);
        centerPanel.add(toolbarScroll, BorderLayout.NORTH);

        String[] cols = {"Question", "A", "B", "C", "D", "Correct"};
        tableModel = new DefaultTableModel(cols, 0);
        questionTable = new JTable(tableModel);
        questionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        questionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        centerPanel.add(new JScrollPane(questionTable), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Button actions
        newQuizButton.addActionListener(e -> createQuiz());
        addQuestionButton.addActionListener(e -> addQuestion());
        deleteQuestionButton.addActionListener(e -> deleteSelectedQuestion());
        aiButton.addActionListener(e -> generateAiQuiz());
    }

    private JButton createButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void createQuiz() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter title first.");
            return;
        }
        currentQuiz = controller.createQuiz(title);
        tableModel.setRowCount(0);
        JOptionPane.showMessageDialog(this, "Created: " + title);
    }

    private void addQuestion() {
        if (currentQuiz == null) {
            JOptionPane.showMessageDialog(this, "Create a quiz first.");
            return;
        }

        String q = JOptionPane.showInputDialog(this, "Question text:");
        if (q == null || q.isBlank()) return;

        String[] opts = new String[4];
        for (int i = 0; i < 4; i++) {
            opts[i] = JOptionPane.showInputDialog(this, "Option " + (i + 1) + ":");
            if (opts[i] == null) return;
        }

        String corr = JOptionPane.showInputDialog(this, "Correct index (0–3):");
        if (corr == null) return;

        try {
            int correct = Integer.parseInt(corr);
            Question question = new Question(q, List.of(opts), correct);
            controller.addQuestionToQuiz(currentQuiz, question);
            tableModel.addRow(new Object[]{q, opts[0], opts[1], opts[2], opts[3], correct});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSelectedQuestion() {
        int row = questionTable.getSelectedRow();
        if (row == -1) return;
        tableModel.removeRow(row);
        if (currentQuiz != null && row < currentQuiz.size()) currentQuiz.removeQuestion(row);
    }

    // 🧠 Final polished version with auto-create + refresh + fallback handling
    private void generateAiQuiz() {
        if (currentQuiz == null) {
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                title = JOptionPane.showInputDialog(this, "Enter quiz title:");
                if (title == null || title.isBlank()) return;
            }
            currentQuiz = controller.createQuiz(title);
            JOptionPane.showMessageDialog(this,
                    "🆕 Created new quiz: " + title,
                    "Quiz Created", JOptionPane.INFORMATION_MESSAGE);
        }

        String topic = JOptionPane.showInputDialog(this, "Enter topic for AI quiz generation:");
        if (topic == null || topic.isBlank()) return;

        try {
            AiQuestionGenerator generator = new AiQuestionGenerator();
            List<Question> aiQuestions = generator.generateQuestions(topic, 5);

            for (Question q : aiQuestions) {
                controller.addQuestionToQuiz(currentQuiz, q);
                tableModel.addRow(new Object[]{
                        q.getText(),
                        q.getOptions().get(0),
                        q.getOptions().get(1),
                        q.getOptions().get(2),
                        q.getOptions().get(3),
                        q.getCorrectIndex()
                });
            }

            JOptionPane.showMessageDialog(this,
                    "✅ Added " + aiQuestions.size() + " questions about " + topic + "!");

            // ✅ Refresh sidebar / Play tab
            SwingUtilities.invokeLater(() -> {
                Container parent = getTopLevelAncestor();
                if (parent instanceof view.MainFrame frame) {
                    try {
                        var refresh = frame.getClass().getDeclaredMethod("refreshQuizList");
                        refresh.setAccessible(true);
                        refresh.invoke(frame);
                    } catch (Exception ignored) {}
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "❌ Error generating questions: " + ex.getMessage(),
                    "AI Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

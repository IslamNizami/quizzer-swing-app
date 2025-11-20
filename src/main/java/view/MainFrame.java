package view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import controller.QuizController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {

    private final QuizController controller;
    private DefaultListModel<String> quizListModel;
    private JList<String> quizList;
    private JSplitPane splitPane;

    public MainFrame(QuizController controller) {
        super("AI Quiz Maker");
        this.controller = controller;

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600)); // ✅ prevent shrinking too much
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initializeMenu();
        initializeLayout();
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save All");
        JMenuItem loadItem = new JMenuItem("Load All");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu themeMenu = new JMenu("Theme");
        JMenuItem lightItem = new JMenuItem("Light");
        JMenuItem darkItem = new JMenuItem("Dark");
        themeMenu.add(lightItem);
        themeMenu.add(darkItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(themeMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        saveItem.addActionListener(e -> saveQuizzes());
        loadItem.addActionListener(e -> loadQuizzes());
        exitItem.addActionListener(e -> System.exit(0));
        lightItem.addActionListener(e -> switchTheme("light"));
        darkItem.addActionListener(e -> switchTheme("dark"));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "AI Quiz Maker\nVersion 1.0\n© 2025 BME Smart Mobility",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        ));
    }

    private void initializeLayout() {
        // Sidebar (Quizzes)
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(170, 0)); // ✅ smaller sidebar
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIManager.getColor("Component.borderColor")));
        sidebar.setBackground(UIManager.getColor("Panel.background"));

        JLabel title = new JLabel("Quizzes");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        quizListModel = new DefaultListModel<>();
        quizList = new JList<>(quizListModel);
        quizList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        quizList.setBackground(UIManager.getColor("List.background"));
        quizList.setForeground(UIManager.getColor("List.foreground"));
        quizList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        sidebar.add(title, BorderLayout.NORTH);
        sidebar.add(new JScrollPane(quizList), BorderLayout.CENTER);

        // Tabbed Content
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.addTab("Create Quiz", new CreateQuizPanel(controller));
        tabbedPane.addTab("Play Quiz", new PlayQuizPanel(controller));

        // Split Pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, tabbedPane);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.18); // ✅ right side always larger
        splitPane.setDividerSize(2);
        add(splitPane, BorderLayout.CENTER);

        // Status Bar
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        refreshQuizList();
    }

    private void refreshQuizList() {
        quizListModel.clear();
        controller.getAllQuizzes().forEach(q -> quizListModel.addElement(q.getTitle()));
    }

    private void saveQuizzes() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Quizzes");
        chooser.setSelectedFile(new File("quizzes.ser"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                controller.saveToFile(chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void loadQuizzes() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load Quizzes");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                controller.loadFromFile(chooser.getSelectedFile());
                refreshQuizList();
                JOptionPane.showMessageDialog(this, "Loaded successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void switchTheme(String mode) {
        try {
            if ("dark".equals(mode)) UIManager.setLookAndFeel(new FlatDarkLaf());
            else UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
            splitPane.setBackground(UIManager.getColor("Panel.background"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame(new QuizController()).setVisible(true));
    }
}

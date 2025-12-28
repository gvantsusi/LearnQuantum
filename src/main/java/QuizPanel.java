import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuizPanel extends JPanel {

    private final QuizManager quizManager;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton nextButton;
    private JButton submitButton;

    private JTable resultTable;
    private DefaultTableModel resultTableModel;

    private List<QuizResultEntry> currentResults = new ArrayList<>();

    private UserProgress progress;

    public QuizPanel(QuizManager quizManager, UserProgress progress) {
        this.quizManager = quizManager;
        this.progress = progress;
        initGui();
        startNewQuiz();
    }

    private void initGui() {
        setLayout(new BorderLayout());

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        questionLabel = new JLabel("Question");
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        questionPanel.add(questionLabel);

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton("Option " + (i + 1));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            buttonGroup.add(optionButtons[i]);
            questionPanel.add(optionButtons[i]);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nextButton = new JButton("Next");
        submitButton = new JButton("Submit");
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(questionPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        resultTableModel = new DefaultTableModel(
                new Object[]{"Question", "Your Answer", "Correct Answer", "Correct?"}, 0
        );
        resultTable = new JTable(resultTableModel);
        JScrollPane tableScroll = new JScrollPane(resultTable);
        add(tableScroll, BorderLayout.CENTER);

        nextButton.addActionListener(e -> onNext());
        submitButton.addActionListener(e -> onSubmit());
    }

    private void startNewQuiz() {
        quizManager.reset();
        currentResults.clear();
        resultTableModel.setRowCount(0);
        showCurrentQuestion();
    }

    private void showCurrentQuestion() {
        Question q = quizManager.getCurrentQuestion();
        buttonGroup.clearSelection();

        if (q == null) {
            questionLabel.setText("No more questions. Click Submit to finish.");
            for (JRadioButton rb : optionButtons) {
                rb.setVisible(false);
            }
            nextButton.setEnabled(false);
            return;
        }

        questionLabel.setText("<html><body style='width:600px'>" + q.getText() + "</body></html>");

        int i = 0;
        for (; i < q.getOptions().size() && i < optionButtons.length; i++) {
            optionButtons[i].setText(q.getOptions().get(i));
            optionButtons[i].setVisible(true);
        }
        for (; i < optionButtons.length; i++) {
            optionButtons[i].setVisible(false);
        }

        nextButton.setEnabled(true);

        // make sure UI refreshes
        revalidate();
        repaint();
    }

    private void onNext() {
        int selectedIndex = getSelectedOptionIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer before clicking Next.");
            return;
        }

        Question current = quizManager.getCurrentQuestion();
        boolean correct = quizManager.answerCurrentQuestion(selectedIndex);

        String userAns = current.getOptions().get(selectedIndex);
        String correctAns = current.getOptions().get(current.getCorrectIndex());

        QuizResultEntry entry = new QuizResultEntry(
                current.getText(),
                userAns,
                correctAns,
                correct
        );
        currentResults.add(entry);

        if (quizManager.hasMoreQuestions()) {
            showCurrentQuestion();
        } else {
            questionLabel.setText("Quiz finished. Click Submit to see results.");
            for (JRadioButton rb : optionButtons) {
                rb.setVisible(false);
            }
            nextButton.setEnabled(false);
        }
    }

    private void onSubmit() {
        // handle last unanswered question (if user didn't press Next)
        Question current = quizManager.getCurrentQuestion();
        if (current != null) {
            int selectedIndex = getSelectedOptionIndex();
            if (selectedIndex != -1) {
                boolean correct = quizManager.answerCurrentQuestion(selectedIndex);

                String userAns = current.getOptions().get(selectedIndex);
                String correctAns = current.getOptions().get(current.getCorrectIndex());

                QuizResultEntry entry = new QuizResultEntry(
                        current.getText(),
                        userAns,
                        correctAns,
                        correct
                );
                currentResults.add(entry);
            }
        }

        // fill table
        resultTableModel.setRowCount(0);
        for (QuizResultEntry entry : currentResults) {
            resultTableModel.addRow(new Object[]{
                    entry.getQuestionText(),
                    entry.getUserAnswer(),
                    entry.getCorrectAnswer(),
                    entry.isCorrect() ? "Yes" : "No"
            });
        }

        int score = quizManager.getScore();
        int total = quizManager.getTotalQuestions();

        JOptionPane.showMessageDialog(this,
                "Your score: " + score + " / " + total,
                "Quiz Result",
                JOptionPane.INFORMATION_MESSAGE);

        if (progress != null) {
            progress.addQuizResult(score, total);
        }

        ProgressStorage.saveProgress(progress);
    }

    private int getSelectedOptionIndex() {
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isVisible() && optionButtons[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public void setProgress(UserProgress progress) {
        this.progress = progress;
    }

    public void updateProgressFromUI() {
        
    }
}

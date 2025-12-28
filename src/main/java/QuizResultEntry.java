import java.io.Serializable;

public class QuizResultEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String questionText;
    private final String userAnswer;
    private final String correctAnswer;
    private final boolean correct;

    public QuizResultEntry(String questionText, String userAnswer, String correctAnswer, boolean correct) {
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}

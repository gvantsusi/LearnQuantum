import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizManager {

    private final List<Question> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    public QuizManager() {
        loadDefaultQuestions();
        shuffle();
    }

    private void loadDefaultQuestions() {
        List<String> o1 = List.of("One state at a time", "Many states at once", "No state", "Random state");
        questions.add(new Question("What does superposition mean in quantum mechanics?", o1, 1));

        List<String> o2 = List.of("Classical correlation", "No correlation", "Quantum correlation beyond distance", "Noise");
        questions.add(new Question("Entanglement describes...", o2, 2));

        List<String> o3 = List.of("Deterministic hidden variables", "Wavefunction collapse", "Always exact prediction", "No probabilities");
        questions.add(new Question("Measurement in quantum mechanics is associated with...", o3, 1));
    }

    public void shuffle() {
        Collections.shuffle(questions);
        currentIndex = 0;
        score = 0;
    }

    public Question getCurrentQuestion() {
        if (questions.isEmpty() || currentIndex >= questions.size()) {
            return null;
        }
        return questions.get(currentIndex);
    }

    public boolean answerCurrentQuestion(int selectedIndex) {
        Question q = getCurrentQuestion();
        if (q == null) return false;
        boolean correct = (selectedIndex == q.getCorrectIndex());
        if (correct) {
            score++;
        }
        currentIndex++;
        return correct;
    }

    public boolean hasMoreQuestions() {
        return currentIndex < questions.size();
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public void reset() {
        shuffle();
    }
}

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuizManagerTest {

    @Test
    public void testScoreIncreasesOnCorrectAnswer() {
        QuizManager qm = new QuizManager();
        Question q = qm.getCurrentQuestion();
        int correctIndex = q.getCorrectIndex();

        boolean correct = qm.answerCurrentQuestion(correctIndex);

        assertTrue(correct);
        assertEquals(1, qm.getScore());
    }

    @Test
    public void testTotalQuestionsPositive() {
        QuizManager qm = new QuizManager();
        assertTrue(qm.getTotalQuestions() > 0);
    }
}

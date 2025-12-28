import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProgressStorageTest {

    @Test
    public void testSaveAndLoadRoundTrip() {
        UserProgress p = new UserProgress("testUser");
        p.setLastTopic("Superposition");
        p.setLastView("LEARN");
        p.addQuizResult(2, 3);

        ProgressStorage.saveProgress(p);
        UserProgress loaded = ProgressStorage.loadProgress();

        assertEquals("testUser", loaded.getUsername());
        assertEquals("Superposition", loaded.getLastTopic());
        assertEquals("LEARN", loaded.getLastView());
        assertFalse(loaded.getQuizHistory().isEmpty());
    }
}

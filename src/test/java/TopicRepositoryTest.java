import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TopicRepositoryTest {

    @Test
    public void testTopicsNotEmpty() {
        TopicRepository repo = new TopicRepository();
        assertFalse(repo.getAllTopics().isEmpty());
    }

    @Test
    public void testContentNotNull() {
        TopicRepository repo = new TopicRepository();
        String anyTopic = repo.getAllTopics().keySet().iterator().next();
        String content = repo.getContentFor(anyTopic);
        assertNotNull(content);
        assertFalse(content.isBlank());
    }
}

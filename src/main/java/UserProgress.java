import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String lastTopic;
    private String lastView;

    private final List<String> quizHistory = new ArrayList<>();

    public UserProgress(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getLastTopic() {
        return lastTopic;
    }

    public void setLastTopic(String lastTopic) {
        this.lastTopic = lastTopic;
    }

    public String getLastView() {
        return lastView;
    }

    public void setLastView(String lastView) {
        this.lastView = lastView;
    }

    public List<String> getQuizHistory() {
        return quizHistory;
    }

    public void addQuizResult(int score, int total) {
        String entry = LocalDateTime.now() + " - Score: " + score + "/" + total;
        quizHistory.add(entry);
    }
}

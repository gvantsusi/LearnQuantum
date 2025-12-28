import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class TopicRepository {

    private final Map<String, String> topics = new LinkedHashMap<>();

    public TopicRepository() {
        try {
            loadFromJson("topics.json");
        } catch (Exception e) {
            System.err.println("Could not load topics.json, using fallback topics.");
            loadFallbackTopics();
        }
    }

    private void loadFromJson(String fileName) throws IOException {
        String json = Files.readString(Paths.get(fileName)).trim();

        // remove { and }
        json = json.substring(1, json.length() - 1).trim();

        // split entries by comma
        String[] entries = json.split("\",");

        for (String entry : entries) {
            String[] pair = entry.split(":", 2);

            String key = pair[0].trim()
                    .replace("\"", "")
                    .replace(",", "");

            String value = pair[1].trim()
                    .replace("\"", "")
                    .replace("}", "")
                    .trim();

            topics.put(key, value);
        }
    }

    private void loadFallbackTopics() {
        topics.put("Superposition",
                "Superposition is the principle that a quantum system can exist in multiple states simultaneously.");
        topics.put("Entanglement",
                "Entanglement describes non-classical correlations between particles.");
        topics.put("Measurement",
                "Measurement causes the collapse of the wavefunction.");
    }

    public Map<String, String> getAllTopics() {
        return topics;
    }

    public String getContentFor(String topicName) {
        return topics.get(topicName);
    }
}

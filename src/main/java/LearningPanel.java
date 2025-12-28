import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LearningPanel extends JPanel {

    private final TopicRepository topicRepository;
    private JComboBox<String> topicComboBox;
    private JTextArea contentArea;

    private UserProgress progress;

    public LearningPanel(TopicRepository topicRepository, UserProgress progress) {
        this.topicRepository = topicRepository;
        this.progress = progress;
        initGui();
        restoreFromProgress();
    }

    public LearningPanel(String jsonFilePath, UserProgress progress) throws IOException {
        this.topicRepository = new TopicRepository();
        this.progress = progress;
        initGui();
        restoreFromProgress();
    }

    private void initGui() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Topic:"));

        topicComboBox = new JComboBox<>();
        for (String topic : topicRepository.getAllTopics().keySet()) {
            topicComboBox.addItem(topic);
        }
        topPanel.add(topicComboBox);

        add(topPanel, BorderLayout.NORTH);

        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentArea);

        add(scrollPane, BorderLayout.CENTER);

        topicComboBox.addActionListener(e -> {
            String selected = (String) topicComboBox.getSelectedItem();
            if (selected != null) {
                contentArea.setText(topicRepository.getContentFor(selected));
                if (progress != null) {
                    progress.setLastTopic(selected);
                }
            }
        });

        if (topicComboBox.getItemCount() > 0 && topicComboBox.getSelectedItem() == null) {
            topicComboBox.setSelectedIndex(0);
        }
    }

    private void restoreFromProgress() {
        if (progress == null) return;

        String lastTopic = progress.getLastTopic();
        if (lastTopic != null) {
            topicComboBox.setSelectedItem(lastTopic);
        }
    }

    public void setProgress(UserProgress progress) {
        this.progress = progress;
        restoreFromProgress();
    }

    public void updateProgressFromUI() {
        if (progress == null) return;
        String selected = (String) topicComboBox.getSelectedItem();
        progress.setLastTopic(selected);
    }
}

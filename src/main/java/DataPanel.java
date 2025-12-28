import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DataPanel extends JPanel {

    private JLabel usernameLabel;
    private JLabel lastTopicLabel;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public DataPanel() {
        setLayout(new BorderLayout());
        
        // Top info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        usernameLabel = new JLabel("Username: ");
        lastTopicLabel = new JLabel("Last Topic: ");

        infoPanel.add(usernameLabel);
        infoPanel.add(lastTopicLabel);

        add(infoPanel, BorderLayout.NORTH);

        // Quiz history table
        tableModel = new DefaultTableModel(
            new Object[]{"Date", "Score"}, 0
        );

        historyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateFromProgress(UserProgress progress) {
        if (progress == null) return;

        usernameLabel.setText("Username: " + progress.getUsername());
        lastTopicLabel.setText("Last Topic: " + progress.getLastTopic());

        tableModel.setRowCount(0); 
        
        for (String historyEntry : progress.getQuizHistory()) {
            tableModel.addRow(new Object[]{ historyEntry, "" });
        }

        revalidate();
        repaint();
    }
}

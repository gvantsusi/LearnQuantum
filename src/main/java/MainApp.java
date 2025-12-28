import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private LearningPanel learningPanel;
    private QuizPanel quizPanel;
    private DataPanel dataPanel;   

    private UserProgress progress;

    public MainApp() {
        super("Quantum Concept Learning App");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Load progress from file (if any)
        progress = ProgressStorage.loadProgress();

        initGui();
        initWindowListener();

        setVisible(true);
    }

    private void initGui() {

        //  MENU BAR 
        JMenuBar menuBar = new JMenuBar();

        JMenu menuLearn = new JMenu("Learn");
        JMenuItem miLearnTopic = new JMenuItem("Learn Topic");
        menuLearn.add(miLearnTopic);

        JMenu menuQuiz = new JMenu("Quiz");
        JMenuItem miTakeQuiz = new JMenuItem("Take Quiz");
        menuQuiz.add(miTakeQuiz);

        JMenu menuData = new JMenu("Data");
        JMenuItem miSave = new JMenuItem("Save Progress");
        JMenuItem miLoad = new JMenuItem("Load Progress");
        JMenuItem miViewData = new JMenuItem("View Progress"); 
        JMenuItem miExit = new JMenuItem("Exit");

        menuData.add(miSave);
        menuData.add(miLoad);
        menuData.add(miViewData);  
        menuData.addSeparator();
        menuData.add(miExit);

        menuBar.add(menuLearn);
        menuBar.add(menuQuiz);
        menuBar.add(menuData);
        setJMenuBar(menuBar);


        // CENTRAL PANEL
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        TopicRepository topicRepo = new TopicRepository();
        QuizManager quizManager = new QuizManager();

        learningPanel = new LearningPanel(topicRepo, progress);
        quizPanel = new QuizPanel(quizManager, progress);
        dataPanel = new DataPanel();             

        cardPanel.add(learningPanel, "LEARN");
        cardPanel.add(quizPanel, "QUIZ");
        cardPanel.add(dataPanel, "DATA");          

        add(cardPanel, BorderLayout.CENTER);


        // Restore last view
        String lastView =
                (progress != null && progress.getLastView() != null)
                        ? progress.getLastView()
                        : "LEARN";

        showCard(lastView);


        //  MENU ACTIONS 

        miLearnTopic.addActionListener(e -> {
            showCard("LEARN");
            if (progress != null) progress.setLastView("LEARN");
        });

        miTakeQuiz.addActionListener(e -> {
            showCard("QUIZ");
            if (progress != null) progress.setLastView("QUIZ");
        });


        miViewData.addActionListener(e -> {              
            dataPanel.updateFromProgress(progress);
            showCard("DATA");
            if (progress != null) progress.setLastView("DATA");
        });


        miSave.addActionListener(e -> saveProgress());


        miLoad.addActionListener(e -> {
            UserProgress loaded = ProgressStorage.loadProgress();
            if (loaded != null) {
                this.progress = loaded;

                learningPanel.setProgress(progress);
                quizPanel.setProgress(progress);

                //  Refresh displays
                learningPanel.updateProgressFromUI();
                quizPanel.updateProgressFromUI();
                dataPanel.updateFromProgress(progress);

                JOptionPane.showMessageDialog(this, "Progress loaded.");
            } else {
                JOptionPane.showMessageDialog(this, "No saved progress found.");
            }
        });


        miExit.addActionListener(e -> exitApp());
    }


    private void initWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });
    }


    private void showCard(String name) {
        cardLayout.show(cardPanel, name);
    }


    private void saveProgress() {
        if (progress == null) {
            progress = new UserProgress("student");
        }

        learningPanel.updateProgressFromUI();
        quizPanel.updateProgressFromUI();

        ProgressStorage.saveProgress(progress);
        JOptionPane.showMessageDialog(this, "Progress saved.");
    }


    private void exitApp() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Save progress before exiting?",
                "Exit",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (result == JOptionPane.CANCEL_OPTION) return;

        if (result == JOptionPane.YES_OPTION) {
            saveProgress();
        }

        dispose();
        System.exit(0);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}

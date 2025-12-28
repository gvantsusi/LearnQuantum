import javax.swing.*;
import java.io.*;

public class ProgressStorage {

    private static final String FILE_NAME = "progress.ser";

    public static void saveProgress(UserProgress progress) {
        if (progress == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(progress);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to save progress: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static UserProgress loadProgress() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            return new UserProgress("student");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object o = ois.readObject();
            if (o instanceof UserProgress) {
                return (UserProgress) o;
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to load progress, starting with defaults.\n" + e.getMessage(),
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        return new UserProgress("student");
    }
}

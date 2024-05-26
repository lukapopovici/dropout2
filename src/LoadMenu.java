import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoadMenu extends JPanel implements Menu {
    private List<Button> buttons;
    private JFrame frame;
    protected Boolean Over = null;
    protected State Altered = null;
    private SQLiteDBCreator dbCreator;

    public LoadMenu() throws SQLException {
        buttons = new ArrayList<>();
        initializeFrame();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    handleMouseClick(e.getX(), e.getY());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttons.add(new Button("ERASE", 750, 500, 100, 50));
        setPreferredSize(new Dimension(1000, 1000));
        Altered = State.LOAD;
        Over = false;
        try {
            dbCreator = SQLiteDBCreator.getInstance("jdbc:sqlite:database.db");
            dbCreator.displayAllRecords();
            initializeButtonsFromDatabase();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeFrame() {
        frame = new JFrame("PauseMenu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
    }

    private void initializeButtonsFromDatabase() throws SQLException {
        List<CustomRecord> lastFiveRecords = dbCreator.getLastFiveCustomRecords();
        int yPos = 100;
        for (CustomRecord record : lastFiveRecords) {
            buttons.add(new Button(record.getDate(), 450, yPos, 100, 50));
            yPos += 100;
        }
    }

    public void Display() {
        frame.setVisible(true);
    }

    public void handleMouseClick(int x, int y) throws SQLException {
        for (Button button : buttons) {
            if (button.isClicked(x, y)) {
                doAction(button.getLabel());
            }
        }
    }

    public void doAction(String dateLabel) throws SQLException {
        SetOverTrue();
        try {
            CustomRecord record = dbCreator.getRecordByDate(dateLabel);
            if (record != null) {
                String recordDetails = "Date: " + record.getDate() + "\nScore: " + record.getScore() + "\nLayout: " + record.getLayout();
                Arena.getInstance().GetLevelLayoutFromString(record.getLayout());
                Arena.getInstance().setLevelIndex(record.getScore());
                Altered=State.GAME;
            } else {
                JOptionPane.showMessageDialog(frame, "Record not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(dateLabel=="ERASE"){
            dbCreator.DELETE();;
            Altered=State.GAME;
        }
    }

    public void SetOverTrue() {
        Over = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Button button : buttons) {
            button.draw(g);
        }
    }

    public static void main(String[] args) throws SQLException {
        LoadMenu menu = new LoadMenu();
        menu.Display();
    }

    public State ReturnState() {
        return Altered;
    }

    public void Update() throws SQLException {
        initializeButtonsFromDatabase();
    }

    public void Begin() {
        Altered = State.LOAD;
        if (Over)
            Over = false;
    }
}

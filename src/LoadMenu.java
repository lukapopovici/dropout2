import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoadMenu extends JPanel {
    private List<Button> buttons;
    private JFrame frame;
    protected Boolean Over = null;

    protected State Altered = null;
    private SQLiteDBCreator dbCreator;
    public LoadMenu() {
        buttons = new ArrayList<>();
        initializeFrame();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        setPreferredSize(new Dimension(1000, 1000));
        Altered = State.PAUSE;
        Over = false;

        try {
            dbCreator = SQLiteDBCreator.getInstance("jdbc:sqlite:test.db");
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
            buttons.add(new Button(record.getLabel(), 450, yPos, 100, 50));
            yPos += 100;
        }
    }

    public void Display() {
        frame.setVisible(true);
    }

    private void handleMouseClick(int x, int y) {
        for (Button button : buttons) {
            if (button.isClicked(x, y)) {
                doAction(button.getLabel());
            }
        }
    }

    protected void doAction(String action) {
        SetOverTrue();
        switch (action) {
            case "Resume":
                System.out.println("sadasd");
                Altered = State.GAME;
                break;
            case "Quit":
                System.out.println("sadasd");
                Altered = State.QUIT;
                break;
        }
    }

    private void SetOverTrue() {
        Over = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Button button : buttons) {
            button.draw(g);
        }
    }

    public static void main(String[] args) {
            LoadMenu menu = new LoadMenu();
            menu.Display();

    }

    public State ReturnState() {
        System.out.println(Altered);
        return Altered;
    }

    public void Update() {
    }

    public void Begin() {
        Altered = State.PAUSE;
        if (Over)
            Over = false;
    }
}

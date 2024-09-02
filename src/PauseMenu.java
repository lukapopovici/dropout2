import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//MENIU PENTRU STAREA DE PAUZA
public class PauseMenu extends JPanel implements Menu {
    private List<Button> buttons;
    private JFrame frame;
    protected Boolean Over = null;

    private SQLiteDBCreator dbCreator;
    protected State Altered = null;
    public PauseMenu() {
        buttons = new ArrayList<>();
        buttons.add(new Button("Resume", 450, 100, 100, 50));
        buttons.add(new Button("Save Game", 450, 200, 100, 50));
        buttons.add(new Button("Load Game", 450, 300, 100, 50));
        buttons.add(new Button("Quit", 450, 400, 100, 50));
        buttons.add(new Button("Settings", 450, 500, 100, 50));

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

        setPreferredSize(new Dimension(1000, 1000));
        Altered=State.PAUSE;
        Over=false;
        try {
            dbCreator = SQLiteDBCreator.getInstance("jdbc:sqlite:database.db");
            dbCreator.displayAllRecords();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initializeFrame() {
        frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
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

    public void doAction(String action) throws SQLException {
        SetOverTrue();
        switch (action) {
            case "Resume":
                Altered=State.GAME;
                break;
            case "Save Game":
                int unixTime = (int) (System.currentTimeMillis() / 1000L);
                dbCreator.addRecord(unixTime,Arena.getInstance().GetIndex(),Arena.getInstance().BuildLevelLayout());
                Altered=State.GAME;
                break;
            case "Load Game":
                Altered=State.LOAD;
                break;
            case "Quit":
                Altered=State.QUIT;
                break;
        }
    }

    public void SetOverTrue() {
        Over=true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Button button : buttons) {
            button.draw(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartMenu menu = new StartMenu();
            menu.Display();
        });
    }


    public State ReturnState() {
        return Altered;
    }

    public void Update() {
    }

    public void Begin() {
        Altered=State.PAUSE;
        if(Over)
            Over=false;
    }
}

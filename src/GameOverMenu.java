import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
//MOSTENIT DE LA INTERFATA MENIU, MENIUL DE GAME OVER. CASTIG/MOR
public class GameOverMenu extends JPanel implements Menu {
    private List<Button> buttons;
    private JFrame frame;
    private Boolean Over = null;
    private State Altered = null;
    private static GameOverMenu instance = null;
    private String displayText;

    private GameOverMenu() {
        buttons = new ArrayList<>();
        buttons.add(new Button("Retry", 450, 100, 100, 50));
        buttons.add(new Button("Load Game", 450, 200, 100, 50));
        buttons.add(new Button("Quit", 450, 300, 100, 50));
        buttons.add(new Button("Settings", 450, 400, 100, 50));

        displayText = "";

        initializeFrame();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        setPreferredSize(new Dimension(1000, 1000));
        Altered = State.OVER;
        Over = false;
    }

    public static GameOverMenu getInstance() {
        if (instance == null) {
            instance = new GameOverMenu();
        }
        return instance;
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

    public void handleMouseClick(int x, int y) {
        for (Button button : buttons) {
            if (button.isClicked(x, y)) {
                doAction(button.getLabel());
            }
        }
    }

    public void doAction(String action) {
        SetOverTrue();
        switch (action) {
            case "Retry":
                Arena.getInstance().cleanMapOfAll();
                Arena.getInstance().setLevelIndex(0);
                Arena.getInstance().GetLevelLayout("Levels/LEVEL1");
                Altered = State.GAME;
                break;
            case "Load Game":
                Altered = State.LOAD;
                break;
            case "Quit":
                Altered = State.QUIT;
                break;
        }
    }

    public void SetOverTrue() {
        Over = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(displayText, 450, 50);
        for (Button button : buttons) {
            button.draw(g);
        }
    }

    public void Begin() {
        Altered = State.OVER;
        if (Over)
            Over = false;
    }

    public State ReturnState() {
        System.out.println(Altered);
        return Altered;
    }

    public void Update() {
        // No implementation required as of now.
    }

    public void SetText(String Result) {
        this.displayText=Result;
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

//MENIU PENTRU START JOC
public class StartMenu extends JPanel implements Menu {
    private List<Button> buttons;
    private JFrame frame;
    protected Boolean Over = null;

    protected State Altered = null;
    public StartMenu() {
        buttons = new ArrayList<>();
        buttons.add(new Button("Start Game", 450, 100, 100, 50));
        buttons.add(new Button("Load Game", 450, 200, 100, 50));
        buttons.add(new Button("Quit", 450, 300, 100, 50));
        buttons.add(new Button("Settings", 450, 400, 100, 50));

        initializeFrame();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        setPreferredSize(new Dimension(1000, 1000));
        Altered=State.START;
        Over=false;

    }

    private void initializeFrame() {
        frame = new JFrame("GOVERMenu");
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
            case "Start Game":
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


    public void Begin() {
        Altered=State.START;
        if(Over)
            Over=false;
    }
    public State ReturnState() {
        return Altered;
    }

    public void Update() {
    }
}

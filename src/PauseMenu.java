import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PauseMenu extends JPanel {
    private List<Button> buttons;
    private JFrame frame;
    protected Boolean Over = null;

    protected State Altered = null;
    public PauseMenu() {
        buttons = new ArrayList<>();
        buttons.add(new Button("Resume", 450, 100, 100, 50));
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
        Altered=State.PAUSE;
        Over=false;

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
                Altered=State.GAME;
                break;
            case "Quit":
                System.out.println("sadasd");
                Altered=State.QUIT;
                break;
        }
    }

    private void SetOverTrue() {
        Over=true;
    }

    @Override
    protected void paintComponent(Graphics g) {
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
        System.out.println(Altered);
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

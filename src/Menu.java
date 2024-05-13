import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class Menu extends JPanel implements Screen{
    protected List<Button> buttons;
    protected JFrame frame;

    //variabile pentru state
    protected Boolean Over = false;

    protected State Altered;

    protected void initializeFrame() {
        frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
    }

    public void Display() {
        frame.setVisible(true);
    }

    protected void handleMouseClick(int x, int y) {
        for (Button button : buttons) {
            if (button.isClicked(x, y)) {
                doAction(button.getLabel());
            }
        }
    }

    protected void doAction(String action) {
        switch (action) {
            case "Start Game":
                Altered=State.GAME;
                SetOverTrue();
                break;
            case "Quit":
                System.out.println("sadasd");
                Altered=State.QUIT;
                SetOverTrue();
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Button button : buttons) {
            button.draw(g);
        }
    }



    @Override
    public void Update() {
            this.Display();
    }
    @Override
    public void Begin() {
        //this.Display();
    }

    @Override
    public State ReturnState() {
        if(!Over)
            return Altered;
        else{
            return State.START;
        }
    }

    @Override
    public void SetOverTrue() {
        Over=true;
    }


}

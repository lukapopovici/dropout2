import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

//CLASA "SEALED" PENTRU STARILE PE CARE LA POATE LUA JOCUL MEU
enum State {
    START,
    GAME,
    PAUSE,
    LOAD,
    SETTINGS,
    OVER,
    QUIT
}

public class Game {

    private final boolean[] keys = new boolean[256];

    private KeyListener keyListener = new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e) {
            keys[e.getKeyCode()] = true;
            updateStatusLabel("Pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    Arena.getInstance().MovePlayerOne('w');
                    break;
                case KeyEvent.VK_A:
                    Arena.getInstance().MovePlayerOne('a');
                    break;
                case KeyEvent.VK_S:
                    Arena.getInstance().MovePlayerOne('s');
                    break;
                case KeyEvent.VK_D:
                    Arena.getInstance().MovePlayerOne('d');
                    break;
                case KeyEvent.VK_Q:
                    Arena.getInstance().MovePlayerOne('q');
                    break;
                case KeyEvent.VK_P:
                    Arena.getInstance().Pause();
                    break;
                case KeyEvent.VK_E:
                    Arena.getInstance().MovePlayerOne('e');
                    break;
                case KeyEvent.VK_LEFT:
                    Arena.getInstance().MovePlayerTwo('<');
                    break;
                case KeyEvent.VK_UP:
                    Arena.getInstance().MovePlayerTwo('^');
                    break;
                case KeyEvent.VK_DOWN:
                    Arena.getInstance().MovePlayerTwo('/');
                    break;
                case KeyEvent.VK_RIGHT:
                    Arena.getInstance().MovePlayerTwo('>');
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keys[e.getKeyCode()] = false;
            updateStatusLabel("Released: " + KeyEvent.getKeyText(e.getKeyCode()));
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_W:
                case KeyEvent.VK_A:
                case KeyEvent.VK_S:
                case KeyEvent.VK_D:
                case KeyEvent.VK_Q:
                case KeyEvent.VK_E:
                    Arena.getInstance().MovePlayerOne('N');
                    Arena.getInstance().MovePlayerTwo('N');
                    break;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    };

    private JFrame frame;
    private Arena arena;
    private StartMenu StartMenu= null;
    private LoadMenu LoadMenu=null;
    private PauseMenu PauseMenu = null;

    private  GameOverMenu GameOverMenu = null;

    private State gameState;
    private State nextState;
    private JLabel statusLabel;


    public Game() throws SQLException {
        StartMenu= new StartMenu();
        PauseMenu= new PauseMenu();
        LoadMenu = new LoadMenu();
        GameOverMenu= GameOverMenu.getInstance();
        gameState = State.START;
        nextState = State.START;
        frame = new JFrame("DROPOUT");
        arena = Arena.CreateInstance(1000, 1000);
        statusLabel = new JLabel("Key Status");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 1100);
        frame.setLayout(new BorderLayout());
        frame.add(StartMenu, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(keyListener);
        frame.setFocusable(true);
    }

    void run() throws SQLException {
        long lastTime = System.nanoTime();
        double amountOfTicks = 30.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }

    void update() throws SQLException {
        if (gameState != nextState) {
            gameState = nextState;
            switch (gameState) {
                case START:
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(StartMenu, BorderLayout.CENTER);
                    StartMenu.Begin();
                    StartMenu.Update();
                    break;
                case GAME:
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(arena, BorderLayout.CENTER);
                    arena.Begin();
                    arena.Update();
                    break;
                case PAUSE:
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(PauseMenu, BorderLayout.CENTER);
                    PauseMenu.Begin();
                    PauseMenu.Update();
                    break;
                case LOAD:
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(LoadMenu, BorderLayout.CENTER);
                    LoadMenu.Begin();
                    LoadMenu.Update();
                    break;
                case OVER:
                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(GameOverMenu, BorderLayout.CENTER);
                    GameOverMenu.Begin();
                    GameOverMenu.Update();
                    break;
                case QUIT:
                    System.exit(0);
                    break;
            }
        } else {
            switch (gameState) {
                case START:
                    State startMenuState = StartMenu.ReturnState();
                    if (startMenuState != null) {
                        nextState = startMenuState;
                    }
                    break;
                case GAME:
                    State arenaMenuState = arena.ReturnState();
                    if (arenaMenuState != null) {
                        nextState = arenaMenuState;
                    }
                    arena.Update();
                    break;
                case OVER:
                    nextState= GameOverMenu.ReturnState();
                    break;
                case PAUSE:
                    nextState = PauseMenu.ReturnState();
                    break;
                case LOAD:
                    nextState= LoadMenu.ReturnState();
                    break;
            }
        }
        frame.revalidate();
        frame.repaint();
    }


    void updateStatusLabel(String text) {
        statusLabel.setText(text);
    }

}

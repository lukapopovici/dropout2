import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Arena extends JPanel implements Screen{
    private static Arena instance;
    private int width;
    private int height;
    private List<Player> players;
    private List<Enemy> enemies;
    private List<Obstacle> obs;

    Player playerOne;

    //Var pentru tranzitii
    protected Boolean Over = false;

    protected State Altered;

    private Arena(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        players = new ArrayList<>();
        enemies = new ArrayList<>();
        obs = new ArrayList<>();
        players.add(new Player(500, 500));
        playerOne = players.getFirst();
        GetLevelLayout("Levels/LEVEL1");
    }

    public static Arena getInstance() {
        return instance;
    }

    public static Arena CreateInstance(int width, int height) {
        if (instance == null) {
            instance = new Arena(width, height);
        }
        return instance;
    }

    private void GetLevelLayout(String fileName) {
        final int MAX_DIMENSION = 150;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] groups = line.split("\\+");
                for (String group : groups) {
                    String[] parts = group.split("/");
                    if (parts.length == 3) {
                        String letter = parts[0];
                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);
                        if ((x < 0 || x > 1000) || (y < 0 || y > 1000)) {
                            System.out.println("Invalid SEED: Coordinates out of range (0-1000)");
                            continue;
                        }
                        switch (letter) {
                            case "E":
                                enemies.add(new Enemy(x, y));
                                break;
                            case "T":
                                obs.add(new Obstacle(x, y));
                                break;
                            default:
                                System.out.println("Invalid SEED: Unknown letter " + letter);
                                break;
                        }
                    } else if (parts.length == 5) {
                        String letter = parts[0];
                        int x1 = Integer.parseInt(parts[1]);
                        int y1 = Integer.parseInt(parts[2]);
                        int x2 = Integer.parseInt(parts[3]);
                        int y2 = Integer.parseInt(parts[4]);
                        if ((x1 < 0 || x1 > 1000) || (y1 < 0 || y1 > 1000)) {
                            System.out.println("Invalid SEED: Coordinates out of range (0-1000)");
                            continue;
                        }
                        if (x2 > MAX_DIMENSION || y2 > MAX_DIMENSION) {
                            System.out.println("Invalid SEED: Obstacle dimension exceeds maximum dimension of 150");
                            continue;
                        }
                        switch (letter) {
                            case "T":
                                obs.add(new Obstacle(x1, y1, x2, y2));
                                break;
                            default:
                                System.out.println("Invalid SEED: Unknown CODE for Entity " + letter);
                                break;
                        }
                    } else {
                        System.out.println("Invalid SEED: Incorrect number of parts");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        drawPlayer(g);
        drawEnemies(g);
        drawTiles(g);

    }

    private void drawEnemies(Graphics g) {
        for (Enemy enemy : enemies) {
            enemy.Draw(g);
        }
    }

    public void drawPlayer(Graphics g) {
        for (Player player : players) {
            player.Draw(g);
        }
    }

    public void drawTiles(Graphics g) {
        for (Obstacle tile : obs) {
            tile.Draw(g);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
    }

    private boolean checkCollision(int playerX, int playerY, int playerWidth, int playerHeight, int obstacleX, int obstacleY, int obstacleWidth, int obstacleHeight) {
        int playerBottomY = playerY + playerHeight;
        int obstacleBottomY = obstacleY + obstacleHeight;

        return (playerX < obstacleX + obstacleWidth &&
                playerX + playerWidth > obstacleX &&
                playerY < obstacleBottomY &&
                playerBottomY > obstacleY);
    }


    private Boolean DoesCollide(int playerX,int playerY,int playerWidth,int playerHeight){
        for (Obstacle tile : obs) {

            if (checkCollision(playerX, playerY, playerWidth, playerHeight, tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight())) {
                System.out.println("col");
                return true;
                    }
            }
            return false;
        }

    private void UpdateEnemies(){
        NotifyEnemies();
        for (Enemy enemy : enemies) {
            enemy.Move();
        }
    }

    public void NotifyEnemies(){
        for (Enemy enemy : enemies) {
            enemy.SetPlayerLocation(playerOne.getX(),playerOne.getY());
        }
    }

    public void MovePlayerOne(char c) {
        if (players.isEmpty()) {
            return;
        }


        switch(c) {
            case 'a':
                int newX = playerOne.getX() - playerOne.GetXSpeed();
                if (newX > 0 && !DoesCollide(newX, playerOne.getY(), playerOne.getWidth(), playerOne.getHeight()))
                    playerOne.MoveLeft();
                break;
            case 'w':
                int newY = playerOne.getVertical() - playerOne.GetYSpeed();
                if (newY > 0 && !DoesCollide(playerOne.getX(), newY, playerOne.getWidth(), playerOne.getHeight()))
                    playerOne.MoveUp();
                break;
            case 's':
                newY = playerOne.getVertical() + playerOne.GetYSpeed();
                if (newY < this.height && !DoesCollide(playerOne.getX(), newY, playerOne.getWidth(), playerOne.getHeight()))
                    playerOne.MoveDown();
                break;
            case 'd':
                newX = playerOne.getX()+ playerOne.GetXSpeed();
                if (newX < this.width && !DoesCollide(newX, playerOne.getY(), playerOne.getWidth(), playerOne.getHeight()))
                    playerOne.MoveRight();
                break;
            case 'q':
                playerOne.AttackBackward();
                break;
            case 'e':
                playerOne.AttackForward();
                break;
            case 'N':
                playerOne.SetIdle();
                break;
        }


    }

    @Override
    public void Update() {
        repaint();
        UpdateEnemies();
    }

    @Override
    public State ReturnState() {
            return Altered;
    }

    @Override
    public void SetOverTrue() {
        Over=true;
    }

    public void Begin() {
        Altered=State.GAME;
        if(Over)
            Over=false;
    }
    public void Pause() {
        System.out.println("paused");
        SetOverTrue();
        Altered=State.PAUSE;
    }
}

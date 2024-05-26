import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Arena extends JPanel implements Screen{
    private static Arena instance;
    private int width;
    private int currentLineIndex = 0;
    private int height;
    private List<Player> players;
    private List<Consumable> consumables;
    private List<Enemy> enemies;
    private List<Obstacle> obs;

    private Timer timer;
    Player playerOne;
    Player playerTwo;
    //Var pentru tranzitii
    protected Boolean Over = false;

    protected State Altered;

    public int GetIndex(){
        return this.currentLineIndex;
    }
    private Arena(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        players = new ArrayList<>();
        enemies = new ArrayList<>();
        consumables = new ArrayList<>();
        consumables.add(new Consumable(400,300));
        obs = new ArrayList<>();
        GetLevelLayout("Levels/LEVEL1");
        FogOFWar.SetPlayerWH(playerOne.getWidth(),playerOne.getHeight());
        startTimer();
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

    public void assignPlayers() {
        try {
            if (players.isEmpty()) {
                throw new Exception("No players available to assign");
            }

            playerOne = players.get(0); // Assign the first player

            if (players.size() > 1) {
                playerTwo = players.get(1); // Assign the second player if available
            } else {
                playerTwo = null;
                throw new Exception("PlayerTwo is not available");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void checkEnemyListEmpty() {
        if (enemies.isEmpty()) {
            if (currentLineIndex < 2) {
                currentLineIndex++;
                GetLevelLayout("Levels/LEVEL1");
            } else {
                Altered = State.OVER;
                GameOverMenu.getInstance().SetText("YOU WON");
            }
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {checkPlayersAlive();checkEnemyListEmpty();}}, 0, 3000);}


    //functia care construieste arena

    public void setLevelIndex(int index) {
        this.currentLineIndex = index;
    }

    // Getter for currentLineIndex
    public int getLevelIndex() {
        return currentLineIndex;
    }
    public void GetLevelLayout(String fileName) {
        cleanMapOfAll();
        enemies.add(new Enemy2(200, 800));
        //de encapsulat in alta functie
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                if (lineCount == currentLineIndex) {
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
                                case "e":
                                    enemies.add(new Enemy2(x, y));
                                    break;
                                case "T":
                                    obs.add(new Obstacle(x, y));
                                    break;
                                case "F":
                                    obs.add(new FogOFWar(x, y));
                                    break;
                                case "P":
                                    players.add(new Player(x, y));
                                    break;
                                case "C":
                                    consumables.add(new Consumable(x, y));
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
                            switch (letter) {
                                case "T":
                                    obs.add(new Obstacle(x1, y1, x2, y2));
                                    break;
                                case "F":
                                    obs.add(new FogOFWar(x1, y1, x2, y2));
                                    break;
                                default:
                                    System.out.println("Invalid SEED: Unknown CODE for Entity " + letter);
                                    break;
                            }
                        } else {
                            System.out.println("Invalid SEED: Incorrect number of parts");
                        }
                    }
                    break;
                }
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assignPlayers();
    }

    public String BuildLevelLayout() {
        StringBuilder levelLayout = new StringBuilder();

        for (Player player : players) {
            levelLayout.append("P/").append(player.getX()).append("/").append(player.getY()).append("+");
        }

        for (Enemy enemy : enemies) {
            if (enemy instanceof Enemy2) {
                levelLayout.append("e/").append(enemy.getX()).append("/").append(enemy.getY()).append("+");
            } else {
                levelLayout.append("E/").append(enemy.getX()).append("/").append(enemy.getY()).append("+");
            }
        }

        for (Obstacle obstacle : obs) {
            if (obstacle instanceof FogOFWar) {
                levelLayout.append("F/").append(obstacle.getX()).append("/").append(obstacle.getY());
                if (obstacle instanceof Obstacle) {
                    Obstacle dimObstacle = (Obstacle) obstacle;
                    levelLayout.append("/").append(dimObstacle.getX()).append("/").append(dimObstacle.getY());
                }
            } else if (obstacle instanceof Obstacle) {
                Obstacle dimObstacle = (Obstacle) obstacle;
                levelLayout.append("T/").append(dimObstacle.getWidth()).append("/").append(dimObstacle.getHeight()).append("/")
                        .append(dimObstacle.getX()).append("/").append(dimObstacle.getY());
            }
            levelLayout.append("+");
        }

        for (Consumable consumable : consumables) {
            levelLayout.append("C/").append(consumable.getX()).append("/").append(consumable.getY()).append("+");
        }


        return levelLayout.toString();
    }

    public void GetLevelLayoutFromString(String levelLayout) {
        System.out.println(levelLayout);
        cleanMapOfAll();
        enemies.add(new Enemy2(200,800));
        String[] groups = levelLayout.split("\\+");
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
                    case "e":
                        enemies.add(new Enemy2(x, y));
                        break;
                    case "T":
                        obs.add(new Obstacle(x, y));
                        break;
                    case "F":
                        obs.add(new FogOFWar(x, y));
                        break;
                    case "P":
                        players.add(new Player(x, y));
                        break;
                    case "C":
                        consumables.add(new Consumable(x, y));
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
                switch (letter) {
                    case "T":
                        System.out.println("asdasdASSSSSSSSSSSSSS");
                        obs.add(new Obstacle(x2, y2, x1, y1));
                        break;
                    case "F":
                        obs.add(new FogOFWar(x1, y1, x2, y2));
                        break;
                    default:
                        System.out.println("Invalid SEED: Unknown CODE for Entity " + letter);
                        break;
                }
            } else {
                System.out.println("Invalid SEED: Incorrect number of parts");
            }
        }
        assignPlayers();
    }

    public void cleanMapOfAll() {
        players.clear();
        enemies.clear();
        obs.clear();
    }

    public void cleanupDead() {
        List<Enemy> aliveEnemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.CheckAlive()) {
                aliveEnemies.add(enemy);
            }
        }
        enemies = aliveEnemies;
    }

    public void cleanupConsumed() {
        List<Consumable> cons = new ArrayList<>();
        for (Consumable c : consumables) {
            if (!c.hasBeenTouched()) {
               cons.add(c);
            }
        }
      consumables=cons;
    }
    private void checkPlayersAlive() {
        boolean anyAlive = false;
        for (Player player : players) {
            if (player.CheckAlive()) {
                anyAlive = true;
                break;
            }
        }
        if (!anyAlive) {
            SetOverTrue();
            Altered = State.OVER;
            GameOverMenu.getInstance().SetText("YOU LOST");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        drawPlayer(g);
        drawEnemies(g);
        drawConsumables(g);
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

    public void drawConsumables(Graphics g) {
        for (Consumable c : consumables) {
            c.Draw(g);
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

    private void UpdateEnemies(){
        NotifyEnemies();
        for (Enemy enemy : enemies) {
            enemy.Move();
        }
    }

    public void NotifyEnemies() {
        for (Enemy enemy : enemies) {
            Player closestPlayer = findClosestPlayer(enemy);
            if (closestPlayer != null) {
                enemy.SetTargetLocation(closestPlayer.getX(), closestPlayer.getY());
            }

            if (enemy instanceof Enemy2) {
                Consumable closestConsumable = findClosestConsumable(enemy);
                if (closestConsumable != null) {
                    ((Enemy2) enemy).SetTargetLocation(closestConsumable.getX(), closestConsumable.getY());
                }
            }
        }
    }

    private Player findClosestPlayer(Enemy enemy) {
        Player closestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        for (Player player : players) {
            double distance = calculateDistance(enemy.getX(), enemy.getY(), player.getX(), player.getY());
            if (distance < minDistance) {
                minDistance = distance;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }

    private Consumable findClosestConsumable(Enemy enemy) {
        Consumable closestConsumable = null;
        double minDistance = Double.MAX_VALUE;

        for (Consumable consumable : consumables) {
            double distance = calculateDistance(enemy.getX(), enemy.getY(), consumable.getX(), consumable.getY());
            if (distance < minDistance) {
                minDistance = distance;
                closestConsumable = consumable;
            }
        }
        return closestConsumable;
    }

    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }


    public void MovePlayerOne(char c) {
        if (players.isEmpty()) {
            return;
        }


        switch(c) {
            case 'a':
                float newX = playerOne.getX() - playerOne.GetXSpeed();
                if (newX > 0 && !CollisionManager.doesCollide(obs,(int) newX, playerOne.getY(), playerOne.getWidth(), playerOne.getHeight()))
                    playerOne.MoveLeft();
                break;
            case 'w':
                float newY = playerOne.getVertical() - playerOne.GetYSpeed();
                if (newY > 0 && !CollisionManager.doesCollide(obs,playerOne.getX(), (int) newY, playerOne.getWidth(), playerOne.getHeight()))
                    playerOne.MoveUp();
                break;
            case 's':
                newY = playerOne.getVertical() + playerOne.GetYSpeed();
                if (newY < this.height && !CollisionManager.doesCollide(obs,playerOne.getX(), (int) newY, playerOne.getWidth(), playerOne.getHeight()))
                    playerOne.MoveDown();
                break;
            case 'd':
                newX = playerOne.getX()+ playerOne.GetXSpeed();
                if (newX < this.width && !CollisionManager.doesCollide(obs,(int) newX, playerOne.getY(), playerOne.getWidth(), playerOne.getHeight()))
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

    public void MovePlayerTwo(char keyCode) {
        try {
            if (playerTwo == null) {
                throw new NullPointerException("PlayerTwo is not initialized");
            }

            switch (keyCode) {
                case '>':
                    float newX = playerTwo.getX() - playerTwo.GetXSpeed();
                    if (newX > 0 && !CollisionManager.doesCollide(obs, (int) newX, playerTwo.getY(), playerTwo.getWidth(), playerTwo.getHeight()))
                        playerTwo.MoveLeft();
                    break;
                case '^':
                    float newY = playerTwo.getVertical() - playerTwo.GetYSpeed();
                    if (newY > 0 && !CollisionManager.doesCollide(obs, playerTwo.getX(), (int) newY, playerTwo.getWidth(), playerTwo.getHeight()))
                        playerTwo.MoveUp();
                    break;
                case '/':
                    newY = playerTwo.getVertical() + playerTwo.GetYSpeed();
                    if (newY < this.height && !CollisionManager.doesCollide(obs, playerTwo.getX(), (int) newY, playerTwo.getWidth(), playerTwo.getHeight()))
                        playerTwo.MoveDown();
                    break;
                case '<':
                    newX = playerTwo.getX() + playerTwo.GetXSpeed();
                    if (newX < this.width && !CollisionManager.doesCollide(obs, (int) newX, playerTwo.getY(), playerTwo.getWidth(), playerTwo.getHeight()))
                        playerTwo.MoveRight();
                    break;
                case 'i':
                    playerTwo.SetIdle();
                    break;
                default:
                    break;
            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void CheckInteractions() {
        CollisionManager.doesEnemyCollide(enemies, playerOne);

        try {
            if (playerTwo == null) {
                throw new NullPointerException("playerTwo is null");
            }
            CollisionManager.doesEnemyCollide(enemies, playerTwo);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        try {
            if (playerTwo == null) {
                throw new NullPointerException("playerTwo is null");
            }
            CollisionManager.doesConsumCollide(consumables, playerTwo);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }

        CollisionManager.doesConsumCollide(consumables, playerOne);


        try {
            if (playerTwo == null) {
                throw new NullPointerException("playerTwo is null");
            }
            CollisionManager.doesPlayerCollide(playerOne, playerTwo);
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
        }
        CollisionManager.doesEnemyCollideWithConsumable(enemies,consumables);
    }

    @Override
    public void Update() {
        CheckInteractions();
        cleanupDead();
        cleanupConsumed();
        repaint();
        UpdateEnemies();
        FogOFWar.SetPLayerPos(playerOne.getX(),playerOne.getY());
        MakeWeakFear();
    }
    void MakeWeakFear(){
        if(enemies.size()==1){
            for (Enemy enemy : enemies) {
                if(!(enemy  instanceof Enemy2)){
                enemy.setMovementStrategy(new RunFromPlayer());
            }
                }
        }
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

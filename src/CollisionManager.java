import java.util.List;

public class CollisionManager {
        public static boolean checkCollision(int playerX, int playerY, int playerWidth, int playerHeight, int obstacleX, int obstacleY, int obstacleWidth, int obstacleHeight) {
            int playerBottomY = playerY + playerHeight;
            int obstacleBottomY = obstacleY + obstacleHeight;

            return (playerX < obstacleX + obstacleWidth &&
                    playerX + playerWidth > obstacleX &&
                    playerY < obstacleBottomY &&
                    playerBottomY > obstacleY);
        }

        public static boolean doesCollide(List<Obstacle> obstacles, int playerX, int playerY, int playerWidth, int playerHeight) {
            for (Obstacle tile : obstacles) {
                if (!(tile instanceof FogOFWar)) {
                    if (checkCollision(playerX, playerY, playerWidth, playerHeight, tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight())) {
                        System.out.println("Collision with an obstacle");
                        return true;
                    }
                }
            }
            return false;
        }
    public static boolean doesConsumCollide(List<Consumable> cons,  Player p) {
        int playerX=p.getX();
        int playerY=p.getY();
        int playerWidth=p.getWidth();;
        int playerHeight=p.getHeight();;
        for (Consumable c : cons) {
                if (checkCollision(playerX, playerY, playerWidth, playerHeight, c.getX(), c.getY(),c.getWidth(), c.getHeight())) {
                    p.WasTouched(c);
                    c.WasTouched(p);
                    return true;
                }
            }
        return false;
    }
    public static boolean doesPlayerCollide(Player p1,  Player p2) {
        int playerX=p1.getX();
        int playerY=p2.getY();
        int playerWidth=p2.getWidth();;
        int playerHeight=p2.getHeight();;
            if (checkCollision(playerX, playerY, playerWidth, playerHeight, p2.getX(), p2.getY(),p2.getWidth(), p2.getHeight())) {
                p2.WasTouched(p1);
                p1.WasTouched(p2);
                return true;
            }
        return false;
    }

        public static boolean doesEnemyCollide(List<Enemy> enemies, Player p) {
            int playerX=p.getX();
            int playerY=p.getY();
            int playerWidth=p.getWidth();;
            int playerHeight=p.getHeight();;
            for (Enemy enemy : enemies) {
                if (checkCollision(playerX, playerY, playerWidth, playerHeight, enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight())) {
                    p.WasTouched(enemy);
                    enemy.WasTouched(p);
                            return true;
                }
            }
            return false;
        }
    public static boolean doesEnemyCollideWithConsumable(List<Enemy> enemies, List<Consumable> consumables) {
        for (Enemy enemy : enemies) {
            for (Consumable consumable : consumables) {
                if (checkCollision(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight(),
                        consumable.getX(), consumable.getY(), consumable.getWidth(), consumable.getHeight())) {
                    enemy.WasTouched(consumable);
                    consumable.WasTouched(enemy);
                    return true;
                }
            }
        }
        return false;
    }

}

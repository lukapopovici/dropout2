import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//CLASA PENTRU OBSTACOLE, OBIECTE REPREZENTATE CA ENTITATI PE ECRAN CARE NU SUNT INAMICI
public class Obstacle implements Entity {
    private int X;
    private int Y;
    private int Height = 80;
    private int Width = 100;
    private static BufferedImage obstacleImage;
    private static final String IMAGE_PATH = "Assets/Tiles/TILE.png";

    static {
        try {
            obstacleImage = ImageIO.read(new File(IMAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Obstacle(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    Obstacle(int X, int Y, int Height, int Width) {
        this.X = X;
        this.Y = Y;
        this.Height = Height;
        this.Width = Width;
    }

    @Override
    public int GetDrawPriority() {
        return 0;
    }

    @Override
    public void Draw(Graphics g) {
        if (obstacleImage != null) {
            g.drawImage(obstacleImage, X, Y, Width, Height, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(X, Y, Width, Height);
        }
    }

    public int getWidth() {
        return this.Width;
    }

    public int getHeight() {
        return this.Height;
    }

    @Override
    public int getY() {
        return this.Y;
    }

    @Override
    public int getX() {
        return this.X;
    }

    @Override
    public void WasTouched(Entity e) {
    }

}
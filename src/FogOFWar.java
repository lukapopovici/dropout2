import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//UN OBSTACOL "SPECIAL"
//CAND UN JUCATOR FACE COLLIDE CU O INSTANTA DE FOG OF WAR, ACEASTA NU VA MAI "OBFUSCA" ECRANUL
//NU VA MAI DESENA NIMIC
public class FogOFWar extends Obstacle {
    private static int PlayerX, PlayerY, PlayerWidth, PlayerHeight;
    private static BufferedImage fogImage;
    private static final String IMAGE_PATH = "Assets/Tiles/FOG.png";

    static {
        try {
            fogImage = ImageIO.read(new File(IMAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    FogOFWar(int X, int Y) {
        super(X, Y);
    }

    FogOFWar(int X, int Y, int Height, int Width) {
        super(X, Y, Height, Width);
    }

    static void SetPlayerWH(int Width, int Height) {
        PlayerWidth = Width;
        PlayerHeight = Height;
    }

    static void SetPLayerPos(int X, int Y) {
        PlayerX = X;
        PlayerY = Y;
    }

    public boolean checkCollision() {
        int playerRight = PlayerX + PlayerWidth;
        int playerBottom = PlayerY + PlayerHeight;
        int thisRight = super.getX() + super.getWidth();
        int thisBottom = super.getY() + super.getHeight();
        return !(getX() > playerRight || thisRight < PlayerX || getY() > playerBottom || thisBottom < PlayerY);
    }

    public void Draw(Graphics g) {
        if (!checkCollision()) {
            if (fogImage != null) {
                g.drawImage(fogImage, super.getX(), super.getY(), super.getWidth(), super.getHeight(), null);
            } else {
                g.setColor(Color.YELLOW);
                g.fillRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
            }
        }
    }
}

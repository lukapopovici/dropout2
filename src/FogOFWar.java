import java.awt.*;

//UN OBSTACOL "SPECIAL"
//CAND UN JUCATOR FACE COLLIDE CU O INSTANTA DE FOG OF WAR, ACEASTA NU VA MAI "OBFUSCA" ECRANUL
//NU VA MAI DESENA NIMIC
public class FogOFWar extends Obstacle{
    static  int PlayerX,PlayerY,PlayerWidth,PlayerHeight;
    FogOFWar(int X, int Y) {
        super(X, Y);
    }

    FogOFWar(int X, int Y,int Height,int Width) {
        super(X, Y,Height,Width);
    }

    static void SetPlayerWH(int Width,int Height){
        PlayerWidth=Width;
        PlayerHeight=Height;
    }
    static void SetPLayerPos(int X,int Y){
        PlayerX=X;
        PlayerY=Y;
    }
    public boolean checkCollision() {
        int playerRight = PlayerX + PlayerWidth;
        int playerBottom = PlayerY + PlayerHeight;
        int thisRight = super.getX() + super.getWidth();
        int thisBottom = super.getY() + super.getHeight();
        return !(getX() > playerRight || thisRight < PlayerX || getY() > playerBottom || thisBottom < PlayerY);
    }
    public void Draw(Graphics g) {
        if(!checkCollision()) {
            g.setColor(Color.YELLOW);
            g.fillRect(super.getX(), super.getY(), super.getWidth(), super.getHeight());
        }
        else
            return;
    }
}

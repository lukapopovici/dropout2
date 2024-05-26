import java.awt.*;

//CLASA PENTRU OBSTACOLE, OBIECTE REPREZENTATE CA ENTITATI PE ECRAN CARE NU SUNT INAMICI
public class Obstacle implements Entity {
    private int X;
    private int Y;
    //valori daca nu ma intereseaza sa pasez in constructor asta, de la o idee
    private int Height=40;
    private int Width=40;

    Obstacle(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    Obstacle(int X, int Y,int Height,int Width) {
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
        g.setColor(Color.BLACK);
        g.fillRect(X, Y, Width, Height);
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

    @Override
    public void Delete() {
    }
}

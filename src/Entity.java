import java.awt.*;

public interface Entity {
    int DrawPriority=0;
    int GetDrawPriority();
    void Draw(Graphics g);
    int getY();
    public int getWidth();
    public int getHeight();
    int getX();
    void WasTouched(Entity e);
    void Delete();
}

import java.awt.*;

public interface Entity {
    int DrawPriority=0;
    int GetDrawPriority();
    void Draw(Graphics g);
    int getY();

    int getX();
    void WasTouched();
    void Delete();
}

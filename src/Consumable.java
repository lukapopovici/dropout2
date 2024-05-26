import java.awt.*;

// CLASA PENTRU TOATE CONSUMABILELE
public class Consumable implements Entity {
    private int x;
    private int y;
    private final int width = 10;
    private final int height = 10;
    private boolean hasBeenTouched = false;

    public Consumable(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int GetDrawPriority() {
        return 0;
    }

    @Override
    public void Draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void WasTouched(Entity e) {
        // Check if the entity touching the consumable is of type Player or Enemy
        if (e instanceof Player || e instanceof Enemy) {
            hasBeenTouched = true;
        }
    }



    public boolean hasBeenTouched() {
        return hasBeenTouched;
    }
}

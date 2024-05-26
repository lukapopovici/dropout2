import java.awt.*;
//FUNCTII PENTRU CLASA BUTON, INSTANTIATA IN MENIURI
class Button {
    private String label;
    private int x, y, width, height;

    public Button(String label, int x, int y, int width, int height) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isClicked(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(label, x + 20, y + 30);
    }

    public String getLabel() {
        return label;
    }
}

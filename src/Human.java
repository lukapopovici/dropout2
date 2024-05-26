import java.awt.*;
import java.awt.image.BufferedImage;
//SEALED CLASS PENTRU STATEURILE IN CARE POATE FI O ENTITATE DE TIP HUMAN
enum HumanState {
    IDLE,
    WALK_FORWARD,
    WALK_VERT,
    WALK_BACKWARD,
    HITTING_LEFT,
    HITTING_RIGHT,
    DEATH
}
//CLASA PENTRU "OAMENI". ENTITATI CARE SE POT MISCA SI CARE FOLOSESC ANIMATII
//FOLOSIT PRIMAR PENTRU MANAGEMENT DE ANIMATII
abstract public class Human implements Entity{
    protected int HitPoints;
    protected Boolean IsAlive;
    protected HumanState CurrentState;

    protected HumanState NextState;
   protected BufferedImage animationImage;
    protected BufferedImage IdleImage;
    protected float verticalSpeedMult;

    protected float horizontalSpeedMult;
    protected BufferedImage WalkImage;
    protected BufferedImage VerticalImage;
    protected BufferedImage AttackImage;

    protected Boolean TurnedLeft;
    protected int FRAME_COUNT = 6;
    protected int speed = 3;
    protected BufferedImage[] animationFrames;
    protected int currentFrameIndex = 0;
    protected int X;
    protected int Width;
    protected int Height;
    protected int Y;

    public void Draw(Graphics g) {
        if (animationFrames != null && animationFrames.length > 0 && g != null) {
            BufferedImage currentFrame = animationFrames[currentFrameIndex];

            int frameWidth = currentFrame.getWidth();
            int frameHeight = currentFrame.getHeight();
            int drawX = X - frameWidth / 2;
            int drawY = Y - frameHeight / 2;

            g.drawImage(currentFrame, drawX, drawY, frameWidth, frameHeight, null);
            updateAnimationFrame();
            changeAnimationBasedOnState();
        }
    }

    protected BufferedImage flipImageHorizontally(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage flipped = new BufferedImage(width, height, original.getType());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                flipped.setRGB(width - x - 1, y, original.getRGB(x, y));
            }
        }
        return flipped;
    }
     abstract void changeAnimationBasedOnState();
    protected void updateAnimationFrame() {
        currentFrameIndex = (currentFrameIndex + 1) % FRAME_COUNT;
    }

    public abstract void WasTouched(Entity e);


    public void KillMe(){
        IsAlive=false;
        this.NextState=HumanState.IDLE;
    }

    public Boolean CheckAlive(){
        return IsAlive;
    }
}

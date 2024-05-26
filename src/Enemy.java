import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//CLASA PENTRU INAMICUL BASIC

public class Enemy extends Human {
    private int TargetX, TargetY;
    private Startegie S;
    public Enemy(int x, int y) {
        this(x, y, 1.0f, 1.0f);
    }

    public Enemy(int x, int y, float vertSpeed, float horSpeed) {
        this.X = x;
        this.Y = y;
        this.IsAlive=true;
        this.verticalSpeedMult = vertSpeed;
        this.horizontalSpeedMult = horSpeed;
        this.TurnedLeft = false;
        this.CurrentState = HumanState.IDLE;
        this.NextState = HumanState.IDLE;
        this.S = new FollowPlayer();
        loadImages();
    }

    private void loadImages() {
        try {
            this.IdleImage = ImageIO.read(new File("Assets/WildZombie/Idle.png"));
            this.WalkImage = ImageIO.read(new File("Assets/WildZombie/Walk.png"));
            this.VerticalImage = ImageIO.read(new File("Assets/WildZombie/Jump.png"));
            this.AttackImage = ImageIO.read(new File("Assets/WildZombie/Attack_1.png"));
            this.FRAME_COUNT = 9;
            BufferedImage animationImage = IdleImage;
            int frameWidth = animationImage.getWidth() / FRAME_COUNT;
            int frameHeight = animationImage.getHeight();
            this.Width = frameWidth;
            this.Height = frameHeight;
            animationFrames = new BufferedImage[FRAME_COUNT];
            for (int i = 0; i < FRAME_COUNT; i++) {
                animationFrames[i] = animationImage.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeAnimationBasedOnState() {
        if (CurrentState == NextState)
            return;
        else {
            animationFrames = null;
            currentFrameIndex = 0;
            animationImage = null;
            FRAME_COUNT = 0;
            switch (NextState) {
                case HumanState.WALK_FORWARD:
                    FRAME_COUNT = 10;
                    animationImage = WalkImage;
                    this.TurnedLeft = false;
                    break;
                case HumanState.IDLE:
                    FRAME_COUNT = 9;
                    animationImage = IdleImage;
                    break;
                case HumanState.WALK_BACKWARD:
                    FRAME_COUNT = 10;
                    animationImage = WalkImage;
                    this.TurnedLeft = true;
                    break;
                case HumanState.WALK_VERT:
                    FRAME_COUNT = 6;
                    animationImage = VerticalImage;
                    break;
                case HumanState.HITTING_RIGHT:
                    FRAME_COUNT = 4;
                    animationImage = AttackImage;
                    this.TurnedLeft = false;
                    break;
                case HumanState.HITTING_LEFT:
                    FRAME_COUNT = 4;
                    animationImage = AttackImage;
                    this.TurnedLeft = true;
                    break;
            }
            assert animationImage != null;
            if (this.TurnedLeft) {
                animationImage = flipImageHorizontally(animationImage);
            }
            int frameWidth = animationImage.getWidth() / FRAME_COUNT;
            int frameHeight = animationImage.getHeight();
            animationFrames = new BufferedImage[FRAME_COUNT];
            for (int i = 0; i < FRAME_COUNT; i++) {
                animationFrames[i] = animationImage.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
            CurrentState = NextState;
        }
    }

    @Override
    public int GetDrawPriority() {
        return 0;
    }

    @Override
    public int getY() {
        return Y;
    }

    @Override
    public int getX() {
        return X;
    }

    @Override
    public void WasTouched(Entity e) {
        if (e instanceof Player) {
            KillMe();
        }
        if (e instanceof Consumable){
            this.speed*=2;
        }
    }

    public void SetTargetLocation(int x, int y) {
        this.TargetX = x;
        this.TargetY = y;
    }

    public void setMovementStrategy(Startegie strategy) {
        this.S = strategy;
    }

    public void Move() {
        S.move(this, TargetX, TargetY);
    }
    @Override
    public int getWidth() {
        return 40;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    public void setTurnedLeft(boolean b) {
        this.TurnedLeft=b;
    }

    public void setNextState(HumanState humanState) {
        this.NextState=humanState;
    }

    public void setX(int i) {
        this.X=i;
    }

    public void setY(int i) {
        this.Y=i;
    }

    public float getVerticalSpeedMult() {
        return this.verticalSpeedMult;
    }

    public float getHorizontalSpeedMult() {
        return this.horizontalSpeedMult;
    }

    public double GetSpeed() {
        return this.speed;
    }
}

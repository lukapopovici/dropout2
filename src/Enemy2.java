import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//CLASA PENTRU INAMIC SPEEDER. PRIORITIZEAA POWERUPURILE, E MAI RAPID DECAT INAMICUL NORMAL SI POATE SA CONSUME POWER UPURI PENTRU A LUA VITEZA

public class Enemy2 extends Enemy {
    private int TargetX, TargetY;

    public Enemy2(int x, int y) {
        this(x, y, 2.0f, 2.0f);
    }

    public Enemy2(int x, int y, float vertSpeed, float horSpeed) {
        super( x,  y, vertSpeed,  horSpeed);
        this.X = x;
        this.Y = y;
        this.IsAlive=true;
        this.verticalSpeedMult = vertSpeed;
        this.horizontalSpeedMult = horSpeed;
        this.TurnedLeft = false;
        this.CurrentState = HumanState.IDLE;
        this.NextState = HumanState.IDLE;
        loadImages();
    }

    private void loadImages() {
        try {
            this.IdleImage = ImageIO.read(new File("Assets/ZombieMan/Idle.png"));
            this.WalkImage = ImageIO.read(new File("Assets/ZombieMan/Walk.png"));
            this.VerticalImage = ImageIO.read(new File("Assets/ZombieMan/Jump.png"));
            this.AttackImage = ImageIO.read(new File("Assets/ZombieMan/Attack_1.png"));
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
                    FRAME_COUNT = 8;
                    animationImage = WalkImage;
                    this.TurnedLeft = false;
                    break;
                case HumanState.IDLE:
                    FRAME_COUNT = 8;
                    animationImage = IdleImage;
                    break;
                case HumanState.WALK_BACKWARD:
                    FRAME_COUNT = 8;
                    animationImage = WalkImage;
                    this.TurnedLeft = true;
                    break;
                case HumanState.WALK_VERT:
                    FRAME_COUNT = 8;
                    animationImage = VerticalImage;
                    break;
                case HumanState.HITTING_RIGHT:
                    FRAME_COUNT = 5;
                    animationImage = AttackImage;
                    this.TurnedLeft = false;
                    break;
                case HumanState.HITTING_LEFT:
                    FRAME_COUNT = 5;
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
    }

    public void SetTargetLocation(int x, int y) {
        this.TargetX = x;
        this.TargetY = y;
    }

    public void Move() {
        double deltaX = Math.abs(TargetX - X);
        double deltaY = Math.abs(TargetY - Y);

        if (deltaX > deltaY) {
            if (X < TargetX) {
                X += speed * horizontalSpeedMult;
                this.NextState = HumanState.WALK_FORWARD;
            } else if (X > TargetX) {
                X -= speed * horizontalSpeedMult;
                TurnedLeft = true;
                this.NextState = HumanState.WALK_BACKWARD;
            }
        } else {
            if (Y < TargetY) {
                Y += speed * verticalSpeedMult;
            } else if (Y > TargetY) {
                Y -= speed * verticalSpeedMult;
                TurnedLeft = true;
            }
            this.NextState = HumanState.WALK_VERT;
        }
    }

    @Override
    public int getWidth() {
        return 40;
    }

    @Override
    public int getHeight() {
        return 40;
    }
}

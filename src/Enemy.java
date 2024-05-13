import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Enemy extends Human{
    private int TargetX,TargetY;
    public Enemy(int x, int y) {
        this.X = x;
        this.Y = y;
        this.verticalSpeedMult=1;
        this.horizontalSpeedMult=1;
        this.TurnedLeft=false;
        this.CurrentState=HumanState.IDLE;
        this.NextState=HumanState.IDLE;
        try {
            this.IdleImage= ImageIO.read(new File("Assets/WildZombie/Idle.png"));
            this.WalkImage= ImageIO.read(new File("Assets/WildZombie/Walk.png"));
            this.VerticalImage= ImageIO.read(new File("Assets/WildZombie/Jump.png"));
            this.AttackImage= ImageIO.read(new File("Assets/WildZombie/Attack_1.png"));
            FRAME_COUNT=9;
            BufferedImage animationImage = IdleImage;
            int frameWidth = animationImage.getWidth() / FRAME_COUNT;
            int frameHeight = animationImage.getHeight();
            this.Width=frameWidth;
            this.Height=frameHeight;
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
        //DE REFACTORAT ASTA
        if (CurrentState == NextState)
            return;
        else {
            animationFrames = null;
            currentFrameIndex = 0;
            animationImage=null;
            FRAME_COUNT = 0;
            switch (NextState) {
                case HumanState.WALK_FORWARD:
                    FRAME_COUNT = 10;
                    animationImage = WalkImage;
                    this.TurnedLeft=false;
                    break;
                case HumanState.IDLE:
                    FRAME_COUNT = 9;
                    animationImage = IdleImage;
                    break;
                case HumanState.WALK_BACKWARD:
                    FRAME_COUNT = 10;
                    animationImage = WalkImage;
                    this.TurnedLeft=true;

                    break;
                case HumanState.WALK_VERT:
                    FRAME_COUNT = 6;
                    animationImage = VerticalImage;

                    break;
                case HumanState.HITTING_RIGHT:
                    FRAME_COUNT = 4;
                    animationImage = AttackImage;
                    this.TurnedLeft=false;

                    break;
                case HumanState.HITTING_LEFT:
                    FRAME_COUNT = 4;
                    animationImage = AttackImage;
                    this.TurnedLeft=true;

                    break;
            }
            assert animationImage != null;
            if(this.TurnedLeft){
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
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public void WasTouched() {

    }

    @Override
    public void Delete() {

    }

    public void SetPlayerLocation(int x, int y) {
        this.TargetX=x;
        this.TargetY=y;
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


}

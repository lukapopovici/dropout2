import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//COD PENTRU CLASA JUCATOR
//MAXIM 2 INSTANTE POSIBILE
public class Player extends Human {

    public Player(int x, int y) {
        this.X = x;
        this.Y = y;
        this.IsAlive=true;
        this.verticalSpeedMult=3;
        this.horizontalSpeedMult=2;
        this.TurnedLeft=false;
        this.CurrentState=HumanState.IDLE;
        this.NextState=HumanState.IDLE;
        try {
            this.IdleImage= ImageIO.read(new File("Assets/Raider_1/Idle.png"));
            this.WalkImage= ImageIO.read(new File("Assets/Raider_1/Walk.png"));
            this.VerticalImage= ImageIO.read(new File("Assets/Raider_1/Jump.png"));
            this.AttackImage= ImageIO.read(new File("Assets/Raider_1/Attack_1.png"));
            BufferedImage animationImage = IdleImage;
            int frameWidth = animationImage.getWidth() / FRAME_COUNT;
            int frameHeight = animationImage.getHeight();
            this.Width=20;
            this.Height=50;
            animationFrames = new BufferedImage[FRAME_COUNT];
            for (int i = 0; i < FRAME_COUNT; i++) {
                animationFrames[i] = animationImage.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAnimationFrame() {
        currentFrameIndex = (currentFrameIndex + 1) % FRAME_COUNT;
    }

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
                    FRAME_COUNT = 8;
                    animationImage = WalkImage;
                    this.TurnedLeft=false;
                    break;
                case HumanState.IDLE:
                    FRAME_COUNT = 6;
                        animationImage = IdleImage;
                    break;
                case HumanState.WALK_BACKWARD:
                    FRAME_COUNT = 8;
                    animationImage = WalkImage;
                    this.TurnedLeft=true;

                    break;
                case HumanState.WALK_VERT:
                    FRAME_COUNT = 11;
                        animationImage = VerticalImage;

                    break;
                case HumanState.HITTING_RIGHT:
                    FRAME_COUNT = 6;
                    animationImage = AttackImage;
                    this.TurnedLeft=false;

                    break;
                case HumanState.HITTING_LEFT:
                        FRAME_COUNT = 6;
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
    //PENTRU A REPREZENTA REZULTATUL INTERACTIUNI JUCATORULUI CU ALTE ENTITATI DIN ARENA
    @Override
    public void WasTouched(Entity e) {
        if (e instanceof Enemy) {
            Enemy enemy = (Enemy) e;
            if (CurrentState != HumanState.HITTING_LEFT && CurrentState != HumanState.HITTING_RIGHT) {
                KillMe();
            } else if (CurrentState == HumanState.HITTING_LEFT && enemy.getX() > getX()) {
                KillMe();
            } else if (CurrentState == HumanState.HITTING_RIGHT && enemy.getX() < getX()) {
                KillMe();
            }
        }
        if (e instanceof Player) {
            IsAlive=true;
        }
        if (e instanceof Consumable) {
             this.speed*=2;
        }

    }



    public void MoveUp() {
        if(this.IsAlive) {
            this.NextState = HumanState.WALK_VERT;
            this.Y -= this.speed * verticalSpeedMult;
        }
    }

    public void MoveDown() {
        if(this.IsAlive) {
            this.NextState = HumanState.WALK_VERT;
            this.Y += this.speed * verticalSpeedMult;
        }
    }

    public void MoveLeft() {
        if(this.IsAlive) {
            this.NextState = HumanState.WALK_BACKWARD;
            this.X -= this.speed * horizontalSpeedMult;
        }
    }

    public void SetIdle(){
        this.NextState=HumanState.IDLE;
    }
    public void MoveRight() {
        if(this.IsAlive) {
            this.NextState = HumanState.WALK_FORWARD;
            this.X += this.speed * horizontalSpeedMult;
        }
    }


    public int getVertical() {
        return Y+this.Height/2;
    }
    @Override
    public int getX() {
        return X;
    }

    public void AttackForward() {
        if(this.IsAlive) {
            this.NextState = HumanState.HITTING_RIGHT;
            this.X += this.speed * horizontalSpeedMult;
        }
    }

    public float GetXSpeed(){
        return this.speed * horizontalSpeedMult;
    }
    public float GetYSpeed(){
        return this.speed * verticalSpeedMult;
    }
    public void AttackBackward() {
        if(this.IsAlive) {
            this.NextState = HumanState.HITTING_LEFT;
            this.X -= this.speed * horizontalSpeedMult;
        }
    }

    public int getWidth() {
        return this.Width;
    }

    public int getHeight() {
        return this.Height;
    }

    @Override
    public int GetDrawPriority() {
        return 0;
    }

    @Override
    public int getY() {
        return this.Y;
    }
}

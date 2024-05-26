public class FollowPlayer implements Startegie {
    @Override
    public void move(Enemy enemy, int targetX, int targetY) {
        int X = enemy.getX();
        int Y = enemy.getY();
        double speed = enemy.GetSpeed();
        float verticalSpeedMult = enemy.getVerticalSpeedMult();
        float horizontalSpeedMult = enemy.getHorizontalSpeedMult();

        double deltaX = Math.abs(targetX - X);
        double deltaY = Math.abs(targetY - Y);

        if (deltaX > deltaY) {
            if (X < targetX) {
                enemy.setX(X + (int) (speed * horizontalSpeedMult));
                enemy.setNextState(HumanState.WALK_FORWARD);
            } else if (X > targetX) {
                enemy.setX(X - (int) (speed * horizontalSpeedMult));
                enemy.setTurnedLeft(true);
                enemy.setNextState(HumanState.WALK_BACKWARD);
            }
        } else {
            if (Y < targetY) {
                enemy.setY(Y + (int) (speed * verticalSpeedMult));
            } else if (Y > targetY) {
                enemy.setY(Y - (int) (speed * verticalSpeedMult));
                enemy.setTurnedLeft(true);
            }
            enemy.setNextState(HumanState.WALK_VERT);
        }
    }
}

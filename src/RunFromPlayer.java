//STRATEGIE PENTRU A FUGI DE JUCATOR FOLOSITA DE CLASA INAMIC (ENEMY)
public class RunFromPlayer implements Startegie {
    int arenaWidth = Arena.getInstance().getWidth();
    int arenaHeight = Arena.getInstance().getHeight();

    public void move(Enemy enemy, int targetX, int targetY) {
        int X = enemy.getX();
        int Y = enemy.getY();
        double speed = enemy.GetSpeed();
        float verticalSpeedMult = enemy.getVerticalSpeedMult();
        float horizontalSpeedMult = enemy.getHorizontalSpeedMult();

        double deltaX = Math.abs(targetX - X);
        double deltaY = Math.abs(targetY - Y);

        if (deltaX > deltaY) {
            if (X > targetX) {
                X = Math.min(X + (int) (speed * horizontalSpeedMult), arenaWidth - enemy.getWidth());
                enemy.setNextState(HumanState.WALK_FORWARD);
            } else if (X < targetX) {
                X = Math.max(X - (int) (speed * horizontalSpeedMult), 0);
                enemy.setTurnedLeft(true);
                enemy.setNextState(HumanState.WALK_BACKWARD);
            }
        } else {
            if (Y > targetY) {
                Y = Math.min(Y + (int) (speed * verticalSpeedMult), arenaHeight - enemy.getHeight());
            } else if (Y < targetY) {
                Y = Math.max(Y - (int) (speed * verticalSpeedMult), 0);
                enemy.setTurnedLeft(true);
            }
            enemy.setNextState(HumanState.WALK_VERT);
        }

        enemy.setX(X);
        enemy.setY(Y);
    }
}

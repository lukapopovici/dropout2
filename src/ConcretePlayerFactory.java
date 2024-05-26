public class ConcretePlayerFactory implements AbstractFactory<Player> {
    @Override
    public Player create(int x, int y) {
        return new Player(x, y);
    }
}

//CLASA FACTORY PENTRU JUCATOR
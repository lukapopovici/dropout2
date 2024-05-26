//IMPLEMENTARE ABSTRACT FACTORY CLASA PARAMETRIZATA
public interface AbstractFactory<T> {
    T create(int x, int y);
}
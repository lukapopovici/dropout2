import java.awt.Graphics;
import java.sql.SQLException;

//INTERFATA PENTRU MENIURI
public interface Menu {
    void Display();
    void Update() throws SQLException;
    void Begin();
    State ReturnState();
    void paintComponent(Graphics g);
    void handleMouseClick(int x, int y) throws SQLException;
    public void doAction(String action) throws SQLException;
    //FUNCTIE CA SA AJUTE LA TRANZITIA STATEULUI
    void SetOverTrue();
}
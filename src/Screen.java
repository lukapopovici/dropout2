//INTERFATA PENTRU UN JFRAME DE TIPUL "GAMEPLAY WINDOW"
//(CARE SA NU FIE MENIU)
public interface Screen {
    void Update();

    State ReturnState();

    void SetOverTrue();
    void Begin();
}

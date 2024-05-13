public interface Screen {
    void Update();

    State ReturnState();

    void SetOverTrue();
    void Begin();
}

package mango;

public interface ILogic {

    void init() throws Exception;

    void input();

    void update(float interval, MouseManager mouseManager);

    void render();

    void cleanup();

}

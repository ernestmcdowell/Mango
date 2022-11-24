package mango.launcher;
import mango.ImGuiLayer;
import mango.WindowManager;
import mango.EngineManager;
import mango.utils.Consts;
import org.lwjgl.Version;
public class Launcher {

    private static WindowManager window;
    private static TestGame game;
    private static ImGuiLayer imGuiLayer;
    private static int width = 3440, height = 2160;

    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        window = new WindowManager(Consts.TITLE, width, height, false, new ImGuiLayer());
        game =  new TestGame();
        EngineManager engine = new EngineManager();


        try{
            engine.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        window.destroy();
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}
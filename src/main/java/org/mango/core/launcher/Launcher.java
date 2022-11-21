package org.mango.core.launcher;

import org.mango.core.TestGame;
import org.mango.core.WindowManager;
import org.mango.core.EngineManager;
import org.mango.core.utils.Consts;
import org.lwjgl.Version;
public class Launcher {

    private static WindowManager window;
    private static TestGame game;

    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        window = new WindowManager(Consts.TITLE, 1920, 1080, false);
        game =  new TestGame();
        EngineManager engine = new EngineManager();

        try{
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}
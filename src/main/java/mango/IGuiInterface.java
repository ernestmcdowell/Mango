package mango;

import mango.launcher.TestGame;

public interface IGuiInterface {

    void drawGui();

    boolean handleGuiInput(TestGame scene, WindowManager window);

}

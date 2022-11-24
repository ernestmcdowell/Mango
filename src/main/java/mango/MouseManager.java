package mango;

import mango.launcher.Launcher;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseManager {

    private final Vector2d previosPos, currentPos;
    private final Vector2f displayVector;

    private boolean inWindow = false, leftButtonPress = false, rightButtonPress = false;

    public MouseManager(){
        previosPos = new Vector2d(-1,-1);
        currentPos = new Vector2d(0,0);
        displayVector = new Vector2f();
    }

    public void init(){
        GLFW.glfwSetCursorPosCallback(Launcher.getWindow().getWindow(), (window, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });

        GLFW.glfwSetCursorEnterCallback(Launcher.getWindow().getWindow(), (window, entered) -> {
                inWindow = entered;
    });
        GLFW.glfwSetMouseButtonCallback(Launcher.getWindow().getWindow(), (window, button, action, mods) -> {
            leftButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }

    public void input(){
        displayVector.x = 0;
        displayVector.y = 0;
        if(previosPos.x > 0 && previosPos.y > 0 && inWindow){
            double x = currentPos.x - previosPos.x;
            double y = currentPos.y - previosPos.y;
            boolean rotateX = x != 0;
            boolean rotateY = y != 0;
            if(rotateX)
                displayVector.y = (float) x;
            if(rotateY)
                displayVector.x = (float) y;
        }
        previosPos.x = currentPos.x;
        previosPos.y = currentPos.y;
    }

    public Vector2f getDisplayVector() {
        return displayVector;
    }

    public boolean isLeftButtonPress() {
        return leftButtonPress;
    }

    public boolean isRightButtonPress() {
        return rightButtonPress;
    }
}


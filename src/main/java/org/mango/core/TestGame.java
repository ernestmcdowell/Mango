package org.mango.core;

import org.mango.core.entity.Model;
import org.mango.core.launcher.Launcher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements ILogic {

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;

    private final ObjectLoader loader;
    private final WindowManager window;

    private Model model;

    public TestGame() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        // vertices to create a recrtangle
        float[] vertices = {

           -0.5f, 0.5f, 0f,
           -0.5f, -0.5f, 0f,
           0.5f, -0.5f, 0f,
           0.5f, -0.5f, 0f,
           0.5f, 0.5f, 0f,
           -0.5f, 0.5f, 0f
        };

        int[] indices = {
                0,1,3,
                3,1,2
        };

        model = loader.loadModel(vertices, indices);
    }

    @Override
    public void input() {
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            System.out.println("Pressing up");
            direction = 1;
        }
        else if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            System.out.println("Pressing down");
            direction = -1;
        }
        else {
            direction = 0;
        }
    }

    @Override
    public void update() {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color <= 0f) {
            color = 0.0f;
        }
    }

    @Override
    public void render() {
        if(window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColour(color, color, color, 0.0f);
        renderer.render(model);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}

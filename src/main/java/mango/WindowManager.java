package mango;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import org.joml.Matrix4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowManager {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    private String glslVersion = null;

    private final String title;

    private static int width, height;
    private long window;
    private boolean resize, vSync;
    private ImGuiLayer imGuiLayer;

    private final Matrix4f projectionmatrix;

    public WindowManager(String title, int width, int height, boolean vSync, ImGuiLayer layer) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        imGuiLayer = layer;
        projectionmatrix = new Matrix4f();
    }

    public void init() {
        initWindow();
        initImGui();
        imGuiGlfw.init(window, true);
        imGuiGl3.init(glslVersion);
        glfwSetErrorCallback(null).free();

    }

    public void initWindow() {
        GLFWErrorCallback.createPrint(System.err).set();


        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        glslVersion = "#version 150";

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        boolean maximised = false;

        if (width == 0 || height == 0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximised = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window!");
        }


        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            WindowManager.width = width;
            WindowManager.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });
        if (maximised) {
            GLFW.glfwMaximizeWindow(window);
        } else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);
        if (isvSync()) {
            GLFW.glfwSwapInterval(1);
        }

        GLFW.glfwShowWindow(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

    }

    public void destroy() {
        imGuiGlfw.dispose();
        imGuiGl3.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
//        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
//        io.setConfigFlags(ImGuiBackendFlags.HasMouseCursors);
//        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.setIniFilename(null);
    }


    public void update() {
//        glClearColor(0.1f, 0.09f, 0.1f, 1.0f);
//        glClear(GL_COLOR_BUFFER_BIT);
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        setupDockSpace();
        ImGui.end();
        //place imgui stuff here
        imGuiLayer.imgui();


        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();

    }

    private void setupDockSpace(){
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
        ImGui.setNextWindowPos(0.0f, 0.f, ImGuiCond.Always);
        ImGui.setNextWindowSize(WindowManager.getWidth(), WindowManager.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus |
                ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("DockSpace", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // Dockspace
        ImGui.dockSpace(ImGui.getID("DockSpace"));


    }




    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColour(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean isResize() {
        return resize;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionmatrix() {
        return projectionmatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / height;
        return projectionmatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height) {
        float aspectRation = (float) width / height;
        return matrix.setPerspective(FOV, aspectRation, Z_NEAR, Z_FAR);
    }
}
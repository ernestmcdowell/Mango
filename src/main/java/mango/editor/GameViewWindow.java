package mango.editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import mango.Framebuffer;
import mango.WindowManager;
import org.lwjgl.opengl.GL;

public class GameViewWindow {

    public static void imgui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);
        int textureId = Framebuffer.getTexture();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        ImGui.end();

    }

    private static ImVec2 getLargestSizeForViewport() {

        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth;
        if(aspectHeight > windowSize.y){
            //swith to pillar box mod
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * WindowManager.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);

    }

    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 winowSize = new ImVec2();
        ImGui.getContentRegionAvail(winowSize);
        winowSize.x -= ImGui.getScrollX();
        winowSize.y -= ImGui.getScrollY();

        float viewportX = (winowSize.x/2) - (aspectSize.x/2);
        float viewportY = (winowSize.y/2) - (aspectSize.y/2);

        return new ImVec2(viewportX, viewportY);

    }


}

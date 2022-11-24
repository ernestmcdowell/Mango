package mango;

import imgui.ImGui;
import imgui.flag.*;
import imgui.type.ImBoolean;

public class ImGuiLayer {

    private boolean showText = false;

    public void imgui() {
        ImGui.begin("A window");


        if (ImGui.button("A button")) {
            showText = true;
        }


        if (showText) {
            ImGui.text("button clicked");
            if (ImGui.button("Stop showing text")) {
                showText = false;
            }
        }

        ImGui.end();

        ImGui.begin("Another window");

        ImGui.end();
    }
}

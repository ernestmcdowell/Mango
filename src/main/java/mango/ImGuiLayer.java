package mango;

import imgui.ImGui;
import imgui.flag.*;
import imgui.type.ImBoolean;
import mango.entity.Material;

public class ImGuiLayer {

    private boolean showText = false;

    public void imgui() {
        ImGui.begin("Project Hierarchy");


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

        ImGui.begin("Asset Browser");



        ImGui.end();
    }
}

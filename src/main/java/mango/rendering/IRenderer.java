package mango.rendering;

import mango.Camera;
import mango.entity.Model;
import mango.lighting.DirectionalLight;
import mango.lighting.PointLight;
import mango.lighting.SpotLight;

public interface IRenderer<T> {

    public void init() throws Exception;

    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

    abstract void bind(Model model);

    public void unbind();

    public void prepare(T t, Camera camera);

    public void cleanup();


}

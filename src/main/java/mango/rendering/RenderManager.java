package mango.rendering;

import mango.Camera;
import mango.ShaderManager;
import mango.WindowManager;
import mango.entity.Entity;
import mango.entity.GameObject;
import mango.entity.SceneManager;
import mango.launcher.Launcher;
import mango.utils.Consts;
import mango.lighting.DirectionalLight;
import mango.lighting.PointLight;
import mango.lighting.SpotLight;
import org.lwjgl.opengl.GL11;
import mango.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

public class RenderManager {
    private final WindowManager window;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private ObjectRenderer objectRenderer;

    public RenderManager() {
        window = Launcher.getWindow();
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer();
        objectRenderer = new ObjectRenderer();
        terrainRenderer = new TerrainRenderer();
        entityRenderer.init();
        terrainRenderer.init();

    }

    public static void renderLights(PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight, ShaderManager shader){
        shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniform("specularPower", Consts.SPECULAR_POWER);

        int numLights = spotLights != null ? spotLights.length : 0;
        for(int i = 0; i < numLights; i++){
            shader.setUniform("spotLights", spotLights[i], i);
        }

        numLights = pointLights != null ? pointLights.length : 0;
        for(int i = 0; i < numLights; i++){
            shader.setUniform("pointLights", pointLights[i], i);
        }
        shader.setUniform("directionalLight", directionalLight);
    }

    public void render(Camera camera, SceneManager scene) {
        clear();
        entityRenderer.render(camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());
        terrainRenderer.render(camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());

    }

    public void processEntity(Entity entity){
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if(entityList != null){
            entityList.add(entity);
        } else{
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }
    }

    public void processGameObject(GameObject go){
        List<GameObject> gameObjectList = objectRenderer.getGameObjects().get(go.getModel());
        if(gameObjectList != null){
            gameObjectList.add(go);
        } else{
            List<GameObject> newGameObjectList = new ArrayList<>();
            newGameObjectList.add(go);
            objectRenderer.getGameObjects().put(go.getModel(), newGameObjectList);
        }
    }

    public void processTerrain(Terrain terrain){
        terrainRenderer.getTerrains().add(terrain);
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT );
    }

    public void cleanup(){
        entityRenderer.cleanup();
        terrainRenderer.cleanup();
    }
}

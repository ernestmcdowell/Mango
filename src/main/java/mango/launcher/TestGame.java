package mango.launcher;

import mango.*;
import mango.entity.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import mango.lighting.DirectionalLight;
import mango.lighting.PointLight;
import mango.lighting.SpotLight;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import mango.rendering.RenderManager;
import mango.terrain.Terrain;
import mango.utils.Consts;

import java.util.Random;

public class TestGame implements ILogic {
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private Entity activeEntity;
    private Camera camera;
    private SceneManager sceneManager;
    Vector3f cameraInc;

    private IGuiInterface guiInterface;

    public TestGame() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
        sceneManager = new SceneManager(-90);
    }

    public IGuiInterface getGuiInterface(){
        return guiInterface;
    }

    public void setGuiInterface(IGuiInterface iGuiInterface) {
        this.guiInterface = guiInterface;
    }


        @Override
    public void init() throws Exception {
        renderer.init();

        //load object model + texture for model
        Model model = loader.loadOBJModel("/models/bunny.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/grass.png")), 1f);

        // create 2 new terrains with textures
        Terrain terrain = new Terrain(new Vector3f(0, -1, -800), loader, new Material(new Texture(loader.loadTexture("textures/grass.png")), 0.1f));
        Terrain terrain2 = new Terrain(new Vector3f(-800, -1, -800), loader, new Material(new Texture(loader.loadTexture("textures/grass.png")), 0.1f));

        // add terrains to the scene
        sceneManager.addTerrains(terrain);
        sceneManager.addTerrains(terrain2);

        // set random x and z positions for the object model
        Random random = new Random();
        for(int i = 0; i < 200; i++){
            float x = random.nextFloat() * 100 -50;
            float z = random.nextFloat() * -200;
            sceneManager.addEntity(new Entity(model, new Vector3f(x,2,z),
                    new Vector3f(random.nextFloat() * 180, random.nextFloat() * 180, 0), 25));
        }
        //add randomly place object models to the scene
        sceneManager.addEntity(new Entity(model, new Vector3f(0, 2, -5f), new Vector3f(0,0, 0), 1));


        //point light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-0.5f, -0.5f, -3.2f);
        Vector3f lightColor = new Vector3f(1,1,1);
        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity, 0, 0, 1);

        //spotlight
        Vector3f coneDirection = new Vector3f(0f,-50f,0f);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        lightIntensity = 50000f;
        SpotLight spotLight = new SpotLight(new PointLight(new Vector3f(0.25f,0f,0f), new Vector3f(1f, 50f, -5f), lightIntensity, 0f,0f,.02f), coneDirection, cutoff);

        coneDirection = new Vector3f(0f, -50f, 0f);
        cutoff = (float) Math.cos(Math.toRadians(140));
        lightIntensity = 50000f;
        SpotLight spotLight1 = new SpotLight(new PointLight( new Vector3f(0f, 0.25f, 0f), new Vector3f(1f, 50f, -5f),
                lightIntensity, 0f, 0f, 0.02f), coneDirection, cutoff);


        //directional light
        lightIntensity = 1f;
        lightPosition = new Vector3f(-1, 0, 0);
        lightColor = new Vector3f(1, 1, 1);
        sceneManager.setDirectionalLight(new DirectionalLight(lightColor, lightPosition, lightIntensity));

        sceneManager.setPointLights(new PointLight[]{pointLight});
        sceneManager.setSpotLights(new SpotLight[]{spotLight, spotLight1});


    }

    @Override
    public void input() {
        /**
         * Moves camera using W,A,S,D left,right,forward,backward
         *  Q,E for up,down respectively
         */
        cameraInc.set(0,0,0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        else if(window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        else if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
            cameraInc.y = -1;
        else if(window.isKeyPressed(GLFW.GLFW_KEY_E))
            cameraInc.y = 1;



        float lightPos = sceneManager.getSpotLights()[0].getPointLight().getPosition().z;
        float lightPos1 = sceneManager.getSpotLights()[1].getPointLight().getPosition().z;
        if(window.isKeyPressed(GLFW.GLFW_KEY_N)){
            sceneManager.getSpotLights()[0].getPointLight().getPosition().z = lightPos + 0.1f;
        }
        else if(window.isKeyPressed(GLFW.GLFW_KEY_M)){
            sceneManager.getSpotLights()[0].getPointLight().getPosition().z = lightPos - 0.1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_O)){
            sceneManager.getSpotLights()[1].getPointLight().getPosition().z = lightPos1 + 0.1f;
        }
        else if(window.isKeyPressed(GLFW.GLFW_KEY_P)){
            sceneManager.getSpotLights()[1].getPointLight().getPosition().z = lightPos1 - 0.1f;
        }

    }

    @Override
    public void update(float interval, MouseManager mouse) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_STEP, cameraInc.y * Consts.CAMERA_STEP,
                cameraInc.z * Consts.CAMERA_STEP);

        if(mouse.isRightButtonPress()){
            System.out.println("Right mouse pressed");
            Vector2f rotVec = mouse.getDisplayVector();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);
        }
        sceneManager.incSpotAngle(0.75f);
        if(sceneManager.getSpotAngle() > 9600)
            sceneManager.setSpotInc(-1);
        else if (sceneManager.getSpotAngle() <= -9600)
            sceneManager.setSpotInc(-1);

        double spotAngleRad = Math.toRadians(sceneManager.getSpotAngle());
        Vector3f coneDir = sceneManager.getSpotLights()[0].getPointLight().getPosition();
        coneDir.x = (float) Math.sin(spotAngleRad);

        coneDir = sceneManager.getSpotLights()[1].getPointLight().getPosition();
        coneDir.x = (float) Math.abs(spotAngleRad * 0.15f);


        sceneManager.incLightAngle(1.1f);
        if(sceneManager.getLightAngle() > 90){
            sceneManager.getDirectionalLight().setIntensity(0);
            if(sceneManager.getLightAngle() >= 360)
                sceneManager.setLightAngle(-90);
        } else if (sceneManager.getLightAngle() <= -80 || sceneManager.getLightAngle() >= 80){
            float factor = 1 - (Math.abs(sceneManager.getLightAngle()) -80) / 10.0f;
            sceneManager.getDirectionalLight().setIntensity(factor);
            sceneManager.getDirectionalLight().getColor().y = Math.max(factor, 0.9f);
            sceneManager.getDirectionalLight().getColor().z = Math.max(factor, 0.5f);
        } else {
            sceneManager.getDirectionalLight().setIntensity(1);
            sceneManager.getDirectionalLight().getColor().x = 1;
            sceneManager.getDirectionalLight().getColor().y = 1;
            sceneManager.getDirectionalLight().getColor().z = 1;
        }

        double angRad = Math.toRadians(sceneManager.getLightAngle());
        sceneManager.getDirectionalLight().getDirection().x = (float) Math.sin(angRad);
        sceneManager.getDirectionalLight().getDirection().y = (float) Math.cos(angRad);

        for(Entity entity : sceneManager.getEntities()){
            renderer.processEntity(entity);
        }

        for(Terrain terrain : sceneManager.getTerrains()){
            renderer.processTerrain(terrain);
        }

    }

    @Override
    public void render() {
        if(window.isResize()) {
            GL11.glViewport(0, 0, WindowManager.getWidth(), WindowManager.getHeight());
            window.setResize(true);
        }

        renderer.render(camera, sceneManager);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}

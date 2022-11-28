package mango.entity;

import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Entity extends Component {

    private boolean firstTime = false;

    private Model model;
    private Vector3f pos, rotation;
    private float scale;
    private boolean isDirty;


    public Entity(Model model, Vector3f pos, Vector3f rotation, float scale){
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void incPos(float x, float y, float z){
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }

    public void setPos(float x, float y, float z){
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void incRotation(float x, float y, float z){
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }



    public Model getModel() {
        return model;
    }

    public Material getMaterial(){
        return this.model.getMaterial();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void imgui(){
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields){
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if(isPrivate){
                    field.setAccessible(true);
                }
                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class){
                    int val = (int) value;
                    int[] imInt = { val };
                    if(ImGui.dragInt(name + ": ", imInt)){
                        field.set(this, imInt[0]);
                    }
                }
                if(isPrivate){
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public void start(){
        System.out.println("Entity starting");
    }

    @Override
    public void update(float dt) {
        if(!firstTime){
            System.out.print("Entity is updating");
            firstTime = true;
        }
    }
}

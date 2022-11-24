package mango.lighting;

import org.joml.Vector3f;

public class SpotLight {

    private PointLight pointLight;

    private Vector3f condeDirection;
    private float cutoff;

    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutoff){
        this.pointLight = pointLight;
        this.condeDirection = coneDirection;
        this.cutoff = cutoff;
    }

    public SpotLight(SpotLight spotLight){
        this.pointLight = spotLight.getPointLight();
        this.condeDirection = spotLight.getCondeDirection();
        setCutoff(spotLight.getCutoff());
    }

    public PointLight getPointLight() {
        return pointLight;
    }

    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    public Vector3f getCondeDirection() {
        return condeDirection;
    }

    public void setCondeDirection(Vector3f condeDirection) {
        this.condeDirection = condeDirection;
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }
}

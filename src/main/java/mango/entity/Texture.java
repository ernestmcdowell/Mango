package mango.entity;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private transient int id ;
    private String filepath;
    private int width, height;


    public Texture(){
        id = -1;
        width = -1;
        height = -1;
    }

    public Texture(int id) {
        this.id = id;
    }

    public Texture(int width, int height, int id) {
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }


    public int getId() {
        return id;
    }

    public String getFilepath(){
        return this.filepath;
    }

    public Texture(int width, int height){
        this.filepath = "Generated";
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof  Texture)) return false;
        Texture oTex = (Texture)o;
        return oTex.getWidth() == this.width && oTex.getHeight() == this.height && oTex.getId() == this.id &&
                oTex.getFilepath().equals(this.filepath);

    }

}

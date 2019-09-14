package Engine.Particles;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ParticleInfo {

    public int id;
    public String name;

    private ArrayList<BufferedImage> images;

    public ParticleInfo(int id, String name){
        this.id = id;
        this.name = name;

        this.images = new ArrayList<>();
    }


    public void addImage(BufferedImage img){
        this.images.add(img);
    }

    public BufferedImage getImage(int i){
        if(i >= 0 && i < this.images.size()){
            return this.images.get(i);
        }
        return null;
    }

    public int size(){
        return images.size();
    }




}

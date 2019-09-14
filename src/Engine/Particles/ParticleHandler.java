package Engine.Particles;

import Engine.Blocks.BlockInfo;
import Engine.FileHandler.FileInput;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ParticleHandler {

    private HashMap<Integer, ParticleInfo> particleInfo;
    private HashMap<String, Integer> nameInt;

    private BufferedImage img;
    private BufferedImage img2;


    public ParticleHandler(){
        this.particleInfo = new HashMap<>();
        this.nameInt = new HashMap<>();

        this.img = FileInput.LoadImage("Assets/textures/particle/particles.png");
        this.img2 = FileInput.LoadImage("Assets/Images/explosion.png");

        this.init();



    }

    private void init() {

        ParticleInfo p1 = new ParticleInfo(0, "Explosion");

        p1.addImage(this.img.getSubimage(8*0,0,8,8));
        p1.addImage(this.img.getSubimage(8*1,0,8,8));
        p1.addImage(this.img.getSubimage(8*2,0,8,8));
        p1.addImage(this.img.getSubimage(8*3,0,8,8));
        p1.addImage(this.img.getSubimage(8*4,0,8,8));
        p1.addImage(this.img.getSubimage(8*5,0,8,8));
        p1.addImage(this.img.getSubimage(8*6,0,8,8));
        p1.addImage(this.img.getSubimage(8*7,0,8,8));
        addParticleInfo(p1);

        ParticleInfo p2 = new ParticleInfo(1, "Explosion2");
        p2.addImage(this.img2.getSubimage(0,0,64,64));
        p2.addImage(this.img2.getSubimage(64,0,64,64));
        p2.addImage(this.img2.getSubimage(64*2,0,64,64));
        p2.addImage(this.img2.getSubimage(64*3,0,64,64));

        p2.addImage(this.img2.getSubimage(0,64,64,64));
        p2.addImage(this.img2.getSubimage(64,64,64,64));
        p2.addImage(this.img2.getSubimage(64*2,64,64,64));
        p2.addImage(this.img2.getSubimage(64*3,64,64,64));

        p2.addImage(this.img2.getSubimage(0,64*2,64,64));
        p2.addImage(this.img2.getSubimage(64,64*2,64,64));
        p2.addImage(this.img2.getSubimage(64*2,64*2,64,64));
        p2.addImage(this.img2.getSubimage(64*3,64*2,64,64));

        p2.addImage(this.img2.getSubimage(0,64*3,64,64));
        p2.addImage(this.img2.getSubimage(64,64*3,64,64));
        p2.addImage(this.img2.getSubimage(64*2,64*3,64,64));
        p2.addImage(this.img2.getSubimage(64*3,64*3,64,64));

        addParticleInfo(p2);

    }

    private void addParticleInfo(ParticleInfo p){
        particleInfo.put(p.id, p);
        nameInt.put(p.name, p.id);
    }

    public ParticleInfo getParticleInfo(int id){
        if(particleInfo.get(id) == null){
            System.out.println("GAME BROKEN" + id);
        }
        return particleInfo.get(id);
    }

    public ParticleInfo getParticleInfo(String name){
        if(nameInt.get(name) != null){
            return this.particleInfo.get(nameInt.get(name));
        }
        return null;
    }

    public int getParticleId(String name){
        if(this.nameInt.get(name) == null){
            System.out.println(name);
        }
        return this.nameInt.get(name);
    }




}

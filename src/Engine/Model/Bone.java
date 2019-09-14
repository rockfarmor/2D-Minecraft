package Engine.Model;

import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bone {

    private BufferedImage img;
    private String id;
    private Vector2d offset;
    private Vector2d origo;
    private double rotation;
    private int zIndex;

    private ArrayList<Bone> subBones;

    public Bone(BufferedImage img, Vector2d origo, Vector2d offset, double rotation, int zIndex, String id) {
        this.img = img;
        this.origo = origo;
        this.offset = offset;
        this.id = id;
        this.rotation = rotation;
        this.subBones = new ArrayList<>();
        this.zIndex = zIndex;
    }


    public void render(Graphics2D g2d, Vector2d pos, int scaleX, int scaleY, Color c) {


        AffineTransform prevTransform = g2d.getTransform();
        AffineTransform at = g2d.getTransform();

        at.translate((int)pos.getX(), (int)pos.getY());
        at.translate((int)this.offset.getX(), (int)this.offset.getY());

        at.scale(scaleX, scaleY);


        at.rotate(this.rotation);

        g2d.setTransform(at);

        for (int i = 0; i < this.subBones.size(); i++) {
            if (this.subBones.get(i).getzIndex() < this.zIndex)
                this.subBones.get(i).render(g2d, new Vector2d(), 1, 1, c);
        }

        //g2d.drawImage(this.img, (int)origo.getX(), (int)origo.getY(), null);
        renderImage(g2d);

        renderShadow(g2d, c);

        for (int i = 0; i < this.subBones.size(); i++) {
            if (this.subBones.get(i).getzIndex() >= this.zIndex)
                this.subBones.get(i).render(g2d, new Vector2d(), 1, 1, c);
        }
        g2d.setTransform(prevTransform);

    }

    public void renderImage(Graphics2D g2d) {
        g2d.drawImage(this.img, (int)origo.getX(), (int)origo.getY(), null);
    }

    public void renderImage(Graphics2D g2d, BufferedImage img) {
        g2d.drawImage(img, (int)origo.getX(), (int)origo.getY(), null);
    }

    public void renderShadow(Graphics2D g2d, Color c){
        renderShadowImg(g2d, c, this.img, 0 , 0, this.img.getWidth(), this.img.getHeight());
    }

    public void renderShadowImg(Graphics2D g2d, Color c, BufferedImage image, int offX, int offY, int width, int height){
        if(c != null && image != null){
            if (c.getAlpha() > 0) {
                BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int pixel = image.getRGB(x, y);
                        int alph = (pixel >> 24) & 0xff;
                        //int red = (pixel >> 16) & 0xff;
                        //int green = (pixel >> 8) & 0xff;
                        //int blue = (pixel >> 0) & 0xff;

                        if (alph > 0) {
                            img.setRGB(x, y, c.getRGB());
                        }
                    }
                }

                g2d.drawImage(img, (int) origo.getX() + offX, (int) origo.getY() + offY, width, height, null);
            }
        }
    }


    public void addBone(Bone b){
        subBones.add(b);
    }

    public int numBones(){
        return subBones.size();
    }

    public Bone getBone(int i){
        return subBones.get(i);
    }

    public BufferedImage getImg() {
        return img;
    }

    public Vector2d getOrigo() {
        return origo;
    }

    public int getzIndex() {
        return zIndex;
    }

    public Vector2d getOffset() {
        return offset;
    }

    public double getRotation() {
        return rotation;
    }

    public void setOffset(Vector2d v){
        this.offset = new Vector2d(v);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

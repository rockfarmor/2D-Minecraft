package Engine.Blocks;

import Engine.Constants;
import Engine.Enum.BlockSolidType;
import Engine.FileHandler.FileInput;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlockInfo {

    public int id;
    public String name;
    public boolean transparant;
    public boolean renderAll;
    public boolean gravityBlock;
    public BlockSolidType solidType;
    public boolean isLightSource;

    public boolean isSlab;


    public BufferedImage img;
    public BufferedImage topImage;
    public BufferedImage shadowImage;
    public BufferedImage shadowImageTop;

    public BufferedImage slabImage;
    public BufferedImage shadowSlabImage;

    private int[] animation;
    private int animationNum;
    private BufferedImage[] animationFrames;


    public BlockInfo(int id, String name, String imgSource, String top, boolean isSlab){
        this.id = id;
        this.transparant = false;
        this.renderAll = true;
        this.solidType = BlockSolidType.Solid;
        this.isLightSource = false;
        this.name = name;

        this.isSlab = isSlab;

        this.slabImage = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);

        this.gravityBlock = false;
        this.animationNum = 0;
        this.animation = null;
        this.animationFrames = null;

        if (this.name == "Sand" || this.name == "Gravel" || this.name == "Red Sand"){
            this.gravityBlock = true;
        }

        if(imgSource == null || imgSource.equals("")) {
            this.img = null;
            this.img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        } else {
            this.img = FileInput.LoadImage("Assets/textures/blocks/" + imgSource);

            if(this.img == null){
                System.out.println("Error loading + " + name);
                this.img = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
            } else {

                if(this.img.getHeight() > 16){
                    if(this.name == "Flowing Water"){
                        animation = new int[64];
                        for (int i = 0; i < 64; i++) {
                            this.animation[i] = i;
                        }

                    } else if(this.name == "Water"){
                        animation = new int[32];
                        for (int i = 0; i < animation.length; i++) {
                            this.animation[i] = i;
                        }

                    }  else if(this.name == "Lava"){
                        int[] lava = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
                        animation = lava;
                    } else if(this.name == "Flowing Lava"){
                        animation = new int[32];
                        for (int i = 0; i < animation.length; i++) {
                            this.animation[i] = i;
                        }

                    } else if(this.name == "Fire"){
                       int[] fire = {16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
                       this.animation = fire;
                    }
                    if(animation != null) {
                        this.animationFrames = new BufferedImage[this.animation.length];
                        for (int i = 0; i < this.animation.length; i++) {
                            int ani = animation[i];

                            int offY = ani * 16;
                            BufferedImage im = img.getSubimage(0, offY, 16,16);
                            this.animationFrames[i] = im;
                        }
                    }
                }
            }

        }


        if(top == null)
            this.topImage = null;
        else {

            this.topImage = FileInput.LoadImage("Assets/textures/blocks/" + top);

            if(this.topImage != null){

                if(this.topImage.getHeight() > 16){
                    this.topImage = this.topImage.getSubimage(0,0,16,16);
                }
            }

        }
        this.slabImage.getGraphics().drawImage(this.img.getSubimage(0,8,16,8), 0,8, null);

        this.shadowImage = new BufferedImage(this.img.getWidth(), this.img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int _y = 0; _y < this.img.getHeight(); _y++) {
            for (int _x = 0; _x < this.img.getWidth(); _x++) {

                int pixel = this.img.getRGB(_x, _y);

                int alph = (pixel >> 24) & 0xff;
                if (alph > 0) {
                    this.shadowImage.setRGB(_x, _y, Color.BLACK.getRGB());
                }
            }
        }

        if(this.topImage != null) {
            this.shadowImageTop = new BufferedImage(this.topImage.getWidth(), this.topImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

            for (int _y = 0; _y < this.topImage.getHeight(); _y++) {
                for (int _x = 0; _x < this.topImage.getWidth(); _x++) {

                    int pixel = this.topImage.getRGB(_x, _y);

                    int alph = (pixel >> 24) & 0xff;
                    if (alph > 0) {
                        this.shadowImageTop.setRGB(_x, _y, Color.BLACK.getRGB());
                    }
                }
            }
        } else {
            this.shadowImageTop = null;
        }

        this.shadowSlabImage = new BufferedImage(this.slabImage.getWidth(), this.slabImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int _y = 0; _y < this.slabImage.getHeight(); _y++) {
            for (int _x = 0; _x < this.slabImage.getWidth(); _x++) {

                int pixel = this.slabImage.getRGB(_x, _y);

                int alph = (pixel >> 24) & 0xff;
                if (alph > 0) {
                    this.shadowSlabImage.setRGB(_x, _y, Color.BLACK.getRGB());
                }
            }
        }

    }


    public void update(){
        if(animation != null){
            animationNum++;
            if(animationNum >= animation.length) animationNum = 0;
        }
    }


    public BufferedImage getImg() {

        if(this.isSlab){
            return this.slabImage;
        } else {

            if(img.getHeight() > 16){
                if(animation != null){

                    if(animationFrames != null) {
                        return animationFrames[animationNum];

                    }
                }
                return img.getSubimage(0,0,16,16);
            }

            return img;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTransparant(boolean transparant) {
        this.transparant = transparant;
    }

    public void setRenderAll(boolean renderAll) {
        this.renderAll = renderAll;
    }

    public void setSolidType(BlockSolidType solidType) {
        this.solidType = solidType;
    }

    public void setLightSource(boolean lightSource) {
        isLightSource = lightSource;
    }

}

package Engine.Renderer;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private BufferedImage sheet;

    private int width;
    private int height;

    private int numImages;

    private BufferedImage[] images;


    public SpriteSheet(BufferedImage sheet, int width, int height){
        this.sheet = sheet;

        this.width = width;
        this.height = height;
        this.numImages = sheet.getWidth() / width * sheet.getHeight() / height;

        this.images = new BufferedImage[this.numImages];

        int cols = sheet.getWidth() / width;
        int rows = sheet.getHeight() / height;

        for (int y = 0; y < sheet.getHeight() / height; y++) {
            for (int x = 0; x < sheet.getWidth() / width; x++) {
                int id = cols * y + x;
                this.images[id] = this.sheet.getSubimage(x * this.width, y * this.height, this.width, this.height);
            }
        }
    }

    public BufferedImage getSubImage(int x, int y, int width, int height){
        return this.sheet.getSubimage(x, y, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getSprite(int i){

        if (i >= 0 && i < this.images.length) return images[i];
        return null;
    }




}

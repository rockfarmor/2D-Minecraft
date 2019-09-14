package Engine.Gui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button {

    private int x;
    private int y;
    private int width;
    private int height;

    private String buttonText;

    private BufferedImage img;

    public Button(String buttonText, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonText = buttonText;
        this.img = null;
    }

    public Button(String buttonText, int x, int y, int width, int height, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonText = buttonText;
        this.img = img;
    }

    public Button(String buttonText, int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.buttonText = buttonText;
        this.img = img;
    }


    public boolean pointInButton(int pX, int pY, int windowX, int windowY, double scale){
        boolean in = false;
        double x_ = windowX + this.x  * scale;
        double y_ = windowY + this.y * scale;
        return pX >= x_ && pX < x_ + this.width*scale && pY >= y_ && pY <= y_ + this.height * scale;
    }


    public void render(Graphics2D g2d){
        if(this.img == null){
            //render with color
            g2d.setColor(Color.red);
            g2d.drawRect(this.x, this.y, this.width, this.height);
        } else {
            //render with img
            g2d.drawImage(this.img, this.x, this.y, this.width, this.height, null);
        }
    }


    public String getButtonText() {
        return buttonText;
    }
}

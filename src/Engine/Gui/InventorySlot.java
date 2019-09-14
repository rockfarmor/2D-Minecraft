package Engine.Gui;

public class InventorySlot {

    private int x, y, width, height, xId, yId;


    public InventorySlot(int x, int y, int width, int height, int xId, int yId) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xId = xId;
        this.yId = yId;
    }

    public boolean pointInButton(int pX, int pY, int windowX, int windowY, double scale){
        double x_ = windowX + this.x  * scale;
        double y_ = windowY + this.y * scale;
        return pX >= x_ && pX < x_ + this.width*scale && pY >= y_ && pY <= y_ + this.height * scale;
    }


    public int getxId() {
        return xId;
    }

    public int getyId() {
        return yId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

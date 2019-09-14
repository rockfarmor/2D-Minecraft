package Engine.Entities;

import Engine.Blocks.Block;
import Engine.Constants;
import Engine.Enum.BlockSolidType;
import Engine.Inventory.InventoryItem;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DropEntity extends Entity {

    private InventoryItem item;

    private long time;

    public static final int radius = 20;

    public static final long Decay_Time = 300000;

    public DropEntity(double x, double y, World state, InventoryItem item) {
        super(x, y, state);
        this.item = item;
        this.time = System.currentTimeMillis();
    }

    @Override
    public void update(double dt) {
        if(!this.getRemove()){


            Vector2d posVecReal = Constants.PosToTilePos(this.getPosition().getX(), this.getPosition().getY());

            Block bReal = getState().getBlock((int)posVecReal.getX(), (int)posVecReal.getY());
            String dir = "";
            if(bReal.getBlockId() == Constants.blockHandler.getBlockId("Flowing Water")){
                if(bReal.getSpecialValue() < Constants.WaterTravel){

                    Block left = getState().getBlock((int)posVecReal.getX()-1, (int)posVecReal.getY());
                    Block right = getState().getBlock((int)posVecReal.getX()+1, (int)posVecReal.getY());

                    if(left.getBlockId() == Constants.blockHandler.getBlockId("Flowing Water") && left.getSpecialValue() < bReal.getSpecialValue()){
                        this.getPosition().setX(this.getPosition().getX()-0.7);
                        dir = "left";
                    } else if(right.getBlockId() == Constants.blockHandler.getBlockId("Flowing Water") && right.getSpecialValue() < bReal.getSpecialValue()){
                        this.getPosition().setX(this.getPosition().getX()+0.7);
                        dir = "right";
                    }
                }
            }

            Vector2d posVec2 = Constants.PosToTilePos(this.getPosition().getX()-4, this.getPosition().getY()-4);
            Vector2d posVec3 = Constants.PosToTilePos(this.getPosition().getX()+4, this.getPosition().getY()-4);
            Vector2d posVec4 = Constants.PosToTilePos(this.getPosition().getX(), this.getPosition().getY()-4-5);
            Block b2 = getState().getBlock((int)posVec2.getX(), (int)posVec2.getY());
            Block b3 = getState().getBlock((int)posVec3.getX(), (int)posVec3.getY());
            Block b4 = getState().getBlock((int)posVec4.getX(), (int)posVec4.getY());


            if(Math.abs(this.getVelocity().getX()) > 0 || !dir.equals("")) {
                if (b2.getSolidType() == BlockSolidType.Solid) {
                    this.getPosition().setX((int) posVec2.getX() * 16 + 16 + 4);
                    this.getVelocity().setX(0);
                }

                if (b3.getSolidType() == BlockSolidType.Solid) {
                    this.getPosition().setX((int) posVec3.getX() * 16 - 4);
                    this.getVelocity().setX(0);
                }
            }

            if(this.getVelocity().getY() < 0){
                if(b4.getSolidType() == BlockSolidType.Solid){
                    this.getVelocity().setY(0);
                    this.getPosition().setY((int) posVec4.getY() * 16 +20);
                }
            }


            Vector2d posVec = Constants.PosToTilePos(this.getPosition().getX(), this.getPosition().getY()+5);
            Block b = getState().getBlock((int)posVec.getX(), (int)posVec.getY());


            if(b.getSolidType() == BlockSolidType.Solid) {
                if(this.getVelocity().getY() > 0) {
                    this.getVelocity().set(0, 0);
                    this.getPosition().setY((int) posVec.getY() * 16 - 4);
                }
            } else {
                this.getVelocity().setY(this.getVelocity().getY() + this.getState().getGravity().getY() * dt * 0.4);
                this.getPosition().set(this.getPosition().getX() + this.getVelocity().getX() * dt, this.getPosition().getY() + this.getVelocity().getY() * dt);
            }

            posVec = Constants.PosToTilePos(this.getPosition().getX(), this.getPosition().getY()-2);
            b = getState().getBlock((int)posVec.getX(), (int)posVec.getY());

            if(b.getSolidType() == BlockSolidType.Solid){
                this.getPosition().setY(this.getPosition().getY()-0.7);
            }

            long lifetime = System.currentTimeMillis() - time;

            if(lifetime > 1000){
                Player p = getState().getPlayer();

                if(Constants.distance(p.getPosition(), this.getPosition()) < radius){
                    this.setRemove(true);
                    p.getInventory().addItem(item.getId(), item.getNum(), item.isItem());

                }
            }

            if(lifetime > 300000){
                this.setRemove(true);
            }



        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if(!this.getRemove()) {
            BufferedImage img = item.getImage();




            int numExtra = (int)(item.getNum() / 40.0);
            int x = (int) this.getPosition().getX() - 4;
            int y = (int) this.getPosition().getY() - 8;
            if(numExtra > 0){
                for (int i = numExtra+1; i >= 0; i--) {


                    int alpha = (int)(i * 60);
                    if(alpha > 255) alpha = 255;
                    g2d.setColor(new Color(0,0,0, alpha));
                    g2d.drawImage(img, x-i, y-i, 8, 8, null);
                    g2d.fillRect(x-i,y-i,8,8);
                }
            }


            g2d.drawImage(img, x, y, 8, 8, null);

            //g2d.setColor(Color.RED);
            //g2d.drawOval(x-radius/2,y-radius/2,radius, radius);

        }
    }


    public InventoryItem getItem() {
        return item;
    }
}

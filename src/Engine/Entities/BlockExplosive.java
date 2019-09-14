package Engine.Entities;

import Engine.Blocks.Block;
import Engine.Constants;
import Engine.World.World;
import Engine.javax.vecmath.Vector2d;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.scene.transform.Affine;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class BlockExplosive extends Entity{

    private int tick;




    public BlockExplosive(double x, double y, World state) {
        super(x, y, state);
        this.tick = 700;
    }

    public BlockExplosive(Vector2d pos, World state) {
        super(pos, state);
        this.tick = 700;
    }


    @Override
    public void update(double dt) {

        if (!this.getRemove()) {

            Vector2d posVec = Constants.PosToTilePos(new Vector2d(this.getPosition().getX(), this.getPosition().getY()-8));
            Block b = getState().getBlock((int)posVec.getX(), (int)posVec.getY() +1, 1);
            if (b.getBlockId() == Block.Air){
                this.getVelocity().setY(this.getVelocity().getY() + this.getState().getGravity().getY() * dt * 0.6);
                this.tick -= 1;
            } else {
                this.getVelocity().scale(0);
                this.tick -= 4;
            }

            this.getPosition().set(this.getPosition().getX() + this.getVelocity().getX() * dt, this.getPosition().getY() + this.getVelocity().getY() * dt);

            if (this.tick < 0){
                this.setRemove(true);
                getState().explode((int)this.getPosition().getX()/16, (int)this.getPosition().getY() / 16, 6);

            }
        }

    }

    @Override
    public void render(Graphics2D g2d) {
        if (!this.getRemove()) {
            double scale = (1 - this.tick / (700.0)) + 1;

            g2d.drawImage(Constants.blockHandler.getBlockInfo("Tnt").img, (int) this.getPosition().getX() - (int)(8 * scale), (int) this.getPosition().getY() - (int)(8 * scale),(int)(16 * scale),(int)(16*scale), null);
            double alpha = ((this.tick % 20) / 20.0) * 255;

            g2d.setColor(new Color(255,255,255, (int)(alpha)));
            g2d.fillRect((int) this.getPosition().getX() - (int)(8 * scale), (int) this.getPosition().getY() - (int)(8 * scale), (int)(16 * scale), (int)(16 * scale));
        }
    }





}

package Engine.FileHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileInput {


    public static BufferedImage LoadImage(String in){

        BufferedImage img = null;


        try {
            img = ImageIO.read(new File(in));

        } catch (IOException e) {
        }


        return img;
    }





}

package Engine.Gui;

import Engine.Renderer.MFrame;
import Engine.World.World;

import java.awt.*;
import java.util.*;

public class GuiHandler {


    private HashMap<String, Window> windows;
    private boolean activeWindow;

    public GuiHandler() {
        this.windows = new HashMap<>();
        init();

    }

    public void init(){
        this.addWindow(WindowFactory.InventoryWindow());
        this.addWindow(WindowFactory.CraftingTableWindow());
    }

    public void update(int mouseX, int mouseY, boolean mouseLeftDown, boolean mouseLeftClicked, MFrame frame, World world){

        Set set = this.windows.entrySet();
        Iterator iterator = set.iterator();
        this.activeWindow = false;
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            Window w = (Window) mentry.getValue();
            if(w.isActive()){
                w.update(mouseX, mouseY, mouseLeftDown, mouseLeftClicked, world);
                w.setPosition(frame.getWidth()/2 - (w.getWidth() * w.getScale()) / 2, frame.getHeight()/2 - (w.getHeight() * w.getScale()) / 2);
                this.activeWindow = true;
            }
        }
    }


    public void render(Graphics2D g2d){
        Set set = this.windows.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            Window w = (Window) mentry.getValue();
            if(w.isActive()){
                w.render(g2d);
            }
        }
    }



    public void activeWindow(String name, World world){

        if(this.getWindow(name) != null){

            Set set = this.windows.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                Window w = (Window)mentry.getValue();
                w.setActive(false, world);
            }
            this.getWindow(name).setActive(true, world);
        }
    }


    public void deActiveWindow(String name, World world){
        if(this.getWindow(name) != null){
            this.getWindow(name).setActive(false, world);
        }

    }

    public boolean isWindowActive(String name){
        if(this.getWindow(name) != null){
            return this.getWindow(name).isActive();
        }
        return false;
    }

    public void toggleWindow(String name, World world){
        if(this.getWindow(name) != null){
            if(this.getWindow(name).isActive()){
                this.deActiveWindow(name, world);
            } else {
                this.activeWindow(name, world);
            }

        }
    }



    public void addWindow(Window w){
        this.windows.put(w.getName(), w);
    }

    public Window getWindow(String windowName){
        return this.windows.get(windowName);
    }


    public boolean isActiveWindow() {
        return activeWindow;
    }
}

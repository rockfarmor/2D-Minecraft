package Engine.Debug;

import java.util.ArrayList;

public class Debugger {

    private ArrayList<Log> logs;

    public Debugger() {
        this.logs = new ArrayList<>();
    }


    public void addLog(String text, int sec){
        this.logs.add(new Log(text, System.currentTimeMillis() + sec * 1000));
    }

    public Log getLog(int i){
        if (i >= 0 && i < logs.size())
            return this.logs.get(i);
        return null;
    }

    public int logSize(){
        return this.logs.size();
    }


    public void update(){
        long time = System.currentTimeMillis();

        for (int i = this.logs.size()-1; i >= 0; i--) {
            Log l = this.logs.get(i);
            if (l.isDone(time)){
                this.logs.remove(i);
            }
        }
    }


}

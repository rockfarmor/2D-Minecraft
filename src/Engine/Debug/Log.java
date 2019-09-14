package Engine.Debug;

public class Log {

    private long timeStamp;
    private String text;

    public Log(String text, long timeStamp){
        this.text = text;
        this.timeStamp = timeStamp;
    }

    public boolean isDone(long timeStamp){
        return this.timeStamp < timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getText() {
        return text;
    }
}

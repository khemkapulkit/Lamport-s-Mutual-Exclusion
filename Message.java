import java.io.Serializable;

public class Message implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    public int senderId;
    public String type;
    public String message;
    public int ts;
    public NodeObject NodeObject;
    public String sequenceNumber;
    
    public Message(int source, String type, String text, int ts) 
    {
        this.senderId = source;
        this.type = type;
        this.message = text;
        this.NodeObject = null;
        this.ts =ts;
    }
}

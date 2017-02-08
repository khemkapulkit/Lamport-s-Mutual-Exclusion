
import java.io.Serializable;

public class NodeDetails implements Serializable
{
	private static final long serialVersionUID = 1L;
	public String nodeHostName;
	public int nodePortNumber;
	
	public NodeDetails(String nodeHostName,int nodePortNumber) 
	{
        this.nodeHostName = nodeHostName;
        this.nodePortNumber = nodePortNumber;
    }
}
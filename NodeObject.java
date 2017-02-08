import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class NodeObject implements Serializable 
{
	private static final long serialVersionUID = 1L;
	public int nodeId;
	
	public int numOfMessages;
	public int maxDelayBetweenTOB;
	
	public int messageSent;
	public int messageToBeSent;
	public boolean isActive;
	public int nodeRandomNumber;
	
	public List<Integer> allNodes = new ArrayList<Integer>();
	
	
    public NodeDetails nodeDetails;						// Information about Node's host name & port number
	public NeighborObject neighborObject;				// Has information about all the neighbors
	
	public HashMap<Integer, NodeDetails> nodeHashMap;	// Has information of all the nodes, used to make connections
	
	
	public HashMap<Integer, Connection> nodeServerConnectionMap;	// Has information of all the nodes, used to make connections
	public HashMap<Integer, Connection> nodeClientConnectionMap;	// Has information of all the nodes, used to make connections
	
	
	public int totalNodes;
	static public int[] clock;
	
	public int connectedcount = 0;
	
	public HashMap<String, Message> sequencedMessages = new HashMap<String, Message>();
	public HashMap<String, Message> receivedMessages = new HashMap<String, Message>();
	
	
	public NodeObject(int nodeId, String configFileName) 
	{
        Random r = new Random();
        
		this.nodeId = nodeId;
		
		ConfigReader.initConfig(configFileName);
		this.totalNodes = ConfigReader.totalNodes;
		this.numOfMessages = ConfigReader.numOfMessages;
		this.maxDelayBetweenTOB = ConfigReader.maxDelayBetweenTOB;
		this.allNodes = ConfigReader.allNodes;
		
        this.messageSent = 0;
        this.messageToBeSent = ConfigReader.numOfMessages;
        
        
        this.nodeServerConnectionMap = new HashMap<Integer, Connection>();
        this.nodeClientConnectionMap = new HashMap<Integer, Connection>();
        
        clock = new int[this.totalNodes];
        for(int i =0;i<this.totalNodes;i++)
        {
        	clock[i]= 0;
        }
        
		this.nodeHashMap = ConfigReader.nodeMap;
        this.nodeDetails = nodeHashMap.get(nodeId);
        this.neighborObject = ConfigReader.neighborMap.get(nodeId);
        
        this.nodeRandomNumber = r.nextInt(100);
        
        if(neighborObject == null)
        	this.neighborObject = new NeighborObject();	
        if(neighborObject.neighbors == null)
        	this.neighborObject.neighbors = new ArrayList<Integer>();
    }
}
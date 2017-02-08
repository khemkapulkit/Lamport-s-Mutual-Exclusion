import java.net.*;
import java.io.*;

public class Client implements Runnable{
	private int neighbourId;
	private NodeObject node;
	private Socket socket;
	private boolean isServerUp=false;
	
	public Client(int neighbourId, NodeObject node){
		this.neighbourId = neighbourId;
		this.node = node;
		Thread t = new Thread(this);
		t.setName("client2");
		t.start();
	}
	
	public void run(){	
		try
		{
			Message message;
			ObjectInputStream reader = null;
			if(neighbourId<node.totalNodes)
			{
				//socket = new Socket(node.nodeHashMap.get(neighbourId).nodeHostName, node.nodeHashMap.get(neighbourId).nodePortNumber);
				
				isConnected(node.nodeHashMap.get(neighbourId).nodeHostName, node.nodeHashMap.get(neighbourId).nodePortNumber);
				
				while(isServerUp == false)
				{
					Thread.sleep(200);
					//System.out.println("Trying to connect to "+ neighbourId);
					isConnected(node.nodeHashMap.get(neighbourId).nodeHostName, node.nodeHashMap.get(neighbourId).nodePortNumber);
				}
				node.connectedcount++;
				Connection nodeconnection = new Connection();
				reader = new ObjectInputStream(socket.getInputStream());
				nodeconnection.reader = reader;
				nodeconnection.writer = new ObjectOutputStream(socket.getOutputStream());
				nodeconnection.sock = socket;
				node.nodeClientConnectionMap.put(neighbourId,nodeconnection);
				
				message = new Message(node.nodeId,"Connect","Hi from "+ node.nodeId,-1);
				
				//System.out.println("Sending conenction request to "+ neighbourId +" from node - " + node.nodeId);
				
				nodeconnection.writer.writeObject(message);
				
				
			}
	
		}catch(Exception e){
			//e.printStackTrace();
			//System.out.println("failed in client " + node.nodeId);
		}
	}
	public void isConnected(String hostname, int port){
		try{
			socket = new Socket(hostname, port);
			isServerUp = true;
		}catch(Exception e){
			socket = null;
			isServerUp = false;
			//e.printStackTrace();
			//System.out.println("failed in client " + node.nodeId + " ");
		}
	}
}

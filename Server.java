import java.net.*;
import java.io.*;


public class Server implements Runnable{
	private Socket sock;
	private NodeObject node;
	
	public Server(Socket sock, NodeObject node){
		this.sock = sock;
		this.node = node;
		Thread t = new Thread(this);
		t.setName("Server1");
		t.start();
	}
	
	public void run()
	{
		try
		{
			ObjectOutputStream writer = new ObjectOutputStream(sock.getOutputStream());
			ObjectInputStream reader = new ObjectInputStream(sock.getInputStream());
			Message message = (Message)reader.readObject();
			
			if(message.type.compareToIgnoreCase("connect")==0)
			{
				//System.out.println("Server: received message from node "+message.senderId + " at node - "+ node.nodeId);
				Connection nodeconnection = new Connection();
				nodeconnection.reader = reader;
				nodeconnection.writer = writer;
				nodeconnection.sock = sock;
				node.nodeServerConnectionMap.put(message.senderId,nodeconnection);
			}	
			else
			{
				System.out.println("Server: received message from node "+message.senderId + " at node - "+ node.nodeId + " with type -" + message.type);
			}
		}catch(Exception e){
			//System.out.println("Failed in server ");
		}
	}
	
}

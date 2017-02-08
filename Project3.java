

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class Project3 implements Runnable{
	
	int nodeId = 0;
	PrintWriter writer = null;
	String configFileName = null;
	String configFileNameWOExt = null;
	static NodeObject nodeObj = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 
		if(args.length == 0) 
		{
			System.exit(0);
		}
		
		Project3 project = new Project3(args[0],args[1]);
		
	}
	
	public Project3(String nodeIdStr, String filename){
		/*
		 *  	Store passed argument as nodeId
		 */
		nodeId = Integer.parseInt(nodeIdStr);
		configFileName = filename;
		
		if (configFileName.indexOf(".") > 0)
	    	configFileNameWOExt = configFileName.substring(0, configFileName.lastIndexOf("."));
		
		/*
		 * 	Create instance of NodeObject class. This will store all the details for this particular node
		 */
	    nodeObj = new NodeObject(nodeId,configFileName);
	    
	    System.out.println("Node Id - " + nodeObj.nodeId );
	    System.out.println("Node Host Name - " + nodeObj.nodeDetails.nodeHostName );
	    System.out.println("Node Port Number - " + nodeObj.nodeDetails.nodePortNumber );
	    
	    Thread t = new Thread(this);
		t.setName("server");
		t.start();
 
 }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String threadName = Thread.currentThread().getName();
		if(threadName.compareToIgnoreCase("Server")==0)
		{
			//System.out.println("In project3 server thread from - "+ nodeId);
			try
			{
				ServerSocket serverSock = new ServerSocket(nodeObj.nodeDetails.nodePortNumber);
				Thread t = new Thread(this);
				t.setName("Client");
				t.start();
				
				int connectedCount=0;
				while(connectedCount < nodeObj.totalNodes)
				{
					nodeObj.connectedcount++;
					new Server(serverSock.accept(),nodeObj);
					//System.out.println("Accepted Socket at node - "+ nodeId);
					connectedCount++;
				}
				//System.out.println("Stopping listening server for "+nodeObj.nodeId);
			}catch(Exception e){
				//e.printStackTrace();
				System.out.println("Error in Project3 Server");
			}
		}
		else if(threadName.compareToIgnoreCase("Client")==0)
		{
			//System.out.println("In project3 client thread from - "+ nodeId );
			for(int nodeId : nodeObj.allNodes){
				try {
						Thread.sleep(2000);
						new Client(nodeId, nodeObj);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(threadName.compareToIgnoreCase("Server")==0)
		{
			while(nodeObj.messageToBeSent > 0)
			{
				boolean CS_Permission = true; 
				while(true)
				{
					try {
						CS_Permission = Mutex.csenter();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					if(CS_Permission)
					{
						break;
					}
					else
					{
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
					}
				}
				
				if(CS_Permission)
				{
					// Execute TOB...
					TOB.tobSend(nodeObj);
					// Call csLeave()....
					System.out.println("~~~~~~~~~~Node -" + nodeObj.nodeId + " sent -" + nodeObj.messageSent);
					try {
						Mutex.csexit();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(nodeObj.maxDelayBetweenTOB);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
		}
		
		if(threadName.compareToIgnoreCase("Client")==0){
			for(int i=0; i<nodeObj.totalNodes ; i++)
			{
				Connection con = nodeObj.nodeServerConnectionMap.get(i);
				new MessageProcessor(con,nodeObj);	
			}
		}
	}
}

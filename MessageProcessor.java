import java.io.IOException;


public class MessageProcessor implements Runnable {

	public Connection con;
	public NodeObject nodeObj;
	public MessageProcessor(Connection con,NodeObject nodeObj)
	{
		this.con = con;
		this.nodeObj = nodeObj;
		Thread t = new Thread(this);
		t.start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true)
			{
				Message message = (Message)con.reader.readObject();
				//System.out.println("Server: received message from node "+message.senderId + " at node - "+ nodeObj.nodeId + " with type -" + message.type);
				if(message.type.compareToIgnoreCase("TOB")==0)
				{
					TOB.tobReceive(nodeObj,message);
				}
				else if(message.type.compareToIgnoreCase("mutex")==0)
				{
	                Mutex.getMessage(message);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class TOB {

	public static int MESSAGES_PER_BROADCAST = 2; 
	
	public static void tobSend(NodeObject nodeObj)
	{
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		
		if(nodeObj.messageToBeSent < MESSAGES_PER_BROADCAST)
		{
			MESSAGES_PER_BROADCAST = nodeObj.messageToBeSent;
		}
		for(int i=0; i<nodeObj.totalNodes; i++)
		{
			Connection con = nodeObj.nodeClientConnectionMap.get(i);
			int messageNumber = nodeObj.messageSent; 
			if(con != null)
			{
				Socket soc = con.sock;
				if(soc != null)
				{
					try {
						for(int msgCount=0; msgCount < MESSAGES_PER_BROADCAST ; msgCount++)
						{
							
							Message message = new Message(nodeObj.nodeId,"TOB",String.valueOf(randomInt),-1);
							message.sequenceNumber = nodeObj.nodeId+String.valueOf(messageNumber);//((nodeObj.nodeId+1) * 10) + messageNumber;
							//System.out.println("Sending tob message to "+ i +" from node - " + nodeObj.nodeId + " at socket - " + con.sock.getPort() + " ---- " + con.sock.getInetAddress());
							con.writer.writeObject(message);	
							messageNumber++;
						}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("Failed IO" );	
						e1.printStackTrace();
					}					
				}
				else
				{
					System.out.println("No Socket found from " + nodeObj.nodeId + " to " + i);	
				}

			}
			else
			{
				System.out.println("No Connection found from " + nodeObj.nodeId + " to " + i);	
			}
		}
		nodeObj.messageSent = nodeObj.messageSent + MESSAGES_PER_BROADCAST; 
		nodeObj.messageToBeSent = nodeObj.messageToBeSent - MESSAGES_PER_BROADCAST;
	}
	public static void tobReceive(NodeObject nodeObj, Message message)
	{
		
		nodeObj.sequencedMessages.put(message.sequenceNumber, message);
		
		//System.out.println("Server: received message from node "+message.senderId + " at node - "+ nodeObj.nodeId + " as -" + message.message);
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("config-"+nodeObj.nodeId+".out" , true)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int k=0; k< nodeObj.totalNodes; k++)
		{
			for(int l=0; l<nodeObj.numOfMessages; l++)
			{
				Message m = nodeObj.sequencedMessages.remove(String.valueOf(k)+String.valueOf(l));
				if(m != null)
				{
					nodeObj.receivedMessages.put(String.valueOf(k)+String.valueOf(l), m);
				}
			}
		}
		if((nodeObj.numOfMessages*nodeObj.totalNodes) == nodeObj.receivedMessages.size())
		{
			for(int k=0; k< nodeObj.totalNodes; k++)
			{
				for(int l=0; l<nodeObj.numOfMessages; l++)
				{
					Message m = nodeObj.receivedMessages.remove(String.valueOf(k)+String.valueOf(l));
					if(m != null)
					{
						//out.println(m.message + " - "+ m.sequenceNumber);
						out.println(m.message);
					}
				}
			}	
			System.out.println("Finished printing for - " + nodeObj.nodeId);
		}
		
		out.close();
	}
	
}

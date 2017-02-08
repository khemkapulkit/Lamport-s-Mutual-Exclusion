import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;



public class Mutex {

	static NodeObject nodeObj = Project3.nodeObj;
	static boolean gotreply[] = new boolean[nodeObj.totalNodes];
	static int clock = 0;
	static boolean enter =false;
	static ArrayList<link> queue = new ArrayList<link>();
	private static final Semaphore sem = new Semaphore(1);
	public static void sortList(){
		Collections.sort(queue, new Comparator<link>(){
		    public int compare(link l1, link l2) {
		    	if(l1.ts != l2.ts){
		        return Integer.compare(l1.ts,l2.ts);
		    	}
		    	else{
		    		return Integer.compare(l1.sender,l2.sender);
		    	}
		    }
		});
	}
	
	
	public static void csexit() throws InterruptedException, IOException{
		System.out.println(nodeObj.nodeId+"leaves CS");
		sem.acquire();
		for(int i=0; i<nodeObj.totalNodes; i++)
		{
			
			Connection con = nodeObj.nodeClientConnectionMap.get(i);
			if(con != null)
			{
				Socket soc = con.sock;
				if(soc != null)
				{
					
						
							Message message = new Message(nodeObj.nodeId,"mutex","release",clock);
							//System.out.println("Sending release message to "+ i +" from node - " + nodeObj.nodeId + " at socket - " + con.sock.getPort() + " ---- " + con.sock.getInetAddress());
							con.writer.writeObject(message);	
						
											
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
		clock++;
		sem.release();
	}	
	
	public static boolean csenter() throws InterruptedException, IOException{
		for(int i=0;i<nodeObj.totalNodes;i++){
			gotreply[i] = false;
		}
		sem.acquire();
		link l = new link();
		l.ts = clock;
		l.sender = nodeObj.nodeId;
		queue.add(l);
		sortList();
		for(int i=0; i<nodeObj.totalNodes; i++)
		{
			
			Connection con = nodeObj.nodeClientConnectionMap.get(i);
			if(con != null)
			{
				Socket soc = con.sock;
				if(soc != null)
				{
					
						
							Message message = new Message(nodeObj.nodeId,"mutex","req",clock);
							//System.out.println("Sending mutex message to "+ i +" from node - " + nodeObj.nodeId + " at socket - " + con.sock.getPort() + " ---- " + con.sock.getInetAddress());
							con.writer.writeObject(message);	
						
											
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
		clock++;
		sem.release();
		while(enter!=true){
			boolean replyrecieved=true;
			boolean topofqueue=false;
			for(int j=0;j<nodeObj.totalNodes;j++){
				if(j!=nodeObj.nodeId){
					if(!gotreply[j]){replyrecieved= false;}
				}
			}
			if(queue.get(0).sender == nodeObj.nodeId){
				topofqueue=true;
			}
			if(replyrecieved && topofqueue){
				enter = true;
			}
		}
		
		System.out.println(nodeObj.nodeId+" enters critical section");
		return true;

			
	}


	public static synchronized void getMessage(Message message) throws IOException, InterruptedException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("mutex-"+nodeObj.nodeId+".out" , true)));
		out.println(message.message + " - "+ message.ts);
        out.close();
		if(message.message.compareToIgnoreCase("req")==0){
			sem.acquire();
			link l1 =new link();
			l1.sender= message.senderId;
			l1.ts= message.ts;
			queue.add(l1);
			sortList();
			clock = Math.max(clock, message.ts)+1;
			Connection con = nodeObj.nodeClientConnectionMap.get(message.senderId);
			
			if(con != null)
			{
				Socket soc = con.sock;
				if(soc != null)
				{
						
							Message message1 = new Message(nodeObj.nodeId,"mutex","reply",clock);
							//System.out.println("Sending mutex reply message to "+ message.senderId +" from node - " + nodeObj.nodeId + " at socket - " + con.sock.getPort() + " ---- " + con.sock.getInetAddress());
							con.writer.writeObject(message1);	
						
											
				}
				else
				{
					System.out.println("No Socket found from " + nodeObj.nodeId + " to " + message.senderId);	
				}

			}
			else
			{
				System.out.println("No Connection found from " + nodeObj.nodeId + " to " + message.senderId);	
			}
			sem.release();
		}
		
		if(message.message.compareToIgnoreCase("reply")==0){
			gotreply[message.senderId]=true;
		}
		
		if(message.message.compareToIgnoreCase("release")==0){
			
			sem.acquire();
			Iterator<link> iter = queue.iterator();
			while (iter.hasNext()) {
			    if (iter.next().sender == message.senderId) {
			        iter.remove();
			    }
			}
			sem.release();
		}
		
	}
	
	
	
}

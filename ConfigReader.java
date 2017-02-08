

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ConfigReader 
{
	
	/*
	 * Read config file on each node and store config details such node details, available tokens for that node 
	 */
    public static HashMap<Integer, NodeDetails> nodeMap = new HashMap<Integer, NodeDetails>();
    public static HashMap<Integer, NeighborObject> neighborMap = new HashMap<Integer, NeighborObject>();
    public static int numOfMessages;
    public static int maxDelayBetweenTOB;
    public static List<Integer> allNodes = new ArrayList<Integer>();
    
    public static String netId;
	public static int totalNodes = 0;
	
    
	public static void initConfig(String configFileName) 
    {
    	File f = new File(configFileName);
    	BufferedReader br;
    	 
    	try 
    	{
    		
			br = new BufferedReader(new FileReader(f));
			String line = null;
			Integer count = 0;
			
			while ((line = br.readLine()) != null) 
			{
				/*
				 * If line is empty or line is a comment in config file skip it
				 */
				if (line.length() == 0 ) {
					continue;
				}
				else if(line.startsWith("#") || line.trim().startsWith("#") ) {
					continue;
				}
				else
				{
					/*
					 * If it is first line without comments, It has information about netId and total nodes
					 */
					if(count == 0)
					{
						//System.out.println("~~~~~~~~~~~~~ Total Nodes and NetId" + line);
						Integer paramCount = 0;
						String totalNodeStr = null,numOfMessagesStr = null,maxDelayBetweenTOBStr = null;
						
						for(String str : line.split(" "))
						{
							if(!str.isEmpty())
							{
								if(paramCount == 0)
								{
									totalNodeStr = str;
									paramCount++;
								}
								else if (paramCount == 1)
								{
									numOfMessagesStr = str;
									paramCount++;
								}
								else if (paramCount == 2)
								{
									maxDelayBetweenTOBStr = str;
									paramCount++;
								}
							}
						}
						totalNodes = Integer.parseInt(totalNodeStr.trim());
						
						numOfMessages = Integer.parseInt(numOfMessagesStr.trim());
						maxDelayBetweenTOB = Integer.parseInt(maxDelayBetweenTOBStr.trim());
						
						
						count++;
						continue;
					}
					/*
					 * If it is line after first line and till the count of totalnodes
					 * i.e. line will have information about nodeId, Node HostName and Node PortNumber
					 */
					else if(count > 0 && count <= totalNodes)
					{
						//System.out.println("~~~~~~~~~~~~~ Node Cofigurations " + line);
						Integer paramCount = 0;
						String nodeIdStr = null,nodeNameStr = null,nodePortStr = null;
						for(String str : line.split(" "))
						{
							if(!str.isEmpty())
							{
								if(paramCount == 0)
								{
									nodeIdStr = str;
									paramCount++;
								}
								else if (paramCount == 1)
								{
									nodeNameStr = str;
									paramCount++;
								}
								else if (paramCount == 2)
								{
									nodePortStr = str;
									paramCount++;
								}
							}
						}
						allNodes.add(Integer.parseInt(nodeIdStr.trim()));
						NodeDetails info = new NodeDetails(nodeNameStr.trim(), Integer.parseInt(nodePortStr.trim()));
						nodeMap.put(Integer.parseInt(nodeIdStr.trim()), info);
						count++;
						continue;
					}
				}
			}
			br.close();
		} 
    	catch (FileNotFoundException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

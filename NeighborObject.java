
import java.io.Serializable;
import java.util.ArrayList;

public class NeighborObject implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Integer> neighbors;
    public NeighborObject() 
    {
    	neighbors = new ArrayList<Integer>();
    }
}

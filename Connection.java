import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {

	public ObjectOutputStream writer;
	public ObjectInputStream reader;
	public Socket sock;
	
	public Connection()
	{
		writer = null;
		reader = null;
		sock = null;
	}
	
}

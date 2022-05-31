import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MiObjectOutputStream extends ObjectOutputStream {

	public MiObjectOutputStream(OutputStream o) throws IOException {
		super(o);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void writeStreamHeader() throws IOException {
		reset();
	}
}

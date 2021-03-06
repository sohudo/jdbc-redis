package br.com.svvs.jdbc.redis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RedisSocketIO implements RedisIO {
	
	private Socket socket = null;
	private OutputStreamWriter toServerSocket   = null;
	private InputStreamReader  fromServerSocket = null;
	
	public RedisSocketIO(String host, int port) throws UnknownHostException, IOException {	
		
		this.socket = new Socket(host, port);
		
		this.toServerSocket   = new OutputStreamWriter(this.socket.getOutputStream());
		this.fromServerSocket = new InputStreamReader(this.socket.getInputStream());
	}
	
	@Override
	public String sendRaw(String command) throws IOException {
		
		this.toServerSocket.write(command.toCharArray());
		this.toServerSocket.flush();
		
		StringBuilder sb = new StringBuilder();
		
		while(this.fromServerSocket.ready()  || sb.length() == 0) {
			char c = (char) this.fromServerSocket.read();
			sb.append((char) c);
		}
		
		return sb.toString();
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
	}

}

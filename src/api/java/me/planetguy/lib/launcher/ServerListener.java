package me.planetguy.lib.launcher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener {
	
	public static void main(String[] args) throws Exception {
		ServerSocket sock=new ServerSocket(Integer.parseInt(args[0]));
		boolean validID=false;
		while(!validID) {
			Socket con=sock.accept();
			ObjectInputStream s=new ObjectInputStream(con.getInputStream());
			String id=s.readUTF();
			validID = (id != null && id.equals(args[1]));
			con.close();
		}
		sock.close();
	}

}

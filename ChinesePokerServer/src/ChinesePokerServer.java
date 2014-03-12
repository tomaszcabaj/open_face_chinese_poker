import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import chinese.poker.common.interfaces.ChinesePokerRemote;

public class ChinesePokerServer {
	static ChinesePokerRemote chinesePokerRemote;

	public static void main(String[] args) {
		try {
			chinesePokerRemote = new ChinesePoker();
			UnicastRemoteObject.exportObject(chinesePokerRemote, 0);
			LocateRegistry.createRegistry(1099);
			Naming.rebind("rmi://192.168.0.122:1099/chinese_poker", chinesePokerRemote);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

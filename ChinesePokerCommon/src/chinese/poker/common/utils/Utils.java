package chinese.poker.common.utils;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.swing.ImageIcon;

import chinese.poker.common.interfaces.ChinesePokerRemote;

public class Utils {

	public static ImageIcon createImageIcon(String path, String description,
			Object resourceRoot) {
		java.net.URL imageURL = resourceRoot.getClass().getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return new ImageIcon(imageURL, description);
		}
	}
	
	public static ChinesePokerRemote rmiLookUp() {
		try {
			return (ChinesePokerRemote) Naming
					.lookup("rmi://192.168.0.122:1099/chinese_poker");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (NotBoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}

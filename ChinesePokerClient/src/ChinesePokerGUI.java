import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import chinese.poker.common.interfaces.ChinesePokerRemote;
import chinese.poker.common.struct.ShortPokerHand;
import chinese.poker.common.utils.Utils;

public class ChinesePokerGUI extends JFrame implements WindowListener {

	private static final long serialVersionUID = -7443161807198078407L;

	private PlayerPanel playerPanel;
	private PlayerPanel opponentPanel;
	private DealCardsPanel newCardPanel;
	
	private Image logo;

	private static int playerId = -1;

	private static ChinesePokerRemote chinesePoker;

	public ChinesePokerGUI(String label) {
		super(label);
	}

	private void createAndShowGUI() {
		initFrame();

		PictureTransferHandler picHandler = new PictureTransferHandler();
		playerPanel = new PlayerPanel(20, 160, picHandler);
		this.add(playerPanel);
		opponentPanel = new PlayerPanel(580, 160, new TransferHandler(null));
		this.add(opponentPanel);
		newCardPanel = new DealCardsPanel(this, 20, 20, picHandler, playerId);
		this.add(newCardPanel);
	}
	
	public PlayerPanel getPlayerPanel() {
		return this.playerPanel;
	}
	
	public PlayerPanel getOpponentPanel() {
		return this.opponentPanel;
	}

	private void initFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(this);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(new Dimension(1070, 650));
		this.setLocation((dim.width - this.getSize().width) / 2, 20);
		this.getContentPane().setBackground(Color.BLACK);
		this.setResizable(false);
		this.setLayout(null);
		this.setVisible(true);
		
		this.logo = Utils.createImageIcon("/images/LOGO.png", "logo", this).getImage();
		List<Image> icons = new ArrayList<Image> (2);
		icons.add(Utils.createImageIcon("/images/LOGO2.gif", "logo", this).getImage());
		icons.add(Utils.createImageIcon("/images/LOGO3.gif", "logo", this).getImage());
		this.setIconImages(icons);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				playerId = registerPlayer();
				if (playerId == -1) {
					JOptionPane.showMessageDialog(null,
							"Maksymalna liczba graczy została osiągnięta.",
							"Open Face Chinese Poker",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				ChinesePokerGUI pokerGUI = new ChinesePokerGUI(
						"Open Face Chinese Poker");
				pokerGUI.createAndShowGUI();
			}
		});
	}

	private static int registerPlayer() {
		try {
			if (chinesePoker == null) {
				chinesePoker = Utils.rmiLookUp();
			}
			return chinesePoker.registerPlayer();
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean submitHands() {
		this.saveHand(this.playerPanel.getTopHandPanel());
		this.saveHand(this.playerPanel.getMiddleHandPanel());
		this.saveHand(this.playerPanel.getBottomHandPanel());

		try {
			if (chinesePoker == null) {
				chinesePoker = Utils.rmiLookUp();
			}
			chinesePoker.sendHands(playerId, 
								   this.playerPanel.getTopHandPanel().getPokerHand(), 
								   this.playerPanel.getMiddleHandPanel().getPokerHand(), 
								   (ShortPokerHand) this.playerPanel.getBottomHandPanel().getPokerHand());
			
			return this.isGameEnd();
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void saveHand(HandPanel handPanel) {
		handPanel.getPokerHand().getCards().clear();
		for (DTPicture dtPicture : handPanel.getDtPictures()) {
			if (dtPicture.getCard() != null) {
				dtPicture.setTransferHandler(new TransferHandler(null));
				handPanel.getPokerHand().getCards().add(dtPicture.getCard());
			}
		}
	}
	
	public boolean isGameEnd() {
		if (!this.playerPanel.getTopHandPanel().getPokerHand().isComplete()) {
			return false;
		}
		if (!this.playerPanel.getMiddleHandPanel().getPokerHand().isComplete()) {
			return false;
		}
		if (!this.playerPanel.getBottomHandPanel().getPokerHand().isComplete()) {
			return false;
		}
		
		for (DTPicture dtPicture : getOpponentPanel().getTopHandPanel().getDtPictures()) {
			if (dtPicture.getCard() == null) {
				return false;
			}
		}
		for (DTPicture dtPicture : getOpponentPanel().getMiddleHandPanel().getDtPictures()) {
			if (dtPicture.getCard() == null) {
				return false;
			}
		}
		for (DTPicture dtPicture : getOpponentPanel().getBottomHandPanel().getDtPictures()) {
			if (dtPicture.getCard() == null) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void windowClosing(WindowEvent event) {
		try {
			if (chinesePoker == null) {
				chinesePoker = Utils.rmiLookUp();
			}
			chinesePoker.unRegisterPlayer(playerId);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void paint(Graphics g) {  
		super.paint(g);
		((Graphics2D) g).drawImage(logo, 640, 20, this);
	} 
	
}
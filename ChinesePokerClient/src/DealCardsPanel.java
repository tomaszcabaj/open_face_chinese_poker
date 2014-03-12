import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import chinese.poker.common.interfaces.ChinesePokerRemote;
import chinese.poker.common.struct.Card;
import chinese.poker.common.struct.Player;
import chinese.poker.common.struct.PokerHand;
import chinese.poker.common.utils.Utils;

public class DealCardsPanel extends JPanel {

	private static final long serialVersionUID = 5107835898509781958L;

	private List<DTPicture> dtPictures;
	private int numberOfNewCards;

	private int playerId;
	private TransferHandler picHandler;

	private ExecutorService executor = Executors.newFixedThreadPool(2);
	private ChinesePokerRemote chinesePoker;

	private ChinesePokerGUI parent;

	public DealCardsPanel(ChinesePokerGUI parent, int x, int y,
			TransferHandler picHandler, int playerId) {
		this.playerId = playerId;
		this.parent = parent;
		this.picHandler = picHandler;

		setBounds(x, y, 550, 119);
		setBackground(Color.BLACK);

		this.numberOfNewCards = 5;
		setLayout(new GridLayout(1, numberOfNewCards + 1));

		this.dtPictures = new ArrayList<DTPicture>(numberOfNewCards);

		JButton dealCardsButton = new JButton(Utils.createImageIcon(
				"/images/DECK.png", "deck", this));
		dealCardsButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		dealCardsButton.setContentAreaFilled(false);
		dealCardsButton.setFocusPainted(false);
		add(dealCardsButton);

		for (int i = 0; i < numberOfNewCards; i++) {
			DTPicture pic;
			pic = new DTPicture(null);
			pic.setTransferHandler(picHandler);
			dtPictures.add(pic);
			DealCardsPanel.this.add(pic);
		}

		this.startNewGame();

		dealCardsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newCardsButtonAction();
			}
		});
	}

	private void newCardsButtonAction() {
		try {
			if (chinesePoker == null) {
				chinesePoker = Utils.rmiLookUp();
			}
			Boolean active = chinesePoker.isActivePlayer(playerId);
			if ((active == null) || (!active)) {
				return;
			}

			for (DTPicture dtPicture : dtPictures) {
				if (dtPicture.getImage() != null) {
					return;
				}
			}
			parent.getOpponentPanel().setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
			parent.getPlayerPanel().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

			if (this.parent.submitHands()) {
				updateWinningHandView(parent.getPlayerPanel().getTopHandPanel(), parent.getOpponentPanel().getTopHandPanel());
				updateWinningHandView(parent.getPlayerPanel().getMiddleHandPanel(), parent.getOpponentPanel().getMiddleHandPanel());
				updateWinningHandView(parent.getPlayerPanel().getBottomHandPanel(), parent.getOpponentPanel().getBottomHandPanel());
				
				validatePlayersHands();
				
				JOptionPane.showMessageDialog(null, "Koniec gry\n" + getScore(),
						"Rozdanie zakończone", JOptionPane.INFORMATION_MESSAGE);
				Border playerBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
				parent.getPlayerPanel().getTopHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
				parent.getPlayerPanel().getMiddleHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
				parent.getPlayerPanel().getBottomHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
				
				chinesePoker.setPlayerReady(playerId);
				this.startNewGame();
			} else {
				updateWinningHandView(parent.getPlayerPanel().getTopHandPanel(), parent.getOpponentPanel().getTopHandPanel());
				updateWinningHandView(parent.getPlayerPanel().getMiddleHandPanel(), parent.getOpponentPanel().getMiddleHandPanel());
				updateWinningHandView(parent.getPlayerPanel().getBottomHandPanel(), parent.getOpponentPanel().getBottomHandPanel());
				
				this.getNewCards(true);
				OpponentsPlay opponentsPlay = new OpponentsPlay();
				executor.execute(opponentsPlay);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private String getScore() {
		try {
			if (chinesePoker == null) {
				chinesePoker = Utils.rmiLookUp();
			}
			return chinesePoker.getScore(playerId);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void getNewCards(boolean generate) {
		for (int i = 0; i < numberOfNewCards; i++) {
			Card newCard = getNewCard(generate);
			dtPictures.get(i).setCard(newCard);
			if (newCard == null) {
				dtPictures.get(i).setVisible(false);
			} else {
				dtPictures.get(i).setVisible(true);
				if (generate) {
					dtPictures.get(i).setTransferHandler(
							new TransferHandler(null));
				} else {
					dtPictures.get(i).setTransferHandler(picHandler);
				}
			}
		}
		for (int i = numberOfNewCards; i < 5; i++) {
			dtPictures.get(i).setCard(null);
			dtPictures.get(i).setVisible(false);
		}
	}

	public void startNewGame() {
		this.numberOfNewCards = 5;

		this.clearPlayersPanels();

		GameStartHolder gameStartHolder = new GameStartHolder();
		executor.execute(gameStartHolder);
	}

	private void clearPlayersPanels() {
		parent.getPlayerPanel().getTopHandPanel().clearCardSlots();
		parent.getPlayerPanel().getMiddleHandPanel().clearCardSlots();
		parent.getPlayerPanel().getBottomHandPanel().clearCardSlots();

		parent.getOpponentPanel().getTopHandPanel().clearCardSlots();
		parent.getOpponentPanel().getMiddleHandPanel().clearCardSlots();
		parent.getOpponentPanel().getBottomHandPanel().clearCardSlots();
	}

	private Card getNewCard(boolean generate) {
		try {
			if (chinesePoker == null) {
				chinesePoker = Utils.rmiLookUp();
			}
			if (generate) {
				return chinesePoker.generateRandomCard();
			} else {
				return chinesePoker.getRandomCard();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void resetDeck() {
		try {
			if (chinesePoker == null) {
				chinesePoker = Utils.rmiLookUp();
			}
			chinesePoker.resetDeck();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	class GameStartHolder implements Runnable {
		@Override
		public void run() {
			try {
				if (chinesePoker == null) {
					chinesePoker = Utils.rmiLookUp();
				}
				while (chinesePoker.isActivePlayer(playerId) == null) {
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (chinesePoker.isActivePlayer(playerId)) {
					JOptionPane.showMessageDialog(null, "Rozpoczynasz grę.",
							"Nowe rozdanie", JOptionPane.INFORMATION_MESSAGE);
					
					parent.getOpponentPanel().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
					parent.getPlayerPanel().setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
					
					try {
						TimeUnit.MILLISECONDS.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					getNewCards(false);
				} else {
					parent.getOpponentPanel().setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
					parent.getPlayerPanel().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
					
					resetDeck();
					getNewCards(true);
					repaint();
					OpponentsPlay opponentsPlay = new OpponentsPlay();
					executor.execute(opponentsPlay);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	class OpponentsPlay implements Runnable {
		@Override
		public void run() {
			try {
				if (chinesePoker == null) {
					chinesePoker = Utils.rmiLookUp();
				}
				while ((!updateOpponentsHand())
						&& ((chinesePoker.isActivePlayer(playerId) == null) || (chinesePoker
								.isActivePlayer(playerId) == false))) {
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				parent.getOpponentPanel().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
				parent.getPlayerPanel().setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				
				updateWinningHandView(parent.getPlayerPanel().getTopHandPanel(), parent.getOpponentPanel().getTopHandPanel());
				updateWinningHandView(parent.getPlayerPanel().getMiddleHandPanel(), parent.getOpponentPanel().getMiddleHandPanel());
				updateWinningHandView(parent.getPlayerPanel().getBottomHandPanel(), parent.getOpponentPanel().getBottomHandPanel());
				
				if (updateOpponentsHand()) {
					validatePlayersHands();
					
					JOptionPane.showMessageDialog(null, "Koniec gry\n" + getScore(),
							"Rozdanie zakończone",
							JOptionPane.INFORMATION_MESSAGE);
					
					Border playerBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
					parent.getPlayerPanel().getTopHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
					parent.getPlayerPanel().getMiddleHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
					parent.getPlayerPanel().getBottomHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
					
					chinesePoker.setPlayerReady(playerId);
					startNewGame();
				} else {
					getNewCards(false);

					numberOfNewCards = 1;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean updateOpponentsHand() {
		try {
			if (chinesePoker == null) {
				chinesePoker = Utils.rmiLookUp();
			}
			Player opponent = chinesePoker.getOpponent(playerId);
			if (opponent == null) {
				parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
				return false;
			}
			int i = 0;
			parent.getOpponentPanel().getTopHandPanel().getPokerHand().clear();
			for (Card card : opponent.getTopHand().getCards()) {
				parent.getOpponentPanel().getTopHandPanel().getDtPictures()
						.get(i++).setCard(card);
				parent.getOpponentPanel().getTopHandPanel().getPokerHand().getCards().add(card);
			}
			
			i = 0;
			parent.getOpponentPanel().getMiddleHandPanel().getPokerHand().clear();
			for (Card card : opponent.getMiddleHand().getCards()) {
				parent.getOpponentPanel().getMiddleHandPanel().getDtPictures()
						.get(i++).setCard(card);
				parent.getOpponentPanel().getMiddleHandPanel().getPokerHand().getCards().add(card);
			}
			
			i = 0;
			parent.getOpponentPanel().getBottomHandPanel().getPokerHand().clear();
			for (Card card : opponent.getBottomHand().getCards()) {
				parent.getOpponentPanel().getBottomHandPanel().getDtPictures()
						.get(i++).setCard(card);
				parent.getOpponentPanel().getBottomHandPanel().getPokerHand().getCards().add(card);
			}

			return parent.isGameEnd();
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void updateWinningHandView(HandPanel playerHandPanel, HandPanel opponentHandPanel) {
		Border playerBorder;
		if (playerHandPanel.getPokerHand().compareTo(opponentHandPanel.getPokerHand()) > 0) {
			if (playerHandPanel.getPokerHand().isRoyalty()) {
				playerBorder = BorderFactory.createLineBorder(Color.YELLOW, 2);
			} else {
				playerBorder = BorderFactory.createLineBorder(new Color(0x32cd32), 2);
			}
		} else if (playerHandPanel.getPokerHand().compareTo(opponentHandPanel.getPokerHand()) < 0) {
			if (playerHandPanel.getPokerHand().isRoyalty()) {
				playerBorder = BorderFactory.createLineBorder(Color.ORANGE, 2);
			} else {
				playerBorder = BorderFactory.createLineBorder(Color.RED, 2);
			}
		} else {
			playerBorder = BorderFactory.createLineBorder(Color.GRAY, 2);
		}
		playerHandPanel.setBorder(BorderFactory.createCompoundBorder(playerBorder, 
	            BorderFactory.createEmptyBorder(7, 7, 0, 0)));
	}
	
	private void validatePlayersHands() {
		PokerHand playerTopHand = parent.getPlayerPanel().getTopHandPanel().getPokerHand();
		PokerHand playerMiddleHand = parent.getPlayerPanel().getMiddleHandPanel().getPokerHand();
		PokerHand playerBottomHand = parent.getPlayerPanel().getBottomHandPanel().getPokerHand();
		
		PokerHand opponentTopHand = parent.getOpponentPanel().getTopHandPanel().getPokerHand();
		PokerHand opponentMiddleHand = parent.getOpponentPanel().getMiddleHandPanel().getPokerHand();
		PokerHand opponentBottomHand = parent.getOpponentPanel().getBottomHandPanel().getPokerHand();
				
		if ((playerTopHand.compareTo(playerMiddleHand) > 0) && (playerMiddleHand.compareTo(playerBottomHand) > 0)) {
			if (!(opponentTopHand.compareTo(opponentMiddleHand) > 0) || !(opponentMiddleHand.compareTo(opponentBottomHand) > 0)) {
				chooseHandBorderColor(parent.getPlayerPanel().getTopHandPanel());
				chooseHandBorderColor(parent.getPlayerPanel().getMiddleHandPanel());
				chooseHandBorderColor(parent.getPlayerPanel().getBottomHandPanel());
			}
		} else {
			Border playerBorder = BorderFactory.createLineBorder(Color.RED, 2);
			parent.getPlayerPanel().getTopHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
			parent.getPlayerPanel().getMiddleHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
			parent.getPlayerPanel().getBottomHandPanel().setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
		}
	}
		
	private void chooseHandBorderColor(HandPanel handPanel) {
		if (handPanel.getPokerHand().isRoyalty()) {
			paintHandBorder(handPanel, Color.YELLOW);
		} else {
			paintHandBorder(handPanel, new Color(0x32cd32));
		}
	}
	
	private void paintHandBorder(HandPanel handPanel, Color color) {
		Border playerBorder = BorderFactory.createLineBorder(color, 2);
		handPanel.setBorder(BorderFactory.createCompoundBorder(playerBorder, BorderFactory.createEmptyBorder(7, 7, 0, 0)));
	}
}

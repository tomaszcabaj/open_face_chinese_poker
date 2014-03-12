import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chinese.poker.common.interfaces.ChinesePokerRemote;
import chinese.poker.common.struct.Card;
import chinese.poker.common.struct.Figure;
import chinese.poker.common.struct.Player;
import chinese.poker.common.struct.PokerHand;
import chinese.poker.common.struct.PokerHandStrengthAnalyzer;
import chinese.poker.common.struct.ShortPokerHand;
import chinese.poker.common.struct.Suit;


public class ChinesePoker implements ChinesePokerRemote {

	private Player[] players = new Player[2];
	private List<Card> cardsInDeck;
	private int currentPlayerId;
	
	private List<Card> dealtCards = new ArrayList<Card>(5);
	
	public ChinesePoker() {
		Random random = new Random();
		currentPlayerId = random.nextInt(2);
	}
	
	@Override
	synchronized public int registerPlayer() throws RemoteException {
		int id;
		if (players[0] == null) {
			id = 0;
			players[id] = new Player(id);
		} else if (players[1] == null) {
			id = 1;
			players[id] = new Player(id);
			dealtCards = new ArrayList<Card>(5);
		} else {
			id = -1;
		}
		return id;
	}
	
	@Override
	synchronized public void unRegisterPlayer(int id) throws RemoteException {
		players[id] = null;
	}
	
	@Override
	public Boolean isActivePlayer(int playerId) throws RemoteException {
		if ((players[0] == null) || (players[1] == null) || !players[0].isReady() || !players[1].isReady()) {
			return null;
		} else {
			return this.currentPlayerId == playerId;
		}
	}
	
	@Override
	synchronized public void resetDeck() {
		cardsInDeck = new ArrayList<Card>();
		for (Suit suit : Suit.values()) {
			for (Figure figure : Figure.values()) {
				cardsInDeck.add(new Card(figure, suit));
			}
		}
		players[0].setCurrentScore(0);
		players[1].setCurrentScore(0);
	}
	
	@Override
	synchronized public Card generateRandomCard() {
		if ((cardsInDeck == null) || (cardsInDeck.isEmpty())) {
			return null;
		} else {
			Random random = new Random();
			int selectedCard = random.nextInt(cardsInDeck.size());
			dealtCards.add(cardsInDeck.get(selectedCard));
			return cardsInDeck.remove(selectedCard);
		}
	}
	
	@Override
	synchronized public Card getRandomCard() {
		if (!dealtCards.isEmpty()) {
			return dealtCards.remove(0);
		} else {
			return null;
		}
	}
	
	
	
	@Override
	synchronized public void sendHands(int playerId, PokerHand topHand, PokerHand middleHand, ShortPokerHand bottomHand) throws RemoteException {
		players[playerId].setTopHand(topHand);
		players[playerId].setMiddleHand(middleHand);
		players[playerId].setBottomHand(bottomHand);
		
		if (isPlayerHandComplete(0) && isPlayerHandComplete(1)) {
			this.players[0].setReady(false);
			this.players[1].setReady(false);
		} else {
			this.currentPlayerId = this.currentPlayerId == 0 ? 1 : 0;
		}
	}
	
	private boolean isPlayerHandComplete(int playerId) {
		return players[playerId].getTopHand().isComplete() && players[playerId].getMiddleHand().isComplete() && players[playerId].getBottomHand().isComplete();
	}
	
	@Override
	public void setPlayerReady(int playerId) {
		this.players[playerId].setReady(true);
		
		if (this.players[0].isReady() && this.players[1].isReady()) {
			clearPlayerHands(0);
			clearPlayerHands(1);
		}
	}
	
	private void clearPlayerHands(int playerId) {
		players[playerId].setTopHand(new PokerHand());
		players[playerId].setMiddleHand(new PokerHand());
		players[playerId].setBottomHand(new ShortPokerHand());
	}
	
	@Override
	public Player getOpponent(int playerId) throws RemoteException {
		if (playerId == 0) {
			return players[1];
		} else {
			return players[0];
		}
	}
	
	public String getScore(int playerId) throws RemoteException {

		if ((players[0].getCurrentScore() == 0) && (players[1].getCurrentScore() == 0)) {
			PokerHand topHand1 = players[0].getTopHand();
			PokerHand middleHand1 = players[0].getMiddleHand();
			ShortPokerHand bottomHand1 = players[0].getBottomHand();
			
			PokerHand topHand2 = players[1].getTopHand();
			PokerHand middleHand2 = players[1].getMiddleHand();
			ShortPokerHand bottomHand2 = players[1].getBottomHand();
			
			Integer player1Score = 0;
			Integer player2Score = 0;
			
			if ((topHand1.compareTo(middleHand1) > 0) && (middleHand1.compareTo(bottomHand1) > 0)) {
				player1Score += getTopHandRoyaltiesScore(topHand1);
				player1Score += getMiddleHandRoyaltiesScore(middleHand1);
				player1Score += getBottomHandRoyaltiesScore(bottomHand1);
			} else {
				topHand1 = null;
				middleHand1 = null;
				bottomHand1 = null;
			}
			
			if ((topHand2.compareTo(middleHand2) > 0) && (middleHand2.compareTo(bottomHand2) > 0)) {
				player2Score += getTopHandRoyaltiesScore(topHand2);
				player2Score += getMiddleHandRoyaltiesScore(middleHand2);
				player2Score += getBottomHandRoyaltiesScore(bottomHand2);
			} else {
				topHand2 = null;
				middleHand2 = null;
				bottomHand2 = null;
			}
			
			Integer handsWon = 0;
			if (topHand1 != null) {
				if ((topHand2 == null) || (topHand1.compareTo(topHand2) > 0)) {
					player1Score++;
					handsWon++;
				} else {
					player2Score++;
					handsWon--;
				}
			} else if (topHand2 != null) {
				player2Score++;
				handsWon--;
			}
			
			if (middleHand1 != null) {
				if ((middleHand2 == null) || (middleHand1.compareTo(middleHand2) > 0)) {
					player1Score++;
					handsWon++;
				} else {
					player2Score++;
					handsWon--;
				}
			} else if (middleHand2 != null) {
				player2Score++;
				handsWon--;
			}
			
			if (bottomHand1 != null) {
				if ((bottomHand2 == null) || (bottomHand1.compareTo(bottomHand2) > 0)) {
					player1Score++;
					handsWon++;
				} else {
					player2Score++;
					handsWon--;
				}
			} else if (bottomHand2 != null) {
				player2Score++;
				handsWon--;
			}
			
			if (handsWon == 3) {
				player1Score += 3;
			} else if (handsWon == -3) {
				player2Score += 3;
			} 
			
			players[0].setCurrentScore(player1Score);
			players[1].setCurrentScore(player2Score);
		}
		
		String scoreMessage = "\n";
		if (playerId == 0) {
			scoreMessage += "Wynik rozdania:    " + players[0].getCurrentScore() + " : " + players[1].getCurrentScore();
			scoreMessage += "\n";
			scoreMessage += "Wynik całkowity:  " + players[0].getScore() + " : " + players[1].getScore();
			scoreMessage += "\n";
		} else {
			scoreMessage += "Wynik rozdania:    " + players[1].getCurrentScore() + " : " + players[0].getCurrentScore();
			scoreMessage += "\n";
			scoreMessage += "Wynik całkowity:  " + players[1].getScore() + " : " + players[0].getScore();
			scoreMessage += "\n";
		}
		
		return scoreMessage;
	}
	
	private int getTopHandRoyaltiesScore(PokerHand pokerHand) {
		if (pokerHand.isRoyalty()) {
			switch (pokerHand.getCategory()) {
				case STRAIGHT_FLUSH : return 10;
				case FOUR_OF_A_KIND : return 8;
				case FULL_HOUSE : return 6;
				case FLUSH : return 4;
				case STRAIGHT : return 2;
			}
		}
		return 0;
	}
	
	private int getMiddleHandRoyaltiesScore(PokerHand pokerHand) {
		if (pokerHand.isRoyalty()) {
			switch (pokerHand.getCategory()) {
				case STRAIGHT_FLUSH : return 20;
				case FOUR_OF_A_KIND : return 16;
				case FULL_HOUSE : return 12;
				case FLUSH : return 8;
				case STRAIGHT : return 4;
			}
		}
		return 0;
	}
	
	private int getBottomHandRoyaltiesScore(ShortPokerHand pokerHand) {
		if (pokerHand.isRoyalty()) {
			PokerHandStrengthAnalyzer analyzer = new PokerHandStrengthAnalyzer();
			switch (pokerHand.getCategory()) {
				case THREE_OF_A_KIND : return analyzer.getPairOrTripsFigure(pokerHand).ordinal() + 10;
				case PAIR : return (analyzer.getPairOrTripsFigure(pokerHand).ordinal() - 3 < 0) ? 0 : analyzer.getPairOrTripsFigure(pokerHand).ordinal() - 3;
			}
		}
		return 0;
	}

}

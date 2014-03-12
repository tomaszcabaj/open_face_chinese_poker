package chinese.poker.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import chinese.poker.common.struct.Card;
import chinese.poker.common.struct.Player;
import chinese.poker.common.struct.PokerHand;
import chinese.poker.common.struct.ShortPokerHand;

public interface ChinesePokerRemote extends Remote {
	
	public int registerPlayer() throws RemoteException;
	public void unRegisterPlayer(int id) throws RemoteException;
	
	public Boolean isActivePlayer(int playerId) throws RemoteException;
	
	public void resetDeck() throws RemoteException;
	public Card generateRandomCard() throws RemoteException;
	public Card getRandomCard() throws RemoteException;
	
	public void sendHands(int playerId, PokerHand topHand, PokerHand middleHand, ShortPokerHand bottomHand) throws RemoteException;
	public Player getOpponent(int playerId) throws RemoteException;
	
	public void setPlayerReady(int playerId) throws RemoteException;
	
	public String getScore(int playerId) throws RemoteException;
}
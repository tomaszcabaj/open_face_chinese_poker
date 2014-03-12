package chinese.poker.common.struct;

import java.io.Serializable;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 4582158805960514915L;

	private int id;
	
	private PokerHand topHand;
	private PokerHand middleHand;
	private ShortPokerHand bottomHand;
	
	private boolean ready;
	
	private int currentScore;
	private int totalScore;
	
	
	public Player(int id) {
		this.id = id;
		
		this.topHand = new PokerHand();
		this.middleHand = new PokerHand();
		this.bottomHand = new ShortPokerHand();
		
		this.ready = true;
		
		this.totalScore = 0;
	}
	
	
	public PokerHand getTopHand() {
		return topHand;
	}
	
	public void setTopHand(PokerHand topHand) {
		this.topHand = topHand;
	}
	
	public PokerHand getMiddleHand() {
		return middleHand;
	}
	
	public void setMiddleHand(PokerHand middleHand) {
		this.middleHand = middleHand;
	}
	
	public ShortPokerHand getBottomHand() {
		return bottomHand;
	}
	
	public void setBottomHand(ShortPokerHand bottomHand) {
		this.bottomHand = bottomHand;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void clearScore() {
		this.totalScore = 0;
	}
	
	private void addToScore(int currentScore) {
		this.totalScore += currentScore;
	}
	
	public int getScore() {
		return this.totalScore;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
		this.addToScore(currentScore);
	}
}

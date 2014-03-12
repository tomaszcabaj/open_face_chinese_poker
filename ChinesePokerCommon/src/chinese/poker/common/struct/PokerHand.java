package chinese.poker.common.struct;

public class PokerHand extends Hand implements Comparable<PokerHand> {
	
	private static final long serialVersionUID = 5502124633840634825L;

	
	private static final int NUMBER_OF_CARDS = 5;
	
	protected PokerHandCategory category = null;
	
	public PokerHand() {
	}
	
	@Override
	public String toString() {
		String result = "";
		for (Card card : this.getCards()) {
			result += card.getFigure() + " " + card.getSuit() + ", ";
		}
		return result;
	}
	
	@Override
	public int compareTo(PokerHand hand) {
		PokerHandStrengthAnalyzer analyzer = new PokerHandStrengthAnalyzer();
		this.setCategory(analyzer.getPokerHandCategory(this));
		hand.setCategory(analyzer.getPokerHandCategory(hand));
		
		PokerHandComparator pokerHandComparator = new PokerHandComparator();
		return pokerHandComparator.compare(this, hand);
	}
	
	@Override
	public int getNumberOfCards() {
		return NUMBER_OF_CARDS;
	}
	
	public PokerHandCategory getCategory() {
		return category;
	}

	private void setCategory(PokerHandCategory category) {
		this.category = category;
	}
	
	public boolean isComplete() {
		if (this.cards.size() == NUMBER_OF_CARDS) {
			return true;
		} else {
			return false;
		}
	}
	
	public void clear() {
		this.category = null;
		this.cards.clear();
	}
	
	public boolean isRoyalty() {
		switch (category) {
			case STRAIGHT_FLUSH : return true;
			case FOUR_OF_A_KIND : return true;
			case FULL_HOUSE : return true;
			case FLUSH : return true;
			case STRAIGHT : return true;
			default : return false;
		}
	}
}

package chinese.poker.common.struct;

import java.util.Collections;

public class ShortPokerHand extends PokerHand {
	
	private static final long serialVersionUID = -6369911638189122967L;
	
	private static final int NUMBER_OF_CARDS = 3;
	
	public ShortPokerHand() {
	}
	
	@Override
	public int getNumberOfCards() {
		return NUMBER_OF_CARDS;
	}
	
	@Override
	public boolean isComplete() {
		if (this.getCards().size() == NUMBER_OF_CARDS) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isRoyalty() {
		switch (category) {
			case THREE_OF_A_KIND : return true;
			case PAIR : {
				PokerHandStrengthAnalyzer analyzer = new PokerHandStrengthAnalyzer();
				if (analyzer.getPairOrTripsFigure(this).compareTo(Figure.FIVE) > 0) {
					return true;
				} else {
					return false;
				}
			}
			default : return false;
		}
	}
	

}

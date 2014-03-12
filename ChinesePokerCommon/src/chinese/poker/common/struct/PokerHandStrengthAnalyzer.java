package chinese.poker.common.struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokerHandStrengthAnalyzer {
	
	public PokerHandCategory getPokerHandCategory(PokerHand pokerHand) {
		if (pokerHand.getNumberOfCards() == 5) {
			if (isStraightFlush(pokerHand)) {
				return PokerHandCategory.STRAIGHT_FLUSH;
			} else if (isFourOfaKind(pokerHand)) {
				return PokerHandCategory.FOUR_OF_A_KIND;
			} else if (isFullHouse(pokerHand)) {
				return PokerHandCategory.FULL_HOUSE;
			} else if (isFlush(pokerHand)) {
				return PokerHandCategory.FLUSH;
			} else if (isStraight(pokerHand)) {
				return PokerHandCategory.STRAIGHT;
			} else if (isTwoPair(pokerHand)) {
				return PokerHandCategory.TWO_PAIR;
			} 
		}
		if (isThreeOfaKind(pokerHand)) {
			return PokerHandCategory.THREE_OF_A_KIND;
		} else if (isPair(pokerHand)) {
			return PokerHandCategory.PAIR;
		} else {
			return PokerHandCategory.HIGH_CARD;
		}
	}
	
	private boolean isStraightFlush(PokerHand pokerHand) {
		if (pokerHand.getCards().size() < 5) {
			return false;
		}
		return isFlush(pokerHand) && isStraight(pokerHand);
	}

	private boolean isFourOfaKind(PokerHand pokerHand) {
		if (pokerHand.getCards().size() < 4) {
			return false;
		}
		List<Figure> figures = getFiguresList(pokerHand);
		for (Figure figure : figures) {
			if (Collections.frequency(figures, figure) == 4) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isFullHouse(PokerHand pokerHand) {
		if (pokerHand.getCards().size() < 5) {
			return false;
		}
		return isThreeOfaKind(pokerHand) && isPair(pokerHand);
	}
	
	private boolean isFlush(PokerHand pokerHand) {
		if (pokerHand.getCards().size() < 5) {
			return false;
		}
		Suit suit = pokerHand.getCards().get(0).getSuit();
		for (Card card : pokerHand.getCards()) {
			if (!suit.equals(card.getSuit())) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isStraight(PokerHand pokerHand) {
		if (pokerHand.getCards().size() < 5) {
			return false;
		} else {
			List<Figure> figures = getFiguresList(pokerHand);
			Collections.sort(figures);
			
			Integer lastOrdinal = null;
			for (int i = 0; i < figures.size(); i++) {
				if ((lastOrdinal != null) && (lastOrdinal + 1 != figures.get(i).ordinal())) {
					if ((i != figures.size() - 1) || (figures.get(i) != Figure.ACE) || (lastOrdinal != 3)) {
						return false;
					}
				} else {
					lastOrdinal = figures.get(i).ordinal();
				}
			}
			
			return true;
		}
	}
	
	private boolean isThreeOfaKind(PokerHand pokerHand) {
		if (pokerHand.getCards().size() < 3) {
			return false;
		}
		List<Figure> figures = getFiguresList(pokerHand);
		for (Figure figure : figures){
			if (Collections.frequency(figures, figure) == 3) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isTwoPair(PokerHand pokerHand) {
		if (pokerHand.getCards().size() < 4) {
			return false;
		}
		List<Figure> figures = getFiguresList(pokerHand);
		int pairCounter = 0;
		for (Figure figure : figures) {
			if (Collections.frequency(figures, figure) == 2) {
				if (++pairCounter == 4) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isPair(PokerHand pokerHand) {
		if (pokerHand.getCards().size() < 2) {
			return false;
		}
		List<Figure> figures = getFiguresList(pokerHand);
		for (Figure figure : figures) {
			if (Collections.frequency(figures, figure) == 2) {
				return true;
			}
		}
		return false;
	}
	
	public Figure getPairOrTripsFigure(ShortPokerHand pokerHand) {
		if ((pokerHand.getCategory() == PokerHandCategory.PAIR) || (pokerHand.getCategory() == PokerHandCategory.THREE_OF_A_KIND)) {
			List<Figure> figures = getFiguresList(pokerHand);
			for (Figure figure : figures) {
				if (Collections.frequency(figures, figure) >= 2) {
					return figure;
				}
			}
		}
		
		return null;
	}
	
	private List<Figure> getFiguresList(PokerHand pokerHand) {
		List<Figure> figures = new ArrayList<Figure>();
		for (Card card : pokerHand.getCards()) {
			figures.add(card.getFigure());
		}
		return figures;
	}
}

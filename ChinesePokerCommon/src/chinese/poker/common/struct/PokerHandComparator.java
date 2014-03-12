package chinese.poker.common.struct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PokerHandComparator implements Comparator<PokerHand> {

	@Override
	public int compare(PokerHand pokerHand1, PokerHand pokerHand2) {
		if (pokerHand1 == null) {
			return -1;
		} else if (pokerHand2 == null) {
			return 1;
		} else if (!pokerHand1.getCategory().equals(pokerHand2.getCategory())) {
			return pokerHand1.getCategory().compareTo(pokerHand2.getCategory());
		} else {
			switch (pokerHand1.getCategory()) {
				case STRAIGHT_FLUSH: 
					return compareHighCards(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
				case FOUR_OF_A_KIND: 
					return compareFourOfaKinds(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
				case FULL_HOUSE: 
					return compareFullHouses(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
				case FLUSH: 
					return compareHighCards(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
				case STRAIGHT: 
					return compareHighCards(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
				case THREE_OF_A_KIND: 
					return compareThreeOfaKinds(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
				case TWO_PAIR: 
					return compareTwoPairs(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
				case PAIR: 
					return comparePairs(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
				case HIGH_CARD: 
					return compareHighCards(getFiguresList(pokerHand1), getFiguresList(pokerHand2));
			}
		}
		
		return 0;
	}
	
	
	private int compareFourOfaKinds(List<Figure> hand1Figures, List<Figure> hand2Figures) {
		Figure quadFigure1 = null;
		for (Figure figure : hand1Figures) {
			if (Collections.frequency(hand1Figures, figure) == 4) {
				quadFigure1 = figure;
			}
		}
		Figure quadFigure2 = null;
		for (Figure figure : hand2Figures) {
			if (Collections.frequency(hand2Figures, figure) == 4) {
				quadFigure2 = figure;
			}
		}
		
		return quadFigure1.compareTo(quadFigure2);
	}
	
	private int compareFullHouses(List<Figure> hand1Figures, List<Figure> hand2Figures) {
		return compareThreeOfaKinds(hand1Figures, hand2Figures);
	}
	
	private int compareThreeOfaKinds(List<Figure> hand1Figures, List<Figure> hand2Figures) {
		Figure tripsFigure1 = null;
		for (Figure figure : hand1Figures) {
			if (Collections.frequency(hand1Figures, figure) == 3) {
				tripsFigure1 = figure;
			}
		}
		Figure tripsFigure2 = null;
		for (Figure figure : hand2Figures) {
			if (Collections.frequency(hand2Figures, figure) == 3) {
				tripsFigure2 = figure;
			}
		}
		
		return tripsFigure1.compareTo(tripsFigure2);
	}
	
	private int compareTwoPairs(List<Figure> hand1Figures, List<Figure> hand2Figures) {
		Figure pairFigure1a = null, pairFigure1b = null;
		for (Figure figure : hand1Figures) {
			if (Collections.frequency(hand1Figures, figure) == 2) {
				if (figure != null) {
					if (pairFigure1a == null) {
						pairFigure1a = figure;
					} else if (!figure.equals(pairFigure1a)) {
						pairFigure1b  = figure;
						break;
					}
				}
			}
		}
		Figure pairFigure2a = null, pairFigure2b = null;
		for (Figure figure : hand2Figures) {
			if (Collections.frequency(hand2Figures, figure) == 2) {
				if (figure != null) {
					if (pairFigure2a == null) {
						pairFigure2a = figure;
					} else if (!figure.equals(pairFigure2a)) {
						pairFigure2b  = figure;
						break;
					}
				}
			}
		}
		
		int result = (pairFigure1a.compareTo(pairFigure1b) > 0 ? pairFigure1a : pairFigure1b).compareTo(pairFigure2a.compareTo(pairFigure2b) > 0 ? pairFigure2a : pairFigure2b);
		if (result != 0) {
			return result;
		} else {
			return (pairFigure1a.compareTo(pairFigure1b) < 0 ? pairFigure1a : pairFigure1b).compareTo(pairFigure2a.compareTo(pairFigure2b) < 0 ? pairFigure2a : pairFigure2b);
		}
	}
	
	private int comparePairs(List<Figure> hand1Figures, List<Figure> hand2Figures) {
		Figure pairFigure1 = null;
		for (Figure figure : hand1Figures) {
			if ((figure != null) && (Collections.frequency(hand1Figures, figure) == 2)) {
				pairFigure1 = figure;
				break;
			}
		}
		Figure pairFigure2 = null;
		for (Figure figure : hand2Figures) {
			if ((figure != null) && (Collections.frequency(hand2Figures, figure) == 2)) {
				pairFigure2 = figure;
				break;
			}
		}
		
		if (pairFigure1.equals(pairFigure2)) {
			return compareHighCards(hand1Figures, hand2Figures);
		} else {
			return pairFigure1.compareTo(pairFigure2);
		}
	}
	
	private int compareHighCards(List<Figure> hand1Figures, List<Figure> hand2Figures) {
		Collections.sort(hand1Figures, new FigureComparator());
		Collections.sort(hand2Figures, new FigureComparator());
		
		for (int i = hand1Figures.size() - 1; i > -1; i--) {
			if (hand1Figures.get(i) != hand2Figures.get(i)) {
				if (hand1Figures.get(i) == null) {
					return -1;
				} if (hand2Figures.get(i) == null) {
					return 1;
				} else {
					return hand1Figures.get(i).compareTo(hand2Figures.get(i));
				}
			}
		}
		return 0;
	}
	
	private List<Figure> getFiguresList(PokerHand pokerHand) {
		List<Figure> figures = new ArrayList<Figure>();
		for (Card card : pokerHand.getCards()) {
			if (card != null) {
				figures.add(card.getFigure());
			} else {
				figures.add(null);
			}
		}
		for (int i = pokerHand.getCards().size(); i < 5; i++) {
			figures.add(null);
		}
		return figures;
	}
	
}

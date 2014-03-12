package chinese.poker.common.struct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

abstract public class Hand implements Serializable {
	
	private static final long serialVersionUID = 5869357667889489969L;
	
	protected List<Card> cards;
	
	public Hand() {
		cards = new ArrayList<Card>();
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	abstract public int getNumberOfCards();
}

package chinese.poker.common.struct;

import java.io.Serializable;

public class Card implements Serializable {

	private static final long serialVersionUID = 5691077272841635573L;
	
	private Figure figure;
	private Suit suit;
	
	private String imagePath;
	
	public Card(Figure figure, Suit suit) {
		this.figure = figure;
		this.suit = suit;
		this.imagePath = "/images/" + figure + "_OF_" + suit + ".png";
	}

	public Figure getFigure() {
		return figure;
	}

	public Suit getSuit() {
		return suit;
	}

	public String getImagePath() {
		return imagePath;
	}

}

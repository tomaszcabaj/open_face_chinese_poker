package chinese.poker.common.struct;

public enum PokerHandCategory {
	
	HIGH_CARD("Wysoka karta"), PAIR("Para"), TWO_PAIR("Dwie pary"), THREE_OF_A_KIND("Trójka"), 
	STRAIGHT("Strit"), FLUSH("Kolor"), FULL_HOUSE("Full"), FOUR_OF_A_KIND("Kareta"), STRAIGHT_FLUSH("Poker");
	
	private String name;
	
	private PokerHandCategory(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}

package chinese.poker.common.struct;

public enum Suit {
	SPADES("Pik"), HEARTS("Kier"), CLUBS("Trefl"), DIAMONDS("Karo");
	
	private String name;
	
	private Suit(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}

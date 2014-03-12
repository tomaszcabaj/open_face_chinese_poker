package chinese.poker.common.struct;

import java.util.Comparator;

public enum Figure {
	TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"),
	TEN("10"), JACK("Walet"), QUEEN("Dama"), KING("Król"), ACE("As");
	
	private String name;
	
	private Figure(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
}

class FigureComparator implements Comparator<Figure> {
	@Override
    public int compare(Figure figure1, Figure figure2) {
		if ((figure1 == null) && (figure2 == null)) {
			return 0;
		} else if (figure1 == null) {
            return -1;
        } else if (figure2 == null) {
            return 1;
        } else {
        	return figure1.compareTo(figure2);
        }
    }
}

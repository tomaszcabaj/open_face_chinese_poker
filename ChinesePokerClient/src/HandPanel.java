import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;

import chinese.poker.common.struct.PokerHand;


public class HandPanel extends JPanel {

	private static final long serialVersionUID = 5107835898509781958L;

	private PokerHand pokerHand;
	private List<DTPicture> dtPictures;
	
	private TransferHandler transferHandler;
	
	public HandPanel(PokerHand pokerHand, int x, int y, TransferHandler picHandler) {
		super(new GridLayout(1, pokerHand.getNumberOfCards()));
		setOpaque(true);
		this.transferHandler = picHandler;
		this.setPokerHand(pokerHand);
		
		
		setBounds(x, y, 87 * pokerHand.getNumberOfCards() + 12, 133);
		setBackground(Color.BLACK);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		setBorder(BorderFactory.createCompoundBorder(border, 
		            BorderFactory.createEmptyBorder(7, 7, 0, 0)));
		
		this.dtPictures = new ArrayList<DTPicture>(pokerHand.getNumberOfCards());
		for (int i = 0; i < pokerHand.getNumberOfCards(); i++) {
			DTPicture pic;
			pic = new DTPicture(null);
			pic.setTransferHandler(this.transferHandler);
			this.dtPictures.add(pic);
			add(pic);
		}
	}
	
	public void clearCardSlots() {
		for (DTPicture dtPicture : dtPictures) {
			dtPicture.setCard(null);
			dtPicture.setTransferHandler(this.transferHandler);
		}
		
		this.pokerHand.clear();
	}
	
	public PokerHand getPokerHand() {
		return pokerHand;
	}

	public void setPokerHand(PokerHand pokerHand) {
		this.pokerHand = pokerHand;
	}

	public List<DTPicture> getDtPictures() {
		return dtPictures;
	}
	
}

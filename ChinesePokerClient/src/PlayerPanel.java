import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import chinese.poker.common.struct.PokerHand;
import chinese.poker.common.struct.ShortPokerHand;


public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 6530162287721447393L;

	private HandPanel topHandPanel;
	private HandPanel middleHandPanel;
	private HandPanel bottomHandPanel;
	
	public PlayerPanel(int x, int y, TransferHandler picHandler) {
		super(null);
		setOpaque(true);
		setBounds(x, y, 462, 440);
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
		
		this.topHandPanel = new HandPanel(new PokerHand(), 8, 15, picHandler);
		this.middleHandPanel = new HandPanel(new PokerHand(), 8, 155, picHandler);
		this.bottomHandPanel = new HandPanel(new ShortPokerHand(), 8, 295, picHandler);
		
		add(this.topHandPanel);
		add(this.middleHandPanel);
		add(this.bottomHandPanel);
	}
	
	public HandPanel getTopHandPanel() {
		return this.topHandPanel;
	}
	
	public HandPanel getMiddleHandPanel() {
		return this.middleHandPanel;
	}
	
	public HandPanel getBottomHandPanel() {
		return this.bottomHandPanel;
	}
	
}
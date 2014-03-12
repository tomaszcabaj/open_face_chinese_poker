import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import chinese.poker.common.struct.Card;
import chinese.poker.common.utils.Utils;

public class DTPicture extends Picture implements MouseMotionListener {

	private static final long serialVersionUID = -6851030258757038296L;

	private MouseEvent firstMouseEvent = null;

	private boolean installInputMapBindings = true;
	
	private Card card;

	
	public DTPicture(Image image) {
		super(image);
		addMouseMotionListener(this);
	}

	public Card getCard() {
		return card;
	}
	
	public void setCard(Card card) {
		this.card = card;
		if (card == null) {
			this.setImage(null);
		} else {
			this.setImage(Utils.createImageIcon(card.getImagePath(), card.getImagePath(), this).getImage());
		}
	}
	
	public void setImage(Image image) {
		this.image = image;
		this.repaint();
	}

	public Image getImage() {
		return this.image;
	}

	public void mousePressed(MouseEvent e) {
		if (image == null)
			return;

		firstMouseEvent = e;
		e.consume();
	}

	public void mouseDragged(MouseEvent e) {
		if (image == null)
			return;

		if (firstMouseEvent != null) {
			e.consume();

			int action = TransferHandler.MOVE;

			int dx = Math.abs(e.getX() - firstMouseEvent.getX());
			int dy = Math.abs(e.getY() - firstMouseEvent.getY());
			if (dx > 5 || dy > 5) {
				JComponent c = (JComponent) e.getSource();
				TransferHandler handler = c.getTransferHandler();

				handler.exportAsDrag(c, firstMouseEvent, action);
				firstMouseEvent = null;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		firstMouseEvent = null;
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void setInstallInputMapBindings(boolean flag) {
		installInputMapBindings = flag;
	}

	public boolean getInstallInputMapBindingds() {
		return installInputMapBindings;
	}
}
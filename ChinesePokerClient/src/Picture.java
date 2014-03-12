import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.accessibility.Accessible;
import javax.swing.JComponent;

public class Picture extends JComponent implements MouseListener,
		FocusListener, Accessible {

	private static final long serialVersionUID = 7709242341426797868L;
	Image image;

	public Picture(Image image) {

		this.image = image;
		setFocusable(true);
		addMouseListener(this);
		addFocusListener(this);
	}

	public void mouseClicked(MouseEvent e) {
		requestFocusInWindow();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void focusGained(FocusEvent e) {
		this.repaint();
	}

	public void focusLost(FocusEvent e) {
		this.repaint();
	}

	protected void paintComponent(Graphics graphics) {
		Graphics g = graphics.create();

		g.fillRect(0, 0, image == null ? 82 : image.getWidth(this),
				image == null ? 116 : image.getHeight(this));

		if (image != null) {
			g.drawImage(image, 0, 0, this);
		}

		g.dispose();
	}
}

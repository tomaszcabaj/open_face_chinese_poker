import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import chinese.poker.common.struct.Card;

public class PictureTransferHandler extends TransferHandler {

	private static final long serialVersionUID = -8126967845224894683L;
	
	DataFlavor pictureFlavor = DataFlavor.imageFlavor;

	DTPicture sourcePic;

	boolean shouldRemove;

	public boolean importData(JComponent c, Transferable t) {
		Image image;
		if (canImport(c, t.getTransferDataFlavors())) {
			DTPicture pic = (DTPicture) c;
			if (sourcePic == pic) {
				shouldRemove = false;
				return true;
			}
			try {
				image = (Image) t.getTransferData(pictureFlavor);
				Image im = pic.getImage();
				Card picCard = pic.getCard();
				Card sourcePicCard = sourcePic.getCard();
				pic.setImage(image);
				pic.setCard(sourcePicCard);
				sourcePic.setImage(im);
				sourcePic.setCard(picCard);
				return false;
			} catch (UnsupportedFlavorException ufe) {
				System.out.println("importData: unsupported data flavor");
			} catch (IOException ioe) {
				System.out.println("importData: I/O exception");
			}
		}
		return false;
	}

	protected Transferable createTransferable(JComponent c) {
		sourcePic = (DTPicture) c;
		shouldRemove = true;
		return new PictureTransferable(sourcePic);
	}

	public int getSourceActions(JComponent c) {
		return MOVE;
	}

	protected void exportDone(JComponent c, Transferable data, int action) {
		if (shouldRemove && (action == MOVE)) {
			sourcePic.setCard(null);
		}
		sourcePic = null;
	}

	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (pictureFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

	class PictureTransferable implements Transferable {
		private Image image;

		PictureTransferable(DTPicture pic) {
			image = pic.image;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return image;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { pictureFlavor };
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return pictureFlavor.equals(flavor);
		}
	}
}

package lab.cadl.lirui.draw2d.demo;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class FigureMover implements MouseListener, MouseMotionListener {
	private IFigure figure;
	private Point location;
	
	public FigureMover(IFigure figure) {
		this.figure = figure;
		figure.addMouseListener(this);
		figure.addMouseMotionListener(this);
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (location == null) {
			return;
		}
		
		Point newLocation = event.getLocation();
		if (newLocation == null) {
			return;
		}
		
		Dimension offset = newLocation.getDifference(location);
		if (offset.width == 0 && offset.height == 0) {
			return;
		}
		
		location = newLocation;
		
		UpdateManager updateManager = figure.getUpdateManager();
		LayoutManager layoutManager = figure.getParent().getLayoutManager();
		Rectangle bounds = figure.getBounds();
		updateManager.addDirtyRegion(figure.getParent(), bounds);
		bounds = bounds.getCopy().translate(offset.width, offset.height);
		layoutManager.setConstraint(figure, bounds);
		figure.translate(offset.width, offset.height);
		updateManager.addDirtyRegion(figure.getParent(), bounds);
		event.consume();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseHover(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent event) {
	}

	@Override
	public void mouseDoubleClicked(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		location = event.getLocation();
		event.consume();
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (location == null) {
			return;
		}
		
		location = null;
		event.consume();
	}
}

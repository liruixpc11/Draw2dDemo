package lab.cadl.lirui.draw2d.demo;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

public class NoteFigure extends Label {
	public NoteFigure(String label) {
		super(label);
		
		setBorder(new NoteBorder());
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		graphics.setBackgroundColor(ColorConstants.white);
		Rectangle bounds = getBounds();
		int fold = NoteBorder.FOLD;
		graphics.fillRectangle(bounds.x, bounds.y + fold, bounds.x + fold, bounds.y + bounds.height);
		graphics.fillRectangle(bounds.x + fold, bounds.y, bounds.x + bounds.width, bounds.y + fold);
		super.paintFigure(graphics);
	}
}

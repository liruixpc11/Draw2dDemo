package lab.cadl.lirui.draw2d.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;

public class PersonFigure extends RectangleFigure {
	public PersonFigure(String name, Date birthday, String note) {
		setBackgroundColor(ColorConstants.lightGray);
		setLayoutManager(new ToolbarLayout());
		setPreferredSize(200, 100);
		setBorder(new LineBorder(1));

		add(new Label(name + " "
				+ new SimpleDateFormat("yyyy-MM-dd").format(birthday)));

		for (String n : note.split("\n")) {
			add(new NoteFigure(n));
		}
	}

	@Override
	public void paintFigure(Graphics graphics) {
		Rectangle rectangle = getBounds();
		graphics.setBackgroundPattern(new Pattern(Display.getCurrent(),
				rectangle.x, rectangle.y, rectangle.x + rectangle.width,
				rectangle.y + rectangle.height, ColorConstants.white,
				ColorConstants.lightGray));
		graphics.fillRectangle(rectangle);
	}

}

package lab.cadl.lirui.draw2d.demo;

import java.util.Date;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class GenealogyView {
	public void run() {
		Shell shell = new Shell(new Display());
		shell.setSize(800, 600);
		shell.setText("Draw2d Demo");
		shell.setLayout(new GridLayout());
		
		Canvas canvas = createCanvas(shell);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Display display = shell.getDisplay();
		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private Canvas createCanvas(Shell parent) {
		IFigure root = createFigure();
		root.setFont(parent.getFont());
		
		Canvas canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setBackground(ColorConstants.white);
		
		LightweightSystem lws = new LightweightSystem(canvas);
		lws.setContents(root);

		return canvas;
	}
	
	private Connection connect(IFigure figure1, IFigure figure2) {
		PolylineConnection connection = new PolylineConnection();
		connection.setSourceAnchor(new ChopboxAnchor(figure1));
		connection.setTargetAnchor(new ChopboxAnchor(figure2));
		return connection;
	}
	
	private IFigure createFigure() {
		Figure root = new Figure();
		XYLayout layout = new XYLayout();
		root.setLayoutManager(layout);

		IFigure previousFigure = null;
		for (Object[] o : new Object[][] {
				new Object[] {"Li Rui", new Point(10, 10), "note1"}, 
				new Object[] {"Demo", new Point(200, 200), "note2"}
				
		}) {
			IFigure figure = createPersonFigure((String) o[0], (String) o[2]); 
			root.add(figure);
			layout.setConstraint(figure, new Rectangle((Point) o[1], figure.getPreferredSize()));

			if (previousFigure != null) {
				root.add(connect(figure, previousFigure));
			}

			previousFigure = figure;
		}
		return root;
	}
	
	private IFigure createPersonFigure(String name, String note) {
		RectangleFigure rectangleFigure = new PersonFigure(name, new Date(), note);
		new FigureMover(rectangleFigure);
		return rectangleFigure;
	}
	
	public static void main(String[] args) {
		new GenealogyView().run();
	}
}

package lab.cadl.lirui.draw2d.demo;

import java.util.Date;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.FreeformViewport;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class GenealogyView {
	private ScalableFreeformLayeredPane root;
	private FreeformLayer primary;
	private ConnectionLayer connections;

	public void run() {
		Shell shell = new Shell(new Display());
		shell.setSize(1440, 900);
		shell.setText("Draw2d Demo");
		shell.setLayout(new GridLayout());
		
		Canvas canvas = createCanvas(shell);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		createMenuBar(shell);
		
		Display display = shell.getDisplay();
		shell.open();
		while (!shell.isDisposed()) {
			while (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void createMenuBar(Shell shell) {
		final Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		
		MenuItem zoomMenuItem = new MenuItem(menuBar, SWT.CASCADE);
		zoomMenuItem.setText("Zoom");
		Menu zoomMenu = new Menu(shell, SWT.DROP_DOWN);
		zoomMenuItem.setMenu(zoomMenu);
		
		createFixedZoomMenuItem(zoomMenu, 0.5);
		createFixedZoomMenuItem(zoomMenu, 1);
		createFixedZoomMenuItem(zoomMenu, 2);
		
		createScaleToFitMenuItem(zoomMenu);
	}
	
	private void createFixedZoomMenuItem(Menu menu, final double scale) {
		String text = String.valueOf(((int) (scale * 100)));
		
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText(text);
		menuItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				root.setScale(scale);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				widgetSelected(arg0);
			}
		});
	}
	
	private void createScaleToFitMenuItem(Menu menu) {
		MenuItem menuItem = new MenuItem(menu, SWT.NULL);
		menuItem.setText("Scale to fit");
		menuItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				scaleToFit();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				widgetSelected(arg0);
			}
		});
	}
	
	private void scaleToFit() {
		FreeformViewport viewport = (FreeformViewport) root.getParent();
		Rectangle viewArea = viewport.getClientArea();
		
		root.setScale(1);
		
		Rectangle extent = root.getFreeformExtent().union(0, 0);
		double wScale = ((double) viewArea.width) / extent.width;
		double hScale = ((double) viewArea.height) / extent.height;
		double newScale = Math.min(hScale, wScale);
		
		root.setScale(newScale);
	}
	
	private Canvas createCanvas(Shell parent) {
		root = new ScalableFreeformLayeredPane();
		root.setFont(parent.getFont());

		drawContents();
		
		FigureCanvas canvas = new FigureCanvas(parent, SWT.DOUBLE_BUFFERED);
		canvas.setViewport(new FreeformViewport());
		canvas.setBackground(ColorConstants.white);
		canvas.setContents(root);
		
		// LightweightSystem lws = new LightweightSystem(canvas);
		// lws.setContents(root);

		return canvas;
	}
	
	private Connection connect(IFigure figure1, IFigure figure2) {
		PolylineConnection connection = new PolylineConnection();
		
		PolygonDecoration decoration = new PolygonDecoration();
		decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP);
		connection.setTargetDecoration(decoration);

		connection.setSourceAnchor(new ChopboxAnchor(figure1));
		connection.setTargetAnchor(new ChopboxAnchor(figure2));
		return connection;
	}
	
	private void drawContents() {
		primary = new FreeformLayer();
		FreeformLayout layout = new FreeformLayout();
		primary.setLayoutManager(layout);
		root.add(primary, "Primary");
		
		connections = new ConnectionLayer();
		connections.setConnectionRouter(new ShortestPathConnectionRouter(primary));
		root.add(connections, "Connections");

		IFigure previousFigure = null;
		for (Object[] o : new Object[][] {
				new Object[] {"Han Meimei", new Point(10, 10), "note1\nadditional note"}, 
				new Object[] {"Li Lei", new Point(200, 200), "ok\nyes"},
				new Object[] {"John", new Point(200, 350), "note2"},
				new Object[] {"Lily", new Point(200, 500), "note2"},
				new Object[] {"Perter", new Point(200, 1000), "note2"},
				
		}) {
			IFigure figure = createPersonFigure((String) o[0], (String) o[2]); 
			primary.add(figure);
			layout.setConstraint(figure, new Rectangle((Point) o[1], figure.getPreferredSize()));

			if (previousFigure != null) {
				connections.add(connect(figure, previousFigure));
			}

			previousFigure = figure;
		}
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

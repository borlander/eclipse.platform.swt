package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.MouseEvent;import org.eclipse.swt.graphics.*;

/**
 * The superclass for paint tools that use click-drag-release motions to
 * draw objects.
 */
public abstract class DragInteractivePaintSession extends BasicPaintSession 
	implements PaintRenderer {
	/**
	 * True if a click-drag is in progress
	 */
	private boolean dragInProgress;
	
	/**
	 * The position of the first click in a click-drag
	 */
	private Point anchorPosition = new Point(-1, -1);

	/**
	 * A temporary point
	 */
	private Point tempPosition = new Point(-1, -1);
	
	/**
	 * Constructs a DragInteractivePaintSession.
	 * 
	 * @param getPaintSurface() the drawing surface to use
	 */
	protected DragInteractivePaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().getPaintStatus().
			setMessage(PaintPlugin.getResourceString("session.DragInteractivePaint.message"));
		anchorPosition.x = -1;
		dragInProgress = false;
	}
	
	/**
	 * Deactivates the tool.
     */
	public void endSession() {
	}
	
	/**
	 * Resets the tool.
	 * Aborts any operation in progress.
	 */
	public void resetSession() {
		getPaintSurface().clearRubberbandSelection();
		anchorPosition.x = -1;
		dragInProgress = false;
	}

	/**
	 * Handles a mouseDown event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDown(MouseEvent event) {
		if (event.button != 1) return;
		if (dragInProgress) return; // spurious event
		dragInProgress = true;
		
		anchorPosition.x = event.x;
		anchorPosition.y = event.y;
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseUp(MouseEvent event) {
		if (event.button != 1) {
			resetSession(); // abort if right or middle mouse button pressed
			return;
		}
		if (! dragInProgress) return; // spurious event
		dragInProgress = false;
		if (anchorPosition.x == -1) return; // spurious event
		
		getPaintSurface().commitRubberbandSelection();
	}
	
	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		if (! dragInProgress) {
			ps.showCurrentPositionStatus();
			return;
		}
		ps.showCurrentRangeStatus(anchorPosition);
		ps.clearRubberbandSelection();
		tempPosition.x = event.x;
		tempPosition.y = event.y;
		ps.addRubberbandSelection(createFigure(anchorPosition, tempPosition));
	}
	
	/**
	 * Draws a permanent entity given 2 points.
	 * 
	 * @param points[0] and points[1] are two points
	 * @param numPoints the number of valid points in the array (must be 2)
	 */
	public void render(final Point[] points, int numPoints) {
		Assert.assert(numPoints == 2);
		getPaintSurface().drawFigure(createFigure(points[0], points[1]));
	}
	
	/**
	 * Template Method: Creates a Figure for drawing rubberband entities and the final product
	 * 
	 * @param anchor the anchor point
	 * @param cursor the point marking the current pointer location
	 */
	protected abstract Figure createFigure(Point anchor, Point cursor);
}

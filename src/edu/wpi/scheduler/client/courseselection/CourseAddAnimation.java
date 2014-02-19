package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class CourseAddAnimation extends Animation {

	private Element start;
	private Element end;
	private Element element = DOM.createDiv();

	public CourseAddAnimation(Element start, Element end) {
		this.start = start;
		this.end = end;

		element.getStyle().setBorderColor("#000000");
		element.getStyle().setBorderStyle(BorderStyle.SOLID);
		element.getStyle().setPosition(Position.FIXED);

	}

	@Override
	protected void onComplete() {
		super.onComplete();
		element.removeFromParent();
	}

	@Override
	protected void onStart() {
		super.onStart();
		RootPanel.get().getElement().appendChild(element);
	}

	@Override
	protected void onUpdate(double progress) {
		if( !start.hasParentElement() || !end.hasParentElement() ){
			this.cancel();
			return;
		}
		
		progress = 1 - progress;
			
		int startX = start.getAbsoluteLeft();
		int startY = start.getAbsoluteTop();
		int startWidth = start.getOffsetWidth();
		int startHeight = start.getOffsetHeight();
		
		int endX = end.getAbsoluteLeft();
		int endY = end.getAbsoluteTop();
		int endWidth = end.getOffsetWidth();
		int endHeight = end.getOffsetHeight();
		
		element.getStyle().setLeft(startX + (endX-startX) * progress, Unit.PX);
		element.getStyle().setTop(startY + (endY-startY) * progress, Unit.PX);
		element.getStyle().setWidth(startWidth + (endWidth-startWidth) * progress, Unit.PX);
		element.getStyle().setHeight(startHeight + (endHeight-startHeight) * progress, Unit.PX);		
		element.getStyle().setBorderWidth(1.0 + progress * 3.0, Unit.PX);
	}

}

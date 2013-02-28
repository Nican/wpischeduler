package edu.wpi.scheduler.client.courseselection;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class CourseAddAnimation extends Animation {

	private Widget start;
	private Widget end;
	private Element element = DOM.createDiv();

	public CourseAddAnimation(Widget start, Widget end) {
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
		if( !start.isAttached() || !end.isAttached() ){
			this.cancel();
			return;
		}
		
		progress = 1 - progress;
			
		int startX = start.getAbsoluteLeft();
		int startY = start.getAbsoluteTop();
		int startWidth = start.getElement().getOffsetWidth();
		int startHeight = start.getElement().getOffsetHeight();
		
		int endX = end.getAbsoluteLeft();
		int endY = end.getAbsoluteTop();
		int endWidth = end.getElement().getOffsetWidth();
		int endHeight = end.getElement().getOffsetHeight();
		
		element.getStyle().setLeft(startX + (endX-startX) * progress, Unit.PX);
		element.getStyle().setTop(startY + (endY-startY) * progress, Unit.PX);
		element.getStyle().setWidth(startWidth + (endWidth-startWidth) * progress, Unit.PX);
		element.getStyle().setHeight(startHeight + (endHeight-startHeight) * progress, Unit.PX);		
		element.getStyle().setBorderWidth(1.0 + progress * 3.0, Unit.PX);
	}

}

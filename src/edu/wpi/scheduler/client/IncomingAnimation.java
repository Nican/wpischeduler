package edu.wpi.scheduler.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

public class IncomingAnimation extends Animation {

	private Element element;
	public static double nextAnimation = 0;

	public IncomingAnimation(Element element) {
		this.element = element;
	}
	
	public void run(){
		double currentTime = Duration.currentTimeMillis();
		double startTime = nextAnimation + 50.0;
		
		if( startTime < currentTime ){
			startTime = currentTime;
		} else if (startTime > currentTime + 500.0 ){
			startTime = currentTime + 300.0;
		}
		
		super.run(300, startTime);
		nextAnimation = startTime;
	}

	@Override
	protected void onUpdate(double progress) {
		double left = -element.getClientWidth() * (1-progress);
		getStyle().setLeft(left, Unit.PX);
	}

	@Override
	protected void onComplete() {
		super.onComplete();
		getStyle().clearPosition();
		getStyle().clearLeft();
	}

	@Override
	protected void onStart() {
		getStyle().setPosition(Position.RELATIVE);
		super.onStart();
	}

	protected Style getStyle() {
		return element.getStyle();
	}

}

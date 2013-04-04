package edu.wpi.scheduler.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

public class IncomingAnimation extends Animation {

	private Element element;
	public static double nextAnimation = 0;

	final boolean leftDirection;
	final boolean hiding;

	public IncomingAnimation(Element element) {
		this(element, true);
	}

	public IncomingAnimation(Element element, boolean fromLeft) {
		this(element, fromLeft, false);
	}

	public IncomingAnimation(Element element, boolean fromLeft, boolean hiding) {
		this.element = element;
		this.leftDirection = fromLeft;
		this.hiding = hiding;
	}

	public void run() {
		double currentTime = Duration.currentTimeMillis();
		double startTime = nextAnimation + 50.0;

		if (startTime < currentTime) {
			startTime = currentTime;
		} else if (startTime > currentTime + 500.0) {
			startTime = currentTime + 300.0;
		}

		super.run(300, startTime);
		nextAnimation = startTime;
	}

	@Override
	protected void onUpdate(double progress) {
		double percent = hiding ? progress : (1 - progress);

		if (this.leftDirection) {
			double left = -element.getClientWidth() * percent;
			getStyle().setLeft(left, Unit.PX);
		} else {
			double top = element.getClientHeight() * (1-percent);
			getStyle().setHeight(top, Unit.PX);
		}
	}

	@Override
	protected void onComplete() {
		super.onComplete();
		getStyle().clearPosition();
		getStyle().clearLeft();
		getStyle().clearOverflow();
	}

	@Override
	protected void onStart() {
		if (this.leftDirection) {
			getStyle().setPosition(Position.RELATIVE);
		} else {
			getStyle().setOverflow(Overflow.HIDDEN);
		}
		super.onStart();
	}

	protected Style getStyle() {
		return element.getStyle();
	}

}

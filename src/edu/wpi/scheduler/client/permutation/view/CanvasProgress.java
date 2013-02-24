package edu.wpi.scheduler.client.permutation.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.Context2d.TextBaseline;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RequiresResize;

import edu.wpi.scheduler.client.controller.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.controller.ScheduleProducer;
import edu.wpi.scheduler.client.controller.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.client.permutation.PermutationController;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;

public class CanvasProgress extends ComplexPanel implements ProducerEventHandler, AnimationCallback, RequiresResize, ResizeHandler {

	public static class CanvasProgressSection {
		public double x;
		public double y;
		public double w;
		public double h;
		public final Section section;

		public CanvasProgressSection(Section section) {
			this.section = section;
		}

	}

	private final double lineHeight = 32.0;
	HandlerRegistration resizeRegistration;

	PermutationController controller;
	Canvas canvas = Canvas.createIfSupported();
	Canvas background = Canvas.createIfSupported();
	Label title = new Label("Looking for schedules...");

	protected List<CanvasProgressSection> sections = new ArrayList<CanvasProgressSection>();

	public CanvasProgress(PermutationController controller) {
		setElement(DOM.createDiv());
		this.controller = controller;

		this.getElement().getStyle().setHeight(100.0, Unit.PCT);

		title.setStyleName("ScheduleLoadingLabel");
		
		this.add(title, getElement());
		this.add(canvas, getElement());
		
		
		
		
		/*
		SimplePanel cover = new SimplePanel();
		Style coverStyle = cover.getElement().getStyle();
		coverStyle.setPosition(Position.ABSOLUTE);
		coverStyle.setZIndex(100);
		coverStyle.setLeft(0.0, Unit.PX);
		coverStyle.setRight(0.0, Unit.PX);
		coverStyle.setTop(0.0, Unit.PX);
		coverStyle.setBottom(0.0, Unit.PX);
		coverStyle.setOpacity(0.3);
		coverStyle.setBackgroundColor("#000");
		
		this.add(cover, getElement());
		
		cover.setWidget(new Label("TESTTT"));
		*/
		
	}

	@Override
	public void onLoad() {
		controller.addProduceHandler(this);
		resizeRegistration = Window.addResizeHandler(this);
		
		updateSize();
	}

	@Override
	public void onUnload() {
		controller.removeProduceHandler(this);
		resizeRegistration.removeHandler();
	}

	public void updateSize() {
		int width = getElement().getClientWidth();
		int height = getElement().getClientHeight();

		if (width != canvas.getCoordinateSpaceWidth() || height != canvas.getCoordinateSpaceHeight()) {
			canvas.setCoordinateSpaceHeight(height);
			canvas.setCoordinateSpaceWidth(width);

			background.setCoordinateSpaceHeight(height);
			background.setCoordinateSpaceWidth(width);

			if (width > 0 && height > 0) {
				initItems();
				redraw();
			}
		}
	}

	private void initItems() {
		sections.clear();

		ScheduleProducer prdocuer = controller.getProducer();
		double width = (double) canvas.getCoordinateSpaceWidth();
		double height = (double) canvas.getCoordinateSpaceHeight();
		int courseCount = prdocuer.producedSections.size();
		int maxSectionSize = 0;
		double coursesHeight = height / courseCount;

		for (int i = 0; i < courseCount; i++) {
			maxSectionSize = Math.max(maxSectionSize, prdocuer.producedSections.get(i).size());
		}

		if (maxSectionSize == 0)
			return;

		double sectionSize = Math.max(width / maxSectionSize, 10.0);
		Context2d context = background.getContext2d();
		context.clearRect(0.0, 0.0, width, height);
		context.setTextAlign(TextAlign.CENTER);

		for (int i = 0; i < courseCount; i++) {
			List<Section> sections = prdocuer.producedSections.get(i);
			double courseY = coursesHeight * i + coursesHeight / 2 - lineHeight / 2;

			if (sections.size() == 0)
				continue;

			Course course = sections.get(0).course;

			for (int j = 0; j < sections.size(); j++) {
				CanvasProgressSection progress = new CanvasProgressSection(sections.get(j));

				progress.x = width / 2 - sectionSize * (sections.size()) / 2 + sectionSize * j;
				progress.y = courseY;
				progress.w = Math.max(sectionSize - 10.0, 10.0);
				progress.h = lineHeight;

				context.beginPath();
				context.setFillStyle("#FF0000");
				context.setStrokeStyle("#FF0000");
				context.rect(progress.x, progress.y, progress.w, progress.h);
				context.stroke();

				context.save();
				context.translate(progress.x + progress.w / 2, progress.y + progress.h / 2);
				context.rotate(Math.PI / 2);

				context.setFillStyle("#000000");
				context.setTextBaseline(TextBaseline.MIDDLE);
				context.fillText(progress.section.number, 0, 0);
				context.restore();

				this.sections.add(progress);
			}

			String title = course.name + " (" + course.department.abbreviation + course.number + ")";
			context.setTextBaseline(TextBaseline.BOTTOM);
			context.fillText(title, width / 2, courseY - 4);
		}

		title.getElement().getStyle().setLeft(width / 2 - title.getElement().getClientWidth() / 2, Unit.PX);
		title.getElement().getStyle().setTop(height / 2 - title.getElement().getClientHeight() / 2, Unit.PX);
	}

	public void redraw() {
		AnimationScheduler.get().requestAnimationFrame(this, canvas.getCanvasElement());
	}

	public CanvasProgressSection getBySection(Section find) {
		for (CanvasProgressSection section : sections) {
			if (section.section.equals(find))
				return section;
		}
		return null;
	}

	@Override
	public void onResize() {
		updateSize();
	}
	
	@Override
	public void onResize(ResizeEvent event) {
		updateSize();
	}

	@Override
	public void execute(double timestamp) {
		updateSize();

		ScheduleProducer prdocuer = controller.getProducer();
		Context2d context = canvas.getContext2d();
		int width = canvas.getCoordinateSpaceWidth();
		int height = canvas.getCoordinateSpaceHeight();

		if (width == 0 || height == 0)
			return;

		context.clearRect(0.0, 0.0, (double) width, (double) height);
		context.drawImage(background.getCanvasElement(), 0.0, 0.0);

		if (prdocuer.treeSearchState.size() > 1) {

			for (int i = 1; i < prdocuer.treeSearchState.size(); i++) {
				CanvasProgressSection progress = getBySection(prdocuer.getSectionFromTree(i - 1));
				CanvasProgressSection progress2 = getBySection(prdocuer.getSectionFromTree(i));

				context.beginPath();
				context.moveTo(progress.x + progress.w / 2, progress.y + progress.h + 1);
				context.lineTo(progress2.x + progress2.w / 2, progress2.y - 1);
				context.stroke();
			}

		}
	}

	@Override
	public void onPermutationUpdated(UpdateType type) {
		if (type == UpdateType.NEW)
			initItems();

		this.redraw();
	}	

}

package edu.wpi.scheduler.client.permutation.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.Context2d.TextBaseline;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RequiresResize;

import edu.wpi.scheduler.client.generator.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.generator.ScheduleProducer;
import edu.wpi.scheduler.client.generator.ScheduleProducer.ProducerEventHandler;
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
		setElement(Document.get().createDivElement());
		this.controller = controller;

		this.getElement().getStyle().setHeight(100.0, Unit.PCT);

		title.setStyleName("ScheduleLoadingLabel");
		
		this.add(title, getElement());
		this.add(canvas, getElement());
	}

	@Override
	public void onLoad() {
		controller.addProduceHandler(this);
		resizeRegistration = Window.addResizeHandler(this);
		
		updateSize();
		redraw();
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
		int courseCount = prdocuer.courses.size();
		int maxSectionSize = 0;
		double coursesHeight = height / courseCount;

		for (int i = 0; i < courseCount; i++) {
			maxSectionSize = Math.max(maxSectionSize, prdocuer.courses.get(i).size());
		}

		if (maxSectionSize == 0)
			return;

		double sectionSize = Math.max(width / maxSectionSize, 10.0);
		Context2d context = background.getContext2d();
		context.clearRect(0.0, 0.0, width, height);
		context.setTextAlign(TextAlign.CENTER);

		for (int i = 0; i < courseCount; i++) {
			List<Section> sections = prdocuer.courses.get(i);
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
		
		Element titleElement = title.getElement();

		titleElement.getStyle().setLeft(width / 2 - titleElement.getClientWidth() / 2, Unit.PX);
		titleElement.getStyle().setTop(height / 2 - titleElement.getClientHeight() / 2, Unit.PX);
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

		//ScheduleProducer producer = controller.getProducer();
		Context2d context = canvas.getContext2d();
		int width = canvas.getCoordinateSpaceWidth();
		int height = canvas.getCoordinateSpaceHeight();

		if (width == 0 || height == 0)
			return;

		context.clearRect(0.0, 0.0, (double) width, (double) height);
		context.drawImage(background.getCanvasElement(), 0.0, 0.0);
		
		/*
		if (producer.treeSearchState.size() > 1) {

			for (int i = 1; i < producer.treeSearchState.size(); i++) {
				CanvasProgressSection progress = getBySection(producer.getSectionFromTree(i - 1));
				CanvasProgressSection progress2 = getBySection(producer.getSectionFromTree(i));

				context.beginPath();
				context.moveTo(progress.x + progress.w / 2, progress.y + progress.h + 1);
				context.lineTo(progress2.x + progress2.w / 2, progress2.y - 1);
				context.stroke();
			}

		}
		
		if(isAttached() && producer.isActive())
			redraw();
			*/
	}

	@Override
	public void onPermutationUpdated(UpdateType type) {
		if (type == UpdateType.NEW)
			initItems();

		this.redraw();
	}	

}

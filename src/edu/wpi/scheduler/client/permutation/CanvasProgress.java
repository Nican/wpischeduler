package edu.wpi.scheduler.client.permutation;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.wpi.scheduler.client.controller.ProducerUpdateEvent.UpdateType;
import edu.wpi.scheduler.client.controller.ScheduleProducer;
import edu.wpi.scheduler.client.controller.ScheduleProducer.ProducerEventHandler;
import edu.wpi.scheduler.shared.model.Course;
import edu.wpi.scheduler.shared.model.Section;

public class CanvasProgress extends SimplePanel implements ResizeHandler, ProducerEventHandler, AnimationCallback {

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
	AnimationScheduler animSched = AnimationScheduler.get();

	protected List<CanvasProgressSection> sections = new ArrayList<CanvasProgressSection>();

	public CanvasProgress(PermutationController controller) {
		this.controller = controller;

		this.getElement().getStyle().setHeight(100.0, Unit.PCT);

		this.add(canvas);

	}

	@Override
	public void onLoad() {
		System.out.println("controller: " + controller);

		resizeRegistration = Window.addResizeHandler(this);
		controller.addProduceHandler(this);

		updateSize();
		initItems();
		redraw();
	}

	@Override
	public void onUnload() {
		resizeRegistration.removeHandler();
		controller.removeProduceHandler(this);
	}

	public void updateSize() {
		setSize(Integer.toString(this.getElement().getClientWidth()), Integer.toString(this.getElement().getClientHeight()));
		initItems();
		redraw();
	}

	public void setSize(String width, String height) {
		// this.canvas.setSize(width, height);
		canvas.getElement().setAttribute("width", width);
		canvas.getElement().setAttribute("height", height);

		background.getElement().setAttribute("width", width);
		background.getElement().setAttribute("height", height);
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
		context.setTextBaseline(TextBaseline.MIDDLE);

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
				context.fillText(progress.section.number, 0, 0);
				context.restore();

				this.sections.add(progress);
			}

			String title = course.name + " (" + course.department.abbreviation + course.number + ")";
			context.fillText(title, width / 2, courseY - lineHeight / 2);

		}

	}

	public void redraw() {
		animSched.requestAnimationFrame(this, canvas.getCanvasElement());
	}

	public CanvasProgressSection getBySection(Section find) {
		for (CanvasProgressSection section : sections) {
			if (section.section.equals(find))
				return section;
		}
		return null;
	}

	@Override
	public void onResize(ResizeEvent event) {
		updateSize();
	}

	@Override
	public void execute(double timestamp) {
		ScheduleProducer prdocuer = controller.getProducer();
		Context2d context = canvas.getContext2d();
		double width = (double) canvas.getCoordinateSpaceWidth();
		double height = (double) canvas.getCoordinateSpaceHeight();

		context.clearRect(0.0, 0.0, width, height);
		context.drawImage(background.getCanvasElement(), 0.0, 0.0);

		if (prdocuer.treeSearchState.size() > 1) {

			for (int i = 1; i < prdocuer.treeSearchState.size(); i++) {
				CanvasProgressSection progress = getBySection(prdocuer.getSectionFromTree(i - 1));
				CanvasProgressSection progress2 = getBySection(prdocuer.getSectionFromTree(i));

				context.beginPath();
				context.moveTo(progress.x + progress.w / 2, progress.y + progress.h);
				context.lineTo(progress2.x + progress2.w / 2, progress2.y);
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

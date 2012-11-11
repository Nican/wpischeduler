package edu.wpi.scheduler.client.permutation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PermutationChooserView extends Composite {

	private static PermutationChooserViewUiBinder uiBinder = GWT
			.create(PermutationChooserViewUiBinder.class);

	interface PermutationChooserViewUiBinder extends
			UiBinder<Widget, PermutationChooserView> {
	}

	public PermutationChooserView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

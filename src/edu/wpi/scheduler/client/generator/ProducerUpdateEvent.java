package edu.wpi.scheduler.client.generator;

import com.google.gwt.event.shared.GwtEvent;

import edu.wpi.scheduler.client.generator.ScheduleProducer.ProducerEventHandler;


public class ProducerUpdateEvent extends GwtEvent<ProducerEventHandler> {
	
	public enum UpdateType {
		NEW,
		UPDATE, //When we find a new schedule (Called multiple times)
		FINISH //When we finish finding schedules
	}
	
	public static final Type<ProducerEventHandler> TYPE = new Type<ProducerEventHandler>();
	private UpdateType type;
	
	public ProducerUpdateEvent( UpdateType type ){
		this.type = type;
	}


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ProducerEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ProducerEventHandler handler) {
		handler.onPermutationUpdated( type );
	}
}
package edu.wpi.scheduler.client.controller;

import com.google.gwt.event.shared.GwtEvent;

public class FavoriteEvent  extends GwtEvent<FavoriteEventHandler>  {
	
	public enum FavoriteEventType {
		ADD,
		REMOVE
	}

	public static final Type<FavoriteEventHandler> TYPE = new Type<FavoriteEventHandler>();
	public final FavoriteEventType type;
	
	public FavoriteEvent(FavoriteEventType type){
		this.type = type;
	}


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FavoriteEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FavoriteEventHandler handler) {
		handler.onFavoriteUpdate(this);
	}
}

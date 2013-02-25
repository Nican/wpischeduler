package edu.wpi.scheduler.client.controller;

import com.google.gwt.event.shared.GwtEvent;

public class FavoriteEvent  extends GwtEvent<FavoriteEventHandler>  {

	public static final Type<FavoriteEventHandler> TYPE = new Type<FavoriteEventHandler>();
	
	public FavoriteEvent(){
	}


	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FavoriteEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FavoriteEventHandler handler) {
		handler.onFavoriteUpdate();
	}
}

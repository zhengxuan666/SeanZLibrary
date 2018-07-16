package com.seanz.library.rxbus.event;

public class RxBusListener {
	public interface IRxBusListener {
		public void onRxBusStateChanged(int state);
	}
}

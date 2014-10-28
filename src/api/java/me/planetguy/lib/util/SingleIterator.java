package me.planetguy.lib.util;

import java.util.Iterator;

public class SingleIterator implements Iterator{

	private Object object;
	
	public SingleIterator(Object o){
		object=o;
	}
	
	@Override
	public boolean hasNext() {
		return object!=null;
	}

	@Override
	public Object next() {
		Object o=object;
		object=null;
		return o;
	}

	@Override
	public void remove() {
		object=null;
	}

}

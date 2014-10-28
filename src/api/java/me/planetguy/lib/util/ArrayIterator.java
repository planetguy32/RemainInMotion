package me.planetguy.lib.util;

import java.util.Iterator;

public class ArrayIterator implements Iterator {

	private Object[] objs;
	
	private int index;
	
	public ArrayIterator(Object[] o){
		objs=o;
	}
	
	@Override
	public boolean hasNext() {
		return index<objs.length;
	}

	@Override
	public Object next() {
		return objs[index++];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}

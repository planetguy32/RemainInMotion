package me.planetguy.lib.util;

import java.util.Iterator;

public class EmptyIterator implements Iterator{

	public static EmptyIterator instance=new EmptyIterator();
	
	private EmptyIterator(){};
	
	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Object next() {
		throw new RuntimeException("Tried to access element of empty iterator!");
	}

	@Override
	public void remove() {}

}

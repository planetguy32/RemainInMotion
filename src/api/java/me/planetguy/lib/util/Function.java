package me.planetguy.lib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Function {

	public static interface Binary<T>{
		public T apply(T arg1, T arg2);
	}
	
	public static interface Unary<TIn, TOut>{
		public TOut apply(TIn arg);
	}
	
	private static ExecutorService threads=Executors.newCachedThreadPool();
	
	public static <TIn, TOut> ArrayList<TOut> map(ArrayList<TIn> in, final Unary<TIn, TOut> func){
		ArrayList<Callable<TOut>> tasks=new ArrayList<Callable<TOut>>();
		for(final TIn o:in){
			Callable<TOut> call=new Callable<TOut>(){
				public TOut call() throws Exception {
					return func.apply(o);
				}
			};
			tasks.add(call);
		}
		try{
			List<Future<TOut>> futures=threads.invokeAll(tasks);
			ArrayList<TOut> results=new ArrayList<TOut>();
			for(Future<TOut> out:futures){
				results.add(out.get());
			}
			return results;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}

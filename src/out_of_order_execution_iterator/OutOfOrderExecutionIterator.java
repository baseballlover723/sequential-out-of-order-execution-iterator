package out_of_order_execution_iterator;

import java.util.List;

public class OutOfOrderExecutionIterator<T> {
	private int maxNumberOfThreads;
	private List<T> collection;

	public OutOfOrderExecutionIterator(int maxNumberOfThreads, List<T> collection) {
		this.maxNumberOfThreads = maxNumberOfThreads;
		this.collection = collection;
	}

	public void forEachExecuteOutOfOrder(Executer<T> execute, Method<T> callback) {
		// TODO mess with thread pools
		// use synchroized int index to assign tasks and keep track if done?
		
		// while still stuff do to DO
		// if not at max threads DO
		// create new thread with task
		// IN THREAD do direct computation
		// IN THREAD spin up new thread
		// END
		// do direct computation
		// add callback to heap ordered by index
		// while heapIndexVal = nextIndexToCallback DO
		// 
		// END
		// END
		
		// direct computation
		for (int i = 0; i < this.collection.size(); i++) {
			// for (int i = this.collection.size() - 1; i >= 0; i--) {
			T obj = this.collection.get(i);
			T result = execute.execute(obj);
			callback.execute(result);
		}
	}
}

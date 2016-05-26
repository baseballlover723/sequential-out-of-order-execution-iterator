package out_of_order_execution_iterator;

import java.util.Collection;

public class OutOfOrderExecutionIterator<T> {
	private int maxNumberOfThreads;
	private Collection<T> collection;

	public OutOfOrderExecutionIterator(int maxNumberOfThreads, Collection<T> collection) {
		this.maxNumberOfThreads = maxNumberOfThreads;
		this.collection = collection;
	}

	public void forEachExecuteOutOfOrder(Executer<T> execute, Method<T> callback) {
		for (T obj : this.collection) {
			T result = execute.execute(obj);
			callback.execute(result);
		}
	}
}

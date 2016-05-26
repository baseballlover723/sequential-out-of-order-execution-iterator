package out_of_order_execution_iterator;

import java.util.ArrayList;

public class OutOfOrderExecutionIterator<T> {
	private int maxNumberOfThreads;
	private ArrayList<T> collection;

	public OutOfOrderExecutionIterator(int maxNumberOfThreads, ArrayList<T> collection) {
		this.maxNumberOfThreads = maxNumberOfThreads;
		this.collection = collection;
	}

	public void forEachExecuteOutOfOrder(Executer<T> execute, Method<T> callback) {
		// for (int i = 0; i < this.collection.size(); i++) {
		for (int i = this.collection.size() - 1; i >= 0; i--) {
			T obj = this.collection.get(i);
			T result = execute.execute(obj);
			callback.execute(result);
		}
	}
}

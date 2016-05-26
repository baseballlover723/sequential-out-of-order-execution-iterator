package out_of_order_execution_iterator;

import java.util.ArrayList;
import java.util.Collection;

public class OutOfOrderExecutionIterator<T> extends ArrayList<T> {
	private static final long serialVersionUID = 1L;
	private int maxNumberOfThreads;

	public OutOfOrderExecutionIterator(int maxNumberOfThreads) {
		this.maxNumberOfThreads = maxNumberOfThreads;
	}

	public void forEachExecuteOutOfOrder(Executer<T> execute, Method<T> callback) {
		for (T obj : this) {
			T result = execute.execute(obj);
			callback.execute(result);
		}
	}
}

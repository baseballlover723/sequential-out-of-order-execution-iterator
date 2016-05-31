package out_of_order_execution_iterator;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class OutOfOrderExecutionIterator<T> {
	private int maxNumberOfThreads;
	private List<T> collection;

	public OutOfOrderExecutionIterator(int maxNumberOfThreads, List<T> collection) {
		if (maxNumberOfThreads < 1) {
			throw new IllegalArgumentException();
		}
		this.maxNumberOfThreads = maxNumberOfThreads;
		this.collection = collection;
	}

	public void forEachExecuteOutOfOrder(Executer<T> execute, Method<T> callback) {
		PriorityBlockingQueue<IndexResult<T>> heap = new PriorityBlockingQueue<IndexResult<T>>();
		Integer currentIndex = 0;
		Integer callbackIndex = 0;
		Integer threadNumber = 1;
		int size = this.collection.size();
		// TODO mess with thread pools
		// use synchroized int index to assign tasks and keep track if done?
		
		this.directComputation(heap, currentIndex, callbackIndex);
		System.out.println(currentIndex);
		
//		// while still stuff do to DO
//		while (currentIndex < size) {
//		// if not at max threads DO
////			synchronized (threadNumber) {
////				if (threadNumber < this.maxNumberOfThreads) {
////					threadNumber++;
////					new Thread(() -> {
////						directComputation();
////					}).start();
//////					this.addThread();
////				}
////			}
//		// do direct computation
//			this.directComputation();
//			
//		// add callback to heap ordered by index
//		// while heapIndexVal = nextIndexToCallback DO
//		// 
//		// END
//		// END
//		
//		// direct computation
//		}
//
//		for (int i = 0; i < this.collection.size(); i++) {
//			// for (int i = this.collection.size() - 1; i >= 0; i--) {
//			T obj = this.collection.get(i);
//			T result = execute.execute(obj);
//			callback.execute(result);
//		}
	}

	private void directComputation(PriorityBlockingQueue<IndexResult<T>> heap, Integer currentIndex,
			Integer callbackIndex) {
		currentIndex++;
	}

	private void addThread() {
		// create new thread with task
		new Thread(() -> {}).start();
// IN THREAD do direct computation
// IN THREAD spin up new thread
// END
	}
}

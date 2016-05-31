package out_of_order_execution_iterator;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

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
		PriorityQueue<IndexResult<T>> heap = new PriorityQueue<IndexResult<T>>();
		Index currentIndex = new Index();
		Index callbackIndex = new Index();
		Queue<Thread> threads = new LinkedList<Thread>();
		int size = this.collection.size();
		// TODO mess with thread pools
		// use synchroized int index to assign tasks and keep track if done?

		// if not at max threads DO
		while (threads.size() + 1 < this.maxNumberOfThreads) {
			this.addThread(threads, size, execute, callback, heap, currentIndex, callbackIndex);
		}
		// while still stuff do to DO
		while (true) {
			Index copyIndex;
			synchronized (currentIndex) {
				if (currentIndex.compareTo(size) >= 0) {
					break;
				}
				copyIndex = new Index(currentIndex);
				currentIndex.increment();
			}
			// do direct computation
			this.directComputation(execute, callback, heap, copyIndex, callbackIndex);
		}

		this.waitForAllThreadsToFinish(threads);

		// for (int i = 0; i < this.collection.size(); i++) {
		// // for (int i = this.collection.size() - 1; i >= 0; i--) {
		// T obj = this.collection.get(i);
		// T result = execute.execute(obj);
		// callback.execute(result);
		// }
	}

	private void waitForAllThreadsToFinish(Queue<Thread> threads) {
		while (!threads.isEmpty()) {
			try {
				threads.poll().join();
			} catch (InterruptedException e) {
				// do nothing
			}
		}
	}

	private void directComputation(Executer<T> execute, Method<T> callback, PriorityQueue<IndexResult<T>> heap,
			Index copyIndex, Index callbackIndex) {
		T result = execute.execute(this.collection.get(copyIndex.getValue()));
		// add callback to heap ordered by index
		this.dealWithCallbacks(callback, result, heap, copyIndex, callbackIndex);
	}

	private void dealWithCallbacks(Method<T> callback, T result, PriorityQueue<IndexResult<T>> heap, Index copyIndex,
			Index callbackIndex) {
		synchronized (heap) {
			heap.offer(new IndexResult<T>(copyIndex, result));
			if (callbackIndex.compareTo(heap.peek().getIndex()) != 0) {
				return;
			}
		}
		// }
		// while heapIndexVal = nextIndexToCallback DO
		synchronized (heap) {
			while (!heap.isEmpty() && callbackIndex.compareTo(heap.peek().getIndex()) == 0) {
				callback.execute(heap.poll().getResult());
				callbackIndex.increment();
			}
		}
		// }
		// END
	}

	private void addThread(Queue<Thread> threads, int size, Executer<T> execute, Method<T> callback,
			PriorityQueue<IndexResult<T>> heap, Index currentIndex, Index callbackIndex) {
		// create new thread with task
		Thread newThread = new Thread(() -> {
			// IN THREAD while still stuff to do DO
			while (true) {
				Index copyIndex;
				synchronized (currentIndex) {
					if (currentIndex.compareTo(size) >= 0) {
						break;
					}
					copyIndex = new Index(currentIndex);
					currentIndex.increment();
				}
				// IN THREAD do direct computation
				this.directComputation(execute, callback, heap, copyIndex, callbackIndex);
			}
			// END
		});
		newThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
		threads.offer(newThread);
		newThread.start();
		// END
	}
}

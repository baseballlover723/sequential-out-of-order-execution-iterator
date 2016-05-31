package out_of_order_execution_iterator;

public class IndexResult<T> implements Comparable<IndexResult<T>>{
	private final Index index;
	private T result;

	public IndexResult(Index index, T result) {
		this.index = new Index(index);
		this.index.lock();
		this.result = result;
	}
	
	public Index getIndex() {
		return this.index;
	}
	
	public T getResult() {
		return this.result;
	}

	// don't care if the types are the same, allows for you to have different object
	@SuppressWarnings("rawtypes")
	@Override
	public int compareTo(IndexResult o) {
		return this.index.compareTo(o.getIndex());
	}
}

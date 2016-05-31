package out_of_order_execution_iterator;

public class IndexResult<T> implements Comparable<IndexResult<T>>{
	private int index;
	private T result;

	public IndexResult(int index, T result) {
		if (index < 0) {
			throw new IllegalArgumentException("Can't have a negative index");
		}
		this.index = index;
		this.result = result;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public T getResult() {
		return this.result;
	}

	// don't care if the types are the same, allows for you to have different object
	@SuppressWarnings("rawtypes")
	@Override
	public int compareTo(IndexResult o) {
		return Integer.compare(this.index, o.getIndex());
	}
}

package out_of_order_execution_iterator;

@FunctionalInterface
public interface Method<T> {
	public void execute(T result);
}

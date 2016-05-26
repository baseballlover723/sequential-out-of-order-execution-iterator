package out_of_order_execution_iterator;

@FunctionalInterface
public interface Executer<T> {
	public T execute(T arg);
}

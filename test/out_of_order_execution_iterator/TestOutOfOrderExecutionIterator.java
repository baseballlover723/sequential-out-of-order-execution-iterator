package out_of_order_execution_iterator;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestOutOfOrderExecutionIterator {
	public static final int LIST_LENGTH = 40;
	private static final int MAX_NUMBER_OF_THREADS = 4;
	
	private OutOfOrderExecutionIterator<Integer> startingCollection;

	@Before
	public void setup() {
		this.startingCollection = new OutOfOrderExecutionIterator<Integer>(MAX_NUMBER_OF_THREADS);
		for (int i = 0; i < LIST_LENGTH; i++) {
			this.startingCollection.add(i);
		}
	}
	
	@Test
	public void test() {
		ArrayList<Integer> newCollection = new ArrayList<Integer>();
		this.startingCollection.forEachExecuteOutOfOrder((Integer obj) -> {
			
		}, (Integer result) -> {
			
		});
	}

}

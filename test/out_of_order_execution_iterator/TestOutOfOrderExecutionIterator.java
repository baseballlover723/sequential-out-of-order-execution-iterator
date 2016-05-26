package out_of_order_execution_iterator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class TestOutOfOrderExecutionIterator {
	private static final int LIST_LENGTH = 10;
	private static final int MAX_NUMBER_OF_THREADS = 4;
	private static final int NUMBER_OF_ITERATIONS = 1;
	private static final int SLEEP_TIME = 10;
	
	private ArrayList<Integer> startingCollection;
	private OutOfOrderExecutionIterator<Integer> iter;

	@Before
	public void setup() {
		this.startingCollection = new ArrayList<Integer>();
		for (int i = 0; i < LIST_LENGTH; i++) {
			this.startingCollection.add(i);
		}
		this.iter = new OutOfOrderExecutionIterator<Integer>(MAX_NUMBER_OF_THREADS, this.startingCollection);
	}
	
	public void stressTest(Tester test) {
		for (int i=0;i<NUMBER_OF_ITERATIONS;i++) {
			test.test();
		}
	}
	
	@Test
	public void testFinishesInOrder() {
		stressTest(() -> {
			ArrayList<Integer> newCollection = new ArrayList<Integer>();
			this.iter.forEachExecuteOutOfOrder((Integer obj) -> {
				try {
					Thread.sleep(new Random().nextInt(SLEEP_TIME));
				} catch (Exception e) {
				}
				return obj;
			}, (Integer result) -> {
				newCollection.add(result);
			});
			
			assertEquals(this.startingCollection, newCollection);
		});
	}
	
	@Test
	public void testStartsExecutionInOrder() {
		stressTest(() -> {
			ArrayList<Integer> newCollection = new ArrayList<Integer>();
			this.iter.forEachExecuteOutOfOrder((Integer obj) -> {
				newCollection.add(obj);
				return obj;
			}, (Integer result) -> {
			});
			
			assertEquals(this.startingCollection, newCollection);
		});
	}

}

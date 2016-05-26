package out_of_order_execution_iterator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class TestOutOfOrderExecutionIterator {
	private static final int LIST_LENGTH = 10;
	private static final int MAX_NUMBER_OF_THREADS = 4;
	private static final int NUMBER_OF_ITERATIONS = 100;
	private static final int SLEEP_MIN = 0;
	private static final int SLEEP_MAX = 5;

	private ArrayList<Integer> startingCollection;
	private OutOfOrderExecutionIterator<Integer> iter;
	private Random random1;
	private Random random2;

	@Before
	public void setup() {
		long seed = new Random().nextLong();
		this.random1 = new Random(seed);
		this.random2 = new Random(seed);

		this.startingCollection = new ArrayList<Integer>();
		for (int i = 0; i < LIST_LENGTH; i++) {
			this.startingCollection.add(i);
		}
		this.iter = new OutOfOrderExecutionIterator<Integer>(MAX_NUMBER_OF_THREADS, this.startingCollection);
	}

	public void stressTest(Tester test) {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			test.test();
		}
	}

	private void randomSleep(Random random) {
		try {
			Thread.sleep(random.nextInt(SLEEP_MAX - SLEEP_MIN) + SLEEP_MIN);
		} catch (Exception e) {
		}

	}

	@Test
	public void testFinishesInOrder() {
		stressTest(() -> {
			ArrayList<Integer> newCollection = new ArrayList<Integer>();
			this.iter.forEachExecuteOutOfOrder((Integer obj) -> {
				randomSleep(this.random1);
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

	@Test
	public void testIsFasterThanSequential() {
		stressTest(() -> {
			double start = System.nanoTime();
			this.iter.forEachExecuteOutOfOrder((Integer obj) -> {
				randomSleep(this.random1);
				return obj;
			}, (Integer result) -> {
			});
			double parallelTime = System.nanoTime() - start + 1_000_000;

			start = System.nanoTime();
			this.startingCollection.forEach((obj) -> {
				randomSleep(this.random2);
			});
			double sequentialTime = System.nanoTime() - start;

			String errorString = "took " + parallelTime + " compared to " + sequentialTime;
			assertTrue(errorString, parallelTime <= sequentialTime);
		});
	}

	@Test
	public void randomTest() {
		assertEquals(this.random1.nextInt(100), this.random2.nextInt(100));
		assertEquals(this.random1.nextInt(100), this.random2.nextInt(100));
		assertEquals(this.random1.nextInt(100), this.random2.nextInt(100));
		assertEquals(this.random1.nextInt(100), this.random2.nextInt(100));
	}

}

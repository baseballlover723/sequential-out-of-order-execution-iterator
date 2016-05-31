package out_of_order_execution_iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class TestOutOfOrderExecutionIterator {
	private static final int LIST_LENGTH = 50;
	private static final int MAX_NUMBER_OF_THREADS = 8;
	private static final int NUMBER_OF_ITERATIONS = 200;
	private static final int SLEEP_MAX = 500_000; // ns

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
			Thread.sleep(0, random.nextInt(SLEEP_MAX));
		} catch (Exception e) {
		}

	}

	@Test(expected = IllegalArgumentException.class)
	public void testMustHaveAtLeast1Thread() {
		new OutOfOrderExecutionIterator<Integer>(0, this.startingCollection);
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
	public void testExecutesEveryElementOncePossiblyOutOfOrder() {
		stressTest(() -> {
			int sum = 0;
			for (Integer i : this.startingCollection) {
				sum += i;
			}
			ArrayList<Integer> newCollection = new ArrayList<Integer>();
			this.iter.forEachExecuteOutOfOrder((Integer obj) -> {
				randomSleep(this.random1);
				synchronized (newCollection) {
					newCollection.add(obj);
				}
				return obj;
			}, (Integer result) -> {
			});

			assertNotEquals(this.startingCollection, newCollection);
			int newSum = 0;
			for (Integer i : newCollection) {
				newSum += i;
			}
			assertEquals(sum, newSum);
		});
	}

	@Test
	public void testIsFasterThanSequential() {
		long[] maxParallel = { 0 };
		long[] totalParallel = { 0 };
		long[] maxSequential = { 0 };
		long[] totalSequential = { 0 };
		stressTest(() -> {
			long start = System.nanoTime();
			this.iter.forEachExecuteOutOfOrder((Integer obj) -> {
				randomSleep(this.random1);
				return obj;
			}, (Integer result) -> {
			});
			long parallelTime = System.nanoTime() - start;

			start = System.nanoTime();
			this.startingCollection.forEach((obj) -> {
				randomSleep(this.random2);
			});
			long sequentialTime = System.nanoTime() - start;

			if (parallelTime > maxParallel[0]) {
				maxParallel[0] = parallelTime;
			}
			if (sequentialTime > maxSequential[0]) {
				maxSequential[0] = sequentialTime;
			}
			totalParallel[0] += parallelTime;
			totalSequential[0] += sequentialTime;

			String errorString = "took " + parallelTime + " compared to " + sequentialTime;
			assertTrue(errorString, parallelTime <= sequentialTime);
		});
		double avgParallel = totalParallel[0] / (double) NUMBER_OF_ITERATIONS;
		double avgSequential = totalSequential[0] / (double) NUMBER_OF_ITERATIONS;
		System.out.println("maxParallel: " + getTimeUnits(maxParallel[0]));
		System.out.println("maxSequential: " + getTimeUnits(maxSequential[0]));
		System.out.println();
		System.out.println("avgParallel: " + getTimeUnits(avgParallel));
		System.out.println("avgSequential: " + getTimeUnits(avgSequential));
	}

	@Test
	public void testDoesntUseMoreThenMaxThreads() {
		int currentThreads = Thread.activeCount();
		int absoluteMaxThreads = currentThreads + MAX_NUMBER_OF_THREADS + 1;
		boolean[] runningTests = { true };
		new Thread(() -> {
			stressTest(() -> {
				ArrayList<Integer> newCollection = new ArrayList<Integer>();
				this.iter.forEachExecuteOutOfOrder((Integer obj) -> {
					randomSleep(this.random1);
					return obj;
				}, (Integer result) -> {
					newCollection.add(result);
				});
			});
			runningTests[0] = false;
		}).start();
		while (runningTests[0]) {
			if (Thread.activeCount() >= absoluteMaxThreads) {
				System.out.println("error");
			}
			assertTrue(Thread.activeCount() < absoluteMaxThreads);
		}
	}
	
	// TODO add a couple for cases for invalid thread counts?
	// TODO test exepctions
	// TODO figure out why changing the bounds on line 103 and 38 dont pass mutation testing

	@Test
	public void randomTest() {
		assertEquals(this.random1.nextInt(100), this.random2.nextInt(100));
		assertEquals(this.random1.nextInt(100), this.random2.nextInt(100));
		assertEquals(this.random1.nextInt(100), this.random2.nextInt(100));
		assertEquals(this.random1.nextInt(100), this.random2.nextInt(100));
	}

	public static String getTimeUnits(long time) {
		return getTimeUnits((double) time);
	}

	/**
	 * returns a string that gives the given time difference in easily read time
	 * units
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeUnits(double time) {
		double newTime = time;

		newTime /= 1_000_000.0;
		if (newTime < 1000) {
			return String.format("%f MiliSeconds", newTime);
		}
		newTime /= 1000.0;
		if (newTime < 300) {
			return String.format("%f Seconds", newTime);
		}
		newTime /= 60.0;
		if (newTime < 180) {
			return String.format("%f Minutes", newTime);
		}
		return String.format("%f Hours", newTime / 60.0);
	}
}

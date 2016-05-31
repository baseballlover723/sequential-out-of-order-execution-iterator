package out_of_order_execution_iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestIndex {
	private static final int NUMBER_OF_THREADS = 1000;

	@Test
	public void testDefaultInitializer() {
		Index index = new Index();
		assertEquals(0, index.getValue());
		assertFalse(index.isLocked());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCantHaveNegativeIndex() {
		new Index(-1);
		fail("did not throw error");
	}

	@Test
	public void testCopyConstructor() {
		Index index = new Index();
		Index copy = new Index(index);

		assertEquals(index, copy);
		assertEquals(index.hashCode(), copy.hashCode());
		assertFalse("copy constructor is the same object", index == copy);

		index.increment();
		assertNotEquals(index, copy);
		assertNotEquals(index.hashCode(), copy.hashCode());

		copy.increment();
		copy.lock();
		assertNotEquals(index, copy);
		assertNotEquals(index.hashCode(), copy.hashCode());
	}

	@Test
	public void testGetIndex() {
		Index index = new Index(5);
		assertEquals(5, index.getValue());
	}

	@Test
	public void testIncrementIndex() {
		Index index = new Index();
		index.increment();
		assertEquals(1, index.getValue());
	}

	@Test(timeout = 100)
	public void testMulithreadedIncrementIndex() throws InterruptedException {
		Index index = new Index();
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < NUMBER_OF_THREADS; i++) {
			es.execute(() -> index.increment());
		}
		es.shutdown();
		es.awaitTermination(100, TimeUnit.MILLISECONDS);
		assertEquals(NUMBER_OF_THREADS, index.getValue());
	}

	@Test
	public void testIsLocked() {
		Index index = new Index();
		assertFalse(index.isLocked());

		index.lock();
		assertTrue(index.isLocked());
		
		index.unlock();
		assertFalse(index.isLocked());
	}
	
	@Test
	public void testLock() {
		Index index = new Index();
		assertTrue(index.lock());
		index.increment();
		assertEquals(0, index.getValue());
		assertFalse(index.lock());		
	}

	@Test
	public void testUnlock() {
		Index index = new Index();
		index.lock();
		index.increment();
		
		assertTrue(index.unlock());
		index.increment();
		assertEquals(1, index.getValue());
		assertFalse(index.unlock());
	}

	@Test
	public void testCompareTo() {
		Index index1 = new Index();
		Index index2 = new Index();

		index2.increment();
		assertTrue(index1.compareTo(index2) < 0);
		assertTrue(index1.compareTo(1) < 0);

		index1.increment();
		assertTrue(index1.compareTo(index2) == 0);
		assertTrue(index1.compareTo(1) == 0);

		index1.increment();
		assertTrue(index1.compareTo(index2) > 0);
		assertTrue(index1.compareTo(1) > 0);
	}

	@Test
	public void testEquals() {
		Index index1 = new Index();
		assertNotEquals(index1, null);
		assertEquals(index1, index1);

		Index index2 = new Index();
		assertEquals(index1, index2);

		index1.increment();
		assertNotEquals(index1, index2);

		index2.increment();
		assertEquals(index1, index2);

		index1.lock();
		assertNotEquals(index1, index2);

		index2.lock();
		assertEquals(index1, index2);

		index1.unlock();
		assertNotEquals(index1, index2);

		index2.unlock();
		assertEquals(index1, index2);
	}

	@Test
	public void testHashCode() {
		Index index1 = new Index();
		assertEquals(index1.hashCode(), index1.hashCode());

		Index index2 = new Index();
		assertEquals(index1.hashCode(), index2.hashCode());

		index1.increment();
		assertNotEquals(index1.hashCode(), index2.hashCode());

		index2.increment();
		assertEquals(index1.hashCode(), index2.hashCode());

		index1.lock();
		assertNotEquals(index1.hashCode(), index2.hashCode());

		index2.lock();
		assertEquals(index1.hashCode(), index2.hashCode());

		index1.unlock();
		assertNotEquals(index1.hashCode(), index2.hashCode());

		index2.unlock();
		assertEquals(index1.hashCode(), index2.hashCode());
	}
}

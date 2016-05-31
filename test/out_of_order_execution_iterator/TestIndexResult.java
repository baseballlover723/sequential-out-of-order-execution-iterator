package out_of_order_execution_iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.PriorityBlockingQueue;

import org.junit.Test;

public class TestIndexResult {
	@Test
	public void testConstructorCopiesIndex() {
		int indexValue = 1;
		Index index = new Index(indexValue);
		IndexResult<String> indexResult = new IndexResult<String>(index, "first");
		assertEquals(indexValue, indexResult.getIndex().getValue());
		
		index.increment();
		assertEquals(indexValue, indexResult.getIndex().getValue());
	}
	
	@Test
	public void testGetIndex() {
		int indexValue = 1;
		IndexResult<String> indexResult = new IndexResult<String>(new Index(indexValue), "first");
		assertEquals(indexValue, indexResult.getIndex().getValue());
	}
	
	@Test
	public void testCantIncrementIndex() {
		IndexResult<String> indexResult = new IndexResult<String>(new Index(), "first");
		assertEquals(0, indexResult.getIndex().getValue());
		indexResult.getIndex().increment();
		assertEquals(0, indexResult.getIndex().getValue());
		
	}

	@Test
	public void testGetResult() {
		String result1 = "first";
		IndexResult<String> indexResult1 = new IndexResult<String>(new Index(0), result1);
		assertEquals(result1, indexResult1.getResult());

		Integer result2 = 51;
		IndexResult<Integer> indexResult2 = new IndexResult<Integer>(new Index(0), result2);
		assertEquals(result2, indexResult2.getResult());
	}

	@Test
	public void testCompare() {
		IndexResult<String> indexResult1 = new IndexResult<String>(new Index(0), "first");
		IndexResult<String> indexResult2 = new IndexResult<String>(new Index(1), "second");
		assertTrue(indexResult1.compareTo(indexResult2) < 0);
		assertTrue(indexResult2.compareTo(indexResult1) > 0);
		assertTrue(indexResult1.compareTo(indexResult1) == 0);

		IndexResult<Integer> indexResult3 = new IndexResult<Integer>(new Index(1), 3);
		assertTrue(indexResult1.compareTo(indexResult3) < 0);
		assertTrue(indexResult3.compareTo(indexResult1) > 0);
	}
	
	@Test
	public void testPriorityQueue() {
		PriorityBlockingQueue<IndexResult<String>> heap = new PriorityBlockingQueue<IndexResult<String>>();
		IndexResult<String> indexResult1 = new IndexResult<String>(new Index(0), "first");
		IndexResult<String> indexResult2 = new IndexResult<String>(new Index(1), "second");
		
		heap.offer(indexResult2);
		heap.offer(indexResult1);
		
		assertEquals(indexResult1, heap.poll());
		assertEquals(indexResult2, heap.poll());
	}
}

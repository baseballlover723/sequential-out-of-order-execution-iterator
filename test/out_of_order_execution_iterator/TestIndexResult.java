package out_of_order_execution_iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TestIndexResult {

	@Before
	public void setUp() {
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCantHaveNegativeIndex() {
		new IndexResult<String>(-1, "first");
		fail("did not throw error");
	}

	@Test
	public void testGetIndex() {
		int index = 1;
		IndexResult<String> indexResult = new IndexResult<String>(index, "first");
		assertEquals(index, indexResult.getIndex());
	}

	@Test
	public void testGetResult() {
		String result1 = "first";
		IndexResult<String> indexResult1 = new IndexResult<String>(0, result1);
		assertEquals(result1, indexResult1.getResult());

		Integer result2 = 51;
		IndexResult<Integer> indexResult2 = new IndexResult<Integer>(0, result2);
		assertEquals(result2, indexResult2.getResult());
	}

	@Test
	public void testCompare() {
		IndexResult<String> indexResult1 = new IndexResult<String>(0, "first");
		IndexResult<String> indexResult2 = new IndexResult<String>(1, "second");
		assertTrue(indexResult1.compareTo(indexResult2) < 0);
		assertTrue(indexResult2.compareTo(indexResult1) > 0);
		assertTrue(indexResult1.compareTo(indexResult1) == 0);

		IndexResult<Integer> indexResult3 = new IndexResult<Integer>(1, 3);
		assertTrue(indexResult1.compareTo(indexResult3) < 0);
		assertTrue(indexResult3.compareTo(indexResult1) > 0);
	}

}

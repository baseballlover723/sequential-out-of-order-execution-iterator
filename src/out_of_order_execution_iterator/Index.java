package out_of_order_execution_iterator;

public class Index implements Comparable<Index> {
	private final Object indexMutex = new Object();
	private int index;

	private final Object lockMutex = new Object();
	private boolean locked;

	public Index() {
		this(0);
	}

	public Index(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Can't have a negative index");
		}
		this.index = index;
		this.locked = false;
	}

	public Index(Index index) {
		this.index = index.getValue();
		this.locked = index.isLocked();
	}

	public int getValue() {
		synchronized (indexMutex) {
			return this.index;
		}
	}

	public void increment() {
		synchronized (indexMutex) {
			if (!this.isLocked()) {
				this.index++;
			}
		}
	}

	public boolean isLocked() {
		synchronized (lockMutex) {
			return this.locked;
		}
	}

	public boolean lock() {
		synchronized (lockMutex) {
			if (this.isLocked()) {
				return false;
			}
			this.locked = true;
			return true;
		}
	}

	public boolean unlock() {
		synchronized (lockMutex) {
			if (!this.isLocked()) {
				return false;
			}
			this.locked = false;
			return true;
		}
	}

	@Override
	public int compareTo(Index o) {
		synchronized (indexMutex) {
			return Integer.compare(this.index, o.getValue());
		}
	}
	
	public int compareTo(int index) {
		synchronized (indexMutex) {
			return Integer.compare(this.index, index);
		}		
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Index) {
			Index other = (Index) o;
			synchronized (indexMutex) {
				synchronized (lockMutex) {
					return this.index == other.getValue() && this.locked == other.isLocked();
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		synchronized (indexMutex) {
			synchronized (lockMutex) {
				return Integer.rotateLeft(this.index, Integer.SIZE / 2) + Boolean.hashCode(this.locked);
			}
		}
	}
}

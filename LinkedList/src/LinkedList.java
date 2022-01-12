import java.util.ListIterator;
import java.util.NoSuchElementException;

public class LinkedList implements ListIterator<String> {
	Node head = null;
	Node tail = null;
	int size = 0;
	Node cursor = null;
	Node currentNode = null;
	int index = 0;
	boolean lastCallNext = false;
	boolean lastCallPrevious = false;
	boolean lastCallAdd = false;
	boolean lastCallRemove = false;

	public void addAtEnd(String element) {
		Node newNode = new Node(element, null, null);
		if (head == null) {
			head = tail = cursor = newNode;
			cursor.previous = currentNode;
			head.previous = null;
			tail.next = null;
		} else {
			tail.next = newNode;
			newNode.previous = tail;
			tail = newNode;
		}
	size++;
	}

	public void add(int index, String element) {
		Node newNode = new Node(element, null, null);
		
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		if (head == null) {
			head = newNode;
			tail = newNode;
		} else if (index == 0) {
			newNode.next = head;
			head.previous = newNode;
			head = newNode;
		} else if (index == size) {
			newNode.previous = tail;
			tail.next = newNode;
			tail = newNode;
		} else {
			Node nodeRef = head;
			for (int i = 1; i < index; i++) {
				nodeRef = nodeRef.next;
			}
			newNode.next = nodeRef.next;
			nodeRef.next = newNode;
			newNode.previous = nodeRef;
			newNode.next.previous = newNode;
		}
		size++;
	}

	public String toString() {
		String string = "[";
		for (Node node = this.head; node != null; node = node.next) {
			if (node.next == null) {
				string = string + node.value;
			} else {
				string = string + node.value + ", ";
			}
		}
		string = string + "]";
	return string;
	}

	public int size() {
		return this.size;
	}

	private Node getNode(int index) {
		currentNode = this.head;
		if (index >= 0 && index <= this.size) {
			int i = 0;
			while (currentNode != null && i++ < index) {
				currentNode = currentNode.next;
			}
		} else {
			currentNode = null;
		}
	return currentNode;
	}

	public String get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		} else {
			Node currentNode = head;
			for (int i = 0; i < index; i++) {
				currentNode = currentNode.next;
			}
		return currentNode.value;
		}
	}

	public void set(int index, String element) {
		currentNode = getNode(index);
		currentNode.value = element;
	}

	public void remove(int index) {
		currentNode = getNode(index);
		Node prev = currentNode.previous;
		Node next = currentNode.next;

		if (currentNode.next == null && currentNode.previous == null) {
			currentNode = null;
		} else if (currentNode.next == null) {
			currentNode = null;
			prev.next = null;
		} else if (currentNode.previous == null) {
			currentNode = null;
			next.previous = null;
			head = next;
		} else {
			currentNode = null;
			prev.next = next;
			next.previous = prev;
		}
	this.size--;
	}

	public void clear() {
		Node pointer = this.head;
		
		while(pointer != null) {
		    pointer.next = null;
		    pointer.previous = null;
		    pointer = pointer.next;
		}
		this.head = null;
		this.tail = null;
		size = 0;
	}

	@Override
	public boolean hasNext() {
		if (cursor != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String next() {
		String value = "";
		if (!hasNext()) {
			throw new NoSuchElementException();
		} else {
			currentNode = cursor;
			value = cursor.value;
			index++;
		}
		lastCallNext = true;
		lastCallPrevious = false;
		lastCallAdd = false;
		lastCallRemove = false;
		cursor = cursor.next;
		return value;
	}

	@Override
	public boolean hasPrevious() {
		if (currentNode != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String previous() {
		String value = "";
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		} else if (lastCallAdd == true) {
			index--;
			lastCallNext = false;
			lastCallPrevious = true;
			lastCallAdd = false;
			lastCallRemove = false;
			return currentNode.next.value;
		} else {
			value = currentNode.value;
			index--;
			cursor = currentNode;
			currentNode = cursor.previous;
			lastCallNext = false;
			lastCallPrevious = true;
			lastCallAdd = false;
			lastCallRemove = false;
			return value;
		}
	}

	@Override
	public int nextIndex() {
		return index;
	}

	@Override
	public int previousIndex() {
		return index - 1;
	}

	@Override
	public void remove() {
		if (currentNode == null) throw new IllegalStateException();
		Node x = currentNode.previous;
		Node y = currentNode.next;
		x.next = y;
		y.previous = x;
		size--;
		
		if (cursor == currentNode)
			cursor = y;
		else {
			index--;
		}
		
		currentNode = null;
		lastCallNext = false;
		lastCallPrevious = false;
		lastCallAdd = false;
		lastCallRemove = true;
	}

	@Override
	public void set(String e) {
		if (lastCallAdd == true || lastCallRemove == true) {
			throw new IllegalStateException();
		} else if (lastCallNext == true) {
			set(previousIndex(), e);
			lastCallNext = false;
			lastCallPrevious = false;
			lastCallAdd = false;
			lastCallRemove = false;
		} else if (lastCallNext == false) {
			set(nextIndex(), e);
			lastCallNext = false;
			lastCallPrevious = false;
			lastCallAdd = false;
			lastCallRemove = false;
		}
	}

	@Override
	public void add(String e) {
		if (lastCallNext == true && lastCallPrevious == false) {
			add(index, e);
			index++;
		} else if (lastCallPrevious == true && lastCallNext == false) {
			add(index, e);
			index++;
		} else {
			addAtEnd(e);
		}
		lastCallAdd = true;
	}

	public ListIterator listIterator() {
		return this;
	}
}

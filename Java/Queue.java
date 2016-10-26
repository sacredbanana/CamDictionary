package project;

public class Queue<E> {
	private QueueItem<E> front;
	
	private QueueItem<E> back;	

	public Queue() {
		front = null;
		back = null;
	}
	
	public E dequeue() {
		if (!isEmpty()) {
			E item = front.item;
			front = front.next;
			if (isEmpty())
				back = null;
			return item;
		} else
			return null;
	}

	public void enqueue(E item) {
		if (isEmpty()) {
			front = new QueueItem<E>(item, null);
			back = front;
		} else {
			back.next = new QueueItem<E>(item, null);
			back = back.next;
		}
	}

	public Object examine() {
		if (!isEmpty())
			return front.item;
		else
			return null;
	}

	public boolean isEmpty() {
		return front == null;
	}
	
	@SuppressWarnings("hiding")
	private class QueueItem<E> {
		public E item;
		public QueueItem<E> next;
		
		public QueueItem(E item, QueueItem<E> next) {
			this.item = item;
			this.next = next;
		}
	}
}

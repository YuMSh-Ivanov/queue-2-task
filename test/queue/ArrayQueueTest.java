package queue;

import org.junit.Test;

import java.util.LinkedList;

public class ArrayQueueTest extends ArrayQueueBaseTest {
    @Test
    public void testArrayQueue() {
        test(ArrayQueue::new, LinkedList::new);
    }

    @Test
    public void testLinkedQueue() {
        test(LinkedQueue::new, LinkedList::new);
    }
}

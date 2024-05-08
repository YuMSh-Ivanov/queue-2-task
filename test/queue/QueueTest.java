package queue;

import org.junit.Test;

import java.util.ArrayDeque;

public class QueueTest extends QueueBaseTest<ArrayDeque<Object>> {
    @Test
    public void testArrayQueue() {
        test(ArrayQueue::new, ArrayDeque::new);
    }

    @Test
    public void testLinkedQueue() {
        test(LinkedQueue::new, ArrayDeque::new);
    }
}

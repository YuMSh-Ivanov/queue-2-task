package queue;

import base.pairs.Pair;
import org.junit.Assert;

import java.util.*;
import java.util.function.Supplier;

public class QueueBaseTest<C extends java.util.Queue<Object>> {
    protected final Random random = new Random(3447982348967759878L);

    private void testEmpty(final Queue queue) {
        assertSize(0, queue);
    }

    private void testSingleton(final Queue queue) {
        assertSize(0, queue);
        final String value = "value";
        queue.addLast(value);
        assertSize(1, queue);
        Assert.assertEquals("element()", value, queue.getFirst());
        Assert.assertEquals("dequeue()", value, queue.removeFirst());
        assertSize(0, queue);
    }

    private void testClear(final Queue queue) {
        assertSize(0, queue);

        final String value = "value";
        queue.addLast(value);
        queue.addLast(value);
        queue.clear();
        assertSize(0, queue);

        final String value1 = "value1";
        queue.addLast(value1);
        Assert.assertEquals("deque()", value1, queue.removeFirst());
    }

    protected Object randomElement() {
        final Object[] ELEMENTS = new Object[]{
                "Hello",
                "world",
                1, 2, 3,
                List.of("a"),
                List.of("a"),
                List.of("b"),
                Map.of()
        };

        return ELEMENTS[random.nextInt(ELEMENTS.length)];
    }

    protected void otherOperations(final List<Pair<Queue, C>> queues, final Pair<Queue, C> pair) {
    }

    private void moreQueues(final List<Pair<Queue, C>> queues, final Pair<Queue, C> pair) {
        if (random.nextInt(50) == 0) {
            pair.first().clear();
            pair.second().clear();
        } else {
            otherOperations(queues, pair);
        }
    }

    private void testRandom(final Pair<Queue, C> initial, final double addFreq) {
        final List<Pair<Queue, C>> queues = new ArrayList<>();
        queues.add(initial);
        int ops = 0;
        for (int i = 0; i < 50_000; i++) {
            final Pair<Queue, C> pair = queues.get(random.nextInt(queues.size()));

            boolean isEmpty = pair.second().isEmpty();
            Assert.assertEquals("isEmpty for " + pair.first(), isEmpty, pair.first().isEmpty());
            if (isEmpty || random.nextDouble() < addFreq) {
                final Object value = randomElement();
                pair.first().addLast(value);
                pair.second().add(value);
            } else {
                Assert.assertEquals("removeFirst() for " + pair.first(), pair.second().remove(), pair.first().removeFirst());
            }

            final int size = pair.second().size();
            assertSize(size, pair.first());

            if (ops++ >= size && random.nextInt(4) == 0) {
                ops -= size;

                moreQueues(queues, pair);
                assertSize(pair.second().size(), pair.first());
            }
        }

        for (int j = 0; j < queues.size(); j++) {
            final Pair<Queue, C> pair = queues.get(j);
            moreQueues(queues, pair);
            final int size = pair.second().size();
            assertSize(size, pair.first());
            for (int i = size; i > 0; i--) {
                Assert.assertEquals("removeFirst() for " + pair.first(), pair.second().remove(), pair.first().removeFirst());
                assertSize(pair.second().size(), pair.first());
            }
        }
    }

    private void assertSize(final int size, final Queue queue) {
        Assert.assertEquals("size() of " + queue, size, queue.size());
        Assert.assertEquals("isEmpty() of " + queue, size == 0, queue.isEmpty());
    }

    protected void test(final Supplier<AbstractQueue> queueActual, final Supplier<C> queueExpected) {
        testEmpty(queueActual.get());
        testSingleton(queueActual.get());
        testClear(queueActual.get());
        testRandom(Pair.of(queueActual.get(), queueExpected.get()), 0.0);
        testRandom(Pair.of(queueActual.get(), queueExpected.get()), 0.2);
        testRandom(Pair.of(queueActual.get(), queueExpected.get()), 0.4);
        testRandom(Pair.of(queueActual.get(), queueExpected.get()), 0.6);
        testRandom(Pair.of(queueActual.get(), queueExpected.get()), 0.8);
        testRandom(Pair.of(queueActual.get(), queueExpected.get()), 1.0);
    }
}

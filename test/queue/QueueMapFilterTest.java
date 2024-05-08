package queue;

import base.pairs.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class QueueMapFilterTest extends QueueBaseTest<LinkedList<Object>> {

    private static <T, L extends List<T>> Collector<T, L, L> toList(final Supplier<L> listSupplier) {
        return new Collector<>() {

            @Override
            public Supplier<L> supplier() {
                return listSupplier;
            }

            @Override
            public BiConsumer<L, T> accumulator() {
                return List::add;
            }

            @Override
            public BinaryOperator<L> combiner() {
                return (left, right) -> {
                    left.addAll(right);
                    return left;
                };
            }

            @Override
            public Function<L, L> finisher() {
                return Function.identity();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
            }
        };
    }

    @Override
    protected void otherOperations(final List<Pair<Queue, LinkedList<Object>>> queues, final Pair<Queue, LinkedList<Object>> pair) {
        final Pair<Queue, LinkedList<Object>> newPair;
        if (random.nextBoolean()) {
            // Choosing predicate.
            final Object randomElement = randomElement();
            final Predicate<Object> predicate = random.nextBoolean() ? Predicate.isEqual(randomElement) : randomElement.getClass()::isInstance;
            newPair = Pair.of(pair.first().filter(predicate), pair.second().stream().filter(predicate).collect(toList(LinkedList::new)));
            Assert.assertEquals("Result of filter has different type than original", pair.first().getClass(), newPair.first().getClass());
        } else {
            // Choosing function.
            final Function<Object, Object> function = random.nextBoolean() ? String::valueOf : Object::hashCode;
            newPair = Pair.of(pair.first().map(function), pair.second().stream().map(function).collect(toList(LinkedList::new)));
            Assert.assertEquals("Result of map has different type than original", pair.first().getClass(), newPair.first().getClass());
        }
        queues.add(newPair);
    }

    @Test
    public void testArrayQueue() {
        test(ArrayQueue::new, LinkedList::new);
    }

    @Test
    public void testLinkedQueue() {
        test(LinkedQueue::new, LinkedList::new);
    }
}

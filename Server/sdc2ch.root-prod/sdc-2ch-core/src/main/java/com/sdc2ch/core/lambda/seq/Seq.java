package com.sdc2ch.core.lambda.seq;

import static com.sdc2ch.core.lambda.tuple.Tuple.tuple;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.sdc2ch.core.lambda.tuple.Tuple;
import com.sdc2ch.core.lambda.tuple.Tuple2;


public interface Seq<T> extends Stream<T> {

	
	Stream<T> stream();

	
	@SuppressWarnings("unchecked")
	default Seq<T> concat(Stream<T> other) {
		return Seq.concat(new Stream[] { this, other });
	}

	
	default Seq<T> cycle() {
		return cycle(this);
	}

	
	default <U> Seq<Tuple2<T, U>> zip(Seq<U> other) {
		return zip(this, other);
	}

	
	default <U, R> Seq<R> zip(Seq<U> other, BiFunction<T, U, R> zipper) {
		return zip(this, other, zipper);
	}

	
	default Seq<Tuple2<T, Long>> zipWithIndex() {
		return zipWithIndex(this);
	}

	
	default <U> U foldLeft(U seed, BiFunction<U, ? super T, U> function) {
		return foldLeft(this, seed, function);
	}

	
	default <U> U foldRight(U seed, BiFunction<? super T, U, U> function) {
		return foldRight(this, seed, function);
	}

	
	default Seq<T> reverse() {
		return reverse(this);
	}

	
	default Seq<T> onEmpty(T value) {
		return onEmptyGet(() -> value);
	}

	
	default Seq<T> onEmptyGet(Supplier<? extends T> supplier) {
		boolean[] first = { true };

		return SeqUtils.transform(this, (delegate, action) -> {
			if (first[0]) {
				first[0] = false;

				if (!delegate.tryAdvance(action))
					action.accept(supplier.get());

				return true;
			} else {
				return delegate.tryAdvance(action);
			}
		});
	}

	
	default Seq<T> skipWhile(Predicate<? super T> predicate) {
		return skipWhile(this, predicate);
	}

	
	default Seq<T> skipUntil(Predicate<? super T> predicate) {
		return skipUntil(this, predicate);
	}

	
	default Seq<T> limitWhile(Predicate<? super T> predicate) {
		return limitWhile(this, predicate);
	}

	
	default Seq<T> limitUntil(Predicate<? super T> predicate) {
		return limitUntil(this, predicate);
	}

	
	default Tuple2<Seq<T>, Seq<T>> duplicate() {
		return duplicate(this);
	}

	
	default Tuple2<Seq<T>, Seq<T>> partition(Predicate<? super T> predicate) {
		return partition(this, predicate);
	}

	
	default Tuple2<Seq<T>, Seq<T>> splitAt(long position) {
		return splitAt(this, position);
	}

	
	default Seq<T> slice(long from, long to) {
		return slice(this, from, to);
	}

	
	default List<T> toList() {
		return toList(this);
	}

	
	default Set<T> toSet() {
		return toSet(this);
	}

	
	default String toString(String separator) {
		return toString(this, separator);
	}

	
	default <U extends Comparable<U>> Optional<T> minBy(Function<T, U> function) {
		return minBy(function, naturalOrder());
	}

	
	default <U> Optional<T> minBy(Function<T, U> function, Comparator<? super U> comparator) {
		return map(t -> tuple(t, function.apply(t))).min(comparing(Tuple2::v2, comparator)).map(t -> t.v1);
	}

	
	default <U extends Comparable<U>> Optional<T> maxBy(Function<T, U> function) {
		return maxBy(function, naturalOrder());
	}

	
	default <U> Optional<T> maxBy(Function<T, U> function, Comparator<? super U> comparator) {
		return map(t -> tuple(t, function.apply(t))).max(comparing(Tuple2::v2, comparator)).map(t -> t.v1);
	}

	
	static <T> Seq<T> of(T value) {
		return seq(Stream.of(value));
	}

	
	@SuppressWarnings("unchecked")
	static <T> Seq<T> of(T... values) {
		return seq(Stream.of(values));
	}
	static <T> Seq<T> of(Iterable<T> iterable) {
		return seq(iterable);
	}

	
	static <T> Seq<T> empty() {
		return seq(Stream.empty());
	}

	
	static <T> Seq<T> iterate(final T seed, final UnaryOperator<T> f) {
		return seq(Stream.iterate(seed, f));
	}

	
	static <T> Seq<T> generate(Supplier<T> s) {
		return seq(Stream.generate(s));
	}

	
	static <T> Seq<T> seq(Stream<T> stream) {
		
		if(stream == null)
			return Seq.empty();
		
		if (stream instanceof Seq)
			return (Seq<T>) stream;

		return new SeqImpl<>(stream);
	}

	
	static <T> Seq<T> seq(Iterable<T> iterable) {
		return seq(iterable.iterator());
	}

	
	static <T> Seq<T> seq(Iterator<T> iterator) {
		return seq(StreamSupport.stream(spliteratorUnknownSize(iterator, ORDERED), false));
	}

	
	static <T> Seq<T> seq(Spliterator<T> spliterator) {
		if (spliterator == null)
			return Seq.empty();

		return seq(StreamSupport.stream(spliterator, false));
	}

	
	static <T> Seq<T> cycle(Stream<T> stream) {
		final List<T> list = new ArrayList<T>();

		class Cycle implements Iterator<T> {
			boolean cycled;
			Iterator<T> it;

			Cycle(Iterator<T> it) {
				this.it = it;
			}

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public T next() {
				if (!it.hasNext()) {
					cycled = true;
					it = list.iterator();
				}

				T next = it.next();

				if (!cycled) {
					list.add(next);
				}

				return next;
			}
		}

		return seq(new Cycle(stream.iterator()));
	}

	
	static <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Stream<Tuple2<T1, T2>> stream) {
		return unzip(stream, t -> t);
	}

	
	static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream, Function<T1, U1> leftUnzipper,
			Function<T2, U2> rightUnzipper) {
		return unzip(stream, t -> tuple(leftUnzipper.apply(t.v1), rightUnzipper.apply(t.v2)));
	}

	
	static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream,
			Function<Tuple2<T1, T2>, Tuple2<U1, U2>> unzipper) {
		return unzip(stream, (t1, t2) -> unzipper.apply(tuple(t1, t2)));
	}

	
	static <T1, T2, U1, U2> Tuple2<Seq<U1>, Seq<U2>> unzip(Stream<Tuple2<T1, T2>> stream,
			BiFunction<T1, T2, Tuple2<U1, U2>> unzipper) {
		return seq(stream).map(t -> unzipper.apply(t.v1, t.v2)).duplicate().map1(s -> s.map(u -> u.v1))
				.map2(s -> s.map(u -> u.v2));
	}

	
	static <T1, T2> Seq<Tuple2<T1, T2>> zip(Stream<T1> left, Stream<T2> right) {
		return zip(left, right, Tuple::tuple);
	}

	
	static <T1, T2, R> Seq<R> zip(Stream<T1> left, Stream<T2> right, BiFunction<T1, T2, R> zipper) {
		final Iterator<T1> it1 = left.iterator();
		final Iterator<T2> it2 = right.iterator();

		class Zip implements Iterator<R> {
			@Override
			public boolean hasNext() {
				return it1.hasNext() && it2.hasNext();
			}

			@Override
			public R next() {
				return zipper.apply(it1.next(), it2.next());
			}
		}

		return seq(new Zip());
	}

	
	static <T> Seq<Tuple2<T, Long>> zipWithIndex(Stream<T> stream) {
		final Iterator<T> it = stream.iterator();

		class ZipWithIndex implements Iterator<Tuple2<T, Long>> {
			long index;

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Tuple2<T, Long> next() {
				return tuple(it.next(), index++);
			}
		}

		return seq(new ZipWithIndex());
	}

	
	static <T, U> U foldLeft(Stream<T> stream, U seed, BiFunction<U, ? super T, U> function) {
		final Iterator<T> it = stream.iterator();
		U result = seed;

		while (it.hasNext())
			result = function.apply(result, it.next());

		return result;
	}

	
	static <T, U> U foldRight(Stream<T> stream, U seed, BiFunction<? super T, U, U> function) {
		return seq(stream).reverse().foldLeft(seed, (u, t) -> function.apply(t, u));
	}

	
	static <T, U> Seq<T> unfold(U seed, Function<U, Optional<Tuple2<T, U>>> unfolder) {
		class Unfold implements Iterator<T> {
			U u;
			Optional<Tuple2<T, U>> unfolded;

			public Unfold(U u) {
				this.u = u;
			}

			void unfold() {
				if (unfolded == null)
					unfolded = unfolder.apply(u);
			}

			@Override
			public boolean hasNext() {
				unfold();
				return unfolded.isPresent();
			}

			@Override
			public T next() {
				unfold();

				try {
					return unfolded.get().v1;
				} finally {
					u = unfolded.get().v2;
					unfolded = null;
				}
			}
		}

		return seq(new Unfold(seed));
	}

	
	static <T> Seq<T> reverse(Stream<T> stream) {
		List<T> list = toList(stream);
		Collections.reverse(list);
		return seq(list);
	}

	
	@SafeVarargs
	static <T> Seq<T> concat(Stream<T>... streams) {
		if (streams == null || streams.length == 0)
			return Seq.empty();

		if (streams.length == 1)
			return seq(streams[0]);

		Stream<T> result = streams[0];
		for (int i = 1; i < streams.length; i++)
			result = Stream.concat(result, streams[i]);

		return seq(result);
	}

	
	static <T> Tuple2<Seq<T>, Seq<T>> duplicate(Stream<T> stream) {
		final LinkedList<T> gap = new LinkedList<>();
		final Iterator<T> it = stream.iterator();

		@SuppressWarnings("unchecked")
		final Iterator<T>[] ahead = new Iterator[] { null };

		class Duplicate implements Iterator<T> {
			@Override
			public boolean hasNext() {
				synchronized (it) {
					if (ahead[0] == null || ahead[0] == this)
						return it.hasNext();

					return !gap.isEmpty();
				}
			}

			@Override
			public T next() {
				synchronized (it) {
					if (ahead[0] == null)
						ahead[0] = this;

					if (ahead[0] == this) {
						T value = it.next();
						gap.offer(value);
						return value;
					}

					return gap.poll();
				}
			}
		}

		return tuple(seq(new Duplicate()), seq(new Duplicate()));
	}

	
	static String toString(Stream<?> stream) {
		return toString(stream, "");
	}

	
	static String toString(Stream<?> stream, String separator) {
		return stream.map(Objects::toString).collect(Collectors.joining(separator));
	}

	
	static <T> List<T> toList(Stream<T> stream) {
		return stream.collect(Collectors.toList());
	}

	
	static <T> Set<T> toSet(Stream<T> stream) {
		return stream.collect(Collectors.toSet());
	}

	
	static <T> Seq<T> slice(Stream<T> stream, long from, long to) {
		long f = Math.max(from, 0);
		long t = Math.max(to - f, 0);

		return seq(stream.skip(f).limit(t));
	}

	
	static <T> Seq<T> skip(Stream<T> stream, long elements) {
		return seq(stream.skip(elements));
	}

	
	static <T> Seq<T> skipWhile(Stream<T> stream, Predicate<? super T> predicate) {
		return skipUntil(stream, predicate.negate());
	}

	
	static <T> Seq<T> skipUntil(Stream<T> stream, Predicate<? super T> predicate) {
		final Iterator<T> it = stream.iterator();

		class SkipUntil implements Iterator<T> {
			T next;
			boolean test = false;

			void skip() {
				while (next == null && it.hasNext()) {
					next = it.next();

					if (test || (test = predicate.test(next)))
						break;
					else
						next = null;
				}
			}

			@Override
			public boolean hasNext() {
				skip();
				return next != null;
			}

			@Override
			public T next() {
				if (next == null)
					throw new NoSuchElementException();

				try {
					return next;
				} finally {
					next = null;
				}
			}
		}

		return seq(new SkipUntil());
	}

	
	static <T> Seq<T> limit(Stream<T> stream, long elements) {
		return seq(stream.limit(elements));
	}

	
	static <T> Seq<T> limitWhile(Stream<T> stream, Predicate<? super T> predicate) {
		return limitUntil(stream, predicate.negate());
	}

	
	static <T> Seq<T> limitUntil(Stream<T> stream, Predicate<? super T> predicate) {
		final Iterator<T> it = stream.iterator();

		class LimitUntil implements Iterator<T> {
			T next;
			boolean test = false;

			void test() {
				if (!test && next == null && it.hasNext()) {
					next = it.next();

					if (test = predicate.test(next))
						next = null;
				}
			}

			@Override
			public boolean hasNext() {
				test();
				return next != null;
			}

			@Override
			public T next() {
				if (next == null)
					throw new NoSuchElementException();

				try {
					return next;
				} finally {
					next = null;
				}
			}
		}

		return seq(new LimitUntil());
	}

	
	static <T> Tuple2<Seq<T>, Seq<T>> partition(Stream<T> stream, Predicate<? super T> predicate) {
		final Iterator<T> it = stream.iterator();
		final LinkedList<T> buffer1 = new LinkedList<>();
		final LinkedList<T> buffer2 = new LinkedList<>();

		class Partition implements Iterator<T> {

			final boolean b;

			Partition(boolean b) {
				this.b = b;
			}

			void fetch() {
				while (buffer(b).isEmpty() && it.hasNext()) {
					T next = it.next();
					buffer(predicate.test(next)).offer(next);
				}
			}

			LinkedList<T> buffer(boolean test) {
				return test ? buffer1 : buffer2;
			}

			@Override
			public boolean hasNext() {
				fetch();
				return !buffer(b).isEmpty();
			}

			@Override
			public T next() {
				return buffer(b).poll();
			}
		}

		return tuple(seq(new Partition(true)), seq(new Partition(false)));
	}

	
	static <T> Tuple2<Seq<T>, Seq<T>> splitAt(Stream<T> stream, long position) {
		return seq(stream).zipWithIndex().partition(t -> t.v2 < position)
				.map((v1, v2) -> tuple(v1.map(t -> t.v1), v2.map(t -> t.v1)));
	}

	
	

	@Override
	Seq<T> filter(Predicate<? super T> predicate);

	@Override
	<R> Seq<R> map(Function<? super T, ? extends R> mapper);

	@Override
	IntStream mapToInt(ToIntFunction<? super T> mapper);

	@Override
	LongStream mapToLong(ToLongFunction<? super T> mapper);

	@Override
	DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);

	@Override
	<R> Seq<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

	@Override
	IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);

	@Override
	LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper);

	@Override
	DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper);

	@Override
	Seq<T> distinct();

	@Override
	Seq<T> sorted();

	@Override
	Seq<T> sorted(Comparator<? super T> comparator);

	@Override
	Seq<T> peek(Consumer<? super T> action);

	@Override
	Seq<T> limit(long maxSize);

	@Override
	Seq<T> skip(long n);

	@Override
	Seq<T> onClose(Runnable closeHandler);

	@Override
	void close();

	
	

	@Override
	default Seq<T> sequential() {
		return this;
	}

	@Override
	default Seq<T> parallel() {
		return this;
	}

	@Override
	default Seq<T> unordered() {
		return this;
	}
}

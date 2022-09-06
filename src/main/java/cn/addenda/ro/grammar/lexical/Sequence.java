package cn.addenda.ro.grammar.lexical;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author addenda
 * @datetime 2021/2/24 11:15
 */
public abstract class Sequence<T, I> {

    protected T source;
    protected int length;

    protected final I END_ITEM;

    /**
     * last：    上一次扫描到的索引的位置。起始为 -1
     * current： 下次扫描索要扫描的索引位置.起始为 0
     */
    protected int last = -1;
    protected int current = 0;

    protected Sequence(T source, I endItem) {
        this.source = source;
        this.length = length();
        this.END_ITEM = endItem;
    }

    public void recordLast() {
        last = current - 1;
    }


    public boolean curEqual(I expected) {
        return equalSequenceItem(takeCur(), expected);
    }

    public boolean preEqual(I expected) {
        return equalSequenceItem(takePre(), expected);
    }

    public boolean nextEqual(I expected) {
        return equalSequenceItem(takeNext(), expected);
    }

    public boolean equalThenAdvance(I expected) {
        if (equalSequenceItem(takeCur(), expected)) {
            advance();
            return true;
        }
        return false;
    }

    public boolean curTest(Predicate<I> predicate) {
        if (isAtEnd()) {
            return false;
        }
        return predicate.test(takeCur());
    }

    public boolean nextTest(Predicate<I> predicate) {
        if (isAtEnd()) {
            return false;
        }
        return predicate.test(takeNext());
    }

    public boolean preTest(Predicate<I> predicate) {
        if (isAtEnd()) {
            return false;
        }
        return predicate.test(takePre());
    }

    /**
     * current 永远指向待处理的token
     */
    public boolean isAtEnd() {
        return current >= length;
    }

    /**
     * 存在返回true，不存在返回false
     */
    public boolean checkExistsToken() {
        return !indexOf(current).equals(END_ITEM);
    }

    public boolean advance() {
        if (!isAtEnd()) {
            current++;
            return true;
        }
        return false;
    }

    public I takeCur() {
        if (isAtEnd()) {
            return END_ITEM;
        }
        return indexOf(current);
    }

    public I takeNext() {
        if (current + 1 >= length) {
            return END_ITEM;
        }
        return indexOf(current + 1);
    }

    public I takePre() {
        if (current == 0) {
            throw new SequenceException("Current index is 0, these is no leading character！");
        }
        return indexOf(current - 1);
    }

    public int getCurrent() {
        return current;
    }

    protected abstract I indexOf(int index);

    protected abstract int length();

    protected abstract boolean equalSequenceItem(I c1, I c2);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sequence<?, ?> sequence = (Sequence<?, ?>) o;
        if (length != sequence.length) {
            return false;
        }
        Object thatSource = sequence.source;
        if (!Objects.equals(thatSource.getClass(), source.getClass())) {
            return false;
        }
        return doEquals((T) thatSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doHashCode(), length);
    }

    protected abstract boolean doEquals(T source);

    protected abstract int doHashCode();
}

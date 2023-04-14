package future;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class ArraySeq <T> extends ArrayList<T> implements Seq<T>{
    @Override
    public void consume(final Consumer<T> consumer) {
        forEach(consumer);
    }
}



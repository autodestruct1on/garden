package gg.cristalix.growagarden.util.function;

@FunctionalInterface
public interface ThrowableConsumer<T> {

  void accept(T t) throws Throwable;

}

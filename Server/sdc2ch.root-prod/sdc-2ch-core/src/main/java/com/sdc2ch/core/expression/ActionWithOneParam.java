package com.sdc2ch.core.expression;

public interface ActionWithOneParam<T,U> {
    T apply(U param) throws Exception;
}

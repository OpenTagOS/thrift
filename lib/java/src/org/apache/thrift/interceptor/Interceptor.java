package org.apache.thrift.interceptor;


public interface Interceptor<I> {
    void call(InterceptionData interceptionData);
}

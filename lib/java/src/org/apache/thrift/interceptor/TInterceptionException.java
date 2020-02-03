package org.apache.thrift.interceptor;

import org.apache.thrift.TException;

public class TInterceptionException extends Exception {
    public TInterceptionException(String message) {
        super(message);
    }
}

package com.company.net;

import java.util.concurrent.Future;

/**
 * Created by pc on 2017/7/17.
 */
public interface IoFuture<V> extends Future<V> {

    Response read();
    void write(Request request);

}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.thrift;

import junit.framework.TestCase;
import org.apache.thrift.interceptor.TInterceptionData;
import org.apache.thrift.interceptor.TInterceptor;
import org.apache.thrift.protocol.TProtocol;

public class TestMultiplexedProcessorInterceptor extends TestCase {

    private TMultiplexedProcessor multiplexedProcessor;

    public void testProcessorWithInterceptorFirst(){
        multiplexedProcessor = new TMultiplexedProcessor();

        TInterceptor interceptor = new StubTInterceptor();
        multiplexedProcessor.registerInterceptor(interceptor);

        StubProcessor processor = new StubProcessor();
        multiplexedProcessor.registerProcessor("service", processor);
        assertEquals(interceptor, processor.interceptor);
    }

    public void testProcessorWithProcessorFirst(){
        multiplexedProcessor = new TMultiplexedProcessor();

        StubProcessor processor = new StubProcessor();
        multiplexedProcessor.registerProcessor("service", processor);

        TInterceptor interceptor = new StubTInterceptor();
        multiplexedProcessor.registerInterceptor(interceptor);

        assertEquals(interceptor, processor.interceptor);
    }

    public void testProcessorWithoutInterceptor(){
        multiplexedProcessor = new TMultiplexedProcessor();

        StubProcessor processor = new StubProcessor();
        multiplexedProcessor.registerProcessor("service", processor);

        assertNull(processor.interceptor);
    }

    static class StubTInterceptor implements TInterceptor {

        @Override
        public void call(TInterceptionData interceptionData) {
        }
    }

    static class StubProcessor implements TProcessor {

        TInterceptor interceptor;

        @Override
        public void process(TProtocol in, TProtocol out) throws TException {
        }

        @Override
        public void registerInterceptor(TInterceptor interceptor) {
            this.interceptor = interceptor;
        }
    }
}

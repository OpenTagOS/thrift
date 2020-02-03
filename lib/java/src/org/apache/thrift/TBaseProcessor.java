package org.apache.thrift;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.thrift.interceptor.Interceptor;
import org.apache.thrift.interceptor.InterceptionData;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolUtil;
import org.apache.thrift.protocol.TType;

public abstract class TBaseProcessor<I> implements TProcessor {
    private final I iface;
    private final List<Interceptor> interceptors;
    private final Map<String, ProcessFunction<I, ? extends TBase>> processMap;

    protected TBaseProcessor(I iface, Map<String, ProcessFunction<I, ? extends TBase>> processFunctionMap) {
        this.iface = iface;
        this.processMap = processFunctionMap;
        this.interceptors = null;
    }

    protected TBaseProcessor(I iface, List<Interceptor> interceptors, Map<String, ProcessFunction<I, ? extends TBase>> processFunctionMap) {
        this.iface = iface;
        this.processMap = processFunctionMap;
        this.interceptors = Collections.unmodifiableList(interceptors);
    }

    public Map<String, ProcessFunction<I, ? extends TBase>> getProcessMapView() {
        return Collections.unmodifiableMap(processMap);
    }

    @Override
    public void process(TProtocol in, TProtocol out) throws TException {
        long startExecutionTime = System.nanoTime();

        InterceptionData.Builder processDataBuilder = InterceptionData.newBuilder();
        processDataBuilder.
                startExecutionTime(startExecutionTime).
                iface(iface);

        try {
            TMessage msg = in.readMessageBegin();
            ProcessFunction fn = processMap.get(msg.name);

            if (fn == null) {
                TProtocolUtil.skip(in, TType.STRUCT);
                in.readMessageEnd();
                TApplicationException ex = new TApplicationException(TApplicationException.UNKNOWN_METHOD, "Invalid method name: '" + msg.name + "'");
                processDataBuilder.exception(ex);
                out.writeMessageBegin(new TMessage(msg.name, TMessageType.EXCEPTION, msg.seqid));
                ex.write(out);
                out.writeMessageEnd();
                out.getTransport().flush();
            } else {
                fn.process(msg.seqid, in, out, iface);

                processDataBuilder.processFunction(fn);
                this.processInterceptors(processDataBuilder.build());
            }
        } catch (TException e) {
            processDataBuilder.exception(e);
            this.processInterceptors(processDataBuilder.build());

            throw e;
        }
    }

    private void processInterceptors(InterceptionData processData) {
        if (Objects.isNull(interceptors)) {
            return;
        }

        for (Interceptor interceptor : interceptors) {
            interceptor.call(processData);
        }
    }
}

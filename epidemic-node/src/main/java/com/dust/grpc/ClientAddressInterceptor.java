package com.dust.grpc;

import io.grpc.*;

import java.net.SocketAddress;
import java.util.Objects;

public class ClientAddressInterceptor implements ServerInterceptor {

    public static final Context.Key<String> CLIENT_ADDRESS = Context.key("client-address");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        var clientAddress = serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
        if (Objects.isNull(clientAddress)) {
            return Contexts.interceptCall(Context.current().withValue(CLIENT_ADDRESS, "none"), serverCall, metadata, serverCallHandler);
        }
        var address = clientAddress.toString();
        var host = address.substring(1, address.indexOf(":"));
        return Contexts.interceptCall(Context.current().withValue(CLIENT_ADDRESS, host), serverCall, metadata, serverCallHandler);
    }
}

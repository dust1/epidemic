package com.dust.grpc;

import io.grpc.*;

import java.net.SocketAddress;

public class ClientAddressInterceptor implements ServerInterceptor {

    public static final Context.Key<SocketAddress> CLIENT_ADDRESS = Context.key("client-address");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        SocketAddress clientAddress = serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
        return Contexts.interceptCall(Context.current().withValue(CLIENT_ADDRESS, clientAddress), serverCall, metadata, serverCallHandler);
    }
}

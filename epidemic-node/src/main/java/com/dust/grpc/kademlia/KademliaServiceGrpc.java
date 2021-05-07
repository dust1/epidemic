package com.dust.grpc.kademlia;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.*;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.*;

/**
 * <pre>
 * 定义的一个服务
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.23.0)",
    comments = "Source: route_guide.proto")
public final class KademliaServiceGrpc {

  private KademliaServiceGrpc() {}

  public static final String SERVICE_NAME = "KademliaService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<PingPackage,
      PingPackage> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = PingPackage.class,
      responseType = PingPackage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<PingPackage,
      PingPackage> getPingMethod() {
    io.grpc.MethodDescriptor<PingPackage, PingPackage> getPingMethod;
    if ((getPingMethod = KademliaServiceGrpc.getPingMethod) == null) {
      synchronized (KademliaServiceGrpc.class) {
        if ((getPingMethod = KademliaServiceGrpc.getPingMethod) == null) {
          KademliaServiceGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<PingPackage, PingPackage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  PingPackage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  PingPackage.getDefaultInstance()))
              .setSchemaDescriptor(new KademliaServiceMethodDescriptorSupplier("Ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<StoreRequest,
      StoreResponse> getStoreMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Store",
      requestType = StoreRequest.class,
      responseType = StoreResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<StoreRequest,
      StoreResponse> getStoreMethod() {
    io.grpc.MethodDescriptor<StoreRequest, StoreResponse> getStoreMethod;
    if ((getStoreMethod = KademliaServiceGrpc.getStoreMethod) == null) {
      synchronized (KademliaServiceGrpc.class) {
        if ((getStoreMethod = KademliaServiceGrpc.getStoreMethod) == null) {
          KademliaServiceGrpc.getStoreMethod = getStoreMethod =
              io.grpc.MethodDescriptor.<StoreRequest, StoreResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Store"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  StoreRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  StoreResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KademliaServiceMethodDescriptorSupplier("Store"))
              .build();
        }
      }
    }
    return getStoreMethod;
  }

  private static volatile io.grpc.MethodDescriptor<FindRequest,
      FindNodeResponse> getFindNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FindNode",
      requestType = FindRequest.class,
      responseType = FindNodeResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<FindRequest,
      FindNodeResponse> getFindNodeMethod() {
    io.grpc.MethodDescriptor<FindRequest, FindNodeResponse> getFindNodeMethod;
    if ((getFindNodeMethod = KademliaServiceGrpc.getFindNodeMethod) == null) {
      synchronized (KademliaServiceGrpc.class) {
        if ((getFindNodeMethod = KademliaServiceGrpc.getFindNodeMethod) == null) {
          KademliaServiceGrpc.getFindNodeMethod = getFindNodeMethod =
              io.grpc.MethodDescriptor.<FindRequest, FindNodeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FindNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  FindRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  FindNodeResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KademliaServiceMethodDescriptorSupplier("FindNode"))
              .build();
        }
      }
    }
    return getFindNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<FindRequest,
      FindValueResponse> getFindValueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findValue",
      requestType = FindRequest.class,
      responseType = FindValueResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<FindRequest,
      FindValueResponse> getFindValueMethod() {
    io.grpc.MethodDescriptor<FindRequest, FindValueResponse> getFindValueMethod;
    if ((getFindValueMethod = KademliaServiceGrpc.getFindValueMethod) == null) {
      synchronized (KademliaServiceGrpc.class) {
        if ((getFindValueMethod = KademliaServiceGrpc.getFindValueMethod) == null) {
          KademliaServiceGrpc.getFindValueMethod = getFindValueMethod =
              io.grpc.MethodDescriptor.<FindRequest, FindValueResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "findValue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  FindRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  FindValueResponse.getDefaultInstance()))
              .setSchemaDescriptor(new KademliaServiceMethodDescriptorSupplier("findValue"))
              .build();
        }
      }
    }
    return getFindValueMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static KademliaServiceStub newStub(io.grpc.Channel channel) {
    return new KademliaServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static KademliaServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new KademliaServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static KademliaServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new KademliaServiceFutureStub(channel);
  }

  /**
   * <pre>
   * 定义的一个服务
   * </pre>
   */
  public static abstract class KademliaServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Ping函数
     * </pre>
     */
    public void ping(PingPackage request,
                     io.grpc.stub.StreamObserver<PingPackage> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     * <pre>
     * 存储
     * </pre>
     */
    public void store(StoreRequest request,
                      io.grpc.stub.StreamObserver<StoreResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStoreMethod(), responseObserver);
    }

    /**
     * <pre>
     * 寻找节点
     * </pre>
     */
    public void findNode(FindRequest request,
                         io.grpc.stub.StreamObserver<FindNodeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFindNodeMethod(), responseObserver);
    }

    /**
     * <pre>
     * 寻找值
     * </pre>
     */
    public void findValue(FindRequest request,
                          io.grpc.stub.StreamObserver<FindValueResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFindValueMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                PingPackage,
                PingPackage>(
                  this, METHODID_PING)))
          .addMethod(
            getStoreMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                StoreRequest,
                StoreResponse>(
                  this, METHODID_STORE)))
          .addMethod(
            getFindNodeMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                FindRequest,
                FindNodeResponse>(
                  this, METHODID_FIND_NODE)))
          .addMethod(
            getFindValueMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                FindRequest,
                FindValueResponse>(
                  this, METHODID_FIND_VALUE)))
          .build();
    }
  }

  /**
   * <pre>
   * 定义的一个服务
   * </pre>
   */
  public static final class KademliaServiceStub extends io.grpc.stub.AbstractStub<KademliaServiceStub> {
    private KademliaServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KademliaServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected KademliaServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KademliaServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping函数
     * </pre>
     */
    public void ping(PingPackage request,
                     io.grpc.stub.StreamObserver<PingPackage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 存储
     * </pre>
     */
    public void store(StoreRequest request,
                      io.grpc.stub.StreamObserver<StoreResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStoreMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 寻找节点
     * </pre>
     */
    public void findNode(FindRequest request,
                         io.grpc.stub.StreamObserver<FindNodeResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getFindNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 寻找值
     * </pre>
     */
    public void findValue(FindRequest request,
                          io.grpc.stub.StreamObserver<FindValueResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getFindValueMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * 定义的一个服务
   * </pre>
   */
  public static final class KademliaServiceBlockingStub extends io.grpc.stub.AbstractStub<KademliaServiceBlockingStub> {
    private KademliaServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KademliaServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected KademliaServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KademliaServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping函数
     * </pre>
     */
    public PingPackage ping(PingPackage request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 存储
     * </pre>
     */
    public StoreResponse store(StoreRequest request) {
      return blockingUnaryCall(
          getChannel(), getStoreMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 寻找节点
     * </pre>
     */
    public java.util.Iterator<FindNodeResponse> findNode(
        FindRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getFindNodeMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * 寻找值
     * </pre>
     */
    public java.util.Iterator<FindValueResponse> findValue(
        FindRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getFindValueMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * 定义的一个服务
   * </pre>
   */
  public static final class KademliaServiceFutureStub extends io.grpc.stub.AbstractStub<KademliaServiceFutureStub> {
    private KademliaServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KademliaServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected KademliaServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KademliaServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Ping函数
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<PingPackage> ping(
        PingPackage request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * 存储
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<StoreResponse> store(
        StoreRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStoreMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_STORE = 1;
  private static final int METHODID_FIND_NODE = 2;
  private static final int METHODID_FIND_VALUE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final KademliaServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(KademliaServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((PingPackage) request,
              (io.grpc.stub.StreamObserver<PingPackage>) responseObserver);
          break;
        case METHODID_STORE:
          serviceImpl.store((StoreRequest) request,
              (io.grpc.stub.StreamObserver<StoreResponse>) responseObserver);
          break;
        case METHODID_FIND_NODE:
          serviceImpl.findNode((FindRequest) request,
              (io.grpc.stub.StreamObserver<FindNodeResponse>) responseObserver);
          break;
        case METHODID_FIND_VALUE:
          serviceImpl.findValue((FindRequest) request,
              (io.grpc.stub.StreamObserver<FindValueResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class KademliaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    KademliaServiceBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return KademliaServiceProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("KademliaService");
    }
  }

  private static final class KademliaServiceFileDescriptorSupplier
      extends KademliaServiceBaseDescriptorSupplier {
    KademliaServiceFileDescriptorSupplier() {}
  }

  private static final class KademliaServiceMethodDescriptorSupplier
      extends KademliaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    KademliaServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (KademliaServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new KademliaServiceFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .addMethod(getStoreMethod())
              .addMethod(getFindNodeMethod())
              .addMethod(getFindValueMethod())
              .build();
        }
      }
    }
    return result;
  }
}

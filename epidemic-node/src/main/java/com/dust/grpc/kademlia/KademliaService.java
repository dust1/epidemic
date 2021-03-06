// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: route_guide.proto

package com.dust.grpc.kademlia;

/**
 * <pre>
 * 定义的一个服务
 * </pre>
 *
 * Protobuf service {@code KademliaService}
 */
public  abstract class KademliaService
    implements com.google.protobuf.Service {
  protected KademliaService() {}

  public interface Interface {
    /**
     * <pre>
     * Ping函数
     * </pre>
     *
     * <code>rpc Ping(.PingRequest) returns (.PingResponse);</code>
     */
    public abstract void ping(
        com.google.protobuf.RpcController controller,
        PingRequest request,
        com.google.protobuf.RpcCallback<PingResponse> done);

    /**
     * <pre>
     * 存储
     * </pre>
     *
     * <code>rpc Store(.StoreRequest) returns (.StoreResponse);</code>
     */
    public abstract void store(
        com.google.protobuf.RpcController controller,
        StoreRequest request,
        com.google.protobuf.RpcCallback<StoreResponse> done);

    /**
     * <pre>
     * 寻找节点
     * </pre>
     *
     * <code>rpc FindNode(.FindRequest) returns (stream .FindNodeResponse);</code>
     */
    public abstract void findNode(
        com.google.protobuf.RpcController controller,
        FindRequest request,
        com.google.protobuf.RpcCallback<FindNodeResponse> done);

    /**
     * <pre>
     * 寻找值
     * </pre>
     *
     * <code>rpc findValue(.FindRequest) returns (stream .FindValueResponse);</code>
     */
    public abstract void findValue(
        com.google.protobuf.RpcController controller,
        FindRequest request,
        com.google.protobuf.RpcCallback<FindValueResponse> done);

  }

  public static com.google.protobuf.Service newReflectiveService(
      final Interface impl) {
    return new KademliaService() {
      @Override
      public  void ping(
          com.google.protobuf.RpcController controller,
          PingRequest request,
          com.google.protobuf.RpcCallback<PingResponse> done) {
        impl.ping(controller, request, done);
      }

      @Override
      public  void store(
          com.google.protobuf.RpcController controller,
          StoreRequest request,
          com.google.protobuf.RpcCallback<StoreResponse> done) {
        impl.store(controller, request, done);
      }

      @Override
      public  void findNode(
          com.google.protobuf.RpcController controller,
          FindRequest request,
          com.google.protobuf.RpcCallback<FindNodeResponse> done) {
        impl.findNode(controller, request, done);
      }

      @Override
      public  void findValue(
          com.google.protobuf.RpcController controller,
          FindRequest request,
          com.google.protobuf.RpcCallback<FindValueResponse> done) {
        impl.findValue(controller, request, done);
      }

    };
  }

  public static com.google.protobuf.BlockingService
      newReflectiveBlockingService(final BlockingInterface impl) {
    return new com.google.protobuf.BlockingService() {
      public final com.google.protobuf.Descriptors.ServiceDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }

      public final com.google.protobuf.Message callBlockingMethod(
          com.google.protobuf.Descriptors.MethodDescriptor method,
          com.google.protobuf.RpcController controller,
          com.google.protobuf.Message request)
          throws com.google.protobuf.ServiceException {
        if (method.getService() != getDescriptor()) {
          throw new IllegalArgumentException(
            "Service.callBlockingMethod() given method descriptor for " +
            "wrong service type.");
        }
        switch(method.getIndex()) {
          case 0:
            return impl.ping(controller, (PingRequest)request);
          case 1:
            return impl.store(controller, (StoreRequest)request);
          case 2:
            return impl.findNode(controller, (FindRequest)request);
          case 3:
            return impl.findValue(controller, (FindRequest)request);
          default:
            throw new AssertionError("Can't get here.");
        }
      }

      public final com.google.protobuf.Message
          getRequestPrototype(
          com.google.protobuf.Descriptors.MethodDescriptor method) {
        if (method.getService() != getDescriptor()) {
          throw new IllegalArgumentException(
            "Service.getRequestPrototype() given method " +
            "descriptor for wrong service type.");
        }
        switch(method.getIndex()) {
          case 0:
            return PingRequest.getDefaultInstance();
          case 1:
            return StoreRequest.getDefaultInstance();
          case 2:
            return FindRequest.getDefaultInstance();
          case 3:
            return FindRequest.getDefaultInstance();
          default:
            throw new AssertionError("Can't get here.");
        }
      }

      public final com.google.protobuf.Message
          getResponsePrototype(
          com.google.protobuf.Descriptors.MethodDescriptor method) {
        if (method.getService() != getDescriptor()) {
          throw new IllegalArgumentException(
            "Service.getResponsePrototype() given method " +
            "descriptor for wrong service type.");
        }
        switch(method.getIndex()) {
          case 0:
            return PingResponse.getDefaultInstance();
          case 1:
            return StoreResponse.getDefaultInstance();
          case 2:
            return FindNodeResponse.getDefaultInstance();
          case 3:
            return FindValueResponse.getDefaultInstance();
          default:
            throw new AssertionError("Can't get here.");
        }
      }

    };
  }

  /**
   * <pre>
   * Ping函数
   * </pre>
   *
   * <code>rpc Ping(.PingRequest) returns (.PingResponse);</code>
   */
  public abstract void ping(
      com.google.protobuf.RpcController controller,
      PingRequest request,
      com.google.protobuf.RpcCallback<PingResponse> done);

  /**
   * <pre>
   * 存储
   * </pre>
   *
   * <code>rpc Store(.StoreRequest) returns (.StoreResponse);</code>
   */
  public abstract void store(
      com.google.protobuf.RpcController controller,
      StoreRequest request,
      com.google.protobuf.RpcCallback<StoreResponse> done);

  /**
   * <pre>
   * 寻找节点
   * </pre>
   *
   * <code>rpc FindNode(.FindRequest) returns (stream .FindNodeResponse);</code>
   */
  public abstract void findNode(
      com.google.protobuf.RpcController controller,
      FindRequest request,
      com.google.protobuf.RpcCallback<FindNodeResponse> done);

  /**
   * <pre>
   * 寻找值
   * </pre>
   *
   * <code>rpc findValue(.FindRequest) returns (stream .FindValueResponse);</code>
   */
  public abstract void findValue(
      com.google.protobuf.RpcController controller,
      FindRequest request,
      com.google.protobuf.RpcCallback<FindValueResponse> done);

  public static final
      com.google.protobuf.Descriptors.ServiceDescriptor
      getDescriptor() {
    return KademliaServiceProto.getDescriptor().getServices().get(0);
  }
  public final com.google.protobuf.Descriptors.ServiceDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }

  public final void callMethod(
      com.google.protobuf.Descriptors.MethodDescriptor method,
      com.google.protobuf.RpcController controller,
      com.google.protobuf.Message request,
      com.google.protobuf.RpcCallback<
        com.google.protobuf.Message> done) {
    if (method.getService() != getDescriptor()) {
      throw new IllegalArgumentException(
        "Service.callMethod() given method descriptor for wrong " +
        "service type.");
    }
    switch(method.getIndex()) {
      case 0:
        this.ping(controller, (PingRequest)request,
          com.google.protobuf.RpcUtil.<PingResponse>specializeCallback(
            done));
        return;
      case 1:
        this.store(controller, (StoreRequest)request,
          com.google.protobuf.RpcUtil.<StoreResponse>specializeCallback(
            done));
        return;
      case 2:
        this.findNode(controller, (FindRequest)request,
          com.google.protobuf.RpcUtil.<FindNodeResponse>specializeCallback(
            done));
        return;
      case 3:
        this.findValue(controller, (FindRequest)request,
          com.google.protobuf.RpcUtil.<FindValueResponse>specializeCallback(
            done));
        return;
      default:
        throw new AssertionError("Can't get here.");
    }
  }

  public final com.google.protobuf.Message
      getRequestPrototype(
      com.google.protobuf.Descriptors.MethodDescriptor method) {
    if (method.getService() != getDescriptor()) {
      throw new IllegalArgumentException(
        "Service.getRequestPrototype() given method " +
        "descriptor for wrong service type.");
    }
    switch(method.getIndex()) {
      case 0:
        return PingRequest.getDefaultInstance();
      case 1:
        return StoreRequest.getDefaultInstance();
      case 2:
        return FindRequest.getDefaultInstance();
      case 3:
        return FindRequest.getDefaultInstance();
      default:
        throw new AssertionError("Can't get here.");
    }
  }

  public final com.google.protobuf.Message
      getResponsePrototype(
      com.google.protobuf.Descriptors.MethodDescriptor method) {
    if (method.getService() != getDescriptor()) {
      throw new IllegalArgumentException(
        "Service.getResponsePrototype() given method " +
        "descriptor for wrong service type.");
    }
    switch(method.getIndex()) {
      case 0:
        return PingResponse.getDefaultInstance();
      case 1:
        return StoreResponse.getDefaultInstance();
      case 2:
        return FindNodeResponse.getDefaultInstance();
      case 3:
        return FindValueResponse.getDefaultInstance();
      default:
        throw new AssertionError("Can't get here.");
    }
  }

  public static Stub newStub(
      com.google.protobuf.RpcChannel channel) {
    return new Stub(channel);
  }

  public static final class Stub extends KademliaService implements Interface {
    private Stub(com.google.protobuf.RpcChannel channel) {
      this.channel = channel;
    }

    private final com.google.protobuf.RpcChannel channel;

    public com.google.protobuf.RpcChannel getChannel() {
      return channel;
    }

    public  void ping(
        com.google.protobuf.RpcController controller,
        PingRequest request,
        com.google.protobuf.RpcCallback<PingResponse> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(0),
        controller,
        request,
        PingResponse.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          PingResponse.class,
          PingResponse.getDefaultInstance()));
    }

    public  void store(
        com.google.protobuf.RpcController controller,
        StoreRequest request,
        com.google.protobuf.RpcCallback<StoreResponse> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(1),
        controller,
        request,
        StoreResponse.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          StoreResponse.class,
          StoreResponse.getDefaultInstance()));
    }

    public  void findNode(
        com.google.protobuf.RpcController controller,
        FindRequest request,
        com.google.protobuf.RpcCallback<FindNodeResponse> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(2),
        controller,
        request,
        FindNodeResponse.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          FindNodeResponse.class,
          FindNodeResponse.getDefaultInstance()));
    }

    public  void findValue(
        com.google.protobuf.RpcController controller,
        FindRequest request,
        com.google.protobuf.RpcCallback<FindValueResponse> done) {
      channel.callMethod(
        getDescriptor().getMethods().get(3),
        controller,
        request,
        FindValueResponse.getDefaultInstance(),
        com.google.protobuf.RpcUtil.generalizeCallback(
          done,
          FindValueResponse.class,
          FindValueResponse.getDefaultInstance()));
    }
  }

  public static BlockingInterface newBlockingStub(
      com.google.protobuf.BlockingRpcChannel channel) {
    return new BlockingStub(channel);
  }

  public interface BlockingInterface {
    public PingResponse ping(
        com.google.protobuf.RpcController controller,
        PingRequest request)
        throws com.google.protobuf.ServiceException;

    public StoreResponse store(
        com.google.protobuf.RpcController controller,
        StoreRequest request)
        throws com.google.protobuf.ServiceException;

    public FindNodeResponse findNode(
        com.google.protobuf.RpcController controller,
        FindRequest request)
        throws com.google.protobuf.ServiceException;

    public FindValueResponse findValue(
        com.google.protobuf.RpcController controller,
        FindRequest request)
        throws com.google.protobuf.ServiceException;
  }

  private static final class BlockingStub implements BlockingInterface {
    private BlockingStub(com.google.protobuf.BlockingRpcChannel channel) {
      this.channel = channel;
    }

    private final com.google.protobuf.BlockingRpcChannel channel;

    public PingResponse ping(
        com.google.protobuf.RpcController controller,
        PingRequest request)
        throws com.google.protobuf.ServiceException {
      return (PingResponse) channel.callBlockingMethod(
        getDescriptor().getMethods().get(0),
        controller,
        request,
        PingResponse.getDefaultInstance());
    }


    public StoreResponse store(
        com.google.protobuf.RpcController controller,
        StoreRequest request)
        throws com.google.protobuf.ServiceException {
      return (StoreResponse) channel.callBlockingMethod(
        getDescriptor().getMethods().get(1),
        controller,
        request,
        StoreResponse.getDefaultInstance());
    }


    public FindNodeResponse findNode(
        com.google.protobuf.RpcController controller,
        FindRequest request)
        throws com.google.protobuf.ServiceException {
      return (FindNodeResponse) channel.callBlockingMethod(
        getDescriptor().getMethods().get(2),
        controller,
        request,
        FindNodeResponse.getDefaultInstance());
    }


    public FindValueResponse findValue(
        com.google.protobuf.RpcController controller,
        FindRequest request)
        throws com.google.protobuf.ServiceException {
      return (FindValueResponse) channel.callBlockingMethod(
        getDescriptor().getMethods().get(3),
        controller,
        request,
        FindValueResponse.getDefaultInstance());
    }

  }

  // @@protoc_insertion_point(class_scope:KademliaService)
}


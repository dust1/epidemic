// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: route_guide.proto

package com.dust.grpc.kademlia;

/**
 * <pre>
 *节点发起请求的时候必须携带自身的网络信息，这样方便其他节点根据这些信息添加节点
 * </pre>
 *
 * Protobuf type {@code NodeInfo}
 */
public final class NodeInfo extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:NodeInfo)
    NodeInfoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use NodeInfo.newBuilder() to construct.
  private NodeInfo(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private NodeInfo() {
    nodeId_ = "";
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new NodeInfo();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private NodeInfo(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            String s = input.readStringRequireUtf8();

            nodeId_ = s;
            break;
          }
          case 16: {

            port_ = input.readInt32();
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return KademliaServiceProto.internal_static_NodeInfo_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return KademliaServiceProto.internal_static_NodeInfo_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            NodeInfo.class, Builder.class);
  }

  public static final int NODEID_FIELD_NUMBER = 1;
  private volatile Object nodeId_;
  /**
   * <pre>
   *发出信息的节点id
   * </pre>
   *
   * <code>string nodeId = 1;</code>
   * @return The nodeId.
   */
  @Override
  public String getNodeId() {
    Object ref = nodeId_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      nodeId_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *发出信息的节点id
   * </pre>
   *
   * <code>string nodeId = 1;</code>
   * @return The bytes for nodeId.
   */
  @Override
  public com.google.protobuf.ByteString
      getNodeIdBytes() {
    Object ref = nodeId_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      nodeId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PORT_FIELD_NUMBER = 2;
  private int port_;
  /**
   * <pre>
   *发出信息的节点对外端口
   * </pre>
   *
   * <code>int32 port = 2;</code>
   * @return The port.
   */
  @Override
  public int getPort() {
    return port_;
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getNodeIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, nodeId_);
    }
    if (port_ != 0) {
      output.writeInt32(2, port_);
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getNodeIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, nodeId_);
    }
    if (port_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, port_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof NodeInfo)) {
      return super.equals(obj);
    }
    NodeInfo other = (NodeInfo) obj;

    if (!getNodeId()
        .equals(other.getNodeId())) return false;
    if (getPort()
        != other.getPort()) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + NODEID_FIELD_NUMBER;
    hash = (53 * hash) + getNodeId().hashCode();
    hash = (37 * hash) + PORT_FIELD_NUMBER;
    hash = (53 * hash) + getPort();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static NodeInfo parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static NodeInfo parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static NodeInfo parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static NodeInfo parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static NodeInfo parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static NodeInfo parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static NodeInfo parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static NodeInfo parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static NodeInfo parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static NodeInfo parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static NodeInfo parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static NodeInfo parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(NodeInfo prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   *节点发起请求的时候必须携带自身的网络信息，这样方便其他节点根据这些信息添加节点
   * </pre>
   *
   * Protobuf type {@code NodeInfo}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:NodeInfo)
      NodeInfoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return KademliaServiceProto.internal_static_NodeInfo_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return KademliaServiceProto.internal_static_NodeInfo_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              NodeInfo.class, Builder.class);
    }

    // Construct using com.dust.grpc.kademlia.NodeInfo.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @Override
    public Builder clear() {
      super.clear();
      nodeId_ = "";

      port_ = 0;

      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return KademliaServiceProto.internal_static_NodeInfo_descriptor;
    }

    @Override
    public NodeInfo getDefaultInstanceForType() {
      return NodeInfo.getDefaultInstance();
    }

    @Override
    public NodeInfo build() {
      NodeInfo result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public NodeInfo buildPartial() {
      NodeInfo result = new NodeInfo(this);
      result.nodeId_ = nodeId_;
      result.port_ = port_;
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }
    @Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.setField(field, value);
    }
    @Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return super.addRepeatedField(field, value);
    }
    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof NodeInfo) {
        return mergeFrom((NodeInfo)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(NodeInfo other) {
      if (other == NodeInfo.getDefaultInstance()) return this;
      if (!other.getNodeId().isEmpty()) {
        nodeId_ = other.nodeId_;
        onChanged();
      }
      if (other.getPort() != 0) {
        setPort(other.getPort());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      NodeInfo parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (NodeInfo) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private Object nodeId_ = "";
    /**
     * <pre>
     *发出信息的节点id
     * </pre>
     *
     * <code>string nodeId = 1;</code>
     * @return The nodeId.
     */
    public String getNodeId() {
      Object ref = nodeId_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        nodeId_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     *发出信息的节点id
     * </pre>
     *
     * <code>string nodeId = 1;</code>
     * @return The bytes for nodeId.
     */
    public com.google.protobuf.ByteString
        getNodeIdBytes() {
      Object ref = nodeId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        nodeId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *发出信息的节点id
     * </pre>
     *
     * <code>string nodeId = 1;</code>
     * @param value The nodeId to set.
     * @return This builder for chaining.
     */
    public Builder setNodeId(
        String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      nodeId_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *发出信息的节点id
     * </pre>
     *
     * <code>string nodeId = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearNodeId() {
      
      nodeId_ = getDefaultInstance().getNodeId();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *发出信息的节点id
     * </pre>
     *
     * <code>string nodeId = 1;</code>
     * @param value The bytes for nodeId to set.
     * @return This builder for chaining.
     */
    public Builder setNodeIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      nodeId_ = value;
      onChanged();
      return this;
    }

    private int port_ ;
    /**
     * <pre>
     *发出信息的节点对外端口
     * </pre>
     *
     * <code>int32 port = 2;</code>
     * @return The port.
     */
    @Override
    public int getPort() {
      return port_;
    }
    /**
     * <pre>
     *发出信息的节点对外端口
     * </pre>
     *
     * <code>int32 port = 2;</code>
     * @param value The port to set.
     * @return This builder for chaining.
     */
    public Builder setPort(int value) {
      
      port_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *发出信息的节点对外端口
     * </pre>
     *
     * <code>int32 port = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearPort() {
      
      port_ = 0;
      onChanged();
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:NodeInfo)
  }

  // @@protoc_insertion_point(class_scope:NodeInfo)
  private static final NodeInfo DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new NodeInfo();
  }

  public static NodeInfo getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<NodeInfo>
      PARSER = new com.google.protobuf.AbstractParser<NodeInfo>() {
    @Override
    public NodeInfo parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new NodeInfo(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<NodeInfo> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<NodeInfo> getParserForType() {
    return PARSER;
  }

  @Override
  public NodeInfo getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


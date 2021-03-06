// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: route_guide.proto

package com.dust.grpc.kademlia;

/**
 * <pre>
 *ping函数发起的请求，携带发起者的id、网络端口和发起时间
 * </pre>
 *
 * Protobuf type {@code PingRequest}
 */
public final class PingRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:PingRequest)
    PingRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use PingRequest.newBuilder() to construct.
  private PingRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private PingRequest() {
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new PingRequest();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private PingRequest(
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
          case 8: {

            timestamp_ = input.readInt32();
            break;
          }
          case 18: {
            NodeInfo.Builder subBuilder = null;
            if (nodeInfo_ != null) {
              subBuilder = nodeInfo_.toBuilder();
            }
            nodeInfo_ = input.readMessage(NodeInfo.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(nodeInfo_);
              nodeInfo_ = subBuilder.buildPartial();
            }

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
    return KademliaServiceProto.internal_static_PingRequest_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return KademliaServiceProto.internal_static_PingRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            PingRequest.class, Builder.class);
  }

  public static final int TIMESTAMP_FIELD_NUMBER = 1;
  private int timestamp_;
  /**
   * <code>int32 timestamp = 1;</code>
   * @return The timestamp.
   */
  @Override
  public int getTimestamp() {
    return timestamp_;
  }

  public static final int NODEINFO_FIELD_NUMBER = 2;
  private NodeInfo nodeInfo_;
  /**
   * <code>.NodeInfo nodeInfo = 2;</code>
   * @return Whether the nodeInfo field is set.
   */
  @Override
  public boolean hasNodeInfo() {
    return nodeInfo_ != null;
  }
  /**
   * <code>.NodeInfo nodeInfo = 2;</code>
   * @return The nodeInfo.
   */
  @Override
  public NodeInfo getNodeInfo() {
    return nodeInfo_ == null ? NodeInfo.getDefaultInstance() : nodeInfo_;
  }
  /**
   * <code>.NodeInfo nodeInfo = 2;</code>
   */
  @Override
  public NodeInfoOrBuilder getNodeInfoOrBuilder() {
    return getNodeInfo();
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
    if (timestamp_ != 0) {
      output.writeInt32(1, timestamp_);
    }
    if (nodeInfo_ != null) {
      output.writeMessage(2, getNodeInfo());
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (timestamp_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, timestamp_);
    }
    if (nodeInfo_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getNodeInfo());
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
    if (!(obj instanceof PingRequest)) {
      return super.equals(obj);
    }
    PingRequest other = (PingRequest) obj;

    if (getTimestamp()
        != other.getTimestamp()) return false;
    if (hasNodeInfo() != other.hasNodeInfo()) return false;
    if (hasNodeInfo()) {
      if (!getNodeInfo()
          .equals(other.getNodeInfo())) return false;
    }
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
    hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
    hash = (53 * hash) + getTimestamp();
    if (hasNodeInfo()) {
      hash = (37 * hash) + NODEINFO_FIELD_NUMBER;
      hash = (53 * hash) + getNodeInfo().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static PingRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static PingRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static PingRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static PingRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static PingRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static PingRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static PingRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static PingRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static PingRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static PingRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static PingRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static PingRequest parseFrom(
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
  public static Builder newBuilder(PingRequest prototype) {
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
   *ping函数发起的请求，携带发起者的id、网络端口和发起时间
   * </pre>
   *
   * Protobuf type {@code PingRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:PingRequest)
      PingRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return KademliaServiceProto.internal_static_PingRequest_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return KademliaServiceProto.internal_static_PingRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              PingRequest.class, Builder.class);
    }

    // Construct using com.dust.grpc.kademlia.PingRequest.newBuilder()
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
      timestamp_ = 0;

      if (nodeInfoBuilder_ == null) {
        nodeInfo_ = null;
      } else {
        nodeInfo_ = null;
        nodeInfoBuilder_ = null;
      }
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return KademliaServiceProto.internal_static_PingRequest_descriptor;
    }

    @Override
    public PingRequest getDefaultInstanceForType() {
      return PingRequest.getDefaultInstance();
    }

    @Override
    public PingRequest build() {
      PingRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public PingRequest buildPartial() {
      PingRequest result = new PingRequest(this);
      result.timestamp_ = timestamp_;
      if (nodeInfoBuilder_ == null) {
        result.nodeInfo_ = nodeInfo_;
      } else {
        result.nodeInfo_ = nodeInfoBuilder_.build();
      }
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
      if (other instanceof PingRequest) {
        return mergeFrom((PingRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(PingRequest other) {
      if (other == PingRequest.getDefaultInstance()) return this;
      if (other.getTimestamp() != 0) {
        setTimestamp(other.getTimestamp());
      }
      if (other.hasNodeInfo()) {
        mergeNodeInfo(other.getNodeInfo());
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
      PingRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (PingRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int timestamp_ ;
    /**
     * <code>int32 timestamp = 1;</code>
     * @return The timestamp.
     */
    @Override
    public int getTimestamp() {
      return timestamp_;
    }
    /**
     * <code>int32 timestamp = 1;</code>
     * @param value The timestamp to set.
     * @return This builder for chaining.
     */
    public Builder setTimestamp(int value) {
      
      timestamp_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 timestamp = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearTimestamp() {
      
      timestamp_ = 0;
      onChanged();
      return this;
    }

    private NodeInfo nodeInfo_;
    private com.google.protobuf.SingleFieldBuilderV3<
        NodeInfo, NodeInfo.Builder, NodeInfoOrBuilder> nodeInfoBuilder_;
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     * @return Whether the nodeInfo field is set.
     */
    public boolean hasNodeInfo() {
      return nodeInfoBuilder_ != null || nodeInfo_ != null;
    }
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     * @return The nodeInfo.
     */
    public NodeInfo getNodeInfo() {
      if (nodeInfoBuilder_ == null) {
        return nodeInfo_ == null ? NodeInfo.getDefaultInstance() : nodeInfo_;
      } else {
        return nodeInfoBuilder_.getMessage();
      }
    }
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     */
    public Builder setNodeInfo(NodeInfo value) {
      if (nodeInfoBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        nodeInfo_ = value;
        onChanged();
      } else {
        nodeInfoBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     */
    public Builder setNodeInfo(
        NodeInfo.Builder builderForValue) {
      if (nodeInfoBuilder_ == null) {
        nodeInfo_ = builderForValue.build();
        onChanged();
      } else {
        nodeInfoBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     */
    public Builder mergeNodeInfo(NodeInfo value) {
      if (nodeInfoBuilder_ == null) {
        if (nodeInfo_ != null) {
          nodeInfo_ =
            NodeInfo.newBuilder(nodeInfo_).mergeFrom(value).buildPartial();
        } else {
          nodeInfo_ = value;
        }
        onChanged();
      } else {
        nodeInfoBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     */
    public Builder clearNodeInfo() {
      if (nodeInfoBuilder_ == null) {
        nodeInfo_ = null;
        onChanged();
      } else {
        nodeInfo_ = null;
        nodeInfoBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     */
    public NodeInfo.Builder getNodeInfoBuilder() {
      
      onChanged();
      return getNodeInfoFieldBuilder().getBuilder();
    }
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     */
    public NodeInfoOrBuilder getNodeInfoOrBuilder() {
      if (nodeInfoBuilder_ != null) {
        return nodeInfoBuilder_.getMessageOrBuilder();
      } else {
        return nodeInfo_ == null ?
            NodeInfo.getDefaultInstance() : nodeInfo_;
      }
    }
    /**
     * <code>.NodeInfo nodeInfo = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        NodeInfo, NodeInfo.Builder, NodeInfoOrBuilder>
        getNodeInfoFieldBuilder() {
      if (nodeInfoBuilder_ == null) {
        nodeInfoBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            NodeInfo, NodeInfo.Builder, NodeInfoOrBuilder>(
                getNodeInfo(),
                getParentForChildren(),
                isClean());
        nodeInfo_ = null;
      }
      return nodeInfoBuilder_;
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


    // @@protoc_insertion_point(builder_scope:PingRequest)
  }

  // @@protoc_insertion_point(class_scope:PingRequest)
  private static final PingRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new PingRequest();
  }

  public static PingRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<PingRequest>
      PARSER = new com.google.protobuf.AbstractParser<PingRequest>() {
    @Override
    public PingRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new PingRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<PingRequest> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<PingRequest> getParserForType() {
    return PARSER;
  }

  @Override
  public PingRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


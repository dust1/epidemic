// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: route_guide.proto

package com.dust.grpc.kademlia;

public interface PingRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:PingRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 timestamp = 1;</code>
   * @return The timestamp.
   */
  int getTimestamp();

  /**
   * <code>.NodeInfo nodeInfo = 2;</code>
   * @return Whether the nodeInfo field is set.
   */
  boolean hasNodeInfo();
  /**
   * <code>.NodeInfo nodeInfo = 2;</code>
   * @return The nodeInfo.
   */
  NodeInfo getNodeInfo();
  /**
   * <code>.NodeInfo nodeInfo = 2;</code>
   */
  NodeInfoOrBuilder getNodeInfoOrBuilder();
}

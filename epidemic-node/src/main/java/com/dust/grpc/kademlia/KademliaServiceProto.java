// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: route_guide.proto

package com.dust.grpc.kademlia;

public final class KademliaServiceProto {
  private KademliaServiceProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_PingPackage_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_PingPackage_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_StoreRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_StoreRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_StoreResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_StoreResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_FindRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_FindRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_FindNodeResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_FindNodeResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_FindValueResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_FindValueResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\021route_guide.proto\"0\n\013PingPackage\022\016\n\006no" +
      "deId\030\001 \001(\t\022\021\n\ttimestamp\030\002 \001(\005\")\n\014StoreRe" +
      "quest\022\013\n\003key\030\001 \001(\t\022\014\n\004data\030\002 \001(\014\"-\n\rStor" +
      "eResponse\022\014\n\004code\030\001 \001(\005\022\016\n\006errmsg\030\002 \001(\t\"" +
      "\032\n\013FindRequest\022\013\n\003key\030\001 \001(\t\">\n\020FindNodeR" +
      "esponse\022\014\n\004host\030\001 \001(\t\022\014\n\004port\030\002 \001(\005\022\016\n\006n" +
      "odeId\030\003 \001(\t\"M\n\021FindValueResponse\022\014\n\004host" +
      "\030\001 \001(\t\022\014\n\004port\030\002 \001(\005\022\016\n\006nodeId\030\003 \001(\t\022\014\n\004" +
      "data\030\004 \001(\0142\305\001\n\017KademliaService\022$\n\004Ping\022\014" +
      ".PingPackage\032\014.PingPackage\"\000\022(\n\005Store\022\r." +
      "StoreRequest\032\016.StoreResponse\"\000\022/\n\010FindNo" +
      "de\022\014.FindRequest\032\021.FindNodeResponse\"\0000\001\022" +
      "1\n\tfindValue\022\014.FindRequest\032\022.FindValueRe" +
      "sponse\"\0000\001B3\n\026com.dust.grpc.kademliaB\024Ka" +
      "demliaServiceProtoP\001\210\001\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_PingPackage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_PingPackage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_PingPackage_descriptor,
        new String[] { "NodeId", "Timestamp", });
    internal_static_StoreRequest_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_StoreRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_StoreRequest_descriptor,
        new String[] { "Key", "Data", });
    internal_static_StoreResponse_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_StoreResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_StoreResponse_descriptor,
        new String[] { "Code", "Errmsg", });
    internal_static_FindRequest_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_FindRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_FindRequest_descriptor,
        new String[] { "Key", });
    internal_static_FindNodeResponse_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_FindNodeResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_FindNodeResponse_descriptor,
        new String[] { "Host", "Port", "NodeId", });
    internal_static_FindValueResponse_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_FindValueResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_FindValueResponse_descriptor,
        new String[] { "Host", "Port", "NodeId", "Data", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

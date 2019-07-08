// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: User.proto

package org.sq.gameDemo.common.proto;

public final class UserProto {
  private UserProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface UserOrBuilder extends
      // @@protoc_insertion_point(interface_extends:User)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *消息id
     * </pre>
     *
     * <code>uint64 msg_id = 1;</code>
     */
    long getMsgId();

    /**
     * <pre>
     *用户id
     * </pre>
     *
     * <code>uint32 id = 2;</code>
     */
    int getId();

    /**
     * <pre>
     *时间戳(单位:毫秒)
     * </pre>
     *
     * <code>uint64 time = 3;</code>
     */
    long getTime();

    /**
     * <code>string name = 4;</code>
     */
    String getName();
    /**
     * <code>string name = 4;</code>
     */
    com.google.protobuf.ByteString
        getNameBytes();

    /**
     * <code>string password = 5;</code>
     */
    String getPassword();
    /**
     * <code>string password = 5;</code>
     */
    com.google.protobuf.ByteString
        getPasswordBytes();

    /**
     * <pre>
     *返回token客户端保存
     * </pre>
     *
     * <code>string token = 6;</code>
     */
    String getToken();
    /**
     * <pre>
     *返回token客户端保存
     * </pre>
     *
     * <code>string token = 6;</code>
     */
    com.google.protobuf.ByteString
        getTokenBytes();
  }
  /**
   * Protobuf type {@code User}
   */
  public  static final class User extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:User)
      UserOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use User.newBuilder() to construct.
    private User(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private User() {
      name_ = "";
      password_ = "";
      token_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new User();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private User(
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

              msgId_ = input.readUInt64();
              break;
            }
            case 16: {

              id_ = input.readUInt32();
              break;
            }
            case 24: {

              time_ = input.readUInt64();
              break;
            }
            case 34: {
              String s = input.readStringRequireUtf8();

              name_ = s;
              break;
            }
            case 42: {
              String s = input.readStringRequireUtf8();

              password_ = s;
              break;
            }
            case 50: {
              String s = input.readStringRequireUtf8();

              token_ = s;
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
      return UserProto.internal_static_User_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return UserProto.internal_static_User_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              User.class, Builder.class);
    }

    public static final int MSG_ID_FIELD_NUMBER = 1;
    private long msgId_;
    /**
     * <pre>
     *消息id
     * </pre>
     *
     * <code>uint64 msg_id = 1;</code>
     */
    public long getMsgId() {
      return msgId_;
    }

    public static final int ID_FIELD_NUMBER = 2;
    private int id_;
    /**
     * <pre>
     *用户id
     * </pre>
     *
     * <code>uint32 id = 2;</code>
     */
    public int getId() {
      return id_;
    }

    public static final int TIME_FIELD_NUMBER = 3;
    private long time_;
    /**
     * <pre>
     *时间戳(单位:毫秒)
     * </pre>
     *
     * <code>uint64 time = 3;</code>
     */
    public long getTime() {
      return time_;
    }

    public static final int NAME_FIELD_NUMBER = 4;
    private volatile Object name_;
    /**
     * <code>string name = 4;</code>
     */
    public String getName() {
      Object ref = name_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        name_ = s;
        return s;
      }
    }
    /**
     * <code>string name = 4;</code>
     */
    public com.google.protobuf.ByteString
        getNameBytes() {
      Object ref = name_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PASSWORD_FIELD_NUMBER = 5;
    private volatile Object password_;
    /**
     * <code>string password = 5;</code>
     */
    public String getPassword() {
      Object ref = password_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        password_ = s;
        return s;
      }
    }
    /**
     * <code>string password = 5;</code>
     */
    public com.google.protobuf.ByteString
        getPasswordBytes() {
      Object ref = password_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        password_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int TOKEN_FIELD_NUMBER = 6;
    private volatile Object token_;
    /**
     * <pre>
     *返回token客户端保存
     * </pre>
     *
     * <code>string token = 6;</code>
     */
    public String getToken() {
      Object ref = token_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        token_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *返回token客户端保存
     * </pre>
     *
     * <code>string token = 6;</code>
     */
    public com.google.protobuf.ByteString
        getTokenBytes() {
      Object ref = token_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        token_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
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
      if (msgId_ != 0L) {
        output.writeUInt64(1, msgId_);
      }
      if (id_ != 0) {
        output.writeUInt32(2, id_);
      }
      if (time_ != 0L) {
        output.writeUInt64(3, time_);
      }
      if (!getNameBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, name_);
      }
      if (!getPasswordBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 5, password_);
      }
      if (!getTokenBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 6, token_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (msgId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(1, msgId_);
      }
      if (id_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(2, id_);
      }
      if (time_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(3, time_);
      }
      if (!getNameBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, name_);
      }
      if (!getPasswordBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, password_);
      }
      if (!getTokenBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(6, token_);
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
      if (!(obj instanceof User)) {
        return super.equals(obj);
      }
      User other = (User) obj;

      if (getMsgId()
          != other.getMsgId()) return false;
      if (getId()
          != other.getId()) return false;
      if (getTime()
          != other.getTime()) return false;
      if (!getName()
          .equals(other.getName())) return false;
      if (!getPassword()
          .equals(other.getPassword())) return false;
      if (!getToken()
          .equals(other.getToken())) return false;
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
      hash = (37 * hash) + MSG_ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getMsgId());
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + getId();
      hash = (37 * hash) + TIME_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTime());
      hash = (37 * hash) + NAME_FIELD_NUMBER;
      hash = (53 * hash) + getName().hashCode();
      hash = (37 * hash) + PASSWORD_FIELD_NUMBER;
      hash = (53 * hash) + getPassword().hashCode();
      hash = (37 * hash) + TOKEN_FIELD_NUMBER;
      hash = (53 * hash) + getToken().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static User parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static User parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static User parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static User parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static User parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static User parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static User parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static User parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static User parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static User parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static User parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static User parseFrom(
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
    public static Builder newBuilder(User prototype) {
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
     * Protobuf type {@code User}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:User)
        UserOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return UserProto.internal_static_User_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return UserProto.internal_static_User_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                User.class, Builder.class);
      }

      // Construct using org.sq.gameDemo.common.proto.UserProto.User.newBuilder()
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
        msgId_ = 0L;

        id_ = 0;

        time_ = 0L;

        name_ = "";

        password_ = "";

        token_ = "";

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return UserProto.internal_static_User_descriptor;
      }

      @Override
      public User getDefaultInstanceForType() {
        return User.getDefaultInstance();
      }

      @Override
      public User build() {
        User result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public User buildPartial() {
        User result = new User(this);
        result.msgId_ = msgId_;
        result.id_ = id_;
        result.time_ = time_;
        result.name_ = name_;
        result.password_ = password_;
        result.token_ = token_;
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
        if (other instanceof User) {
          return mergeFrom((User)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(User other) {
        if (other == User.getDefaultInstance()) return this;
        if (other.getMsgId() != 0L) {
          setMsgId(other.getMsgId());
        }
        if (other.getId() != 0) {
          setId(other.getId());
        }
        if (other.getTime() != 0L) {
          setTime(other.getTime());
        }
        if (!other.getName().isEmpty()) {
          name_ = other.name_;
          onChanged();
        }
        if (!other.getPassword().isEmpty()) {
          password_ = other.password_;
          onChanged();
        }
        if (!other.getToken().isEmpty()) {
          token_ = other.token_;
          onChanged();
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
        User parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (User) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private long msgId_ ;
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>uint64 msg_id = 1;</code>
       */
      public long getMsgId() {
        return msgId_;
      }
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>uint64 msg_id = 1;</code>
       */
      public Builder setMsgId(long value) {
        
        msgId_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *消息id
       * </pre>
       *
       * <code>uint64 msg_id = 1;</code>
       */
      public Builder clearMsgId() {
        
        msgId_ = 0L;
        onChanged();
        return this;
      }

      private int id_ ;
      /**
       * <pre>
       *用户id
       * </pre>
       *
       * <code>uint32 id = 2;</code>
       */
      public int getId() {
        return id_;
      }
      /**
       * <pre>
       *用户id
       * </pre>
       *
       * <code>uint32 id = 2;</code>
       */
      public Builder setId(int value) {
        
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *用户id
       * </pre>
       *
       * <code>uint32 id = 2;</code>
       */
      public Builder clearId() {
        
        id_ = 0;
        onChanged();
        return this;
      }

      private long time_ ;
      /**
       * <pre>
       *时间戳(单位:毫秒)
       * </pre>
       *
       * <code>uint64 time = 3;</code>
       */
      public long getTime() {
        return time_;
      }
      /**
       * <pre>
       *时间戳(单位:毫秒)
       * </pre>
       *
       * <code>uint64 time = 3;</code>
       */
      public Builder setTime(long value) {
        
        time_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *时间戳(单位:毫秒)
       * </pre>
       *
       * <code>uint64 time = 3;</code>
       */
      public Builder clearTime() {
        
        time_ = 0L;
        onChanged();
        return this;
      }

      private Object name_ = "";
      /**
       * <code>string name = 4;</code>
       */
      public String getName() {
        Object ref = name_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string name = 4;</code>
       */
      public com.google.protobuf.ByteString
          getNameBytes() {
        Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string name = 4;</code>
       */
      public Builder setName(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        name_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string name = 4;</code>
       */
      public Builder clearName() {
        
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }
      /**
       * <code>string name = 4;</code>
       */
      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        name_ = value;
        onChanged();
        return this;
      }

      private Object password_ = "";
      /**
       * <code>string password = 5;</code>
       */
      public String getPassword() {
        Object ref = password_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          password_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <code>string password = 5;</code>
       */
      public com.google.protobuf.ByteString
          getPasswordBytes() {
        Object ref = password_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          password_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>string password = 5;</code>
       */
      public Builder setPassword(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        password_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>string password = 5;</code>
       */
      public Builder clearPassword() {
        
        password_ = getDefaultInstance().getPassword();
        onChanged();
        return this;
      }
      /**
       * <code>string password = 5;</code>
       */
      public Builder setPasswordBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        password_ = value;
        onChanged();
        return this;
      }

      private Object token_ = "";
      /**
       * <pre>
       *返回token客户端保存
       * </pre>
       *
       * <code>string token = 6;</code>
       */
      public String getToken() {
        Object ref = token_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          token_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       *返回token客户端保存
       * </pre>
       *
       * <code>string token = 6;</code>
       */
      public com.google.protobuf.ByteString
          getTokenBytes() {
        Object ref = token_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          token_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *返回token客户端保存
       * </pre>
       *
       * <code>string token = 6;</code>
       */
      public Builder setToken(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        token_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *返回token客户端保存
       * </pre>
       *
       * <code>string token = 6;</code>
       */
      public Builder clearToken() {
        
        token_ = getDefaultInstance().getToken();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *返回token客户端保存
       * </pre>
       *
       * <code>string token = 6;</code>
       */
      public Builder setTokenBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        token_ = value;
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


      // @@protoc_insertion_point(builder_scope:User)
    }

    // @@protoc_insertion_point(class_scope:User)
    private static final User DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new User();
    }

    public static User getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<User>
        PARSER = new com.google.protobuf.AbstractParser<User>() {
      @Override
      public User parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new User(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<User> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<User> getParserForType() {
      return PARSER;
    }

    @Override
    public User getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_User_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_User_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\nUser.proto\"_\n\004User\022\016\n\006msg_id\030\001 \001(\004\022\n\n\002" +
      "id\030\002 \001(\r\022\014\n\004time\030\003 \001(\004\022\014\n\004name\030\004 \001(\t\022\020\n\010" +
      "password\030\005 \001(\t\022\r\n\005token\030\006 \001(\tB)\n\034org.sq." +
      "gameDemo.common.protoB\tUserProtob\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_User_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_User_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_User_descriptor,
        new String[] { "MsgId", "Id", "Time", "Name", "Password", "Token", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

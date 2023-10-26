package com.chen.serialization;

import java.io.IOException;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 12:30
 * @description
 */
public interface RpcSerialization {
    <T> byte[] serialize(T obj) throws IOException;
    <T> T deserialize(byte[] data,Class<T> clz) throws IOException;
}

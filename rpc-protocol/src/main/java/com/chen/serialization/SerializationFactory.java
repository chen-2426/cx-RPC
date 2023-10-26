package com.chen.serialization;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 12:43
 * @description
 */

public class SerializationFactory {
    public static RpcSerialization getRpcSerialization(byte serializationType){
        SerializationTypeEnum typeEnum = SerializationTypeEnum.findByType(serializationType);
        switch (typeEnum){
            case HESSIAN:
                return new HessianSerialization();
            case JSON:
                return new JsonSerialization();
            default:
                throw new IllegalArgumentException("Serialization type is illegal: "+serializationType);
        }
    }
}

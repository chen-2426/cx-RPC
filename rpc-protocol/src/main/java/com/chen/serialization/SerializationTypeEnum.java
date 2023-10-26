package com.chen.serialization;

import lombok.Getter;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 12:52
 * @description
 */
public enum SerializationTypeEnum {
    HESSIAN(0x10),
    JSON(0x20);
    @Getter
    private final int type;
    SerializationTypeEnum(int type) {
        this.type = type;
    }

    public static SerializationTypeEnum findByType(byte serializationType) {
        for (SerializationTypeEnum value : SerializationTypeEnum.values()) {
            if(value.getType() == serializationType){
                return value;
            }
        }
        return HESSIAN; // 默认为HESSIAN序列化
    }
}

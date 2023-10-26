package com.chen.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/26 12:59
 * @description
 */
public class HessianSerialization implements RpcSerialization {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if(obj==null){
            throw new NullPointerException();
        }
        byte[] result;
        HessianSerializerOutput hessianOutput;
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            hessianOutput = new HessianSerializerOutput(os);
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            result = os.toByteArray();
        }catch (Exception e){
            throw new SerializationException(e);
        }
        return result;
    }
    @SuppressWarnings("unchecked") //警告抑制
    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        if(data == null){
            throw new NullPointerException();
        }
        T result;
        try(ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            HessianSerializerInput hessianSerializerInput = new HessianSerializerInput(is);
            result= (T)hessianSerializerInput.readObject(clz);
        }catch (Exception e){
            throw new SerializationException(e);
        }
        return result;
    }
}

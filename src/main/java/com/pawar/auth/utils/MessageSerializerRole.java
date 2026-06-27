package com.pawar.auth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawar.auth.entity.Role;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageSerializerRole implements Serializer<Role> {

	private static final Logger logger = LoggerFactory.getLogger(MessageSerializerRole.class);

    @Override
    public byte[] serialize(String topic, Role data) {
        byte[] serializedValue = null;
        ObjectMapper om = new ObjectMapper();
        if(data != null) {
            try {
                serializedValue = om.writeValueAsString(data).getBytes();
                logger.info("serializedValue : {} ",serializedValue);
            } catch (JsonProcessingException e) {

            }
        }

        return serializedValue;
    }
}

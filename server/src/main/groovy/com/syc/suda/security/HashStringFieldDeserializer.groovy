package com.syc.suda.security

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class HashStringFieldDeserializer extends StdDeserializer<HashStringField> {

    HashStringFieldDeserializer() {
        this(null)
    }

    HashStringFieldDeserializer(Class<?> vc) {
        super(vc)
    }

    @Override
    HashStringField deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser)

        String cleartext = node.get("cleartext").asText()

        return cleartext != null ? new HashStringField(cleartext) : null
    }
}

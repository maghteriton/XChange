package org.knowm.xchange.bitget;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchange.bitget.model.dto.response.BitgetOrderHistoryResponse;

import java.io.IOException;

public class FeeDetailDeserializer
        extends JsonDeserializer<BitgetOrderHistoryResponse.BitgetFeeDetail> {

    @Override
    public BitgetOrderHistoryResponse.BitgetFeeDetail deserialize(
            JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String jsonText = jsonParser.getText();
        if (jsonText == null || jsonText.isEmpty()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonText, BitgetOrderHistoryResponse.BitgetFeeDetail.class);
    }
}

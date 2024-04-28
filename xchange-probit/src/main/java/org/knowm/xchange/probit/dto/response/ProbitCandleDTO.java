package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class ProbitCandleDTO {

  private final String marketId;
  private final BigDecimal open;
  private final BigDecimal close;
  private final BigDecimal low;
  private final BigDecimal high;
  private final BigDecimal baseVolume;
  private final BigDecimal quoteVolume;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private final Date startTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private final Date endTime;

  @JsonCreator
  public ProbitCandleDTO(
      @JsonProperty("market_id") String marketId,
      @JsonProperty("open") BigDecimal open,
      @JsonProperty("close") BigDecimal close,
      @JsonProperty("low") BigDecimal low,
      @JsonProperty("high") BigDecimal high,
      @JsonProperty("base_volume") BigDecimal baseVolume,
      @JsonProperty("quote_volume") BigDecimal quoteVolume,
      @JsonProperty("start_time") Date startTime,
      @JsonProperty("end_time") Date endTime) {
    this.marketId = marketId;
    this.open = open;
    this.close = close;
    this.low = low;
    this.high = high;
    this.baseVolume = baseVolume;
    this.quoteVolume = quoteVolume;
    this.startTime = startTime;
    this.endTime = endTime;
  }

}

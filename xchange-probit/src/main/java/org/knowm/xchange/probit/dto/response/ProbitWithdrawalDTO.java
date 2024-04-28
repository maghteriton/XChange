package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class ProbitWithdrawalDTO {
  private final String id;
  private final String type;
  private final String currencyId;
  private final String platformId;
  private final String status;
  private final String hash;
  private final Integer confirmations;
  private final String amount;
  private final String address;
  private final String fee;
  private final String destinationTag;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private final Date time;

  private final String feeCurrencyId;

  @JsonCreator
  public ProbitWithdrawalDTO(
      @JsonProperty("id") String id,
      @JsonProperty("type") String type,
      @JsonProperty("currency_id") String currencyId,
      @JsonProperty("platform_id") String platformId,
      @JsonProperty("status") String status,
      @JsonProperty("hash") String hash,
      @JsonProperty("confirmations") Integer confirmations,
      @JsonProperty("amount") String amount,
      @JsonProperty("address") String address,
      @JsonProperty("fee") String fee,
      @JsonProperty("destination_tag") String destinationTag,
      @JsonProperty("time") Date time,
      @JsonProperty("fee_currency_id") String feeCurrencyId) {
    this.id = id;
    this.type = type;
    this.currencyId = currencyId;
    this.platformId = platformId;
    this.status = status;
    this.hash = hash;
    this.confirmations = confirmations;
    this.amount = amount;
    this.address = address;
    this.fee = fee;
    this.destinationTag = destinationTag;
    this.time = time;
    this.feeCurrencyId = feeCurrencyId;
  }
}

package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.knowm.xchange.probit.model.ProbitStatus;
import org.knowm.xchange.probit.model.ProbitTransferType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class ProbitTransferDTO {
  private final String id;
  private final String type;
  private final String status;
  private final BigDecimal amount;
  private final String address;

  private final Date time;

  private final String hash;
  private final String currencyId;
  private final Integer confirmations;
  private final BigDecimal fee;
  private final String destinationTag;
  private final String platformId;
  private final String feeCurrencyId;
  private final String delayedReason;
  private final String paymentServiceName;
  private final String paymentServiceDisplayName;
  private final String crypto;

  @JsonCreator
  public ProbitTransferDTO(
      @JsonProperty("id") String id,
      @JsonProperty("type") String type,
      @JsonProperty("status") String status,
      @JsonProperty("amount") BigDecimal amount,
      @JsonProperty("address") String address,
      @JsonProperty("time") Date time,
      @JsonProperty("hash") String hash,
      @JsonProperty("currency_id") String currencyId,
      @JsonProperty("confirmations") Integer confirmations,
      @JsonProperty("fee") BigDecimal fee,
      @JsonProperty("destination_tag") String destinationTag,
      @JsonProperty("platform_id") String platformId,
      @JsonProperty("fee_currency_id") String feeCurrencyId,
      @JsonProperty("delayed_reason") String delayedReason,
      @JsonProperty("payment_service_name") String paymentServiceName,
      @JsonProperty("payment_service_display_name") String paymentServiceDisplayName,
      @JsonProperty("crypto") String crypto) {
    this.id = id;
    this.type = type;
    this.status = status;
    this.amount = amount;
    this.address = address;
    this.time = time;
    this.hash = hash;
    this.currencyId = currencyId;
    this.confirmations = confirmations;
    this.fee = fee;
    this.destinationTag = destinationTag;
    this.platformId = platformId;
    this.feeCurrencyId = feeCurrencyId;
    this.delayedReason = delayedReason;
    this.paymentServiceName = paymentServiceName;
    this.paymentServiceDisplayName = paymentServiceDisplayName;
    this.crypto = crypto;
  }
}

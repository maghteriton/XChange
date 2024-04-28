package org.knowm.xchange.probit.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ProbitResultDTO<T> {

  private final T data;

  @JsonCreator
  public ProbitResultDTO(@JsonProperty("data") T data) {
    this.data = data;
  }

}

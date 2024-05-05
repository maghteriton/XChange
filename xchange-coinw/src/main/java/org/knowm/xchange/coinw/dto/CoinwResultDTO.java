package org.knowm.xchange.coinw.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinwResultDTO<T> {

  private String code;
  private List<T> data;
  private String msg;
  private boolean success;
}

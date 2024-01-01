package org.knowm.xchange.bingx.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.bingx.dto.BingxSymbolDTO;

import java.util.List;

public class BingxSymbolWrapper {

  private List<BingxSymbolDTO> symbols;

  @JsonCreator
  public BingxSymbolWrapper(@JsonProperty("symbols") List<BingxSymbolDTO> symbols) {
    this.symbols = symbols;
  }

  public List<BingxSymbolDTO> getSymbols() {
    return symbols;
  }
}

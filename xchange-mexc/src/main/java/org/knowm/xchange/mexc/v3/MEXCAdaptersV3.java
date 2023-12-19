package org.knowm.xchange.mexc.v3;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.instrument.Instrument;

public class MEXCAdaptersV3 {

  private MEXCAdaptersV3() {
    // hides the public one
  }

  public static Instrument adaptSymbol(String mexcSymbolV3, Currency counter) {
    String counterName = counter.getSymbol();
    if (mexcSymbolV3.contains(counterName)) {
      String[] symbolTokenized = mexcSymbolV3.split(counterName);
      return new CurrencyPair(symbolTokenized[0], counterName);
    }
    return null;
  }
}

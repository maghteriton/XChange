package org.knowm.xchange.mexc.v3;

import java.io.IOException;
import java.util.List;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.mexc.v3.dto.CoinInfo;
import org.knowm.xchange.mexc.v3.service.MEXCAccountServiceV3;

public class MEXCExchangeV3 extends BaseExchange implements Exchange {

  @Override
  protected void initServices() {
    this.accountService = new MEXCAccountServiceV3(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.mexc.com");
    exchangeSpecification.setHost("mexc.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("MEXC");
    exchangeSpecification.setExchangeDescription("MEXC");

    return exchangeSpecification;
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    List<CoinInfo> allCoins = ((MEXCAccountServiceV3) accountService).getAllCoins();
  }
}

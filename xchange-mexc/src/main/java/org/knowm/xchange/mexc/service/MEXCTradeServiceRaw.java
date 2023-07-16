package org.knowm.xchange.mexc.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.mexc.MEXCAdapters;
import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.dto.trade.MEXCDeal;
import org.knowm.xchange.mexc.dto.trade.MEXCOrder;
import org.knowm.xchange.mexc.dto.trade.MEXCOrderRequestPayload;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MEXCTradeServiceRaw extends MEXCBaseService {
  public MEXCTradeServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public MEXCResult<String> placeOrder(MEXCOrderRequestPayload orderRequestPayload)
      throws IOException {
    return mexcAuthenticated.placeOrder(
        apiKey, nonceFactory, signatureCreator, orderRequestPayload);
  }

  public MEXCResult<List<MEXCOrder>> getOrders(List<String> orderIds) throws IOException {
    return mexcAuthenticated.getOrders(apiKey, nonceFactory, signatureCreator, orderIds);
  }

  public MEXCResult<List<MEXCDeal>> getTradeHistory(CurrencyPair currencyPair) throws IOException {
    return mexcAuthenticated.getDealHistory(
        apiKey, nonceFactory, signatureCreator, MEXCAdapters.convertToMEXCSymbol(currencyPair));
  }

  public MEXCResult<Map<String, String>> cancelOrderById(String oderIds) throws IOException {
    return mexcAuthenticated.cancelOrderById(apiKey, nonceFactory, signatureCreator, oderIds);
  }
}

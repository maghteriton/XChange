package org.knowm.xchange.bingx.service.trade;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.bingx.BingxException;
import org.knowm.xchange.bingx.dto.BingxOrderDTO;
import org.knowm.xchange.bingx.dto.BingxResultDTO;
import org.knowm.xchange.bingx.dto.TradeCommissionRateDTO;
import org.knowm.xchange.bingx.dto.wrapper.BingxCancelLimitOrderWrapper;
import org.knowm.xchange.bingx.dto.wrapper.BingxCreateLimitOrderWrapper;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.trade.TradeService;

import java.io.IOException;
import java.math.BigDecimal;

public class BingxTradeService extends BingxTradeServiceRaw implements TradeService {
  public BingxTradeService(Exchange exchange) {
    super(exchange);
  }


  public TradeCommissionRateDTO getCommissionRate(
          String symbol) throws IOException {
    BingxResultDTO<TradeCommissionRateDTO> commissionRate;
    try {
      commissionRate = tradeAPI.getCommissionRate(
              apiKey, nonceFactory, 5000, signatureCreator, symbol);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return commissionRate.getData();
  }

  public BingxOrderDTO queryOrder(
          String symbol, String orderId) throws IOException {
    BingxResultDTO<BingxOrderDTO> bingxOrderDTOBingxResultDTO;
    try {
      bingxOrderDTOBingxResultDTO = tradeAPI.queryOrder(
              apiKey, nonceFactory, 5000, signatureCreator, symbol, orderId);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return bingxOrderDTOBingxResultDTO.getData();
  }

  public BingxCreateLimitOrderWrapper createLimitOrder(
      String symbol, String side, BigDecimal quantity, BigDecimal price) throws IOException {
    BingxResultDTO<BingxCreateLimitOrderWrapper> limitOrder;
    try {
      limitOrder =
          tradeAPI.createLimitOrder(
              apiKey, nonceFactory, 5000, signatureCreator, symbol, side, "LIMIT", quantity, price);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return limitOrder.getData();
  }

  public BingxCancelLimitOrderWrapper cancelLimitOrder(
          String symbol, String orderId) throws IOException {
    BingxResultDTO<BingxCancelLimitOrderWrapper> limitOrder;
    try {
      limitOrder =
              tradeAPI.cancelLimitOrder(
                      apiKey, nonceFactory, 5000, signatureCreator, symbol, orderId);
    } catch (BingxException e) {
      throw new ExchangeException(e);
    }
    return limitOrder.getData();
  }
}

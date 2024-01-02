package org.knowm.xchange.bingx;

import java.io.IOException;
import java.util.List;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bingx.dto.*;
import org.knowm.xchange.bingx.service.account.BingxAccountService;
import org.knowm.xchange.bingx.service.market.BingxMarketDataService;
import org.knowm.xchange.bingx.service.trade.BingxTradeService;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.exceptions.ExchangeException;

public class BingxExchange extends BaseExchange implements Exchange {

  private static ResilienceRegistries RESILIENCE_REGISTRIES;
  @Override
  protected void initServices() {
    this.marketDataService = new BingxMarketDataService(this, getResilienceRegistries());
    this.tradeService = new BingxTradeService(this, getResilienceRegistries());
    this.accountService = new BingxAccountService(this, getResilienceRegistries());
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri(" https://open-api.bingx.com");
    exchangeSpecification.setHost("https://bingx.com/");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Bingx");
    exchangeSpecification.setExchangeDescription("Bingx");
    exchangeSpecification.getResilience().setRateLimiterEnabled(true);
    return exchangeSpecification;
  }

  @Override
  public ResilienceRegistries getResilienceRegistries() {
    if (RESILIENCE_REGISTRIES == null) {
      RESILIENCE_REGISTRIES = BingxResilience.createRegistries();
    }
    return RESILIENCE_REGISTRIES;
  }

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {
    super.applySpecification(exchangeSpecification);
  }

  @Override
  public void remoteInit() throws IOException, ExchangeException {
    //single commission rate for all currencies
    TradeCommissionRateDTO commissionRate = ((BingxTradeService) tradeService).getCommissionRate("BTC-USDT");
    List<BingxSymbolDTO> symbols = ((BingxMarketDataService) marketDataService).getSymbols().getSymbols();
    List<BingxWalletDTO> wallets = ((BingxAccountService) accountService).getWallets(null);

    this.exchangeMetaData = BingxAdapter.adaptToExchangeMetaData(this.exchangeMetaData, symbols, wallets, commissionRate);
  }

  public void test() {
/*    TradeCommissionRateDTO btcUsdt = ((BingxTradeService) tradeService).getCommissionRate("BTC-USDT");
    List<BingxDepositAddressesDTO> usdt = ((BingxAccountService) accountService).getDepositAddresses("USDT");

    BingxWithdrawWrapper withdraw = ((BingxAccountService) accountService).withdraw("THA9VH9ebtpWcJykNCEhHqiV8ExcCvgiMN",
            null,
            new BigDecimal(8),
            "USDT",
            "TRC20",
            "1");


    BingxCreateLimitOrderWrapper buy = ((BingxTradeService) tradeService).createLimitOrder("XRP-USDT", "BUY", new BigDecimal("10"), new BigDecimal("0.59"));
    BingxOrderDTO bingxOrderDTO = ((BingxTradeService) tradeService).queryOrder("XRP-USDT", String.valueOf(buy.getOrderId()));
    BingxCancelLimitOrderWrapper bingxCancelLimitOrderWrapper = ((BingxTradeService) tradeService).cancelLimitOrder("XRP-USDT", String.valueOf(buy.getOrderId()));

    List<BingxSymbolDTO> symbols = ((BingxMarketDataService) marketDataService).getSymbols();
    BingxMarketDepthDTO marketDepth = ((BingxMarketDataService) marketDataService).getMarketDepth("BTC-USDT");
    List<List<String>> kLineData = ((BingxMarketDataService) marketDataService).getKLineData("BTC-USDT", KLineInterval.min30, 5, null, null);
    List<BingxBalanceDTO> balances = ((BingxAccountService) accountService).getBalances();
    List<BingxDepositDTO> depositHistory = ((BingxAccountService) accountService).getDepositHistory(null);
    List<BingxWalletDTO> wallets = ((BingxAccountService) accountService).getWallets(null);*/
  }

}

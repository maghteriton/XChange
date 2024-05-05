package org.knowm.xchange.coinw.service.account;

import java.io.IOException;
import java.util.List;
import org.knowm.xchange.client.ResilienceRegistries;
import org.knowm.xchange.coinw.CoinwExchange;
import org.knowm.xchange.coinw.dto.response.CoinwDepositAddressResponseDTO;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.DepositAddress;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.*;

public class CoinwAccountService extends CoinwAccountServiceRaw implements AccountService {


  private static final String CONTRACT_ADDR_NOT_SUPPORTED_BY_API = "notSupportedByAPI";

  public CoinwAccountService(CoinwExchange exchange, ResilienceRegistries resilienceRegistries) {
    super(exchange, resilienceRegistries);
  }


  @Override
  public DepositAddress getDepositAddress(Currency currency, String chain) throws IOException {
    List<CoinwDepositAddressResponseDTO> data = getDepositAddressData(currency, chain).getData();


    return null;
  }
}

package org.knowm.xchange.mexc.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.mexc.dto.MEXCResult;
import org.knowm.xchange.mexc.dto.account.*;

public class MEXCAccountServiceRaw extends MEXCBaseService {

  private static final String COLON = ":";

  public MEXCAccountServiceRaw(Exchange exchange) {
    super(exchange);
  }

  public MEXCResult<Map<String, MEXCBalance>> getWalletBalances() throws IOException {
    return mexcAuthenticated.getWalletBalances(apiKey, nonceFactory, signatureCreator);
  }

  public MEXCWithdrawal createMEXCWithdraw(
      Currency currency, String chain, BigDecimal amount, AddressWithTag addressWithTag)
      throws IOException {
    // Transaction MEMO can be entered after the address separated by a :, format: Address:MEMO
    String address =
        addressWithTag.getAddressTag() != null
            ? addressWithTag.getAddress() + COLON + addressWithTag.getAddressTag()
            : addressWithTag.getAddress();
    MEXCWithdrawRequestPayload mexcWithdrawRequestPayload =
        new MEXCWithdrawRequestPayload(currency.getCurrencyCode(), chain, amount, address, null);

    return mexcAuthenticated
        .withdrawFunds(apiKey, nonceFactory, signatureCreator, mexcWithdrawRequestPayload)
        .getData();
  }

  public MEXCAssetTransferHistory<MEXCDepositRecord> getMEXCDeposits() throws IOException {
    return mexcAuthenticated.getDepositList(apiKey, nonceFactory, signatureCreator).getData();
  }

  public MEXCAssetTransferHistory<MEXCWithdrawalRecord> getMEXCWithdrawals() throws IOException {
    return mexcAuthenticated.getWithdrawList(apiKey, nonceFactory, signatureCreator).getData();
  }

  public MEXCDepositAddress getMEXCDepositAddress(Currency currency) throws IOException {
    return mexcAuthenticated
        .getDepositAddressList(apiKey, nonceFactory, signatureCreator, currency.getCurrencyCode())
        .getData();
  }
}

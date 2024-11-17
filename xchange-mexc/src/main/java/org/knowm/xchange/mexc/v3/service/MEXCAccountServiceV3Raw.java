package org.knowm.xchange.mexc.v3.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AddressWithTag;
import org.knowm.xchange.mexc.v3.dto.CoinInfo;
import org.knowm.xchange.mexc.v3.dto.WithdrawResponse;
import org.knowm.xchange.mexc.v3.UserDataClient;

public class MEXCAccountServiceV3Raw extends MEXCBaseServiceV3 {

    public MEXCAccountServiceV3Raw(Exchange exchange) {
        super(exchange);
        UserDataClient.init(exchange.getExchangeSpecification().getApiKey(), exchange.getExchangeSpecification().getSecretKey());
    }

    public List<CoinInfo> getAllCoins() throws IOException {
        return mexcAuthenticatedV3.getCurrencyInformation(apiKeyV3, signatureCreatorV3, nonceFactoryV3);
    }

    public List<String> getDefaultSymbols() throws IOException {
        return mexcAuthenticatedV3.getDefaultSymbols().getData();
    }

    public String createMEXCWithdraw(Currency currency, String chain, BigDecimal amount, AddressWithTag addressWithTag) {

        HashMap<String, String> withdrawParams = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("coin", currency.getCurrencyCode())
                .put("address", addressWithTag.getAddress())
                .put("amount", amount.toPlainString())
                .put("network", chain)
                .put("recvWindow","5000")
                .build());

        if(addressWithTag.getAddressTag() != null) {
            withdrawParams.put("memo", addressWithTag.getAddressTag());
        }

        return UserDataClient.post("/api/v3/capital/withdraw/apply", withdrawParams, new TypeReference<WithdrawResponse>() {}).getId();
    }
}

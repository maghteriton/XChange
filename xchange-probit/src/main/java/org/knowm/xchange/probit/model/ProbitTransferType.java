package org.knowm.xchange.probit.model;

public enum ProbitTransferType {
  DEPOSIT("deposit"),
  WITHDRAWAL("withdrawal");

  private final String type;

  private ProbitTransferType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}

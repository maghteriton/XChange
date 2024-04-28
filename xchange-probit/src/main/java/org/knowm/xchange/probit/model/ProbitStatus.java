package org.knowm.xchange.probit.model;

public enum ProbitStatus {
  REQUESTED("requested"),
  PENDING("pending"),
  CONFIRMING("confirming"),
  CONFIRMED("confirmed"),
  APPLYING("applying"),
  DONE("done"),
  CANCELLING("cancelling"),
  CANCELLED("cancelled"),
  FAILED("failed");

  private final String status;

  ProbitStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}

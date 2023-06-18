package org.knowm.xchange.mexc.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MEXCAssetTransferHistory<T> {

  private final Integer pageSize;
  private final Integer totalPage;
  private final Integer totalSize;
  private final Integer pageNum;
  private final List<T> recordList;

  public MEXCAssetTransferHistory(
      @JsonProperty("page_size") Integer pageSize,
      @JsonProperty("total_page") Integer totalPage,
      @JsonProperty("total_size") Integer totalSize,
      @JsonProperty("page_num") Integer pageNum,
      @JsonProperty("result_list") List<T> recordList) {
    this.pageSize = pageSize;
    this.totalPage = totalPage;
    this.totalSize = totalSize;
    this.pageNum = pageNum;
    this.recordList = recordList;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public Integer getTotalPage() {
    return totalPage;
  }

  public Integer getTotalSize() {
    return totalSize;
  }

  public Integer getPageNum() {
    return pageNum;
  }

  public List<T> getRecordList() {
    return recordList;
  }
}

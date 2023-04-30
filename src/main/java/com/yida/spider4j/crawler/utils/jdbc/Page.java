package com.yida.spider4j.crawler.utils.jdbc;

import java.util.List;

/**
 * @author yida
 * @package com.witarchive.common.jdbc
 * @date 2023-04-27 09:39
 * @description 分页对象
 */
public class Page<T> {
    //默认每页显示条数
    public static final int DEFAULT_PAGE_SIZE = 10;
    //默认每页显示条数最大值
    public static final int DEFAULT_MAX_PAGE_SIZE = 1000;
    //总纪录数
    private long totalRecordCount;
    //每页显示条数
    private int pageSize = DEFAULT_PAGE_SIZE;
    //总页数
    private int totalPageCount;

    //当前页码(从1开始计算)
    private int currentPage;
    //数据起始位置(从零开始计算)
    private int startIndex;
    //起始页码
    private int startPage;
    //结束页码
    private int endPage;
    //当前页的数据集合
    private List<T> pageDataList;

    public Page(int currentPage, long totalRecordCount, List<T> pageDataList) {
        this(currentPage, totalRecordCount, DEFAULT_PAGE_SIZE, pageDataList);
    }

    public Page(int currentPage, long totalRecordCount, int pageSize, List<T> pageDataList) {
        this.totalRecordCount = totalRecordCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.pageDataList = pageDataList;
        //1.算出总页数
        if (this.totalRecordCount % this.pageSize == 0) {
            this.totalPageCount = (int) (this.totalRecordCount / this.pageSize);
        } else {
            this.totalPageCount = (int) (this.totalRecordCount / this.pageSize + 1);
        }

        //2.算出页号在数据库的起始位置
        this.startIndex = (this.currentPage - 1) * this.pageSize;

        //3.算出jsp页面的起始页码和结束页码，小于10个，全显示，大于10个，只显示10个
        if (this.totalPageCount <= 10) {
            this.startPage = 1;
            this.endPage = this.totalPageCount;
        } else {
            this.startPage = this.currentPage - 4;
            this.endPage = this.currentPage + 5;

            if (this.startPage < 1) {
                this.startPage = 1;
                this.endPage = 10;
            }
            if (this.endPage > this.totalPageCount) {
                this.endPage = this.totalPageCount;
                this.startPage = this.totalPageCount - 9;
            }
        }
    }

    public long getTotalRecordCount() {
        return totalRecordCount;
    }

    public void setTotalRecordCount(long totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public List<T> getPageDataList() {
        return pageDataList;
    }

    public void setPageDataList(List<T> pageDataList) {
        this.pageDataList = pageDataList;
    }
}

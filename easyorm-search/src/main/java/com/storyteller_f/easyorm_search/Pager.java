package com.storyteller_f.easyorm_search;

import com.storyteller_f.sql_query.function.Count;
import com.storyteller_f.sql_query.obtain.Obtain;
import com.storyteller_f.sql_query.query.Select;

import java.util.List;

public class Pager {
    private Obtain obtain;
    private int totalCount;
    private int pageCount;
    private int onceShowCount;
    private int currentPageIndex;

    public Pager(int onceShowCount, int currentPageIndex,Obtain obtain) {
        this.onceShowCount = onceShowCount;
        this.currentPageIndex = currentPageIndex;
        this.obtain=obtain;
    }

    public Pager() {
    }
    public <T> Select<T> getSelect(Class<T> tClass) {
        return obtain.getSelect(tClass).limit(getOffset(),onceShowCount);
    }

    public <T> Select<T> getSelect() {
        return obtain.<T>getSelect().limit(getOffset(),onceShowCount);
    }

    private int getOffset() {
        return onceShowCount * (currentPageIndex - 1);
    }

    public <T> List<T> execute(Class<T> tClass) throws Exception {
        //获取全部行数
        //todo 如果是datasource，连续访问数据库，可以延迟关闭
        Count execute = new Select<Count>(obtain).function(Count.class,tClass).one();
        totalCount= execute.getCount();
        //判断是否超出了范围
        if (getOffset()>totalCount){
            System.out.println("超过最大");
        }
        return getSelect(tClass).execute();
    }
    public Pager(int totalCount, int pageCount, int onceShowCount, int currentPageIndex) {
        this.totalCount = totalCount;
        this.pageCount = pageCount;
        this.onceShowCount = onceShowCount;
        this.currentPageIndex = currentPageIndex;
    }

    public Obtain getObtain() {
        return obtain;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getOnceShowCount() {
        return onceShowCount;
    }

    public void setOnceShowCount(int onceShowCount) {
        this.onceShowCount = onceShowCount;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }
}

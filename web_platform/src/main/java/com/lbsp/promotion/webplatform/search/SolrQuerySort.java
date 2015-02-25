package com.lbsp.promotion.webplatform.search;

/**
 * Created by Barry on 2015/2/24.
 */
public class SolrQuerySort {

    private String column;

    private Order order;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public SolrQuerySort(String column){
        this.column = column;
        this.order = Order.ASC;
    }

    public SolrQuerySort(String column,Order order){
        this.column = column;
        this.order = order;
    }

    public enum Order {
        ASC {
            public String value() {
                return "asc";
            }
        },
        DESC {
            public String value() {
                return "desc";
            }
        }
    }

}

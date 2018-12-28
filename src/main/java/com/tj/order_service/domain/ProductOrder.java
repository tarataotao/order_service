package com.tj.order_service.domain;



import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品订单实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductOrder implements Serializable {

    private int id ;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 订单号
     */
    private String tradeNo;
    /**
     * 价格,分
     */
    private int price;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 用户名
     */
    private String userName;

}

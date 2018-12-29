package com.tj.order_service.service;

import com.tj.order_service.domain.ProductOrder;

/**
 * 订单业务
 */
public interface ProductOrderService {
    /**
     * 下单接口
     * @param userId
     * @param productId
     * @return
     */
    ProductOrder save(int userId, int productId);

    /**
     * 使用openReign的方法来调用服务
     * @param userId
     * @param productId
     * @return
     */
    ProductOrder saveByOpenReign(int userId,int productId);
}

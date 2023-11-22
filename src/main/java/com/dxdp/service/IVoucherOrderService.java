package com.dxdp.service;

import com.dxdp.dto.Result;
import com.dxdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyang
 * @since 2021-12-22
 */
public interface IVoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);

    //适用于适用于1.0和2.0的seckillVoucher()
    // Result createVoucherOrder(Long voucherId);

    void createVoucherOrder(VoucherOrder voucherOrder);
}

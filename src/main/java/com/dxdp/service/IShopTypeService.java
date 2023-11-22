package com.dxdp.service;

import com.dxdp.dto.Result;
import com.dxdp.entity.ShopType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyang
 * @since 2021-12-22
 */
public interface IShopTypeService extends IService<ShopType> {

    Result queryByRedis();
}

package com.dxdp.service;

import com.dxdp.dto.Result;
import com.dxdp.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyang
 * @since 2021-12-22
 */
public interface IShopService extends IService<Shop> {

    Result queryById(Long id);

    Result updateShop(Shop shop);

    public void saveShop2Redis(Long id, Long expireSeconds);

    Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
}

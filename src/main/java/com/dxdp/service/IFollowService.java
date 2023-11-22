package com.dxdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdp.dto.Result;
import com.dxdp.entity.Follow;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xuyang
 * @since 2021-12-22
 */
public interface IFollowService extends IService<Follow> {

    Result follow(Long followUserId, Boolean isFollow);

    Result isFollow(Long followUserId);

    Result followCommons(Long id);
}

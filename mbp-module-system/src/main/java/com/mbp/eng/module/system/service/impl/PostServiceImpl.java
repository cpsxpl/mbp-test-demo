package com.mbp.eng.module.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mbp.eng.framework.common.enums.CommonStatusEnum;
import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.common.util.object.BeanUtils;
import com.mbp.eng.module.system.domain.SystemPost;
import com.mbp.eng.module.system.mapper.PostMapper;
import com.mbp.eng.module.system.service.PostService;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.PostPageReqVO;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.PostSaveReqVO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.mbp.eng.framework.common.util.collection.CollectionUtils.convertMap;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.POST_CODE_DUPLICATE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.POST_NAME_DUPLICATE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.POST_NOT_ENABLE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.POST_NOT_FOUND;


/**
 * 岗位 Service 实现类
 */
@Service("postService")
@Validated
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;

    @Override
    public Long createPost(PostSaveReqVO createReqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(null, createReqVO.getName(), createReqVO.getCode());

        // 插入岗位
        SystemPost post = BeanUtils.toBean(createReqVO, SystemPost.class);
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public void updatePost(PostSaveReqVO updateReqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(updateReqVO.getId(), updateReqVO.getName(), updateReqVO.getCode());

        // 更新岗位
        SystemPost updateObj = BeanUtils.toBean(updateReqVO, SystemPost.class);
        postMapper.updateById(updateObj);
    }

    @Override
    public void deletePost(Long id) {
        // 校验是否存在
        validatePostExists(id);
        // 删除岗位
        postMapper.deleteById(id);
    }

    @Override
    public void deletePostList(List<Long> ids) {
        postMapper.deleteByIds(ids);
    }

    private void validatePostForCreateOrUpdate(Long id, String name, String code) {
        // 校验自己存在
        validatePostExists(id);
        // 校验岗位名的唯一性
        validatePostNameUnique(id, name);
        // 校验岗位编码的唯一性
        validatePostCodeUnique(id, code);
    }

    private void validatePostNameUnique(Long id, String name) {
        SystemPost post = postMapper.selectByName(name);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(POST_NAME_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw exception(POST_NAME_DUPLICATE);
        }
    }

    private void validatePostCodeUnique(Long id, String code) {
        SystemPost post = postMapper.selectByCode(code);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(POST_CODE_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw exception(POST_CODE_DUPLICATE);
        }
    }

    private void validatePostExists(Long id) {
        if (id == null) {
            return;
        }
        if (postMapper.selectById(id) == null) {
            throw exception(POST_NOT_FOUND);
        }
    }

    @Override
    public List<SystemPost> getPostList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return postMapper.selectByIds(ids);
    }

    @Override
    public List<SystemPost> getPostList(Collection<Long> ids, Collection<Integer> statuses) {
        return postMapper.selectList(ids, statuses);
    }

    @Override
    public PageResult<SystemPost> getPostPage(PostPageReqVO reqVO) {
        return postMapper.selectPage(reqVO);
    }

    @Override
    public SystemPost getPost(Long id) {
        return postMapper.selectById(id);
    }

    @Override
    public void validatePostList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<SystemPost> posts = postMapper.selectByIds(ids);
        Map<Long, SystemPost> postMap = convertMap(posts, SystemPost::getId);
        // 校验
        ids.forEach(id -> {
            SystemPost post = postMap.get(id);
            if (post == null) {
                throw exception(POST_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(post.getStatus())) {
                throw exception(POST_NOT_ENABLE, post.getName());
            }
        });
    }
}

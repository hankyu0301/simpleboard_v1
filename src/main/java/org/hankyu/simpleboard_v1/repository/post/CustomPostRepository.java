package org.hankyu.simpleboard_v1.repository.post;

import org.hankyu.simpleboard_v1.dto.post.PostReadCondition;
import org.hankyu.simpleboard_v1.dto.post.PostSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
    Page<PostSimpleDto> findAllByCondition(PostReadCondition cond);
}

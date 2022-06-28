package org.hankyu.simpleboard_v1.repository.post;

import org.hankyu.simpleboard_v1.entity.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

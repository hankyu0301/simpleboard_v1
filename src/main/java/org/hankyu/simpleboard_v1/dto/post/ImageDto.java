package org.hankyu.simpleboard_v1.dto.post;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.hankyu.simpleboard_v1.entity.post.Image;

@Data
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String originName;
    private String uniqueName;
    public static ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getOriginName(), image.getUniqueName());
    }
}

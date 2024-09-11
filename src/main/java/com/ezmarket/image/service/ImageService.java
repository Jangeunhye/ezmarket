package com.ezmarket.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.ezmarket.image.domain.entity.Image;
import com.ezmarket.image.dto.ImageDto;
import com.ezmarket.image.repository.ImageRepository;
import com.ezmarket.item.domain.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;


    public void saveImage(Item item, ArrayList<MultipartFile> files) throws Exception{

        // S3에 업로드
        List<String> imageUrlList = s3Service.uploadFile(files);

        // Image 엔티티로 변환하기
        imageUrlList.forEach((url)->{
            Image image = Image.builder()
                    .imageUrl(url)
                    .build();

            // 양방향 매핑
            item.addItemImages(image);
            image.linkItem(item);
        });



    }

}

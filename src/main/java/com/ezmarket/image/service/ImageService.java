package com.ezmarket.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.ezmarket.image.domain.entity.Image;
import com.ezmarket.image.dto.ImageDto;
import com.ezmarket.image.repository.ImageRepository;
import com.ezmarket.item.domain.entity.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    public void updateImage(Item item, ArrayList<MultipartFile> files) throws Exception{

        // 이미지 수정있을 때
        if(!files.isEmpty()){
            // 기존 이미지 파일 가져오기
            List<Image> savedImageList = imageRepository.findAllByItem(item);

            // 기존 이미지 삭제
            savedImageList.forEach((image)-> {
                imageRepository.delete(image);

                // .com/의 글자수가 5이기 때문
                String fileUrl = image.getImageUrl().substring(image.getImageUrl().lastIndexOf(".com/")+5);
                s3Service.deleteFile(fileUrl);
            });

            // item 의 imageLIst 영속성 컥테스트 초기화
            item.getImageList().clear();

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

    public void deleteImage(Item item) throws Exception {
            // 기존 이미지 파일 가져오기
            List<Image> imageList = imageRepository.findAllByItem(item);

            // 기존 이미지 삭제
            imageList.forEach((image)-> {

                // .com/의 글자수가 5이기 때문
                String fileUrl = image.getImageUrl().substring(image.getImageUrl().lastIndexOf(".com/")+5);
                s3Service.deleteFile(fileUrl);
            });

    }

    }

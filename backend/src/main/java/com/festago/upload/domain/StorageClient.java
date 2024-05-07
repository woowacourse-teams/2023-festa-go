package com.festago.upload.domain;

import org.springframework.web.multipart.MultipartFile;

public interface StorageClient {

    /**
     * MultipartFile을 보관(영속)하는 클래스 <br/> 업로드 작업이 끝나면, 업로드한 파일의 정보를 가진 UploadStatus.UPLOADED 상태의 UploadFile를 반환해야 한다.
     * <br/> 반환된 UploadFile을 영속하는 책임은 해당 클래스를 사용하는 클라이언트가 구현해야 한다. <br/>
     *
     * @param file 업로드 할 MultipartFile
     * @return UploadStatus.PENDING 상태의 영속되지 않은 UploadFile 엔티티
     */
    UploadFile storage(MultipartFile file);
}

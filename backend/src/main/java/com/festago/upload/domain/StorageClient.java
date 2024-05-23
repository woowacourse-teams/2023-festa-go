package com.festago.upload.domain;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StorageClient {

    /**
     * MultipartFile을 보관(영속)하는 메서드 <br/> 업로드 작업이 끝나면, 업로드한 파일의 정보를 가진 UploadStatus.UPLOADED 상태의 UploadFile를 반환해야 한다.
     * <br/> 반환된 UploadFile을 영속하는 책임은 해당 메서드를 사용하는 클라이언트가 구현해야 한다. <br/>
     *
     * @param file 업로드 할 MultipartFile
     * @return UploadStatus.PENDING 상태의 영속되지 않은 UploadFile 엔티티
     */
    UploadFile storage(MultipartFile file);

    /**
     * 업로드 파일을 삭제하는 메서드 <br/> 삭제 작업이 끝나면, UploadFile이 가진 정보에 대한 업로드 된 파일이 없으므로, 인자로 들어온 UploadFiles를 삭제해야 한다. <br/> 삭제가
     * 끝나고 UploadFile을 삭제하는 책임은 해당 메서드를 사용하는 클라이언트가 구현해야 한다. <br/>
     *
     * @param uploadFiles 삭제하려는 업로드 된 파일의 정보가 담긴 UploadFile 목록
     */
    void delete(List<UploadFile> uploadFiles);
}

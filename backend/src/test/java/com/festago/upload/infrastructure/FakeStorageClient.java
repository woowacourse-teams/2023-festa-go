package com.festago.upload.infrastructure;

import com.festago.support.fixture.UploadFileFixture;
import com.festago.upload.domain.StorageClient;
import com.festago.upload.domain.UploadFile;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class FakeStorageClient implements StorageClient {

    @Override
    public UploadFile storage(MultipartFile file) {
        return UploadFileFixture.builder().build();
    }

    @Override
    public void delete(List<UploadFile> uploadFiles) {
        // NOOP
    }
}

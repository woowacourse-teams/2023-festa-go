package com.festago.upload.application;

import static java.util.stream.Collectors.toSet;

import com.festago.upload.domain.FileOwnerType;
import com.festago.upload.domain.UploadFile;
import com.festago.upload.repository.UploadFileRepository;
import com.festago.upload.util.UriUploadFileIdParser;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadFileStatusChangeService {

    private final UploadFileRepository uploadFileRepository;

    /**
     * 인자로 들어오는 fileUris에 해당하는 UploadFile을 모두 찾아 해당 UploadFile의 주인을 설정하고, ATTACHED 상태로 변경한다. <br/>
     *
     * @param ownerId   상태를 변경할 UploadFile의 주인 식별자
     * @param ownerType 상태를 변경할 UploadFile의 주인 타입
     * @param fileUris  주인을 설정하고 ATTACHED 상태로 변경할 UploadFileUri 목록
     */
    public void changeAttached(Long ownerId, FileOwnerType ownerType, Collection<String> fileUris) {
        Set<UUID> uploadFileIds = parseFileIds(fileUris);
        uploadFileRepository.findByIdIn(uploadFileIds)
            .forEach(uploadFile -> uploadFile.changeAttached(ownerId, ownerType));
    }

    private Set<UUID> parseFileIds(Collection<String> fileUris) {
        return fileUris.stream()
            .map(UriUploadFileIdParser::parse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toSet());
    }

    /**
     * 인자로 들어오는 ownerId와 ownerType에 해당하는 UploadFile을 모두 찾고 상태를 새롭게 변경한다. <br/>
     *
     * @param ownerId   상태를 변경할 UploadFile의 주인 식별자
     * @param ownerType 상태를 변경할 UploadFile의 주인 타입
     * @param fileUris  새롭게 변경된 UploadFileUri 목록
     */
    public void changeRenewal(Long ownerId, FileOwnerType ownerType, Collection<String> fileUris) {
        Set<UUID> uploadFileIds = parseFileIds(fileUris);
        uploadFileRepository.findAllByOwnerIdAndOwnerType(ownerId, ownerType)
            .forEach(uploadFile -> uploadFile.renewalStatus(ownerId, ownerType, uploadFileIds));
    }

    /**
     * 인자로 들어오는 ownerId와 ownerType에 해당하는 UploadFile을 모두 찾고 ABANDONED 상태로 변경한다.
     *
     * @param ownerId   상태를 변경할 UploadFile의 주인 식별자
     * @param ownerType 상태를 변경할 UploadFile의 주인 타입
     */
    public void changeAllAbandoned(Long ownerId, FileOwnerType ownerType) {
        uploadFileRepository.findAllByOwnerIdAndOwnerType(ownerId, ownerType)
            .forEach(UploadFile::changeAbandoned);
    }
}

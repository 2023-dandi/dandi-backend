package dandi.dandi.member.application.service;

import com.amazonaws.SdkClientException;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.image.application.out.ImageUploader;
import dandi.dandi.image.exception.ImageUploadFailedException;
import dandi.dandi.member.application.port.in.ProfileImageUpdateResponse;
import dandi.dandi.member.application.port.in.ProfileImageUseCase;
import dandi.dandi.member.application.port.out.MemberPersistencePort;
import dandi.dandi.member.domain.Member;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileImageService implements ProfileImageUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ProfileImageService.class);

    private static final String S3_FILE_KEY_FORMAT = "%s/%s_%s";

    private final MemberPersistencePort memberPersistencePort;
    private final ImageUploader imageUploader;
    private final String initialProfileImageUrl;
    private final String profileImageDir;
    private final String imageAccessUrl;

    public ProfileImageService(MemberPersistencePort memberPersistencePort, ImageUploader imageUploader,
                               @Value("${image.member-initial-profile-image-url}") String initialProfileImageUrl,
                               @Value("${image.profile-dir}") String profileImageDir,
                               @Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.memberPersistencePort = memberPersistencePort;
        this.imageUploader = imageUploader;
        this.initialProfileImageUrl = initialProfileImageUrl;
        this.profileImageDir = profileImageDir;
        this.imageAccessUrl = imageAccessUrl;
    }

    @Override
    @Transactional
    public ProfileImageUpdateResponse updateProfileImage(Long memberId, MultipartFile profileImage) {
        Member member = memberPersistencePort.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
        String fileKey = generateFileKey(profileImage);
        memberPersistencePort.updateProfileImageUrl(member.getId(), fileKey);
        uploadImage(fileKey, profileImage);
        deleteCurrentProfileImageIfNotProfileImage(member);
        return new ProfileImageUpdateResponse(imageAccessUrl + fileKey);
    }

    private void uploadImage(String fileKey, MultipartFile profileImage) {
        try {
            imageUploader.upload(fileKey, profileImage.getInputStream());
        } catch (SdkClientException | IOException e) {
            throw new ImageUploadFailedException();
        }
    }

    private String generateFileKey(MultipartFile profileImage) {
        String uuid = UUID.randomUUID().toString();
        return String.format(S3_FILE_KEY_FORMAT, profileImageDir, uuid, profileImage.getOriginalFilename());
    }

    private void deleteCurrentProfileImageIfNotProfileImage(Member member) {
        if (!member.hasProfileImgUrl(initialProfileImageUrl)) {
            deletePreviousProfileImage(member.getProfileImgUrl());
        }
    }

    private void deletePreviousProfileImage(String currentProfileImageUrl) {
        try {
            imageUploader.delete(currentProfileImageUrl);
        } catch (SdkClientException | IOException e) {
            logger.info("Profile Image Deletion Failed : " + currentProfileImageUrl);
        }
    }

    public boolean isInitialProfileImage(String profileImage) {
        return profileImage.equals(initialProfileImageUrl);
    }
}

package dandi.dandi.member.application;

import com.amazonaws.SdkClientException;
import dandi.dandi.auth.exception.UnauthorizedException;
import dandi.dandi.image.exception.ImageUploadFailedException;
import dandi.dandi.member.application.dto.ProfileImageUpdateResponse;
import dandi.dandi.member.domain.Member;
import dandi.dandi.member.domain.MemberRepository;
import dandi.dandi.member.domain.ProfileImageUploader;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileImageService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileImageService.class);

    private static final String S3_FILE_KEY_FORMAT = "%s/%s_%s";

    private final MemberRepository memberRepository;
    private final ProfileImageUploader profileImageUploader;
    private final String initialProfileImageUrl;
    private final String profileImageDir;

    public ProfileImageService(MemberRepository memberRepository, ProfileImageUploader profileImageUploader,
                               @Value("${image.member-initial-profile-image-url}") String initialProfileImageUrl,
                               @Value("${image.profile-dir}") String profileImageDir) {
        this.memberRepository = memberRepository;
        this.profileImageUploader = profileImageUploader;
        this.initialProfileImageUrl = initialProfileImageUrl;
        this.profileImageDir = profileImageDir;
    }

    @Transactional
    public ProfileImageUpdateResponse updateProfileImage(Long memberId, MultipartFile profileImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(UnauthorizedException::notExistentMember);
        String fileKey = generateFileKey(profileImage);
        memberRepository.updateProfileImageUrl(member.getId(), fileKey);
        uploadImage(fileKey, profileImage);
        deleteCurrentProfileImageIfNotProfileImage(member);
        return new ProfileImageUpdateResponse(fileKey);
    }

    private void uploadImage(String fileKey, MultipartFile profileImage) {
        try {
            profileImageUploader.upload(fileKey, profileImage.getInputStream());
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
            profileImageUploader.delete(currentProfileImageUrl);
        } catch (SdkClientException | IOException e) {
            logger.info("Profile Image Deletion Failed : " + currentProfileImageUrl);
        }
    }
}

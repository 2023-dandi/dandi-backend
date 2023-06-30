package dandi.dandi.image.aspect;

import dandi.dandi.advice.InternalServerException;
import dandi.dandi.image.application.in.ImageResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ImageAccessUrlAspect {

    private final String imageAccessUrl;

    public ImageAccessUrlAspect(@Value("${cloud.aws.cloud-front.uri}") String imageAccessUrl) {
        this.imageAccessUrl = imageAccessUrl;
    }

    @Around(value = "@annotation(imageUrlInclusion)")
    public Object appendImageAccessUrl(ProceedingJoinPoint joinPoint, ImageUrlInclusion imageUrlInclusion) {
        try {
            Object returnObj = joinPoint.proceed();
            ImageResponse imageResponse = (ImageResponse) returnObj;
            return imageResponse.addImageAccessUrl(this.imageAccessUrl);
        } catch (Throwable e) {
            throw new InternalServerException("AOP 에러");
        }
    }
}

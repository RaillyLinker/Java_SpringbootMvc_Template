package com.raillylinker.util_components;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// [커스텀 유틸 함수 모음]
@Component
public class CustomUtil {
    // (파일명, 경로, 확장자 분리 함수)
    // sample.jpg -> sample, jpg
    public @NotNull FilePathParts splitFilePath(
            @NotNull String filePath
    ) {
        // 확장자가 없다면 전체 파일 이름이 그대로 fileName
        @NotNull String fileName = filePath.contains(".") ? filePath.substring(0, filePath.lastIndexOf(".")) : filePath;
        @Nullable String extension = filePath.contains(".") ? filePath.substring(filePath.lastIndexOf(".") + 1) : null;

        return new FilePathParts(fileName, extension);
    }

    @Data
    public static class FilePathParts {
        private final @NotNull String fileName;
        private final @Nullable String extension;
    }


    // ----
    // (Multipart File 을 로컬에 저장)
    // 반환값 : 저장된 파일명
    public @NotNull String multipartFileLocalSave(
            // 파일을 저장할 로컬 위치 Path
            @NotNull Path saveDirectoryPath,
            // 저장할 파일명(파일명 뒤에 (현재 일시 yyyy_MM_dd_'T'_HH_mm_ss_SSS_z) 가 붙습니다.)
            // null 이라면 multipartFile 의 originalFilename 을 사용합니다.
            @Nullable String fileName,
            // 저장할 MultipartFile
            @NotNull MultipartFile multipartFile
    ) {
        @NotNull String savedFileName = "";

        // 파일 저장 기본 디렉토리 생성
        try {
            Files.createDirectories(saveDirectoryPath);

            // 원본 파일명(with suffix)
            @NotNull String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

            @NotNull FilePathParts fileNameSplit = splitFilePath(originalFilename);

            // 확장자 없는 파일명
            @NotNull String fileNameWithoutExtension = (fileName != null) ? fileName : fileNameSplit.getFileName();

            // 확장자
            @NotNull String fileExtension = (fileNameSplit.getExtension() != null) ? "." + fileNameSplit.getExtension() : "";

            // 저장할 파일명 생성
            savedFileName =
                    fileNameWithoutExtension + "(" +
                            LocalDateTime.now().atZone(ZoneId.systemDefault())
                                    .format(DateTimeFormatter.ofPattern("yyyy_MM_dd_'T'_HH_mm_ss_SSS_z")) +
                            ")" + fileExtension;

            // 파일 저장
            @NotNull Path targetPath = saveDirectoryPath.resolve(savedFileName).normalize();
            multipartFile.transferTo(targetPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return savedFileName;
    }


    // ----
    // (byteArray 를 Hex String 으로 반환)
    public @NotNull String bytesToHex(
            @NotNull byte[] bytes
    ) {
        @NotNull StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}

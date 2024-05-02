package com.festago.upload.util;

public class FileNameExtensionParser {

    private FileNameExtensionParser() {
    }

    /**
     * 파일 이름의 확장자를 추출합니다. <br/> 확장자가 있으면 .png 와 같은 형식의 확장자를 반환합니다. <br/> 확장자가 없으면 빈 문자열을 반환합니다. <br/>
     *
     * @param filename null이 아닌 확장자를 추출할 파일 이름
     * @return .png, .jpg 와 같은 확장자, 만약 확장자가 없으면 빈 문자열
     */
    public static String parse(String filename) {
        filename = filename.strip();
        int lastIndexOfDot = filename.lastIndexOf(".");
        if (lastIndexOfDot == -1 || lastIndexOfDot == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastIndexOfDot);
    }
}

package com.ray.etc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.util.FileCopyUtils;

import com.ray.vodto.UploadedFile;

/**
 * @packageName : com.ray.etc
 * @fileName : UploadFileProcess.java
 * @author : webshjin
 * @date : 2023. 9. 1.
 * @description : 업로드된 파일을 처리한다
 */
public class UploadFileProcess {

	/**
	 * @MethodName : fileUpload
	 * @author : webshjin
	 * @param :
	 * @throws IOException
	 * @returnValue :
	 * @descriptiton : 파일 업로드 처리의 전체 컨트롤
	 * @date : 2023. 9. 1.
	 */
	public static UploadedFile fileUpload(String originalFilename, long size, String contentType, byte[] data,
			String realPath) throws IOException {

		String completePath = makeCalculatePath(realPath); // 물리적경로 + /년/월/일

		UploadedFile uf = new UploadedFile();

		if (size > 0) {
			uf.setNewFileName(getNewFileName(originalFilename, realPath, completePath));

			uf.setOriginalFileName(originalFilename);
			uf.setSize(size);

			FileCopyUtils.copy(data, new File(realPath + uf.getNewFileName())); // 원본 파일 저장

			if (ImgMimeType.contentTypeIsImage(contentType)) {
				// 스케일 다운 -> thumbnail 이름으로 파일 저장
				makeThumbNailImage(uf, completePath, realPath); // 썸네일 이미지 저장
			}

		}

//		if (uf != null) {
//			System.out.println(uf.toString());
//		}

		return uf;
	}

	/**
	 * @MethodName : makeThumbNailImage
	 * @author : webshjin
	 * @param :
	 * @throws IOException
	 * @returnValue :
	 * @descriptiton : 이미지(원본)를 읽어와 스케일 다운 시키고, 썸네일 파일로 저장
	 * @date : 2023. 9. 1.
	 */
	private static void makeThumbNailImage(UploadedFile uf, String completePath, String realPath) throws IOException {

		BufferedImage originImg = ImageIO.read(new File(realPath + uf.getNewFileName())); // 원본파일

		BufferedImage thumbNailImg = Scalr.resize(originImg, Mode.FIT_TO_HEIGHT, 50); // 리사이징

		String thumbImgName = "thumb_" + uf.getOriginalFileName();

		File saveTarget = new File(completePath + File.separator + thumbImgName);
		String ext = uf.getOriginalFileName().substring(uf.getOriginalFileName().lastIndexOf(".") + 1);

		if (ImageIO.write(thumbNailImg, ext, saveTarget)) { // 썸네일 이미지를 저장 -> 성공이면 uf에 담기
			uf.setThumbFileName(completePath.substring(realPath.length()) + File.separator + thumbImgName);
		}

	}

	/**
	 * @MethodName : getNewFileName
	 * @author : webshjin
	 * @param :
	 * @returnValue : "\년\월\일\새로운유니크한파일이름.확장자" 반환
	 * @descriptiton
	 * @date : 2023. 9. 1.
	 */
	private static String getNewFileName(String originalFilename, String realPath, String completePath) {
		String uuid = UUID.randomUUID().toString();
		String ext = originalFilename.substring(originalFilename.lastIndexOf("."));

		String newFileName = uuid + "_" + originalFilename;

		return completePath.substring(realPath.length()) + File.separator + newFileName;
	}

	/**
	 * @MethodName : makeCalculatePath
	 * @author : webshjin
	 * @param : realPath(저장되는실제경로)
	 * @returnValue : realPath + date (realPaht + 현재 /년/월/일 폴더 경로)
	 * @descriptiton
	 * @date : 2023. 9. 1.
	 */
	private static String makeCalculatePath(String realPath) {
		Calendar cal = Calendar.getInstance();
		String year = File.separator + (cal.get(Calendar.YEAR) + ""); // "\2023"
		String month = year + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1); // "\2023\09"
		String date = month + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE)); // "\2023\09\01"

		System.out.println(year + ", " + month + ", " + date);

		makeDirectory(realPath, year, month, date);

		return realPath + date;
	}

	// ... strings : 가변인자 메서드 기법 (전달된 year, month, date 값이 strings 하나의 매개변수로
	// 할당 된다. (배열 형식으로)
	private static void makeDirectory(String realPath, String... strings) {
		// realPath 경로 + \년\월\일 폴더가 모두 존재하지 않는다면...
		if (!new File(realPath + strings[strings.length - 1]).exists()) {
			for (String path : strings) {
				File tmp = new File(realPath + path);
				if (!tmp.exists()) {
					tmp.mkdir();
				}
			}
		}

	}

	/**
	 * @MethodName : deleteFile
	 * @author : webshjin
	 * @param :
	 * @returnValue :
	 * @descriptiton : fileList의 remInd번째에 있는 파일을 삭제한다
	 * @date : 2023. 9. 4.
	 */
	public static void deleteFile(List<UploadedFile> fileList, String remFile, String realPath) {

		for (UploadedFile uf :  fileList) {  
			if (remFile.equals(uf.getOriginalFileName())) { // 지워야 할 파일을 찾았다
				
				File delFile = new File(realPath + uf.getNewFileName());
				if (delFile.exists()) {
					delFile.delete();
				}

				if (uf.getThumbFileName() != null) {
					File thumbFile = new File(realPath + uf.getThumbFileName());
					if (thumbFile.exists()) {
						thumbFile.delete();
					}
				}
			}
		}

		

	}

	/**
	 * @MethodName : deleteAllFile
	 * @author : webshjin
	 * @param : 
	 * @returnValue : 
	 * @descriptiton : 유저가 글쓰기를 취소했을 경우 모든 업로드된 파일을 지움
	 * @date : 2023. 9. 4.
	 */
	public static void deleteAllFile(List<UploadedFile> fileList, String realPath) {
		for (UploadedFile uf : fileList) {
			File delFile = new File(realPath + uf.getNewFileName());
			if (delFile.exists()) {
				delFile.delete();
			}

			if (uf.getThumbFileName() != null) {
				File thumbFile = new File(realPath + uf.getThumbFileName());
				if (thumbFile.exists()) {
					thumbFile.delete();
				}
			}
		}
		
	}

}
